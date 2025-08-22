package com.tivic.manager.mob.lote.impressao;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportDataJariNIP;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.builders.AitBuilder;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitBuilder.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lote.impressao.LoteNotificacaoBuilder.LoteNotificacaoDTOListBuilder;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.manager.mob.lote.impressao.geracaodocumento.GeracaoDocumentosSSE;
import com.tivic.manager.mob.lote.impressao.geracaodocumento.IGeracaoDocumentosSSE;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearchBuilder;
import com.tivic.manager.mob.lote.impressao.notificacao.GeraNotificacaoNicNipComJuros;
import com.tivic.manager.mob.lote.impressao.notificacao.GeraNotificacaoNipComJuros;
import com.tivic.manager.mob.lote.impressao.validator.CriarLoteNotificacaoValidations;
import com.tivic.manager.mob.lote.impressao.validator.LoteNotificacaoImpressaoValidations;
import com.tivic.manager.mob.lote.impressao.viaunica.nai.GeraTipoImpressaoNAI;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.GeraTipoImpressaoNIP;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.NipImpressaoDTO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ListObject;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.detran.mg.notificacao.TipoNotificacaoEnum;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteNotificacaoService implements ILoteNotificacaoService{
		
	private ILoteImpressaoRepository loteImpressaoRepository;
	AitReportDataJariNIP jariNip;
	IArquivoRepository arquivoRepository;
	AitRepository aitRepository;
	ILoteImpressaoAitRepository loteImpressaoAitRepository;
	ICorreiosEtiquetaService correiosEtiquetaService;
	ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	IGeracaoDocumentosSSE geracaoDocumentoSse;
	private IParametroRepository parametroRepository;
	private ManagerLog managerLog;
	private AitInconsistenciaRepository aitInconsistenciaRepository;
	IAitMovimentoService aitMovimentoService;
	
	public LoteNotificacaoService() throws Exception {
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		jariNip = new AitReportDataJariNIP();
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
		loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
		geracaoDocumentoSse = (IGeracaoDocumentosSSE) BeansFactory.get(IGeracaoDocumentosSSE.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitInconsistenciaRepository = (AitInconsistenciaRepository) BeansFactory.get(AitInconsistenciaRepository.class);
	}
	
	@Override
	public List<Ait> getAllAitsEmitirNAI() throws IllegalArgumentException, SQLException, Exception {
		return getAllAitsEmitirNAI (new CustomConnection());
	}
	
	public List<Ait> getAllAitsEmitirNAI (CustomConnection customConnection) throws IllegalArgumentException, SQLException, Exception {
		customConnection.initConnection(false);
		try{
			return new ListAitsCandidatosNaiFactory()
					.getEstrategiaListAits(TipoLoteNotificacaoEnum.LOTE_NAI.getKey())
					.build(new Criterios(), customConnection.getConnection());
		}
		finally{
			customConnection.closeConnection();
		}
	}
		
	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNaiLote(List<Ait> listAitsNais, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GerarMovimentoNotificacaoFactory()
				.strategy(TipoLoteNotificacaoEnum.LOTE_NAI.getKey())
				.gerarMovimentos(listAitsNais, cdUsuario);
		return detranDTOs;
	}
	
	@Override
	public List<Ait> getAllAitsEmitirNIP() throws Exception {
		return getAllAitsEmitirNIP (new CustomConnection());
	}
	
	public List<Ait> getAllAitsEmitirNIP (CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		try{
			return new ListAitsCandidatosNipFactory()
					.getEstrategiaListAits(TipoLoteNotificacaoEnum.LOTE_NIP.getKey(), new SearchCriterios(), customConnection)
					.build();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNipLote(List<Ait> listAitsNips, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GerarMovimentoNotificacaoFactory()
				.strategy(TipoLoteNotificacaoEnum.LOTE_NIP.getKey())
				.gerarMovimentos(listAitsNips, cdUsuario);
		return detranDTOs;
	}
	
	@Override
	public List<Ait> getAllAitsEmitirFimPrazoDefesa() throws Exception {
		return getAllAitsEmitirFimPrazoDefesa (new CustomConnection());
	}
	
	@Override
	public List<Ait> getAllAitsEmitirFimPrazoDefesa (CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		try{
			return new ListAitsCandidatosNipFactory()
					.getEstrategiaListAits(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), new SearchCriterios(), customConnection)
					.build();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoFimPrazoDefesa(List<Ait> listAits, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GerarMovimentoNotificacaoFactory()
				.strategy(TipoLoteNotificacaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey())
				.gerarMovimentos(listAits, cdUsuario);
		return detranDTOs;
	}
	
	@Override
	public PagedResponse<AitDTO> buscarAitsParaLoteImpressao(int tipoLote, SearchCriterios searchCriterios) throws ValidacaoException, Exception {
		int cdInfracaoNic = this.parametroRepository.getValorOfParametroAsInt("MOB_CD_TIPO_INFRACAO_NIC");
		int comparator = (tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey() || tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()) 
                 ? ItemComparator.IN : ItemComparator.NOTIN;
		searchCriterios.addCriterios("A.cd_infracao", String.valueOf(cdInfracaoNic), comparator, Types.INTEGER);
		
		if (tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()) {
	        tipoLote = TipoLoteNotificacaoEnum.LOTE_NAI.getKey();
	    }
	    else if (tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()) {
	        tipoLote = TipoLoteNotificacaoEnum.LOTE_NIP.getKey();
	    }
		searchCriterios.addCriteriosEqualInteger("K.tp_status", tipoLote, tipoLote > -1);
		Search<AitDTO> searchAits = new BuscarAitsParaLoteImpressaoFactory()
				.getStrategy(tipoLote)
				.montarSearchLoteImpressao(searchCriterios);
		List<AitDTO> listAitDTO = searchAits.getList(AitDTO.class);
		return new CriacaoLoteAitDTOListBuilder(listAitDTO, searchAits.getRsm().getTotal()).build();
	}
	
	@Override
	public List<AitDTO> buscarQuantidadeAitsParaLoteImpressao(int quantidadeAit, int tipoLote) throws ValidacaoException, Exception {
		Search<AitDTO> searchAits = new BuscarAitsParaLoteImpressaoFactory()
				.getStrategy(tipoLote)
				.buscarAitsParaLoteImpressao(quantidadeAit);
		return searchAits.getList(AitDTO.class);
	}
	
	@Override
	public LoteImpressao gerarLoteNotificacao(LoteImpressao loteImpressao) throws ValidacaoException, Exception {
		return gerarLoteNotificacao(loteImpressao, false);
	}
	
	@Override
	public LoteImpressao gerarLoteNotificacao(LoteImpressao loteImpressao, boolean isTask) throws ValidacaoException, Exception {
		List<Ait> aitList = new ListObject<Ait, LoteImpressaoAit>(Ait.class).convertList(loteImpressao.getAits());
		int tipoLote = loteImpressao.getTpLoteImpressao();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
		    int tpRemessaCorreios = 0;
	        if (tipoLote == TipoLoteNotificacaoEnum.LOTE_NAI.getKey() || tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NAI.getKey()) {
	            tpRemessaCorreios = Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NAI"));
	        } else if (tipoLote == TipoLoteNotificacaoEnum.LOTE_NIP.getKey() || tipoLote == TipoLoteNotificacaoEnum.LOTE_NIC_NIP.getKey()) {
	            tpRemessaCorreios = Integer.parseInt(ParametroServices.getValorOfParametro("MOB_TIPO_ENVIO_CORREIOS_NIP"));
	        }
			for (int i = 0; i < aitList.size(); i++) {
	            Ait aitLote = aitList.get(i);
	            if (!validarERegistrarInconsistencia(aitLote.getCdAit(), tipoLote, customConnection)) {
	                aitList.remove(i);
	                i--;
	            }
	        }					
			new CriarLoteNotificacaoValidations(tpRemessaCorreios, isTask)
				.validate(aitList.size(), customConnection);
			LoteImpressao loteImpressaoGerado = new GerarLoteImpressaoFactory()
					.getStrategy(tipoLote)
					.gerarLoteImpressao(aitList, loteImpressao.getCdUsuario(), customConnection);
			reservarEtiquetas(loteImpressaoGerado, tpRemessaCorreios, customConnection);
			customConnection.finishConnection();
			return loteImpressaoGerado;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private boolean validarERegistrarInconsistencia(int cdAit, int tipoNotificacao, CustomConnection customConnection) throws Exception {
	    Ait ait = aitRepository.get(cdAit);
	    try {
	        new LoteNotificacaoImpressaoValidations().validate(ait, customConnection);
	        return true;
	    } catch (LoteNotificacaoException e) {
	        this.managerLog.info("Erro de validação inconsistencia para o AIT: " + ait.getCdAit() + " - ", e.getMessage());
	        AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder()
	            .build(ait, e.getCodErro(), tipoNotificacao);
	        salvarInconsistenciaParaAitDoLote(aitInconsistencia);
	        return false;
	    }
	}
	
	private void salvarInconsistenciaParaAitDoLote(AitInconsistencia aitInconsistencia) throws Exception {
	    CustomConnection customConnection = new CustomConnection();
	    try {
	    	customConnection.initConnection(true);
	        aitInconsistenciaRepository.insert(aitInconsistencia, customConnection);
	        customConnection.finishConnection();      
	    } finally {
	    	customConnection.closeConnection();
	    }
	}
	
	@Override
	public LoteImpressao gerarLoteNotificacaoAitViaUnica(List<Ait> aitList, int cdUsuario, int tipoLote) throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = new GerarLoteImpressaoFactory()
					.getStrategy(tipoLote)
					.gerarLoteImpressao(aitList, cdUsuario, customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void reservarEtiquetas(LoteImpressao loteImpressao, int tpRemessaCorreios, CustomConnection customConnection) throws Exception {
		if (tpRemessaCorreios == TipoRemessaCorreiosEnum.CARTA_SIMPLES.getKey() 
				|| this.parametroRepository.getValorOfParametroAsBoolean("LG_CLIENTE_ECARTAS"))
			return;
		List<CorreiosEtiqueta> correiosEtiquetasList = correiosEtiquetaService.buscarListaEtiqueta(tpRemessaCorreios, loteImpressao.getAits().size());
		for (CorreiosEtiqueta correiosEtiqueta : correiosEtiquetasList) {
			correiosEtiqueta.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
			correiosEtiquetaRepository.update(correiosEtiqueta, customConnection);
		}
	}
	
	@Override
	public LoteImpressao save(LoteImpressao loteImpressao) throws Exception{
		return save(loteImpressao, new CustomConnection());
	}

	@Override
	public LoteImpressao save(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		try {
			if(loteImpressao==null)
				throw new ValidacaoException("Erro ao salvar. LoteImpressao é nulo");
			int retorno = 0;
			customConnection.initConnection(true);
			if(loteImpressao.getCdLoteImpressao()==0){
				loteImpressao.setCdLoteImpressao(loteImpressaoRepository
						.insert(loteImpressao, customConnection)
						.getCdLoteImpressao());
			}
			else {
				loteImpressao = loteImpressaoRepository.update(loteImpressao, customConnection);
				customConnection.finishConnection();
				return loteImpressao;
			}
			if(loteImpressao.getAits()!=null && loteImpressao.getAits().size()>0) {
				for (LoteImpressaoAit loteImpressaoAit : loteImpressao.getAits()) {
					loteImpressaoAit.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
				}
				retorno = LoteImpressaoServices.vincularAits(loteImpressao.getAits(), customConnection.getConnection()).getCode();
				if (retorno <= 0)
					throw new ValidacaoException("Erro ao vincular AITs");
			}
			customConnection.finishConnection();
			return loteImpressao;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario) throws Exception {
		gerarDocumentosLote(cdLoteImpressao, cdUsuario, new CustomConnection());
	}
	
	@Override
	public void gerarDocumentosLote(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			if (loteImpressao.getCdArquivo() > 0) {
				throw new ValidacaoException("Os documentos deste lote já foram gerados.");
			}
			new GerarDocumentosLoteImpressaoFactory()
					.getStrategy(loteImpressao)
					.gerarDocumentos(loteImpressao, cdUsuario, customConnection);
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void gerarDocsLoteAitViaUnica(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			if (loteImpressao.getCdArquivo() > 0) {
				throw new ValidacaoException("Os documentos deste lote já foram gerados.");
			}
			new GeraDocLoteAitViaUnicaFactory()
					.getStrategy(loteImpressao)
					.gerarDocumentos(loteImpressao, cdUsuario, customConnection);
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception { 
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void iniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		geracaoDocumentoSse.iniciarThreadGeracaoDocumentos(String.valueOf(cdLoteImpressao), new LoteNotificacaoGeracaoDocumentosTask(cdLoteImpressao, cdUsuario));
		String status = LoteImpressaoSituacaoEnum.valueOf(getStatus(cdLoteImpressao, customConnection).getStDocumento());
		this.managerLog.info("Geração de documentos iniciada.", "STATUS " +  status);
    }
	
	@Override
	public List<LoteImpressaoAit> reiniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		List<LoteImpressaoAit> loteImpressaoAitList = new ArrayList<LoteImpressaoAit>();
		try {
			customConnection.initConnection(true);
			loteImpressaoAitList = reiniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario, customConnection);
			customConnection.finishConnection();
			return loteImpressaoAitList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public List<LoteImpressaoAit> reiniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
	    List<LoteImpressaoAit> loteImpressaoAitList = loteImpressaoAitRepository.find(searchCriteriosNotificacaoDocumentos(cdLoteImpressao), customConnection);
	    List<LoteImpressaoArquivo> loteImpressaoArquivoList = loteImpressaoArquivoRepository.find(searchCriteriosNotificacaoDocumentos(cdLoteImpressao), customConnection);
	    LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
	    deletarArquivoLoteImpressao(loteImpressaoArquivoList, cdLoteImpressao, customConnection);
	    atualizarLoteImpressaoAit(loteImpressaoAitList, cdLoteImpressao, customConnection);
	    atualizarLoteImpressao(loteImpressao, cdLoteImpressao, customConnection);
	    iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario, customConnection);
	    return loteImpressaoAitList;
	}

	private void atualizarLoteImpressaoAit(List<LoteImpressaoAit> loteImpressaoAitList, int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
	    	loteImpressaoAit.setStImpressao(TipoSituacaoLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey());
	    	loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
	    }
	}

	private void atualizarLoteImpressao(LoteImpressao loteImpressao, int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		loteImpressao.setStLoteImpressao(TipoSituacaoLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey());
		loteImpressao.setCdArquivo(0);
    	loteImpressaoRepository.update(loteImpressao, customConnection);
	}
	
	private void deletarArquivoLoteImpressao(List<LoteImpressaoArquivo> loteImpressaoArquivoList, int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    for (LoteImpressaoArquivo loteImpressaoArquivo : loteImpressaoArquivoList) {
	    	loteImpressaoArquivoRepository.delete(loteImpressaoArquivo, customConnection);
	    }
	}

	public LoteImpressaoStatus getStatus(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		try {
			LoteImpressao lote =  loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			List<LoteImpressaoAit> loteImpressaoAitsList = loteImpressaoAitRepository.find(searchCriteriosNotificacaoDocumentos(cdLoteImpressao), customConnection);
			int qtDocumentosGerados = 0;
			for (LoteImpressaoAit loteImpressaoAit: loteImpressaoAitsList) {
				if (loteImpressaoAit.getStImpressao() > LoteImpressaoAitSituacao.AGUARDANDO_GERACAO)
					qtDocumentosGerados++;
			}
			LoteImpressaoStatus status = new LoteImpressaoStatusBuilder(lote, loteImpressaoAitsList.size(), qtDocumentosGerados)
					.build();
			customConnection.finishConnection();
			return status;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void getStatusGeracaoDocumentos(SseEventSink sseEventSink, Sse sse, int cdLoteImpressao) throws Exception {
		GeracaoDocumentosSSE events = geracaoDocumentoSse.setSse(sseEventSink, sse);
		
		while(events.ativo()) {
			LoteImpressaoStatus status = getStatus(cdLoteImpressao, new CustomConnection());
			events.setStatus(status).notificar();			
		}
	}
	
	private SearchCriterios searchCriteriosNotificacaoDocumentos(int cdLoteImpressao) {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, true);
		return search;
	}
	
	@Override
	public PagedResponse<LoteNotificacaoDTO> buscarLotes(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception { 
		Search<LoteNotificacaoDTO> lotes = this.loteImpressaoRepository.findLotes(loteImpressaoSearch);
		if(lotes.getList(LoteNotificacaoDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há Lote com esse filtro.");
		} 
		return new LoteNotificacaoDTOListBuilder(lotes.getList(LoteNotificacaoDTO.class), lotes.getRsm().getTotal()).build();
	}
	
	@Override
	public PagedResponse<LoteImpressaoAitDTO> buscarLotesAits(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception {
		Search<LoteImpressaoAitDTO> list = this.loteImpressaoRepository.findLoteAits(loteImpressaoSearch);
		if(list.getList(LoteImpressaoAitDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT nesse Lote com esse filtro.");
		} 
		return new LoteImpressaoAitDTOListBuilder(list.getList(LoteImpressaoAitDTO.class), list.getRsm().getTotal()).build();
	}
	
	@Override
	public byte[] imprimirLoteNotificacao(int cdLoteImpressao) throws Exception {
		return imprimirLoteNotificacao(cdLoteImpressao, new CustomConnection());
	}
	
	@Override
	public byte[] imprimirLoteNotificacao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Arquivo arquivoLote = this.arquivoRepository.get(loteImpressao.getCdArquivo(), customConnection);
			if (loteImpressao.getCdArquivo() <= 0 || arquivoLote == null) {
				throw new ValidacaoException("O arquivo para download não foi encontrado.");
			}
			loteImpressao.setStLoteImpressao (LoteImpressaoAitSituacao.IMPRESSO);
			save(loteImpressao, customConnection);
			customConnection.finishConnection();
			return arquivoLote.getBlbArquivo();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario) throws Exception {
		return gerarLoteNotificacaoNaiViaUnica(cdAit, cdUsuario, new CustomConnection());
	}
	
	@Override
	public byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			byte[] impressaoNai = new GeraTipoImpressaoNAI(cdAit, cdUsuario).gerar(customConnection);
			customConnection.finishConnection();
			return impressaoNai;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public NipImpressaoDTO gerarLoteNotificacaoNipViaUnica(int cdAit, int cdUsuario) throws Exception {
		return gerarLoteNotificacaoNipViaUnica(cdAit, cdUsuario, new CustomConnection());
	}
	
	@Override
	public NipImpressaoDTO gerarLoteNotificacaoNipViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			TipoStatusEnum tpNotificacao = getTipoNotificacaoNipAdvertencia(cdAit, customConnection);
			NipImpressaoDTO impressaoNIP = new GeraTipoImpressaoNIP(cdAit, tpNotificacao, cdUsuario).gerar(customConnection);
			customConnection.finishConnection();
			return impressaoNIP;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private TipoStatusEnum getTipoNotificacaoNipAdvertencia(int cdAit, CustomConnection customConnection) throws Exception {
	    List<AitMovimento> movimentos = aitMovimentoService.getByAit(cdAit, customConnection);

	    boolean possuiNip = movimentos.stream()
	            .anyMatch(movimento -> movimento.getTpStatus() == TipoStatusEnum.NIP_ENVIADA.getKey() &&
	                    movimento.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());

	    if (possuiNip) {
	        return TipoStatusEnum.NIP_ENVIADA;
	    }

	    boolean possuiAdvertencia = movimentos.stream()
	            .anyMatch(movimento -> movimento.getTpStatus() == TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey() &&
	                    movimento.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());

	    return possuiAdvertencia ? TipoStatusEnum.NOTIFICACAO_ADVERTENCIA : TipoStatusEnum.NIP_ENVIADA;
	}

	
	@Override
	public Ait atualizarDadosAit(Ait ait, CustomConnection customConnection) throws Exception {
		sol.util.Result r = AitServices.updateDetran(ait, customConnection.getConnection());
		if (r.getCode() < 0){
			throw new ValidacaoException ("Não foi possivel atualizar os dados do AIT: " + ait.getCdAit());
		}
		return aitRepository.get(ait.getCdAit(), customConnection);
	}

	@Override
	public LoteImpressaoAitDTO buscarLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception, NoContentException{ 
		List<LoteImpressaoAitDTO> lote = this.loteImpressaoRepository.getLote(loteImpressaoSearch).getList(LoteImpressaoAitDTO.class);
		if(lote.isEmpty()) {
			throw new NoContentException("Nenhum Lote encontrado.");
		}
		return lote.get(0);
	}
	
	
	public LoteImpressaoAit buscarNotificacao(int cdAit, int tipoNotificacao, CustomConnection customConnection) throws Exception {
		List<LoteNotificacaoDTO> searchLotesList = this.loteImpressaoRepository.findLotes(searchNotificacaoSegundaVia(cdAit, tipoNotificacao)).getList(LoteNotificacaoDTO.class);
		if (searchLotesList.size() > 0) {
			LoteImpressaoAit loteAit = loteImpressaoAitRepository.get(searchLotesList.get(0).getCdLoteImpressao(), cdAit, customConnection);
			return loteAit;
		}
		return new LoteImpressaoAit();
	}
	
	private LoteImpressaoSearch searchNotificacaoSegundaVia(int cdAit, int tipoNotificacao) {
		LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
				.setCdAit(cdAit)
				.setTpDocumento(tipoNotificacao)
				.build();
		return loteImpressaoSearch;
	}
	
	@Override	
	public byte[] gerarNotificacaoNipComJuros(int cdAit, Boolean printPortal) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			return new GeraNotificacaoNipComJuros().gerarDocumento(cdAit, printPortal, customConnection);
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override	
	public byte[] gerarNotificacaoNicNipComJuros(int cdAit, Boolean printPortal) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			return new GeraNotificacaoNicNipComJuros().gerarDocumento(cdAit, printPortal, customConnection);
		}
		finally {
			customConnection.closeConnection();
		}
	}	
	
	@Override
	public String getEstadoOrgaoAutuador() {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
		return EstadoDAO.get(cidade.getCdEstado()).getSgEstado();
	}

	@Override
	public Arquivo pegarArquivoLote(int cdLoteNotificacao, int tpArquivo) throws Exception {
		SearchCriterios searchCriterios = searchCriteriosArquivoLote(cdLoteNotificacao, tpArquivo);
		List<Arquivo> arquivoList = searchArquivoLote(searchCriterios).getList(Arquivo.class);
		if (arquivoList.isEmpty()) {
			throw new NoContentException("Nenhum arquivo encontrado.");
		}
		Arquivo arquivo = arquivoList.get(0);
		return arquivo;
	}
	
	private SearchCriterios searchCriteriosArquivoLote(int cdLoteNotificacao, int tpArquivo) {
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("B.cd_lote_impressao", cdLoteNotificacao);
		criterios.addCriteriosEqualInteger("A.cd_tipo_arquivo", tpArquivo);
		return criterios;
	}

	private Search<Arquivo> searchArquivoLote(SearchCriterios criterios) throws Exception {
		Search<Arquivo> search = new SearchBuilder<Arquivo>("GRL_ARQUIVO A")
				.addField("A.cd_arquivo, A.dt_arquivamento, A.cd_tipo_arquivo, A.blb_arquivo")
				.addJoinTable(" JOIN mob_lote_impressao_arquivo B ON (B.cd_arquivo = A.cd_arquivo) ")
				.searchCriterios(criterios)
				.build();
		return search;
	}
	
	public LoteImpressaoAit deleteLoteImpressaoAit(LoteImpressaoAit loteImpressaoAit, CustomConnection customConnection) throws Exception {
		return this.loteImpressaoAitRepository.delete(loteImpressaoAit, customConnection);
	}
	
	public LoteImpressao deleteLoteImpressao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addCdLoteImpressao(cdLoteImpressao)
				.build();	
		return this.loteImpressaoRepository.delete(loteImpressao, customConnection);
	}

	public LoteImpressao deleteLoteImpressao(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		return this.loteImpressaoRepository.delete(loteImpressao, customConnection);
	}
	
	public void atualizarLotePrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, List<Ait> aits, List<LoteImpressaoAitDTO> aitsNovoLote, CustomConnection customConnection) throws ValidacaoException, Exception {
		if(!aitsNovoLote.isEmpty()) {
			int tipoLote = aitsNovoLote.get(0).getTpRecurso() == TipoNotificacaoEnum.NOVO_PRAZO_DEFESA.getKey() ?
							TipoStatusEnum.NAI_ENVIADO.getKey(): TipoStatusEnum.NIP_ENVIADA.getKey();
			List<Ait> listaAitsLote = new ArrayList<Ait>();
			List<LoteImpressaoAit> loteImpressaoAitList = new ArrayList<LoteImpressaoAit>();
			for (LoteImpressaoAitDTO loteImpressaoAitDTO : aitsNovoLote) {
				LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
						.addCdAit(loteImpressaoAitDTO.getCdAit())
						.addCdLoteImpressao(loteImpressaoAitDTO.getCdLoteImpressao())
						.build();
				loteImpressaoAitList.add(loteImpressaoAit);
				listaAitsLote.add(new AitBuilder()
						.cdAit(loteImpressaoAitDTO.getCdAit())
						.build());
				try {
					this.managerLog.info("ATUALIZANDO LOTE", " Lote de impressão com os AITs impedidos.");
					deleteLoteImpressaoAit(loteImpressaoAit, customConnection);
				} catch (Exception e) {
					throw new Exception("Falha ao remover o AIT "+ loteImpressaoAitDTO.getCdAit() +" do lote de impressão: " + aitsNovoLote.get(0).getCdLoteImpressao());
				}
			}
			LoteImpressao loteImpressao = gerarLoteImpressao(loteImpressaoAitList, aitsNovoLote.get(0).getCdUsuario(), tipoLote); 
			gerarLoteNotificacao(loteImpressao);
		}
		if(aits.size() == aitsNovoLote.size()) 
			deleteLoteImpressao(alteraPrazoRecursoDTO.getCdLoteImpressao(), customConnection);
		else
			reiniciarGeracaoDocumentos(alteraPrazoRecursoDTO.getCdLoteImpressao(), alteraPrazoRecursoDTO.getCdUsuario(), customConnection);
	}
	
	private LoteImpressao gerarLoteImpressao(List<LoteImpressaoAit> listAitLote, int cdUsuario, int tipoLote) {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addCdUsuario(cdUsuario)
				.addTpLoteImpressao(tipoLote)
				.addAits(listAitLote)
				.build();
		
		return loteImpressao;
	}
	
	public List<Ait> buscarAitLoteImpressao(int cdLoteImpressao, int tipoRecurso, CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao);
		Search<Ait> search = new SearchBuilder<Ait>("mob_ait A")
				.addJoinTable("JOIN mob_lote_impressao_ait B ON(A.cd_ait = B.cd_ait)")
				.searchCriterios(searchCriterios)
				.customConnection(customConnection)
				.build();
		List<Ait> aits = search.getList(Ait.class);
		if(aits.isEmpty()) 
			throw new ValidacaoException("Nenhum AIT foi encontrado para alterar o prazo. Por favor, contate o suporte.");
		return aits;
	}
	
	@Override
	public Boolean verificarAitsComMesmoLote(int cdAit, int tpDocumento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		List<LoteImpressaoAit> loteImpressaoAit = loteImpressaoAitRepository.find(searchCriterios, new CustomConnection());
		if(loteImpressaoAit.isEmpty()) {
			return false;
		}
		int qtdLote = getPertencenteLote(loteImpressaoAit.get(0).getCdLoteImpressao(), tpDocumento);
		if(qtdLote > 1)
			return true;
		return false;
	}
	
	private int getPertencenteLote(int cdLoteImpressao, int tpDocumento) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao);
		searchCriterios.addCriteriosEqualInteger("B.tp_documento", tpDocumento);
		Search<QuantidadePertencenteLoteDTO> search = new SearchBuilder<QuantidadePertencenteLoteDTO>("mob_lote_impressao_ait A")
				.fields("COUNT(A.*) AS qtd_cd_lote_impressao")
				.addJoinTable("JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
				.searchCriterios(searchCriterios)
				.build();
		List<QuantidadePertencenteLoteDTO> totalCdLoteImpressao = search.getList(QuantidadePertencenteLoteDTO.class);
		return totalCdLoteImpressao.get(0).getQtdCdLoteImpressao();
	}
}
