package com.tivic.manager.mob.aitpagamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.sql.Timestamp;
import java.sql.Types;

import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.banco.BancoSearch;
import com.tivic.manager.grl.banco.IBancoService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.cancelamentomovimentos.CancelaPagamentoAit;
import com.tivic.manager.mob.aitpagamento.enums.SituacaoPagamentoEnum;
import com.tivic.manager.mob.aitpagamento.validatiors.ValidationAitPagamento;
import com.tivic.manager.mob.restituicao.RestituicaoDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class AitPagamentoService implements IAitPagamentoService {
	private AitPagamentoRepository aitPagamentoRepository;
	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private AitMovimento aitMovimento;
	private IAitMovimentoService aitMovimentoService;
	private IBancoService bancoService;
	
	public AitPagamentoService() throws Exception {
		this.aitPagamentoRepository = (AitPagamentoRepository) BeansFactory.get(AitPagamentoRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.bancoService = (IBancoService) BeansFactory.get(IBancoService.class);
		this.aitMovimento = new AitMovimento();
	}
	
	@Override
	public PagedResponse<AitPagamento> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PagedResponse<AitPagamento> aitPagamentoList = find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aitPagamentoList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public PagedResponse<AitPagamento> find(SearchCriterios searchCriterios, CustomConnection customConnection)	throws Exception {
		List<AitPagamento> aitPagamentoList = this.aitPagamentoRepository.find(searchCriterios, customConnection);
		return new PagedResponse<AitPagamento>(aitPagamentoList, aitPagamentoList.size());
	}

	@Override
	public void cancelarPagamento(AitPagamento aitPagamento, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			new ValidationAitPagamento(aitPagamento).validate(customConnection);
			cancelarPagamento(aitPagamento, cdUsuario, customConnection);
			this.setAitMovimento(aitPagamento, customConnection);
			this.atualizaEnvioDetran(customConnection);
			this.criaMovimento(cdUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void cancelarPagamento(AitPagamento aitPagamento, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoService.getStatusMovimento(aitPagamento.getCdAit(), TipoStatusEnum.MULTA_PAGA.getKey());
		aitPagamento.setStPagamento(SituacaoPagamentoEnum.CANCELADO.getKey());
		aitPagamento.setDtCancelamento(new GregorianCalendar());
		if(aitMovimento.getTpStatus() == TipoStatusEnum.MULTA_PAGA.getKey()) {
			aitPagamento.setCdMovimento(aitMovimento.getCdMovimento());
			this.aitPagamentoRepository.update(aitPagamento, customConnection);
		}
	}
	
	private void setAitMovimento(AitPagamento aitPagamento, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(aitPagamento.getCdAit(), customConnection);
		this.aitMovimento = this.aitMovimentoRepository.get(ait.getCdMovimentoAtual(),ait.getCdAit(), customConnection);		
	}
	
	private void criaMovimento(int cdUsuario, CustomConnection customConnection) throws Exception {
		this.aitMovimento.setCdUsuario(cdUsuario);
		AitMovimento aitMovimento = this.aitMovimentoService.getStatusMovimento(this.aitMovimento.getCdAit(),TipoStatusEnum.MULTA_PAGA.getKey());
		if(aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey() && 
				aitMovimento.getTpStatus() == TipoStatusEnum.MULTA_PAGA.getKey()) {
			new CancelaPagamentoAit(this.aitMovimento, customConnection);			
		}
	}
	
	private void atualizaEnvioDetran(CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = this.aitMovimentoService.getStatusMovimento(this.aitMovimento.getCdAit(),TipoStatusEnum.MULTA_PAGA.getKey());
		if(aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey() && 
				aitMovimento.getTpStatus() == TipoStatusEnum.MULTA_PAGA.getKey()) {
			aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
			this.aitMovimentoRepository.update(aitMovimento, customConnection);
		}
	}
	
	@Override
	public PagedResponse<AitPagamentoRecebidoDTO> findValoresRecebidos(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<AitPagamentoRecebidoDTO> aitPagamentoRelatorioSearch = findValoresRecebidos(searchCriterios, customConnection);
			if(aitPagamentoRelatorioSearch.getList(AitPagamentoRecebidoDTO.class).isEmpty()) {
				throw new ValidacaoException("Nenhum valor recebido encontrado.");
			}
			List<AitPagamentoRecebidoDTO> aitPagamentoList = new ArrayList<AitPagamentoRecebidoDTO>(aitPagamentoRelatorioSearch.getList(AitPagamentoRecebidoDTO.class));
			customConnection.finishConnection();
			return new PagedResponse<AitPagamentoRecebidoDTO>(aitPagamentoList, aitPagamentoList.size());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<AitPagamentoRecebidoDTO> findValoresRecebidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<AitPagamentoRecebidoDTO> search = new SearchBuilder<AitPagamentoRecebidoDTO>("mob_ait_pagamento A")
				.fields("DATE_TRUNC('month', a.dt_pagamento) as dt_periodo_pagamento, "
						+ "COUNT(a.cd_ait) as qtd_nips, " 
						+ "SUM(b.vl_multa) as vl_total, "
						+ "SUM(a.vl_funset) as vl_funset, " 
						+ "SUM(a.vl_pago) as vl_liquido")
				.addJoinTable("JOIN mob_ait B on (A.cd_ait = B.cd_ait)")
				.searchCriterios(searchCriterios)
				.groupBy(" dt_periodo_pagamento ")
				.orderBy(" dt_periodo_pagamento ")
				.count()
				.customConnection(customConnection)
			.build();
		return search;
	}
	
	@Override
	public PagedResponse<RestituicaoDTO> findRestituicao(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			int tpConsulta = Integer.parseInt((searchCriterios.getAndRemoveCriterio("tp_consulta")).getValue());
			customConnection.initConnection(false);
			Search<RestituicaoDTO> restituicaoSearch = selectTpConsulta(searchCriterios, customConnection, tpConsulta);
			customConnection.finishConnection();
			return new PagedResponse<RestituicaoDTO>(restituicaoSearch.getList(RestituicaoDTO.class), restituicaoSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Search<RestituicaoDTO> selectTpConsulta(SearchCriterios searchCriterios, CustomConnection customConnection, int tpConsulta) throws Exception {
		switch(tpConsulta) {
		case 1: 
			return findAitsDuplicados(searchCriterios, customConnection);
		case 2: 
			return findAitsRecursoDeferido(searchCriterios, customConnection);
		case 3:
			return findAitsCancelados(searchCriterios, customConnection);
		}
		return null;
	}
	
	public Search<RestituicaoDTO> findAitsDuplicados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_movimento");
		searchCriterios.getAndRemoveCriterio("B.dt_movimento");
		Search<RestituicaoDTO> search = new SearchBuilder<RestituicaoDTO>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.dt_infracao, A.vl_multa, A.tp_status, A.nm_proprietario, "
						+ "B.dt_pagamento ")
				.addJoinTable("LEFT JOIN mob_ait_pagamento B ON A.cd_ait = B.cd_ait ")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" (SELECT COUNT(*) FROM mob_ait_pagamento C WHERE C.cd_ait = A.cd_ait) > 1")
				.orderBy("A.dt_infracao")
				.customConnection(customConnection)
				.count()
			.build();
		
		return search;
	}
	
	public Search<RestituicaoDTO> findAitsRecursoDeferido(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("B.dt_pagamento");
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.MULTA_PAGA.getKey());
		Search<RestituicaoDTO> search = new SearchBuilder<RestituicaoDTO>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.dt_infracao, A.vl_multa, A.tp_status, A.nm_proprietario, "
						+ "B.dt_movimento AS dt_pagamento")
				.addJoinTable("LEFT JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS "
						+ "(SELECT C.* FROM mob_ait_movimento C WHERE ("
						+ "C.tp_status IN (" + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + ", " + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + ", " + TipoStatusEnum.CETRAN_DEFERIDO.getKey() + ")) "
						+ "AND C.cd_ait = A.cd_ait)")
				.orderBy("A.dt_infracao")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	public Search<RestituicaoDTO> findAitsCancelados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("B.dt_pagamento");
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey());
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.MULTA_PAGA.getKey());
		Search<RestituicaoDTO> search = new SearchBuilder<RestituicaoDTO>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.dt_infracao, A.vl_multa, A.tp_status, A.nm_proprietario, "
						+ "B.dt_movimento as dt_pagamento")
				.addJoinTable("LEFT JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS "
						+ "(SELECT 1 FROM mob_ait_movimento C "
						+ "WHERE C.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey() + " AND C.cd_ait = A.cd_ait)")
				.orderBy("A.dt_infracao")
				.customConnection(customConnection)
				.count()
			.build();
		return search;
	}
	
	@Override
	public PagedResponse<AitPagamentoRecebidoDTO> reportPeriodo(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<AitPagamentoRecebidoDTO> search = selectConsulta(searchCriterios, customConnection);
			customConnection.finishConnection();
			return new PagedResponse<AitPagamentoRecebidoDTO>(search.getList(AitPagamentoRecebidoDTO.class), search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Search<AitPagamentoRecebidoDTO> selectConsulta(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		int tpConsulta = Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_consulta").getValue());
		switch(tpConsulta) {
		case 1: 
			return reportPeriodo(searchCriterios, customConnection);
		case 2:
			return getEmTramitacao(searchCriterios, customConnection);
		case 3:
			return getVencidas(searchCriterios, customConnection);
		case 4:
			return getBancoArrecadador(searchCriterios, customConnection);
		
		}
		return null;
	}
	
	public Search<AitPagamentoRecebidoDTO> reportPeriodo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String periodicidade = searchCriterios.getAndRemoveCriterio("periodicidade").getValue();
		String aggregate = searchCriterios.getAndRemoveCriterio("aggregate_function").getValue();
		searchCriterios.getAndRemoveCriterio("A.dt_infracao");
		searchCriterios.getAndRemoveCriterio("A.dt_infracao");
		Search<AitPagamentoRecebidoDTO> search = new SearchBuilder<AitPagamentoRecebidoDTO>("mob_ait_pagamento A")
				.fields(periodicidade + ", ROUND(CAST(SUM(A.vl_pago) AS decimal (16,2)), 2) AS vl_total, "
						+ "COUNT(A.cd_ait) AS qtd_pagamentos ")
				.addJoinTable("INNER JOIN mob_ait B ON B.cd_ait = A.cd_ait ")
				.searchCriterios(searchCriterios)
				.orderBy(aggregate + " ")
				.groupBy(aggregate + " ")
			.build();
		return search;
	}
	
	public Search<AitPagamentoRecebidoDTO> getEmTramitacao(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("periodicidade");
		searchCriterios.getAndRemoveCriterio("aggregate_function");
		List<String> movimentos = new ArrayList<String>();
		movimentos.add(String.valueOf(TipoStatusEnum.MULTA_PAGA.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.MULTA_PAGA_OUTRA_UF.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.PENALIDADE_SUSPENSA.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.CETRAN_DEFERIDO.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()));
		searchCriterios.addCriterios("B.tp_status", movimentos.toString().replace("[", "").replace("]", ""), ItemComparator.NOTIN, Types.INTEGER);
		Search<AitPagamentoRecebidoDTO> search = new SearchBuilder<AitPagamentoRecebidoDTO>("mob_ait A")
				.fields(" COUNT(A.cd_ait) qtd_pagamentos, A.sg_uf_veiculo, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total")
				.addJoinTable("INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual ")
				.additionalCriterias(" A.sg_uf_veiculo IS NOT NULL AND B.cd_ait = A.cd_ait "
						+ "AND EXISTS (SELECT 1 FROM mob_ait_movimento C WHERE C.cd_ait = A.cd_ait AND C.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + ")")
				.searchCriterios(searchCriterios)
				.groupBy("A.sg_uf_veiculo ")
			.build();
		return search;
	}
	
	public Search<AitPagamentoRecebidoDTO> getVencidas(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		Search<AitPagamentoRecebidoDTO> search = new SearchBuilder<AitPagamentoRecebidoDTO>("mob_ait A")
				.fields(" COUNT(A.cd_ait) qtd_pagamentos, A.sg_uf_veiculo, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total ")
				.addJoinTable(" INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual ")
				.additionalCriterias(" A.dt_vencimento <= '" + LocalDate.now()
									+ "' AND B.cd_ait = A.cd_ait "
									+ " AND B.tp_status != " + TipoStatusEnum.MULTA_PAGA.getKey() + " AND B.tp_status != " + TipoStatusEnum.MULTA_PAGA_OUTRA_UF.getKey())
				.searchCriterios(searchCriterios)
				.groupBy("A.sg_uf_veiculo ")
				.count()
				.build();
		return search;
	}
	
	public Search<AitPagamentoRecebidoDTO> getBancoArrecadador(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		searchCriterios.getAndRemoveCriterio("A.dt_pagamento");
		Search<AitPagamentoRecebidoDTO> search = new SearchBuilder<AitPagamentoRecebidoDTO>("mob_ait A")
				.fields(" COUNT(A.cd_ait) qtd_pagamentos, C.nr_banco, ROUND(CAST(SUM(A.vl_multa) AS decimal (16,2)), 2) AS vl_total ")
				.addJoinTable(" INNER JOIN mob_ait_movimento B ON B.cd_movimento = A.cd_movimento_atual ")
				.addJoinTable(" INNER JOIN mob_ait_pagamento C ON C.cd_ait = A.cd_ait ")
				.additionalCriterias("B.tp_status = " + TipoStatusEnum.MULTA_PAGA.getKey() + " AND B.cd_ait = A.cd_ait ")
				.searchCriterios(searchCriterios)
				.groupBy(" C.nr_banco")
				.count()
			.build();
		return search;
	}
	
	@Override
	public PagedResponse<RelatorioAitDTO> detalhamento(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<RelatorioAitDTO> search = selectDetalhamento(searchCriterios, customConnection);
			customConnection.finishConnection();
			return new PagedResponse<RelatorioAitDTO>(search.getList(RelatorioAitDTO.class), search.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Search<RelatorioAitDTO> selectDetalhamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		int tpConsulta = Integer.parseInt(searchCriterios.getAndRemoveCriterio("tp_consulta").getValue());
		switch(tpConsulta) {
		case 1: 
			return getDetalhamentoPeriodo(searchCriterios, customConnection);
		case 2:
			return getEmTramitacaoDetalhamento(searchCriterios, customConnection);
		case 3:
			return getVencidasDetalhamento(searchCriterios, customConnection);
		case 4:
			return getBancoArrecadadorDetalhamento(searchCriterios, customConnection);
		}
		return null;
	}
	
	public Search<RelatorioAitDTO> getDetalhamentoPeriodo(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait_pagamento A")
				.fields(" B.id_ait, B.dt_infracao, B.dt_vencimento, B.vl_multa, A.vl_pago, B.tp_status, B.lg_enviado_detran, dt_pagamento ")
				.addJoinTable(" INNER JOIN mob_ait B ON B.cd_ait = A.cd_ait ")
				.searchCriterios(searchCriterios)
			.build();
		return search;
	}
	
	public Search<RelatorioAitDTO> getEmTramitacaoDetalhamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("C.nr_banco");
		List<String> movimentos = new ArrayList<String>();
		movimentos.add(String.valueOf(TipoStatusEnum.MULTA_PAGA.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.MULTA_PAGA_OUTRA_UF.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.PENALIDADE_SUSPENSA.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.JARI_COM_PROVIMENTO.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.CETRAN_DEFERIDO.getKey()));
		movimentos.add(String.valueOf(TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey()));
		searchCriterios.addCriterios("B.tp_status", movimentos.toString().replace("[", "").replace("]", ""), ItemComparator.NOTIN, Types.INTEGER);
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields(" A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran ")
				.addJoinTable(" INNER JOIN mob_ait_movimento B ON B.cd_ait = A.cd_ait ")
				.additionalCriterias(" B.cd_movimento = A.cd_movimento_atual "
						+ "AND EXISTS (SELECT 1 FROM mob_ait_movimento C WHERE C.cd_ait = A.cd_ait AND C.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + ")")
				.searchCriterios(searchCriterios)
				.orderBy(" A.dt_infracao DESC")
			.build();
		return search;
	}
	
	public Search<RelatorioAitDTO> getVencidasDetalhamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("C.nr_banco");
		searchCriterios.addCriteriosMinorDate("dt_vencimento", DateUtil.formatDate(new Timestamp(DateUtil.getDataAtual().getTimeInMillis()), "yyyy-MM-dd"));
		searchCriterios.addCriterios("A.dt_vencimento", null, ItemComparator.NOTISNULL, Types.DATE);
		searchCriterios.addCriterios("B.tp_status", TipoStatusEnum.MULTA_PAGA.getKey().toString(), ItemComparator.DIFFERENT, Types.INTEGER);
		searchCriterios.addCriterios("B.tp_status", TipoStatusEnum.MULTA_PAGA_OUTRA_UF.getKey().toString(), ItemComparator.DIFFERENT, Types.INTEGER);
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields(" A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran ")
				.addJoinTable(" INNER JOIN mob_ait_movimento B ON B.cd_ait = A.cd_ait ")
				.additionalCriterias(" B.cd_movimento = A.cd_movimento_atual ")
				.searchCriterios(searchCriterios)
				.orderBy(" A.dt_infracao DESC")
			.build();
		return search;
	}
	
	public Search<RelatorioAitDTO> getBancoArrecadadorDetalhamento(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		searchCriterios.getAndRemoveCriterio("A.sg_uf_veiculo");
		searchCriterios.addCriteriosEqualInteger("B.tp_status", TipoStatusEnum.MULTA_PAGA.getKey());
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields(" A.id_ait, A.dt_infracao, A.dt_vencimento, A.vl_multa, B.tp_status, B.lg_enviado_detran ")
				.addJoinTable(" INNER JOIN mob_ait_movimento B ON B.cd_ait = A.cd_ait ")
				.addJoinTable(" INNER JOIN mob_ait_pagamento C ON C.cd_ait = A.cd_ait ")
				.additionalCriterias(" B.cd_movimento = A.cd_movimento_atual ")
				.searchCriterios(searchCriterios)
			.build();
		return search;
	}
	
	@Override
	public byte[] gerarRelatorio(List<AitPagamentoRecebidoDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		return imprimirRelatorio(itemsList, searchCriterios).getReportPdf("mob/relatorio_pagamentos");
	}
	
	private Report imprimirRelatorio(List<AitPagamentoRecebidoDTO> itemsList, SearchCriterios searchCriterios) throws Exception {
		ReportCriterios reportCriterios = getReportCriterios(searchCriterios);
		setBanco(itemsList);
		Report report = new ReportBuilder()
			.list(itemsList)
			.reportCriterios(reportCriterios)
			.build();
		return report;
	}

	private ReportCriterios getReportCriterios(SearchCriterios searchCriterios) throws Exception {
		String tpConsulta = searchCriterios.getAndRemoveCriterio("tp_consulta").getValue();
		String tpPeriodicidade = searchCriterios.getAndRemoveCriterio("tp_periodicidade").getValue();
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("TP_CONSULTA", Integer.parseInt(tpConsulta));
		reportCriterios.addParametros("TP_PERIODICIDADE", Integer.parseInt(tpPeriodicidade));
		reportCriterios.addParametros("CURRENCY", "R$ ");
		return reportCriterios;
	}
	
	private void setBanco(List<AitPagamentoRecebidoDTO> itemsList) throws Exception {
		for (AitPagamentoRecebidoDTO ait : itemsList) {
			SearchCriterios searchCriterios = new BancoSearch()
					.setNrBanco(ait.getNrBanco())
					.build();
			String banco = ait.getNrBanco() == null ? "Não informado" : this.bancoService.find(searchCriterios).getItems().get(0).getNmBanco();
			ait.setNrBanco(banco);
		}
	}
}
