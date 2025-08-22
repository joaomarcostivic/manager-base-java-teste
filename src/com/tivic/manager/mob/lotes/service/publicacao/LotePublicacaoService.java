package com.tivic.manager.mob.lotes.service.publicacao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoSearch;
import com.tivic.manager.mob.lotes.dto.publicacao.LotePublicacaoNotificacaoDTO;
import com.tivic.manager.mob.lotes.dto.publicacao.NotificacaoPublicacaoPendenteDTO;
import com.tivic.manager.mob.lotes.enums.publicacao.TipoLotePublicacaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.publicacao.map.TipoPublicacaoMap;
import com.tivic.manager.mob.lotes.publicacao.strategy.EditalPublicacaoGenerator;
import com.tivic.manager.mob.lotes.publicacao.strategy.LotePublicacaoGenerator;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.ILotePublicacaoAitRepository;
import com.tivic.manager.mob.lotes.repository.publicacao.LotePublicacaoRepository;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class LotePublicacaoService implements ILotePublicacaoService {
	private LotePublicacaoRepository lotePublicacaoRepository;
	private IParametroRepository parametroRepository;
	private ILotePublicacaoAitRepository lotePublicacaoAitRepository;
	private LoteRepository loteRepository;
	private IArquivoService arquivoService;
	private ServicoDetranServices servicoDetranServices;
	private IAitMovimentoService aitMovimentoServices;
	
	public LotePublicacaoService() throws Exception {
		this.lotePublicacaoRepository = (LotePublicacaoRepository) BeansFactory.get(LotePublicacaoRepository.class);
		this.lotePublicacaoAitRepository = (ILotePublicacaoAitRepository) BeansFactory.get(ILotePublicacaoAitRepository.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	@Override
	public PagedResponse<LotePublicacaoNotificacaoDTO> buscarArquivosPublicados(LotePublicacaoSearch lotePublicacaoSearch) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LotePublicacaoNotificacaoDTO> publicacoesSearch = buscarArquivosPublicados(lotePublicacaoSearch, customConnection);
			if (publicacoesSearch.getList(LotePublicacaoNotificacaoDTO.class).isEmpty()) {
				throw new ValidacaoException("Nenhum registro encontrado.");
			}
			List<LotePublicacaoNotificacaoDTO> lotePublicacaoDtoList = publicacoesSearch.getList(LotePublicacaoNotificacaoDTO.class);
			customConnection.finishConnection();
			return new PagedResponse<LotePublicacaoNotificacaoDTO>(lotePublicacaoDtoList, publicacoesSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();	
		}
	}
	
	@Override
	public Search<LotePublicacaoNotificacaoDTO> buscarArquivosPublicados(LotePublicacaoSearch lotePublicacaoSearch, CustomConnection customConnection) throws Exception {
	    Search<LotePublicacaoNotificacaoDTO> publicacoes = new SearchBuilder<LotePublicacaoNotificacaoDTO>("mob_lote_publicacao A")
	        .fields("DISTINCT (B.cd_lote_publicacao), A.cd_lote_publicacao, A.tp_publicacao, C.dt_criacao, A.dt_publicacao, "
	              + "D.cd_pessoa, E.nm_pessoa AS nm_usuario")
	        .addJoinTable("JOIN mob_lote_publicacao_ait B ON (A.cd_lote_publicacao = B.cd_lote_publicacao)")
	        .addJoinTable("LEFT OUTER JOIN grl_lote C ON (A.cd_lote = C.cd_lote)")
	        .addJoinTable("LEFT OUTER JOIN seg_usuario D ON (D.cd_usuario = C.cd_criador)")
	        .addJoinTable("LEFT OUTER JOIN grl_pessoa E ON (E.cd_pessoa = D.cd_pessoa)")
	        .addJoinTable("JOIN mob_ait F ON (B.cd_ait = F.cd_ait)")
	        .searchCriterios(setCriteriosSearchLotes(lotePublicacaoSearch))
	        .orderBy("C.dt_criacao DESC")
	        .count()
	        .build();
	    return publicacoes;
	}
	
	private SearchCriterios setCriteriosSearchLotes(LotePublicacaoSearch lotePublicacaoSearch) {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    String codsPublicacaoNotificacao = String.valueOf(TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NAI.getKey()) + ", " +
	                                       String.valueOf(TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NIP.getKey());
	    searchCriterios.addCriterios("A.tp_publicacao", codsPublicacaoNotificacao, ItemComparator.IN, Types.INTEGER);
	    searchCriterios.addCriteriosEqualInteger("A.tp_publicacao", lotePublicacaoSearch.getTpPublicacao(), lotePublicacaoSearch.getTpPublicacao() > 0);
	    searchCriterios.addCriteriosGreaterDate("C.dt_criacao", lotePublicacaoSearch.getDtCriacaoInicial(), lotePublicacaoSearch.getDtCriacaoInicial() != null);
	    searchCriterios.addCriteriosMinorDate("C.dt_criacao", lotePublicacaoSearch.getDtCriacaoFinal(), lotePublicacaoSearch.getDtCriacaoFinal() != null);
	    searchCriterios.addCriteriosGreaterDate("A.dt_publicacao", lotePublicacaoSearch.getDtPublicacaoInicial(), lotePublicacaoSearch.getDtPublicacaoInicial() != null);
	    searchCriterios.addCriteriosMinorDate("A.dt_publicacao", lotePublicacaoSearch.getDtPublicacaoFinal(), lotePublicacaoSearch.getDtPublicacaoFinal() != null);
	    searchCriterios.addCriterios("F.id_ait", "%" + lotePublicacaoSearch.getIdAit() + "%", ItemComparator.LIKE, Types.VARCHAR,
	                                 lotePublicacaoSearch.getIdAit() != null && !lotePublicacaoSearch.getIdAit().isEmpty());
	    searchCriterios.setQtDeslocamento((lotePublicacaoSearch.getLimit() * lotePublicacaoSearch.getPage()) - lotePublicacaoSearch.getLimit());
	    searchCriterios.setQtLimite(lotePublicacaoSearch.getLimit());
	    return searchCriterios;
	}
	
	@Override
	public PagedResponse<NotificacaoPublicacaoPendenteDTO> buscarPendentesPublicacao(SearchCriterios searchCriterios, boolean lgNaoEntregues, int tpDocumetno) throws Exception {
		Search<NotificacaoPublicacaoPendenteDTO> search = lgNaoEntregues ? buscarPendentesPublicacaoComAr(searchCriterios, tpDocumetno) : buscarPendentesPublicacaoSemAr(searchCriterios, tpDocumetno);
		List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtoList = search.getList(NotificacaoPublicacaoPendenteDTO.class);
		if (notificacaoPublicacaoPendenteDtoList.isEmpty()) {
			throw new NoContentException("Nenhum Registro encontrado.");
		}
		PagedResponse<NotificacaoPublicacaoPendenteDTO> pagedNotificacaoPublicacaoPendenteDto = 
				new PagedResponse<NotificacaoPublicacaoPendenteDTO>(notificacaoPublicacaoPendenteDtoList, search.getRsm().getTotal());
		return pagedNotificacaoPublicacaoPendenteDto;
	}
	
	private Search<NotificacaoPublicacaoPendenteDTO> buscarPendentesPublicacaoComAr(SearchCriterios searchCriterios, int tpDocumento) throws Exception {
		String notificacaoEntregue = "1";
		String notificacaoPublicada = "100";
		searchCriterios.addCriterios("D1.st_aviso_recebimento", notificacaoEntregue, ItemComparator.GREATER, Types.INTEGER);
		searchCriterios.addCriterios("D1.st_aviso_recebimento", notificacaoPublicada, ItemComparator.MINOR, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("C.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<NotificacaoPublicacaoPendenteDTO> search = new SearchBuilder<NotificacaoPublicacaoPendenteDTO>("mob_ait A")
				.fields("DISTINCT A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, A.dt_movimento, B.nr_cod_detran, C.dt_movimento, "
						+ "(A.vl_multa - (20 * A.vl_multa) /100 ) as vl_multa_desconto")
				.addJoinTable(" INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao) ")
				.addJoinTable(" INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait) ")
				.addJoinTable(" INNER JOIN mob_correios_etiqueta D1 ON (A.cd_ait = D1.cd_ait )")
				.searchCriterios(searchCriterios)
				.additionalCriterias( " NOT EXISTS "
							+ " ("
							+ "		SELECT D.cd_lote_publicacao, D.tp_publicacao, E.cd_ait FROM mob_lote_publicacao D"
							+ "		JOIN mob_lote_publicacao_ait E ON (D.cd_lote_publicacao = E.cd_lote_publicacao)"
							+ "		WHERE"
							+ "			("
							+ "				D.tp_publicacao = " + tpDocumento
							+ "			)"
							+ "	AND E.cd_ait = A.cd_ait"
							+ " ) "
							+ " AND A.cd_ait NOT IN (" 
							+ "	SELECT DISTINCT C1.cd_ait FROM mob_ait_movimento C1"  
							+ "		WHERE (" 
							+ "			C1.tp_status IN(" 
							+	    		TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() + ", "  
							+    			TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey() + ", "
							+        		TipoStatusEnum.CANCELAMENTO_NIP.getKey() + " "
							+ "  		)"
						    + "		) " 
							+ "	GROUP BY C1.cd_ait" 
						    + " ) ")
				.additionalCriterias(incluirBuscaMovimentoImpedimento(searchCriterios, tpDocumento))
				.additionalCriterias(incluirMovimentosValidos(tpDocumento))
				.orderBy(" A.dt_movimento DESC ")
				.count()
				.build();
		return search;
	}
	
	private Search<NotificacaoPublicacaoPendenteDTO> buscarPendentesPublicacaoSemAr(SearchCriterios searchCriterios, int tpDocumento) throws Exception {
		searchCriterios.addCriteriosEqualInteger("C.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<NotificacaoPublicacaoPendenteDTO> search = new SearchBuilder<NotificacaoPublicacaoPendenteDTO>("mob_ait A")
				.fields("DISTINCT A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, A.dt_movimento, B.nr_cod_detran, C.dt_movimento, "
						+ "(A.vl_multa - (20 * A.vl_multa) /100 ) as vl_multa_desconto")
				.addJoinTable(" INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao) ")
				.addJoinTable(" INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias( " NOT EXISTS "
						   + " ("
						   + "		SELECT D.cd_lote_publicacao, D.tp_publicacao, E.cd_ait FROM mob_lote_publicacao D"
						   + "		JOIN mob_lote_publicacao_ait E ON (D.cd_lote_publicacao = E.cd_lote_publicacao)"
						   + "		WHERE"
						   + "			("
						   + "				D.tp_publicacao = " + tpDocumento
						   + "			)"
						   + "	AND E.cd_ait = A.cd_ait"
						   + " ) "
						   + "  AND A.cd_ait NOT IN (" 
						   + "	SELECT DISTINCT C1.cd_ait FROM mob_ait_movimento C1"  
						   + "		WHERE (" 
						   + "			C1.tp_status IN(" 
						   +	    		TipoStatusEnum.CANCELA_REGISTRO_MULTA.getKey() + ","  
						   +    			TipoStatusEnum.CANCELAMENTO_AUTUACAO.getKey() + ","
						   + 				TipoStatusEnum.CANCELAMENTO_MULTA.getKey() + ""
						   + "  		)"
						   + "		) " 
						   + "	GROUP BY C1.cd_ait" 
						   + " ) ")
				.additionalCriterias(incluirBuscaMovimentoImpedimento(searchCriterios, tpDocumento))
				.additionalCriterias(incluirMovimentosValidos(tpDocumento))
				.orderBy(" A.dt_movimento DESC ")
				.count()
				.build();
		return search;
	}
	
	private String incluirBuscaMovimentoImpedimento(SearchCriterios searchCriterios, int tpDocumento) throws Exception {
		boolean parametroListarAits = parametroRepository.getValorOfParametroAsBoolean("MOB_PUBLICACAO_NIP_TODAS_SITUACOES");
		if (tpDocumento == TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NAI.getKey() ) {
 			return " A.cd_ait NOT IN ( SELECT DISTINCT C2.cd_ait FROM mob_ait_movimento C2 "
			        + " WHERE " 
					+ " 	C2.tp_status IN(" 
					+	    	TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey() + ", "  
					+    		TipoStatusEnum.DEFESA_PREVIA.getKey() + ", "
					+        	TipoStatusEnum.NIP_ENVIADA.getKey() + ", "
					+        	TipoStatusEnum.MULTA_PAGA.getKey() + ""
					+ "  	) "
			        + " ) ";
 		}
		if (parametroListarAits) {
			return null;
		} else {
			return " A.cd_ait NOT IN ( SELECT DISTINCT C2.cd_ait FROM mob_ait_movimento C2 "
					+ " WHERE" 
					+ " 	C2.tp_status IN(" 
					+	    	TipoStatusEnum.RECURSO_JARI.getKey() + ", "  
					+    		TipoStatusEnum.RECURSO_CETRAN.getKey() + ", "
					+        	TipoStatusEnum.MULTA_PAGA.getKey() + ""
					+ "  	)"
					+ " ) ";
		}
	}
	
	private String incluirMovimentosValidos(int tpDocumento) throws Exception {
		if( tpDocumento == TipoLotePublicacaoEnum.LOTE_PUBLICACAO_NAI.getKey()) {
			return null;
		}
		return "EXISTS ( SELECT C2.tp_status " 
				+ "FROM mob_ait_movimento C2 "
				+ "WHERE (C2.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + ") "
				+ 			"AND C2.cd_ait = A.cd_ait "
				+			"AND NOT EXISTS ( "
				+ 			"SELECT C3.tp_status, C3.dt_movimento, C3.lg_enviado_detran "
				+ 			"FROM mob_ait_movimento C3 "
				+			"WHERE (C3.tp_status = " + TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
				+			" AND C3.dt_movimento > ( "
				+ 			"SELECT C4.dt_movimento FROM mob_ait_movimento C4 WHERE C4.tp_status = " + TipoStatusEnum.NIP_ENVIADA.getKey() + " AND C4.cd_ait = A.cd_ait "
						+ 	"	ORDER BY C4.dt_movimento DESC LIMIT 1) "
				+ 			" AND C3.cd_ait = A.cd_ait)) AND C2.cd_ait = A.cd_ait)";
	}
	
	@Override
	public Arquivo publicar(int cdUsuario, int tpPublicacao, List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LotePublicacao lotePublicacao = new LotePublicacaoGenerator(cdUsuario, tpPublicacao, notificacaoPublicacaoPendenteDtos)
					.gerarLoteImpressao(customConnection);
			byte[] printNotificacoesPublicadas = new EditalPublicacaoGenerator().gerar(tpPublicacao, notificacaoPublicacaoPendenteDtos);
			Arquivo arquivoPublicacao = salvarDocumentoLote(lotePublicacao, printNotificacoesPublicadas, cdUsuario, customConnection);
			vincularArquivoPublicacao(lotePublicacao, arquivoPublicacao, customConnection);
			customConnection.finishConnection();
			return arquivoPublicacao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Arquivo salvarDocumentoLote(LotePublicacao lotePublicacao, byte[] bytePdfPublicacao, int cdUsuario, CustomConnection customConnection) throws Exception {
		Arquivo arquivoPublicacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfPublicacao)
				.setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_PUBLICACAO_NOTIFICACAO_" + lotePublicacao.getCdLotePublicacao() + ".doc" )
				.setNmDocumento("Lote de Publicação Notificação")
				.setDtArquivamento(DateUtil.getDataAtual())
				.setDtCriacao(DateUtil.getDataAtual())
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PUBLICACAO_NOTIFICACOES")))
				.build();
		this.arquivoService.save(arquivoPublicacao, customConnection);
		return arquivoPublicacao;
	}
	
	private void vincularArquivoPublicacao(LotePublicacao lotePublicacao, Arquivo arquivoPublicacao, CustomConnection customConnection) throws Exception {
		Lote lote = this.loteRepository.get(lotePublicacao.getCdLote(), customConnection);
		lote.setCdArquivo(arquivoPublicacao.getCdArquivo());
		this.loteRepository.update(lote, customConnection);
	}
	
	@Override
	public LotePublicacao save(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception{
		try {
			if(lotePublicacao == null)
				throw new ValidacaoException("Erro ao salvar. LotePublicacao é nulo.");
			customConnection.initConnection(true);
			if(lotePublicacao.getCdLotePublicacao() == 0){
				lotePublicacao.setCdLotePublicacao(lotePublicacaoRepository.insert(lotePublicacao, customConnection).getCdLotePublicacao());
			} else {
				lotePublicacao = lotePublicacaoRepository.update(lotePublicacao, customConnection);
				customConnection.finishConnection();
				return lotePublicacao;
			} if(lotePublicacao.getAits()!=null && lotePublicacao.getAits().size()>0) {
				for (LotePublicacaoAit lotePublicacaoAit : lotePublicacao.getAits()) {
					lotePublicacaoAit.setCdLotePublicacao(lotePublicacao.getCdLotePublicacao());
					criarLotePublicacaoAit(lotePublicacao, lotePublicacaoAit.getCdAit(), customConnection);
				}
			}
			customConnection.finishConnection();
			return lotePublicacao;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private void criarLotePublicacaoAit(LotePublicacao lotePublicacao, int cdAit, CustomConnection customConnection) throws Exception {
		LotePublicacaoAit lotePublicacaoAit = new LotePublicacaoAitBuilder()
				.setCdLotePublicacao(lotePublicacao.getCdLotePublicacao())
				.setCdAit(cdAit)
				.build();
		lotePublicacaoAitRepository.insert(lotePublicacaoAit, customConnection);
	}
	
	@Override
	public LotePublicacaoNotificacaoDTO confirmar(int cdLotePublicacao, GregorianCalendar dtConfirmacaoPublicado, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LotePublicacao lotePublicacao = new ConfirmaPublicacao(customConnection)
					.confirmar(cdLotePublicacao, dtConfirmacaoPublicado, cdUsuario);
			customConnection.finishConnection();
			Lote lote = this.loteRepository.get(lotePublicacao.getCdLote(), customConnection);
			LotePublicacaoNotificacaoDTO lotePublicacaoNotificacaoDto = new LotePublicacaoNotificacaoDTO();
			lotePublicacaoNotificacaoDto.setCdLotePublicacao(lotePublicacao.getCdLotePublicacao());
			lotePublicacaoNotificacaoDto.setDtPublicacao(lotePublicacao.getDtPublicacao());
			lotePublicacaoNotificacaoDto.setTpPublicacao(lotePublicacao.getTpPublicacao());
			lotePublicacaoNotificacaoDto.setDtCriacao(lote.getDtCriacao());
			return lotePublicacaoNotificacaoDto;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Arquivo getArquivoLote(int cdLotePublicacao, int tpArquivo) throws Exception { 
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("C.cd_lote_publicacao", cdLotePublicacao);
		searchCriterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", tpArquivo);
		List<Arquivo> arquivoList = searchArquivoLote(searchCriterios).getList(Arquivo.class);
		if(arquivoList.isEmpty()) {
			throw new NoContentException("Nenhum arquivo encontrado.");
		}
		Arquivo arquivo = arquivoList.get(0);
		return arquivo;
	}

	private Search<Arquivo> searchArquivoLote(SearchCriterios criterios) throws Exception {
		Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo A")
				.addField("A.cd_arquivo, A.dt_arquivamento, A.cd_tipo_arquivo, A.blb_arquivo")
				.addJoinTable(" JOIN grl_lote B ON (B.cd_arquivo = A.cd_arquivo) ")
				.addJoinTable(" JOIN mob_lote_publicacao C ON (C.cd_lote = B.cd_lote) ")
				.searchCriterios(criterios)
				.build();
		return search;
	}

	@Override
	public List<LotePublicacao> buscarLotesAguardandoEnvio(int tpPublicacao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.tp_publicacao", tpPublicacao);
		searchCriterios.addCriteriosEqualInteger("A.st_lote", Integer.valueOf(LoteImpressaoSituacao.AGUARDANDO_ENVIO));
		List<LotePublicacao> lotePublicacaoList = new SearchBuilder<LotePublicacao>("mob_lote_publicacao A")
				.searchCriterios(searchCriterios)
				.build()
				.getList(LotePublicacao.class);
		if (lotePublicacaoList.isEmpty()) {
			throw new NoContentException("Nenhum lote de publicação pendente de envio encontrado");
		}
		return lotePublicacaoList;
	}

	@Override
	public List<ServicoDetranDTO> enviarPublicacoesLote(LotePublicacao lotePublicacao) throws Exception {
		return enviarPublicacoesLote(lotePublicacao, new CustomConnection());
	}

	@Override
	public List<ServicoDetranDTO> enviarPublicacoesLote(LotePublicacao lotePublicacao, CustomConnection customConnection) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		try {
			customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_lote_publicacao", lotePublicacao.getCdLotePublicacao());
			List<LotePublicacaoAit> lotePublicacaoAitList = this.lotePublicacaoAitRepository.find(searchCriterios, customConnection);
			List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
			int tpMovimento = new TipoPublicacaoMap().getStatusMovimentoNotificacao(lotePublicacao.getTpPublicacao());
			int tpStatusPublicacao =  new TipoPublicacaoMap().getStatusMovimentoPublicacao(tpMovimento);
			for (LotePublicacaoAit lotePublicacaoAit : lotePublicacaoAitList) {
				AitMovimento movimentoPublicacao = this.aitMovimentoServices.getMovimentoTpStatus(lotePublicacaoAit.getCdAit(), tpStatusPublicacao);
				if(movimentoPublicacao.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey())
					aitMovimentoList.add(movimentoPublicacao);
			}
			List<ServicoDetranDTO> retornoDetran = servicoDetranServices.remessa(aitMovimentoList);
			lotePublicacao.setStLote(LoteImpressaoSituacao.ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_ENVIADO);
			this.lotePublicacaoRepository.update(lotePublicacao, customConnection);
			customConnection.finishConnection();
			return retornoDetran;
		} finally {
			customConnection.closeConnection();
		}
	}
}
