package com.tivic.manager.mob.ait;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.EfeitoSuspensivoDTO;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.ait.builders.AitListBuilder;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitSneDTO;
import com.tivic.manager.mob.aitmovimento.CancelaRegistro;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class AitService implements IAitService {

	private AitRepository aitRepository;
	private ServicoDetranServices servicoDetranServices;
	private IAitMovimentoService aitMovimentoService;
	
	public AitService() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);	
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public Ait insert(Ait ait) throws Exception{
		return insert(ait, new CustomConnection());
	}
	
	@Override
	public Ait insert(Ait ait, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			aitRepository.insert(ait, customConnection);
			customConnection.finishConnection();
			return ait;
		} finally{
			customConnection.closeConnection();
		}
	}

	@Override
	public Ait update(Ait ait) throws Exception{
		return update(ait, new CustomConnection());
	}
	
	@Override
	public Ait update(Ait ait, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			aitRepository.update(ait, customConnection);
			customConnection.finishConnection();
			return ait;
		} finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Search<EfeitoSuspensivoDTO> findEfeitoSuspensivo(SearchCriterios searchCriterios)
			throws Exception {
		return findEfeitoSuspensivo(searchCriterios, new CustomConnection());
	}

	@Override
	public Search<EfeitoSuspensivoDTO> findEfeitoSuspensivo( SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		try {
			int EFEITO_SUSPENSIVO = 16;
			customConnection.initConnection(true);
			Search<EfeitoSuspensivoDTO> search = new SearchBuilder<EfeitoSuspensivoDTO>("mob_ait A")
					.fields("A.cd_ait, A.lg_enviado_detran, A.tp_status, A.nr_erro, A.id_ait, A.dt_resultado_jari, A.dt_resultado_cetran, " +
							" B.dt_movimento as dt_dias, B.tp_status, B.nr_processo, B.dt_movimento, D.nr_documento, E.nm_situacao_documento")
					.addJoinTable("LEFT OUTER JOIN mob_ait_movimento         	B ON(A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)")
					.addJoinTable("LEFT OUTER JOIN mob_ait_movimento_documento 	C ON (C.cd_ait = A.cd_ait AND B.cd_movimento = C.cd_movimento)")
					.addJoinTable("LEFT OUTER JOIN ptc_documento 				D ON (D.cd_documento = C.cd_documento)")
					.addJoinTable("LEFT OUTER JOIN ptc_situacao_documento 		E ON (D.cd_situacao_documento = E.cd_situacao_documento)")
					.additionalCriterias("B.lg_enviado_detran = 1 AND (A.nr_erro IS NULL OR A.nr_erro LIKE '%0%') "+
							"AND NOT EXISTS (SELECT * FROM MOB_AIT_MOVIMENTO Z " + 
							"WHERE Z.TP_STATUS IN ("+ EFEITO_SUSPENSIVO +") AND Z.cd_ait = A.cd_ait) ")
					.searchCriterios(searchCriterios)
					.orderBy("B.dt_movimento ASC")
					.count()
					.build();
			customConnection.finishConnection();
			return search;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public List<ServicoDetranDTO> SuspenderInfracao(List<EfeitoSuspensivoDTO> efeitoSuspensivoListDto) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {				
			customConnection.initConnection(true);			
			List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
			List<ServicoDetranDTO> servicoDetranlistDTO = new ArrayList<ServicoDetranDTO>();			
			for (EfeitoSuspensivoDTO efeitoSuspensivoDto: efeitoSuspensivoListDto) {
				aitMovimentoList.add(new AitEfeitoSuspensaoRetomada()
						.criarMovimentoSuspensaoRetomadaAuto(efeitoSuspensivoDto, customConnection));
				if(efeitoSuspensivoDto.getNrDocumento() != null)
					atualizaNrDocumentoAit(efeitoSuspensivoDto.getCdAit(), efeitoSuspensivoDto.getNrDocumento().toString().replaceAll("[\\D]", ""), customConnection);
			}
			customConnection.finishConnection();
			servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
			servicoDetranlistDTO = servicoDetranServices.remessa(aitMovimentoList);
			return servicoDetranlistDTO; 
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public boolean hasAit(String idAit) throws Exception {
		return hasAit(idAit, new CustomConnection());
	}
	
	@Override
	public boolean hasAit(String idAit, CustomConnection customConnection) throws Exception {
		return this.aitRepository.hasAit(idAit, customConnection);
	}
	
	@Override
	public Ait updateDetran(Ait ait) throws Exception {
		return updateDetran(ait, new CustomConnection());
	}
	
	@Override
	public Ait updateDetran(Ait ait, CustomConnection customConnection) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		try {
			customConnection.initConnection(true);
			servicoDetranServices.searchDetran(ait);
			if(ait.getNrCnhAutuacao()==null || ait.getNrCnhAutuacao().equals("")) {
				ait.setCdEnderecoCondutor(0);
				ait.setNmCondutorAutuacao(null);
				ait.setUfCnhAutuacao(null);
				ait.setTpCnhCondutor(-1);
			}
			this.aitRepository.update(ait, customConnection);
			customConnection.finishConnection();
			return ait;
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao atualizar AIT.");
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Ait get(int cdAit) throws Exception {
		return get(cdAit, new CustomConnection());
	}

	@Override
	public Ait get(int cdAit, CustomConnection customConnection) throws Exception {
		return this.aitRepository.get(cdAit, customConnection);
	}

	@Override
	public Ait getById(String idAit) throws Exception {
		return getById(idAit, new CustomConnection());
	}

	@Override
	public Ait getById(String idAit, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("id_ait", idAit, true);
		List<Ait> aits = this.aitRepository.find(searchCriterios, customConnection);
		if(aits.isEmpty())
			throw new Exception("Nenhum ait encontrado");
		return aits.get(0);
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Ait> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			List<Ait> aits = this.aitRepository.find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return aits;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	public void atualizaNrDocumentoAit(int cdAit, String nrDocumento, CustomConnection customConnection) throws Exception {
		Ait aitBusca = aitRepository.get(cdAit);
		aitBusca.setNrDocumento(nrDocumento);
		save(aitBusca, customConnection);
	}
	
	public Ait save(Ait ait) throws Exception{
		return save(ait, new CustomConnection());
	}

	public Ait save(Ait ait, CustomConnection customConnection) throws Exception{
		try {
			if(ait==null)
				throw new ValidacaoException("Erro ao salvar. LoteImpressao é nulo");
			customConnection.initConnection(true);
			if(ait.getCdAit()==0){
				aitRepository.insert(ait, customConnection).getCdAit();
			
			} else {
				ait = aitRepository.update(ait, customConnection);
				customConnection.finishConnection();
				return ait;
			}
			customConnection.finishConnection();
			return ait;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<Ait> getByData(GregorianCalendar dataInicial, GregorianCalendar dataFinal, int limit, int offset) throws Exception {
		return getByData(dataInicial, dataFinal, limit, offset, new CustomConnection());
	}

	@Override
	public List<Ait> getByData(GregorianCalendar dataInicial, GregorianCalendar dataFinal, int limit, int offset, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("dt_infracao", Util.convCalendarStringSql(dataInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR);
		searchCriterios.addCriterios("dt_infracao", Util.convCalendarStringSql(dataFinal), ItemComparator.MINOR_EQUAL, Types.VARCHAR);
		searchCriterios.setQtLimite(limit);
		searchCriterios.setQtDeslocamento(offset);
		return this.aitRepository.find(searchCriterios, customConnection);
	}

	@Override
	public PagedResponse<Ait> findPagedResponse(SearchCriterios searchCriterios) throws Exception {
		return findPagedResponse(searchCriterios, new CustomConnection());
	}

	@Override
	public PagedResponse<Ait> findPagedResponse(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
			.searchCriterios(searchCriterios)
			.customConnection(customConnection)
			.count()
			.orderBy("dt_infracao DESC")
		.build();
		List<Ait> aits = new AitListBuilder(search)
			.movimentos()
			.movimentoAtual()
		.build();
		return new PagedResponse<Ait>(aits, search.getRsm().getTotal());
	}
	
	public PagedResponse<RelatorioAitDTO> filtrarListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento, String radar) throws ValidacaoException, Exception {
		SearchCriterios searchCriteriosCopy = new SearchCriterios();
		SearchCriterios criterias = new SearchCriterios();
		SearchCriterios criteriasCopy = new SearchCriterios();
		searchCriteriosCopy.getCriterios().addAll(searchCriterios.getCriterios());
		criterias.getCriterios().addAll(searchCriterios.getCriterios());
		criteriasCopy.getCriterios().addAll(searchCriterios.getCriterios());
		Search<RelatorioAitDTO> searchRelatorioAitDTO = searchListagemAits(searchCriterios, lgExcetoCanceladas, tpEquipamento, radar);
		List<RelatorioAitDTO> listRelatorioAitDTO = setCadastrado(searchRelatorioAitDTO, criterias);
		double totalValor = pegarTotalMultas(searchCriteriosCopy, radar);
		listRelatorioAitDTO = setCtMovimento(listRelatorioAitDTO, criterias.getAndRemoveCriterio("ctMovimento"));
		return new RelatorioAitDTOListBuilder(listRelatorioAitDTO, searchRelatorioAitDTO.getRsm().getTotal(), totalValor).build();
	}
	
	private List<RelatorioAitDTO> setCadastrado(Search<RelatorioAitDTO> searchRelatorioAitDTO, SearchCriterios searchCriterios) throws Exception {
		List<RelatorioAitDTO> listRelatorio = new ArrayList<RelatorioAitDTO>();
		for(RelatorioAitDTO relatorioAitDTO : searchRelatorioAitDTO.getList(RelatorioAitDTO.class)) {
			if(relatorioAitDTO.getCtMovimento() == TipoStatusEnum.REGISTRO_INFRACAO.getKey() && relatorioAitDTO.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()) {
				relatorioAitDTO.setCtMovimento(TipoStatusEnum.CADASTRADO.getKey());
			}
			listRelatorio.add(relatorioAitDTO);
		}
		return listRelatorio;
	}
	
	private List<RelatorioAitDTO> setCtMovimento(List<RelatorioAitDTO> listRelatorioAitDTO, ItemComparator itemComparatorCtMovimento) throws Exception {
		if(itemComparatorCtMovimento != null) {
			List<RelatorioAitDTO> listRelatorio = new ArrayList<RelatorioAitDTO>();
			for(RelatorioAitDTO relatorioAitDTO: listRelatorioAitDTO) {
				relatorioAitDTO.setCtMovimento(Integer.parseInt(itemComparatorCtMovimento.getValue().toString()));
				listRelatorio.add(relatorioAitDTO);
			}
		}
		return listRelatorioAitDTO;
	}
	
	public Search<RelatorioAitDTO> searchListagemAits(SearchCriterios searchCriterios, boolean lgExcetoCanceladas, Integer tpEquipamento, String radar) throws ValidacaoException, Exception {
		String incluirContemMovimento = incluirContemMovimento(searchCriterios);
		incluirCondicionalCanceladas(searchCriterios, lgExcetoCanceladas);		
		incluirTpEquipamento(searchCriterios, tpEquipamento);
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("DISTINCT ON(A.cd_ait) A.cd_ait, B.dt_movimento, A.dt_infracao, A.id_ait, A.nr_placa, D.nr_cod_detran, A.vl_multa, A.nm_proprietario, A.dt_digitacao, A.ds_local_infracao, B.cd_ocorrencia,"
					+ " A.nr_cpf_cnpj_proprietario, E.tp_talao, F.tp_equipamento, D.tp_competencia, A.cd_agente, C.nm_agente, C.nr_matricula, A.cd_usuario, A.tp_convenio, A.lg_auto_assinado, A.nr_renavan, A.tp_status, "
					+ "A.st_ait"
				)	
			.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
			.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
			.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
			.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
			.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
			.addJoinTable("LEFT OUTER JOIN mob_via G ON (F.ds_local = G.nm_via)")
			.addJoinTable("LEFT OUTER JOIN mob_faixa H ON (G.cd_via = H.cd_via)")
			.searchCriterios(searchCriterios)
			.additionalCriterias(incluirContemMovimento)
			.additionalCriterias(incluirNaoContemMovimento(searchCriterios))
			.additionalCriterias( "(" + incluirCondicionalRadar(radar) + ")")
			.orderBy("A.cd_ait, A.dt_infracao DESC")
			.count()
		.build();
		return search;	
	}

	private void incluirCondicionalCanceladas(SearchCriterios searchCriterios,  boolean lgExcetoCanceladas) throws Exception {
		String value = TipoStatusEnum.CADASTRO_CANCELADO.getKey() + ", " +
					   TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_MULTA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_PREVIA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_DEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_DEFESA_INDEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_FICI.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_RECURSO_JARI.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_JARI_COM_PROVIMENTO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_JARI_SEM_ACOLHIMENTO.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_PREVIA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_INDEFERIDA.getKey() + ", " +
					   TipoStatusEnum.CANCELAMENTO_NIP.getKey();
		if (lgExcetoCanceladas) {
			 searchCriterios.addCriterios("B.tp_status", value, ItemComparator.NOTIN, Types.INTEGER);
		}
	}

	private void incluirTpEquipamento(SearchCriterios searchCriterios, Integer tpEquipamento) throws Exception {
		if (tpEquipamento == EquipamentoEnum.NENHUM.getKey()) {
			searchCriterios.getAndRemoveCriterio("F.tp_equipamento");
			searchCriterios.addCriterios("A.cd_equipamento", tpEquipamento.toString(), ItemComparator.ISNULL, Types.INTEGER);
		}
	}
	
	private String incluirContemMovimento(SearchCriterios searchCriterios) throws Exception {
		ItemComparator itemComparatorCtMovimento = searchCriterios.getAndRemoveCriterio("ctMovimento");
		ItemComparator itemComparatorDtInicial = searchCriterios.getAndRemoveCriterio("dtInicialContemMovimento");
		ItemComparator itemComparatorDtFinal = searchCriterios.getAndRemoveCriterio("dtFinalContemMovimento");		
		ItemComparator itemComparatorLg = searchCriterios.getAndRemoveCriterio("B.lg_enviado_detran");
		
		if(itemComparatorCtMovimento != null) {
			Integer ctMovimento = setCtMovimento(Integer.parseInt(itemComparatorCtMovimento.getValue()), Integer.parseInt(itemComparatorLg.getValue()));
			
			if (ctMovimento != null) {
				return "EXISTS (SELECT * FROM mob_ait_movimento B WHERE A.cd_ait = B.cd_ait AND B.tp_status = "+ctMovimento+" "
						+(itemComparatorLg.getValue().equals(EnviadoDetranEnum.LG_DETRAN_ENVIADA.getKey().toString()) ? " AND B.lg_enviado_detran = " + itemComparatorLg.getValue() + " " : " ")
						+(itemComparatorDtInicial != null ? " AND date(B.dt_movimento) >= '" + itemComparatorDtInicial.getValue() + "' " : " ") 
						+(itemComparatorDtFinal != null ? " AND date(B.dt_movimento) <= '" + itemComparatorDtFinal.getValue() + "' " : " ")+")";
			}
		}
		return null;
	}
	
	private int setCtMovimento(int ctMovimento, int lgEnviadoDetran) throws Exception {
		if(ctMovimento == TipoStatusEnum.CADASTRADO.getKey() && lgEnviadoDetran == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()) {
			return TipoStatusEnum.REGISTRO_INFRACAO.getKey();
		} else {
			return ctMovimento;
		}
	}
	
	private String incluirNaoContemMovimento(SearchCriterios searchCriterios) throws Exception {
		ItemComparator itemComparatorMovimentoNaoContido = searchCriterios.getAndRemoveCriterio("tpNaoContemMovimento");
		ItemComparator itemComparatorDtInicial = searchCriterios.getAndRemoveCriterio("dtInicialMovimentoNaoContido");
		ItemComparator itemComparatorDtFinal = searchCriterios.getAndRemoveCriterio("dtFinalMovimentoNaoContido");
		if(itemComparatorMovimentoNaoContido != null) {
			Integer tpNaoContemMovimento = Integer.parseInt(itemComparatorMovimentoNaoContido.getValue());
			if (tpNaoContemMovimento != null) {
				return "NOT EXISTS (SELECT * FROM mob_ait_movimento B WHERE A.cd_ait = B.cd_ait AND B.tp_status = "+tpNaoContemMovimento+" "
						+(itemComparatorDtInicial != null ? " AND date(B.dt_movimento) >= '" + itemComparatorDtInicial.getValue() + "' " : " ") 
						+(itemComparatorDtFinal != null ? " AND date(B.dt_movimento) <= '" + itemComparatorDtFinal.getValue() + "' " : " ")+")";
			}
		}
		return null;
	}

	private Double pegarTotalMultas(SearchCriterios searchCriterios, String radar) throws ValidacaoException, Exception {
		Search<RelatorioAitDTO> search = new SearchBuilder<RelatorioAitDTO>("mob_ait A")
				.fields("SUM(A.vl_multa) AS total_multas")	
			.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait AND A.cd_movimento_atual = B.cd_movimento)")
			.addJoinTable("LEFT OUTER JOIN mob_agente C ON (A.cd_agente = C.cd_agente)")
			.addJoinTable("LEFT OUTER JOIN mob_infracao D ON (A.cd_infracao = D.cd_infracao)")
			.addJoinTable("LEFT OUTER JOIN mob_talonario E ON (A.cd_talao = E.cd_talao)")
			.addJoinTable("LEFT OUTER JOIN grl_equipamento F ON (A.cd_equipamento = F.cd_equipamento)")
			.addJoinTable("LEFT OUTER JOIN mob_via G ON (F.ds_local = G.nm_via)")
			.addJoinTable("LEFT OUTER JOIN mob_faixa H ON (G.cd_via = H.cd_via)")
			.searchCriterios(searchCriterios)
			.additionalCriterias(incluirContemMovimento(searchCriterios))
			.additionalCriterias(incluirNaoContemMovimento(searchCriterios))
			.additionalCriterias( "(" + incluirCondicionalRadar(radar) + ")")
		.build();
		if(search.getList(RelatorioAitDTO.class).isEmpty() || search.getList(RelatorioAitDTO.class).get(0).getTotalMultas() == null)
			return 0.0;
		return search.getList(RelatorioAitDTO.class).get(0).getTotalMultas();	
	}
	
	@Override
	public void cancelarAit(AitMovimento aitMovimento) throws Exception {
		 CustomConnection customConnection = new CustomConnection();
		 try {
			 customConnection.initConnection(true);
			 cancelarAit(aitMovimento, customConnection);
			 customConnection.finishConnection();
		 } finally {
			 customConnection.closeConnection();
		 }
	}

	@Override
	public void cancelarAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		new ValidaMovimentoCancelamento(aitMovimento.getCdAit(), customConnection).cancelamento();
		new CancelaRegistro(aitMovimento, customConnection);
	}
	
	@Override
	public void gerarCancelamento(AitMovimento aitMovimento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			gerarCancelamento(aitMovimento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void gerarCancelamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		new CancelaRegistro(aitMovimento, customConnection);
	}
	
	@Override
	public void cancelarListaAit(List<AitMovimento> aitMovimentoList) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			cancelarListaAit(aitMovimentoList, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public void cancelarListaAit(List<AitMovimento> aitMovimentoList, CustomConnection customConnection) throws Exception {
		for(AitMovimento aitMovimento : aitMovimentoList) {
			if(this.validarCancelamento(aitMovimento, customConnection)) {
				new CancelaRegistro(aitMovimento, customConnection);
			}
		}
	}
	
	private boolean validarCancelamento(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		AitMovimento ultimoMovimento = this.aitMovimentoService.getUltimoMovimento(aitMovimento.getCdAit(), customConnection);
			return ultimoMovimento.getTpStatus() != TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey();
	}
	
	@Override
	public PagedResponse<RelatorioAitSneDTO> filtrarAitsOpcaoSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		ItemComparator contendoMovimento = searchCriterios.getAndRemoveCriterio("B.tp_status");
		Search<RelatorioAitSneDTO> searchRelatorioAitSneDTO = Integer.valueOf(contendoMovimento.getValue()) > TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey()
				? searchContendoMovimentoSne(contendoMovimento, searchCriterios) : searchAitsSne(searchCriterios);
		if(searchRelatorioAitSneDTO.getList(RelatorioAitSneDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT's com esse tipo de filtro.");
		}
		double totalValor = Integer.valueOf(contendoMovimento.getValue()) > TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey()
				? TotalMultasContendoMovimentoSne(searchCriterios, searchRelatorioAitSneDTO) : TotalMultasSne(searchCriterios);
		PagedResponse<RelatorioAitSneDTO> aitSneDTOBuilder = new RelatorioAitSneDTOListBuilder(searchRelatorioAitSneDTO.getList(RelatorioAitSneDTO.class), 
				searchRelatorioAitSneDTO.getRsm().getTotal(), totalValor).build();
		
		return aitSneDTOBuilder;
	}
	
	public Search<RelatorioAitSneDTO> searchAitsSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("A.dt_infracao, B.dt_movimento, A.id_ait, A.nr_placa, A.tp_status, B.tp_status as ct_Movimento, C.nr_cod_detran, A.vl_multa")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("LEFT OUTER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;
	}
	
	public Search<RelatorioAitSneDTO> searchContendoMovimentoSne(ItemComparator contendoMovimento, SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		ItemComparator dtInicialMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_inicial");
		ItemComparator dtFinalMovimento = searchCriterios.getAndRemoveCriterio("B.dt_movimento_final");
		ItemComparator stAdesaoSne = searchCriterios.getAndRemoveCriterio("B.st_adesao_sne");
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("A.dt_infracao, A.id_ait, A.nr_placa, A.tp_status, B.tp_status as ct_Movimento, C.nr_cod_detran, A.vl_multa")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait )")
				.addJoinTable("LEFT OUTER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao)")
				.searchCriterios(searchCriterios)
				.additionalCriterias("EXISTS"
						+ " ("
						+ " 	SELECT G.cd_ait, G.tp_status, G.dt_movimento FROM mob_ait_movimento G"
						+ "		WHERE  ( G.tp_status = " + contendoMovimento.getValue()   
						+ "				 AND G.cd_ait = A.cd_ait AND G.st_adesao_sne = " + stAdesaoSne.getValue()
						+ " )) "
						+ "AND B.dt_movimento BETWEEN '" + dtInicialMovimento.getValue() + "'" 
						+ "AND '" + dtFinalMovimento.getValue() + "'"
						+ "AND B.tp_status = " + contendoMovimento.getValue())
				.orderBy("A.dt_infracao DESC")
				.count()
				.build();
		return search;	
	}
	
	private Double TotalMultasSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		return searchTotalMultasSne(searchCriterios).getList(RelatorioAitSneDTO.class).get(0).getTotalMultas();
	}
	
	public Search<RelatorioAitSneDTO> searchTotalMultasSne(SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		SearchCriterios searchTotalMultas = new SearchCriterios();
		ItemComparator stSne = searchCriterios.getAndRemoveCriterio("B.st_adesao_sne");
		searchTotalMultas.setCriterios(searchCriterios.getCriterios());
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("sum(A.vl_multa) as total_multas")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.searchCriterios(searchTotalMultas)
				.additionalCriterias("B.st_adesao_sne = " + Integer.valueOf(stSne.getValue()))
				.count()
				.build();
		return search;	
	}
	
	private Double TotalMultasContendoMovimentoSne(SearchCriterios searchCriterios, Search<RelatorioAitSneDTO> searchContendoMovimento) throws ValidacaoException, Exception {
		return searchTotalMultasContendoMovimentoSne(searchCriterios, searchContendoMovimento).getList(RelatorioAitSneDTO.class).get(0).getTotalMultas();
	}
	
	public Search<RelatorioAitSneDTO> searchTotalMultasContendoMovimentoSne(SearchCriterios searchCriterios, Search<RelatorioAitSneDTO> searchContendoMovimento) throws ValidacaoException, Exception {
		SearchCriterios searchTotalMultas = new SearchCriterios();
		searchTotalMultas.setCriterios(searchCriterios.getCriterios());
		Search<RelatorioAitSneDTO> search = new SearchBuilder<RelatorioAitSneDTO>("mob_ait A")
				.fields("SUM(A.vl_multa) AS total_multas")	
				.addJoinTable("LEFT OUTER JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.additionalCriterias(searchContendoMovimento.getAdditionalCriterias())
				.searchCriterios(searchTotalMultas)
				.count()
				.build();
		return search;	
	}
	
	@Override
    public boolean eventoHasAit(int cdEvento) throws Exception {
        CustomConnection customConnection = new CustomConnection();
        try {
            customConnection.initConnection(false);
            boolean cdEventoUnico = eventoHasAit(cdEvento, customConnection);
            customConnection.finishConnection();
            return cdEventoUnico;
        } finally {
            customConnection.closeConnection();
        }
    }

    @Override
    public boolean eventoHasAit(int cdEvento, CustomConnection customConnection) throws Exception {
        SearchCriterios searchCriterios = new SearchCriterios();
        searchCriterios.addCriteriosEqualInteger("C.cd_evento", cdEvento);
        List<Ait> aits= this.aitRepository.findByEvento(searchCriterios, customConnection);

        return !aits.isEmpty();
    }
    
    private String incluirCondicionalRadar(String radar) throws Exception {
    	if(radar != null) {
    		List<Object[]> equipamentosAndNrFaixas = deserializeJsonRadar(radar);
    		List<String> criterios = new ArrayList();
    		String criterio;
            for (Object[] obj : equipamentosAndNrFaixas) {
            	if (obj[1].equals(0)) {
                    criterio = "F.cd_equipamento = " + obj[0];
                } else {
                    criterio = "(F.cd_equipamento = " + obj[0] + " AND H.nr_faixa = " + obj[1] + ")";
                }
            	criterios.add(criterio);
            }
            return concatCriterios(criterios);
    	}
    	return " 1=1 ";
	}
    
    private List<Object[]> deserializeJsonRadar(String radar) throws Exception {
        List<Object[]> radarDataList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        List<JsonNode> radares = Arrays.asList(objectMapper.readValue(radar, JsonNode[].class));
        radares.forEach(objRadar -> {
            int cdEquipamento = objRadar.get("cdEquipamento").asInt();
            int nrFaixa = objRadar.get("nrFaixa").asInt();
            radarDataList.add(new Object[]{cdEquipamento, nrFaixa});
        });
        return radarDataList;
    }
    
    private String concatCriterios(List<String> criterios) {
    	return criterios.stream().map(String::valueOf).collect(Collectors.joining(" OR "));
    }

	@Override
	public void definirComoInconsistente(Ait ait) throws Exception {
		checarSituacaoAit(ait);
		ait.setStAit(StConsistenciaAitEnum.ST_INCONSISTENTE.getKey());
		update(ait);
		atualizarAitMovimento(ait);
	}
	
	
	private void checarSituacaoAit(Ait ait) throws Exception {
		if (ait.getStAit() != StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey()) {
			throw new Exception("Este AIT não está pendente de validação.");
		}
	}
	

	private void atualizarAitMovimento(Ait ait) throws Exception {
		AitMovimento movimento = aitMovimentoService.getStatusMovimento(ait.getCdAit(),	TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		if (movimento.getLgEnviadoDetran() == TipoSituacaoAitEnum.NAO_ENVIADO.getKey()) {
			movimento.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIAR.getKey());
			aitMovimentoService.update(movimento);
		} else {
			if(movimento.getLgEnviadoDetran() == TipoSituacaoAitEnum.NAO_ENVIAR.getKey()) {
				movimento.setLgEnviadoDetran(TipoSituacaoAitEnum.NAO_ENVIADO.getKey());
				aitMovimentoService.update(movimento);
			}
		}
	}

	@Override
	public void reverterConsistencia(Ait ait) throws Exception {
		if(ait.getStAit() == StConsistenciaAitEnum.ST_INCONSISTENTE.getKey()) {
			atualizarAitMovimento(ait);			
		}
		ait.setStAit(StConsistenciaAitEnum.ST_PENDENTE_CONFIRMACAO.getKey());
		ait.setCdOcorrencia(0);
		ait.setTxtCancelamento(null);
		update(ait);
	}
}
