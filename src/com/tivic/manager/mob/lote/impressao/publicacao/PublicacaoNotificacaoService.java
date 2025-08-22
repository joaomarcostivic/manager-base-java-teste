package com.tivic.manager.mob.lote.impressao.publicacao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.lote.impressao.publicacao.builder.LotePublicacaoNotificacaoDtoBuilder;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.LotePublicacaoNotificacaoDto;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.NotificacaoPublicacaoPendenteDto;
import com.tivic.manager.mob.lote.impressao.publicacao.map.TipoPublicacaoNotificacaoMap;
import com.tivic.manager.mob.lote.impressao.publicacao.service.ConfirmaPublicacaoNotificacao;
import com.tivic.manager.mob.lote.impressao.publicacao.service.GeraEditalPublicacaoNotificacao;
import com.tivic.manager.mob.lote.impressao.publicacao.service.GeraLotePublicacaoNotificacao;
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

public class PublicacaoNotificacaoService implements IPublicacaoNotificacaoService {
	
	private IArquivoService arquivoService;
	private ILoteNotificacaoService loteNotificacaoService;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private IAitMovimentoService aitMovimentoServices;
	private ServicoDetranServices servicoDetranServices;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private IParametroRepository parametroRepository;
	
	public PublicacaoNotificacaoService() throws Exception {
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public PagedResponse<LotePublicacaoNotificacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			Search<LotePublicacaoNotificacaoDto> publicacoesSearch = buscarArquivosPublicados(searchCriterios, customConnection);
			if (publicacoesSearch.getList(LotePublicacaoNotificacaoDto.class).isEmpty()) {
				throw new ValidacaoException("Nenhum Registro encontrado.");
			}
			List<LotePublicacaoNotificacaoDto> lotePublicacaoDtoList = publicacoesSearch.getList(LotePublicacaoNotificacaoDto.class);
			customConnection.finishConnection();
			return new PagedResponse<LotePublicacaoNotificacaoDto>(lotePublicacaoDtoList, publicacoesSearch.getRsm().getTotal());
		} finally {
			customConnection.closeConnection();	
		}
	}
	
	@Override
	public Search<LotePublicacaoNotificacaoDto> buscarArquivosPublicados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		String codsPublicacaoNotificacao = String.valueOf(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey())+ ", "
										+ String.valueOf(TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NIP.getKey());
		searchCriterios.addCriterios("A.tp_documento", codsPublicacaoNotificacao, ItemComparator.IN, Types.INTEGER);
		Search<LotePublicacaoNotificacaoDto> publicacoes = new SearchBuilder<LotePublicacaoNotificacaoDto>("mob_lote_impressao A")
				.fields("DISTINCT (D.cd_lote_impressao), A.cd_lote_impressao, A.tp_documento, A.dt_criacao, A.dt_envio AS dt_publicacao, "
						+ "B.cd_pessoa, C.nm_pessoa AS nm_usuario")
				.addJoinTable("JOIN seg_usuario B ON(B.cd_usuario = A.cd_usuario)")
				.addJoinTable("JOIN grl_pessoa C ON(C.cd_pessoa = B.cd_pessoa)")
				.addJoinTable("JOIN mob_lote_impressao_ait D ON(A.cd_lote_impressao = D.cd_lote_impressao)")
				.addJoinTable("JOIN mob_ait E ON(D.cd_ait = E.cd_ait)")
				.searchCriterios(searchCriterios)
				.orderBy(" A.dt_criacao DESC ")
				.count()
			.build();
		return publicacoes;
	}
	
	public PagedResponse<NotificacaoPublicacaoPendenteDto> buscarPendentesPublicacao(SearchCriterios searchCriterios, boolean lgNaoEntregues, int tpDocumetno) throws Exception {
		Search<NotificacaoPublicacaoPendenteDto> search = lgNaoEntregues ? buscarPendentesPublicacaoComAr(searchCriterios, tpDocumetno) : buscarPendentesPublicacaoSemAr(searchCriterios, tpDocumetno);
		List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtoList = search.getList(NotificacaoPublicacaoPendenteDto.class);
		if (notificacaoPublicacaoPendenteDtoList.isEmpty()) {
			throw new NoContentException("Nenhum Registro encontrado.");
		}
		PagedResponse<NotificacaoPublicacaoPendenteDto> pagedNotificacaoPublicacaoPendenteDto = 
				new PagedResponse<NotificacaoPublicacaoPendenteDto>(notificacaoPublicacaoPendenteDtoList, search.getRsm().getTotal());
		return pagedNotificacaoPublicacaoPendenteDto;
	}
	
	private Search<NotificacaoPublicacaoPendenteDto> buscarPendentesPublicacaoComAr(SearchCriterios searchCriterios, int tpDocumento) throws Exception {
		String notificacaoEntregue = "1";
		String notificacaoPublicada = "100";
		searchCriterios.addCriterios("D1.st_aviso_recebimento", notificacaoEntregue, ItemComparator.GREATER, Types.INTEGER);
		searchCriterios.addCriterios("D1.st_aviso_recebimento", notificacaoPublicada, ItemComparator.MINOR, Types.INTEGER);
		searchCriterios.addCriteriosEqualInteger("C.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<NotificacaoPublicacaoPendenteDto> search = new SearchBuilder<NotificacaoPublicacaoPendenteDto>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, B.nr_cod_detran, C.dt_movimento, "
						+ "(A.vl_multa - (20 * A.vl_multa) /100 ) as vl_multa_desconto")
				.addJoinTable(" INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao) ")
				.addJoinTable(" INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait) ")
				.addJoinTable(" INNER JOIN mob_correios_etiqueta D1 ON (A.cd_ait = D1.cd_ait )")
				.searchCriterios(searchCriterios)
				.additionalCriterias( " NOT EXISTS "
							+ " ("
							+ "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D"
							+ "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
							+ "		WHERE"
							+ "			("
							+ "				D.tp_documento = " + tpDocumento
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
	
	private Search<NotificacaoPublicacaoPendenteDto> buscarPendentesPublicacaoSemAr(SearchCriterios searchCriterios, int tpDocumento) throws Exception {
		searchCriterios.addCriteriosEqualInteger("C.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey());
		Search<NotificacaoPublicacaoPendenteDto> search = new SearchBuilder<NotificacaoPublicacaoPendenteDto>("mob_ait A")
				.fields("A.cd_ait, A.id_ait, A.nr_placa, A.dt_infracao, A.ds_local_infracao, A.vl_multa, B.nr_cod_detran, C.dt_movimento, "
						+ "(A.vl_multa - (20 * A.vl_multa) /100 ) as vl_multa_desconto")
				.addJoinTable(" INNER JOIN mob_infracao B on (A.cd_infracao = B.cd_infracao) ")
				.addJoinTable(" INNER JOIN mob_ait_movimento C on (A.cd_ait = C.cd_ait) ")
				.searchCriterios(searchCriterios)
				.additionalCriterias( " NOT EXISTS "
						   + " ("
						   + "		SELECT D.cd_lote_impressao, D.tp_lote_impressao, E.cd_ait FROM mob_lote_impressao D"
						   + "		JOIN mob_lote_impressao_ait E ON (D.cd_lote_impressao = E.cd_lote_Impressao)"
						   + "		WHERE"
						   + "			("
						   + "				D.tp_documento = " + tpDocumento
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
		if (tpDocumento == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey() ) {
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
		if( tpDocumento == TipoLoteDocumentoEnum.LOTE_PUBLICACAO_NAI.getKey()) {
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
	public Arquivo publicar(int cdUsuario, int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao lotePublicacao = new GeraLotePublicacaoNotificacao(cdUsuario, tpDocumento, notificacaoPublicacaoPendenteDtos)
					.gerarLoteImpressao(customConnection);
			byte[] printNotificacoesPublicadas = new GeraEditalPublicacaoNotificacao().gerar(tpDocumento, notificacaoPublicacaoPendenteDtos);
			Arquivo arquivoPublicacao = salvarDocumentoLote(lotePublicacao, printNotificacoesPublicadas, cdUsuario, customConnection);
			vincularArquivoPublicacao(lotePublicacao, arquivoPublicacao, customConnection);
			customConnection.finishConnection();
			return arquivoPublicacao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Arquivo salvarDocumentoLote(LoteImpressao loteImpressao, byte[] bytePdfPublicacao, int cdUsuario, CustomConnection customConnection) throws Exception {
		Arquivo arquivoPublicacao = new ArquivoBuilder()
				.setBlbArquivo(bytePdfPublicacao)
				.setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_PUBLICACAO_NOTIFICACAO_" + loteImpressao.getCdLoteImpressao() + ".doc" )
				.setNmDocumento("Lote de Publicação Notificação")
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_TIPO_ARQUIVO_PUBLICACAO_NOTIFICACOES")))
				.build();
		this.arquivoService.save(arquivoPublicacao, customConnection);
		loteImpressao.setCdArquivo(arquivoPublicacao.getCdArquivo());
		this.loteNotificacaoService.save(loteImpressao, customConnection);
		return arquivoPublicacao;
	}
	
	private void vincularArquivoPublicacao(LoteImpressao loteImpressao, Arquivo arquivoPublicacao, CustomConnection customConnection) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoPublicacao.getCdArquivo())
				.setCdLoteImpressao(loteImpressao.getCdLoteImpressao())
				.build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, customConnection);
	}

	@Override
	public LotePublicacaoNotificacaoDto confirmar(int cdLoteImpressao, GregorianCalendar dtConfirmacaoPublicado, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao lotePublicacao = new ConfirmaPublicacaoNotificacao(customConnection)
					.confirmar(cdLoteImpressao, dtConfirmacaoPublicado, cdUsuario);
			customConnection.finishConnection();
			LotePublicacaoNotificacaoDto lotePublicacaoNotificacaoDto = new LotePublicacaoNotificacaoDtoBuilder(lotePublicacao).build();
			return lotePublicacaoNotificacaoDto;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<LoteImpressao> buscarLotesAguardandoEnvio(int tpDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.tp_documento", tpDocumento);
		searchCriterios.addCriteriosEqualInteger("A.st_lote_impressao", Integer.valueOf(LoteImpressaoSituacao.AGUARDANDO_ENVIO));
		List<LoteImpressao> lotePublicacaoList = new SearchBuilder<LoteImpressao>("mob_lote_impressao A")
				.searchCriterios(searchCriterios)
				.build()
				.getList(LoteImpressao.class);
		if (lotePublicacaoList.isEmpty()) {
			throw new NoContentException("Nenhum lote de publicação pendente de envio encontrado");
		}
		return lotePublicacaoList;
	}

	@Override
	public List<ServicoDetranDTO> enviarPublicacoesLote(LoteImpressao lotePublicacao) throws Exception {
		return enviarPublicacoesLote(lotePublicacao, new CustomConnection());
	}
	@Override
	public List<ServicoDetranDTO> enviarPublicacoesLote(LoteImpressao lotePublicacao, CustomConnection customConnection) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		try {
			customConnection.initConnection(true);
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", lotePublicacao.getCdLoteImpressao());
			List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios, customConnection);
			List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
			int tpMovimento = new TipoPublicacaoNotificacaoMap().getStatusMovimentoNotificacao(lotePublicacao.getTpDocumento());
			int tpStatusPublicacao =  new TipoPublicacaoNotificacaoMap().getStatusMovimentoPublicacao(tpMovimento);
			for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
				AitMovimento movimentoPublicacao = this.aitMovimentoServices.getMovimentoTpStatus(loteImpressaoAit.getCdAit(), tpStatusPublicacao);
				if(movimentoPublicacao.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey())
					aitMovimentoList.add(movimentoPublicacao);
			}
			List<ServicoDetranDTO> retornoDetran = servicoDetranServices.remessa(aitMovimentoList);
			lotePublicacao.setStLoteImpressao(LoteImpressaoSituacao.ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_ENVIADO);
			this.loteImpressaoRepository.update(lotePublicacao, customConnection);
			customConnection.finishConnection();
			return retornoDetran;
		} finally {
			customConnection.closeConnection();
		}
	}
	
}
