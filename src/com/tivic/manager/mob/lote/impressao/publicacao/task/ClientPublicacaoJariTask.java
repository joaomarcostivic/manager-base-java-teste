package com.tivic.manager.mob.lote.impressao.publicacao.task;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.PublicacaoResultadoJARIBuilder;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.TipoSituacaoPublicacaoDO;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.TipoLoteDocumentoEnum;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolosv3.publicacao.DocumentoPublicacaoMap;
import com.tivic.manager.ptc.protocolosv3.publicacao.MovimentoJulgamentoMap;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class ClientPublicacaoJariTask implements IClientPublicacaoJariTask {
	private IAitMovimentoService aitMovimentoService;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private DocumentoRepository documentoRepository;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private ServicoDetranServices servicoDetranServices;
	private AitMovimentoRepository aitMovimentoRepository;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private ManagerLog managerLog;
	private List<AitMovimento> aitMovimentoJulgamentoList;
	private IUsuarioRepository usuarioRepository;
	
	public ClientPublicacaoJariTask() throws Exception {
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitMovimentoJulgamentoList = new ArrayList<AitMovimento>();
		this.usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
	}
	
	@Override
	public void taskEnviarPublicacaoJari() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			managerLog.info("TASK PUBLICACAO JARI INICIADA: ", new GregorianCalendar().getTime().toString());
			List<LoteImpressao> loteImpressaoList = buscarLotesAguardandoEnvio();
			managerLog.info("QUANTIDADE DE LOTE PARA IMPRESSAO: ", String.valueOf(loteImpressaoList.size()));
			for (LoteImpressao loteImpressao : loteImpressaoList) {
				pegarDadosPublicacao(loteImpressao, customConnection);				
			}
			managerLog.info("QUANTIDADE DE AIT MOVIMENTO JULGAMENTO: ",  String.valueOf(this.aitMovimentoJulgamentoList.size()));
			for (AitMovimento aitMovimento : this.aitMovimentoJulgamentoList) {
				gerarPublicacaoResultadoJARI(aitMovimento.getCdAit(), aitMovimento.getNrProcesso(), aitMovimento.getDtPublicacaoDo());
			} 			
			for (LoteImpressao loteImpressao : loteImpressaoList) {
				loteImpressao.setStLoteImpressao(LoteImpressaoSituacao.ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_ENVIADO);
				this.loteImpressaoRepository.update(loteImpressao, customConnection);
			}			
			customConnection.finishConnection();
		} catch(NoContentException nce) {
			managerLog.info("NÃO EXISTE PUBLICACAO JARI: ", new GregorianCalendar().getTime().toString());
		} catch( Exception e) {
			managerLog.showLog(e);
		} finally {
			customConnection.closeConnection();
			managerLog.info("TASK PUBLICACAO JARI FINALIZADA: ", new GregorianCalendar().getTime().toString());
		}
	}
	
	private List<LoteImpressao> buscarLotesAguardandoEnvio() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.tp_documento", TipoLoteDocumentoEnum.LOTE_PUBLICACAO_RESULTADO_JARI.getKey());
		searchCriterios.addCriteriosEqualInteger("A.st_lote_impressao", Integer.valueOf(LoteImpressaoSituacao.AGUARDANDO_ENVIO));
		List<LoteImpressao> lotePublicacaoList = new SearchBuilder<LoteImpressao>("mob_lote_impressao A")
				.searchCriterios(searchCriterios)
				.build()
				.getList(LoteImpressao.class);
		if (lotePublicacaoList.isEmpty()) {
			throw new NoContentException("Nenhum lote de publicação a Jari pendente de envio encontrado");
		}
		return lotePublicacaoList;
	}	

	private void pegarDadosPublicacao(LoteImpressao lotePublicacao, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", lotePublicacao.getCdLoteImpressao());
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios, customConnection);
		int tpMovimento = new DocumentoPublicacaoMap().getCodMovimento(lotePublicacao.getTpDocumento());
		
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			AitMovimento aitMovimento 					= this.aitMovimentoService.getMovimentoTpStatus(loteImpressaoAit.getCdAit(), tpMovimento);
			SearchCriterios criteriosMovimento 			= new SearchCriterios();
			criteriosMovimento.addCriteriosEqualInteger("cd_ait", loteImpressaoAit.getCdAit());
			criteriosMovimento.addCriteriosEqualInteger("cd_movimento", aitMovimento.getCdMovimento());
			AitMovimentoDocumento aitMovimentoDocumento = this.aitMovimentoDocumentoRepository.find(criteriosMovimento, customConnection).get(0);
			Documento documentoPublicado 				= this.documentoRepository.get(aitMovimentoDocumento.getCdDocumento(), customConnection);
			int tpMovimentoJulgamento 					= new MovimentoJulgamentoMap().get(tpMovimento, documentoPublicado.getCdSituacaoDocumento());
			AitMovimento movimentoJulgamento 			= getJariSemMovimentoPublicacaoRegistrada(loteImpressaoAit.getCdAit(), tpMovimentoJulgamento);
			if (movimentoJulgamento != null) {
	            this.aitMovimentoJulgamentoList.add(movimentoJulgamento);
	        }
		}
	}
	
	private AitMovimento getJariSemMovimentoPublicacaoRegistrada(int cdAit, int tpStatus) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", tpStatus);
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>(" mob_ait_movimento A")
				.fields("A.*")
				.searchCriterios(searchCriterios)
				.additionalCriterias(" NOT EXISTS ( "
				+ "	SELECT 1 FROM mob_ait_movimento A2 "
				+ "		WHERE A.cd_ait = A2.cd_ait AND A2.tp_status = "+ TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey()
			    + " 	AND A2.lg_enviado_detran = "+ TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
			    + " 	AND NOT EXISTS ("
			    + "			SELECT 1 FROM mob_ait_movimento A3"
			    + "				WHERE A2.cd_ait = A3.cd_ait AND A3.tp_status = " + TipoStatusEnum.CANCELAMENTO_PUBLICACAO_RESULTADO_JARI.getKey()
			    + " 			AND A3.lg_enviado_detran = "+ TipoLgEnviadoDetranEnum.REGISTRADO.getKey()
			    + " 			AND A3.dt_movimento < A2.dt_movimento ) "
			    + " ) "
			    ).build();
		if(search.getList(AitMovimento.class).size() > 0) {
			return search.getList(AitMovimento.class).get(0);
		}
		return null;
	}
	
	private void gerarPublicacaoResultadoJARI(int cdAit, String nrProcesso, GregorianCalendar dtPublicacao) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
			AitMovimento aitMovimento = new AitMovimento();
			AitMovimento movimentoPublicacao = aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey());
			if (movimentoPublicacao.getCdMovimento() > 0) {
				aitMovimento = movimentoPublicacao;
			} else {
				aitMovimento = new PublicacaoResultadoJARIBuilder()
						.setCdAit(cdAit).setTpStatus(TipoStatusEnum.PUBLICACAO_RESULTADO_JARI.getKey())
						.setNrProcesso(nrProcesso).setDtPublicacaoDo(dtPublicacao).setDtMovimento(DateUtil.getDataAtual())
						.setStPublicacaoDo(TipoSituacaoPublicacaoDO.PUBLICADO_DO.getKey()).setCdUsuario(getUsuario()).build();
				aitMovimentoRepository.insert(aitMovimento, customConnection);
			}
			aitMovimentoList.add(aitMovimento);
			customConnection.finishConnection();
			servicoDetranServices.remessa(aitMovimentoList);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private int getUsuario() throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("nm_login", "TIVIC");
		List<Usuario> usuario = this.usuarioRepository.find(searchCriterios, new CustomConnection());
		if (usuario.isEmpty()) {
			throw new ValidacaoException("Usuário TIVIC não localizado.");
		}
		return usuario.get(0).getCdUsuario();	
	}
	
}
