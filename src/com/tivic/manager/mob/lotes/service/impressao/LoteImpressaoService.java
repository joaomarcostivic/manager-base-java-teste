package com.tivic.manager.mob.lotes.service.impressao;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.NoContentException;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaRepository;
import com.tivic.manager.mob.correios.ICorreiosEtiquetaService;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaRepository;
import com.tivic.manager.mob.lotes.builders.ArquivoBuilder;
import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearch;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoStatusBuilder;
import com.tivic.manager.mob.lotes.dto.CreateAitLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.dto.impressao.AitDTO;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.dto.impressao.LoteImpressaoDTO;
import com.tivic.manager.mob.lotes.dto.impressao.NipImpressaoDTO;
import com.tivic.manager.mob.lotes.enums.correios.TipoRemessaCorreiosEnum;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.DocumentGeneratorFactory;
import com.tivic.manager.mob.lotes.factory.impressao.DocumentViaUnicaGeneratorFactory;
import com.tivic.manager.mob.lotes.factory.impressao.GeraMovimentoNotificacaoFactory;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNaiFactory;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNipFactory;
import com.tivic.manager.mob.lotes.impressao.ait.GeraNotificacaoNicNipComJuros;
import com.tivic.manager.mob.lotes.impressao.ait.GeraNotificacaoNipComJuros;
import com.tivic.manager.mob.lotes.impressao.strategy.documento.IDocumentGeneratorStrategy;
import com.tivic.manager.mob.lotes.impressao.strategy.lote.LoteImpressaoGenerator;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.ProcessadorImpressaoNai;
import com.tivic.manager.mob.lotes.impressao.strategy.viaunica.ProcessadorImpressaoNip;
import com.tivic.manager.mob.lotes.impressao.task.LoteGeracaoDocumentosTask;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.arquivo.Arquivo;
import com.tivic.manager.mob.lotes.model.arquivo.LoteImpressaoArquivo;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.arquivo.ArquivoRepository;
import com.tivic.manager.mob.lotes.repository.arquivo.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lotes.repository.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.DadosEmptyException;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.manager.mob.lotes.service.impressao.processamento.GeracaoDocumentosSSE;
import com.tivic.manager.mob.lotes.service.impressao.processamento.IGeracaoDocumentosSSE;
import com.tivic.manager.mob.lotes.validator.CriarLoteNotificacaoValidations;
import com.tivic.manager.mob.lotes.validator.LoteNotificacaoImpressaoValidations;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.concurrent.AsyncListProcessor;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import sol.dao.ItemComparator;

public class LoteImpressaoService implements ILoteImpressaoService {
	private LoteRepository loteRepository;
	private LoteImpressaoRepository loteImpressaoRepository;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private ArquivoRepository arquivoRepository;
	private IParametroRepository parametroRepository;
	private ICorreiosEtiquetaService correiosEtiquetaService;
	private ICorreiosEtiquetaRepository correiosEtiquetaRepository;
	private AitRepository aitRepository;
	private ManagerLog managerLog;
	private AitInconsistenciaRepository aitInconsistenciaRepository;
	private IGeracaoDocumentosSSE geracaoDocumentoSse;
	private IAitMovimentoService aitMovimentoService;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;

	public LoteImpressaoService() throws Exception {
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.loteImpressaoRepository = (LoteImpressaoRepository) BeansFactory.get(LoteImpressaoRepository.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory.get(ILoteImpressaoAitRepository.class);
		this.arquivoRepository = (ArquivoRepository) BeansFactory.get(ArquivoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		this.correiosEtiquetaRepository = (ICorreiosEtiquetaRepository) BeansFactory.get(ICorreiosEtiquetaRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.aitInconsistenciaRepository = (AitInconsistenciaRepository) BeansFactory.get(AitInconsistenciaRepository.class);
		this.geracaoDocumentoSse = (IGeracaoDocumentosSSE) BeansFactory.get(IGeracaoDocumentosSSE.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory.get(ILoteImpressaoArquivoRepository.class);
	}
	
	@Override
	public LoteImpressao get(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public List<LoteImpressao> find(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			List<LoteImpressao> loteImpressao = loteImpressaoRepository.find(searchCriterios, customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public PagedResponse<LoteImpressaoDTO> buscarLotes(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception { 
		Search<LoteImpressaoDTO> lotes = this.loteImpressaoRepository.findLotes(loteImpressaoSearch);
		if(lotes.getList(LoteImpressaoDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há Lote com esse filtro.");
		} 
		return new PagedResponse<>(lotes.getList(LoteImpressaoDTO.class), lotes.getRsm().getTotal());
	}
	
	@Override
	public LoteImpressao insert(CreateLoteImpressaoDTO loteImpressaoRequest) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = insert(loteImpressaoRequest, customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private LoteImpressao insert(CreateLoteImpressaoDTO createLoteImpressao, CustomConnection customConnection) throws Exception {
	    List<Ait> aitList = processarAitInconsistente(createLoteImpressao.getAits(), createLoteImpressao.getTpImpressao(), customConnection);    
	    int tpRemessaCorreios = getTipoRemessaCorreios(createLoteImpressao.getTpImpressao());
	    new CriarLoteNotificacaoValidations(tpRemessaCorreios)
	        .validate(aitList.size(), customConnection);
	    
	    Lote lote = criarLote(createLoteImpressao, customConnection);
	    LoteImpressao loteImpressao = criarLoteImpressao(createLoteImpressao, lote, customConnection);
	    for (Ait aitLote : aitList) {
	        criarLoteImpressaoAit(loteImpressao, aitLote.getCdAit(), customConnection);
	    }
	    List<LoteImpressaoAit> loteImpressaoAits = aitList.stream()
	    	    .map(ait -> new LoteImpressaoAit()) 
	    	    .collect(Collectors.toList());
	    loteImpressao.setAits(loteImpressaoAits);
	    reservarEtiquetas(loteImpressao, tpRemessaCorreios, customConnection);
	    return loteImpressao;
	}
	
	@Override
	public byte[] insertViaUnica(CreateLoteImpressaoDTO loteImpressaoRequest) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			byte[] impressao = insertViaUnica(loteImpressaoRequest, customConnection);
			customConnection.finishConnection();
			return impressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private byte[] insertViaUnica(CreateLoteImpressaoDTO createLoteImpressao, CustomConnection customConnection) throws Exception {
		List<Ait> aitList = new ArrayList<Ait>();
		for (CreateAitLoteImpressaoDTO aitDto: createLoteImpressao.getAits()) {
			Ait aitLote = aitRepository.get(aitDto.getCdAit());
			aitList.add(aitLote);		
		}
	    new GeraMovimentoNotificacaoFactory()
		    .strategy(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey())
		    .gerarMovimentos(aitList, createLoteImpressao.getCdCriador());
	    
	    LoteImpressao loteImpressao = this.gerarLoteNotificacaoAitViaUnica(createLoteImpressao, aitList);
	    return this.gerarDocumentoParaAitViaUnica(loteImpressao, customConnection);
	}
	
	private List<Ait> processarAitInconsistente(List<CreateAitLoteImpressaoDTO> aits, int tpImpressao, CustomConnection customConnection) throws Exception, ValidacaoException {
	    List<Ait> aitList = new ArrayList<Ait>();
		
		for (CreateAitLoteImpressaoDTO aitDto: aits) {
			Ait aitLote = aitRepository.get(aitDto.getCdAit());
			if (validarERegistrarInconsistencia(aitLote, tpImpressao, customConnection)) {
				aitList.add(aitLote);
			}			
		}
		if (aitList.isEmpty() || aitList == null) {
			throw new ValidacaoException("Todos os AITs possuem inconsistências.");
		}
	    return aitList;
	}	

	private int getTipoRemessaCorreios(int tpImpressao) throws Exception {
	    int tpRemessaCorreios = 0;
	    if (tpImpressao == TipoLoteImpressaoEnum.LOTE_NAI.getKey() || tpImpressao == TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey()) {
	        tpRemessaCorreios = this.parametroRepository.getValorOfParametroAsInt("MOB_TIPO_ENVIO_CORREIOS_NAI");
	    } else if (tpImpressao == TipoLoteImpressaoEnum.LOTE_NIP.getKey() || tpImpressao == TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()) {
	        tpRemessaCorreios = this.parametroRepository.getValorOfParametroAsInt("MOB_TIPO_ENVIO_CORREIOS_NIP");
	    }
	    return tpRemessaCorreios;
	}
	
	@Override
	public LoteImpressao gerarLoteNotificacaoAitViaUnica(CreateLoteImpressaoDTO createLoteImpressao, List<Ait> aitList) throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			Lote lote = criarLote(createLoteImpressao, customConnection);
		    LoteImpressao loteImpressao = criarLoteImpressao(createLoteImpressao, lote, customConnection);
			for (Ait aitLote : aitList) {
				criarLoteImpressaoAit(loteImpressao, aitLote.getCdAit(), customConnection);
			}
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private Lote criarLote(CreateLoteImpressaoDTO createLoteImpressao, CustomConnection customConnection) throws Exception {
	    Lote lote = new LoteBuilder()
	        .setDtCriacao(DateUtil.getDataAtual())
	        .setCdCriador(createLoteImpressao.getCdCriador())
	        .setIdLote(getIdLote(createLoteImpressao.getTpImpressao()))
	        .build();
	    loteRepository.insert(lote, customConnection);
	    return lote;
	}

	private LoteImpressao criarLoteImpressao(CreateLoteImpressaoDTO createLoteImpressao, Lote lote, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.setCdLote(lote.getCdLote())
				.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_IMPRESSAO.getKey())
				.setTpImpressao(createLoteImpressao.getTpImpressao())
				.build();
		loteImpressaoRepository.insert(loteImpressao, customConnection);
	    return loteImpressao;
	}

	private void criarLoteImpressaoAit(LoteImpressao loteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
				.setCdLoteImpressao(loteImpressao.getCdLoteImpressao())
				.setCdAit(cdAit)
				.setStImpressao(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey())
				.build();
		loteImpressaoAitRepository.insert(loteImpressaoAit, customConnection);
	}
	
	private boolean validarERegistrarInconsistencia(Ait ait, int tpLoteImpressao, CustomConnection customConnection) throws Exception {
	    try {
	        new LoteNotificacaoImpressaoValidations().validate(ait, customConnection);
	        return true;
	    } catch (LoteNotificacaoException e) {
	        this.managerLog.info("Erro de validação inconsistencia para o AIT: " + ait.getCdAit() + " - ", e.getMessage());
	        AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder()
	            .build(ait, e.getCodErro(), tpLoteImpressao);
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
	public String getIdLote(int tpLoteImpressao) {
	    LocalDateTime now = LocalDateTime.now();
	    String dataFormatada = now.format(DateTimeFormatter.ofPattern("ddMMHHmm"));

	    Map<Integer, String> tipoLoteFormatos = new HashMap<>();
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NAI.getKey(), "NAI/%s");
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), "NAI/%s");
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NIP.getKey(), "NIP/%s");
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), "NIP/%s");
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey(), "NIC-NAI/%s");
	    tipoLoteFormatos.put(TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey(), "NIC-NIP/%s");

	    String formato = tipoLoteFormatos.get(tpLoteImpressao);
	    if (formato == null) {
	        throw new IllegalArgumentException("Tipo de notificação inválido: " + tpLoteImpressao);
	    }

	    return String.format(formato, dataFormatada);
	}

	@Override
	public byte[] processarLote(int cdLoteImpressao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			byte[] lote = processarLote(cdLoteImpressao, customConnection);
			customConnection.finishConnection();
			return lote;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private byte[] processarLote(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
		Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
		
		loteImpressao.setAits(this.loteImpressaoAitRepository.findByCdLoteImpressao(cdLoteImpressao));
		
		byte[] documento = DocumentGeneratorFactory.getStrategy(loteImpressao.getTpImpressao(), customConnection).generate(loteImpressao, lote.getIdLote(), customConnection);
		
		Arquivo arquivo = saveArquivoLote(loteImpressao, documento, lote.getCdCriador(), customConnection);
		lote.setCdArquivo(arquivo.getCdArquivo());
		loteImpressao.setStLote(StatusLoteImpressaoEnum.IMPRESSO.getKey());
		this.loteImpressaoRepository.update(loteImpressao, customConnection);
		this.loteRepository.update(lote, customConnection);
		return documento;
	}
	
	private Arquivo saveArquivoLote(LoteImpressao loteImpressao, byte[] content, int cdUsuario, CustomConnection customConnection) throws Exception {
		Arquivo arquivo = new ArquivoBuilder()
				.setBlbArquivo(content)
				.setCdUsuario(cdUsuario)
				.setNmArquivo("LOTE_NOTIFICACAO_" + loteImpressao.getCdLoteImpressao() + ".pdf" )
				.setNmDocumento("Lote de Notificações " + loteImpressao.getCdLoteImpressao())
				.build();
		this.arquivoRepository.insert(arquivo, customConnection);
		return arquivo;
	}
	
	@Override
	public PagedResponse<AitDTO> buscarAitsParaLoteImpressao(int tipoLote) throws Exception {
		SearchCriterios criteriosBuscaAits = adicionarCriteriosBuscaAits(tipoLote);
	    if (tipoLote == TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey()) {
	        tipoLote = TipoLoteImpressaoEnum.LOTE_NAI.getKey();
	    } else if (tipoLote == TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()) {
	        tipoLote = TipoLoteImpressaoEnum.LOTE_NIP.getKey();
	    }
	    Search<AitDTO> searchAits = loteImpressaoRepository.buscarAitsParaLoteImpressao(criteriosBuscaAits, tipoLote);
	    List<AitDTO> listAitDTO = searchAits.getList(AitDTO.class);
	    return new PagedResponse<>(listAitDTO, listAitDTO.size());
	}
	
	private SearchCriterios adicionarCriteriosBuscaAits(int tipoLote) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		int cdInfracaoNic = this.parametroRepository.getValorOfParametroAsInt("MOB_CD_TIPO_INFRACAO_NIC");
	    int comparator = (tipoLote == TipoLoteImpressaoEnum.LOTE_NIC_NAI.getKey() 
	                   || tipoLote == TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey()) 
	                   ? ItemComparator.IN : ItemComparator.NOTIN;
	    searchCriterios.addCriterios("A.cd_infracao", String.valueOf(cdInfracaoNic), comparator, Types.INTEGER);
	    
	    String movimentoNotificacao = (tipoLote == TipoLoteImpressaoEnum.LOTE_NIP.getKey() || tipoLote == TipoLoteImpressaoEnum.LOTE_NIC_NIP.getKey())
	    		? String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + "," + String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) 
	    		: String.valueOf(TipoStatusEnum.NAI_ENVIADO.getKey());
	    searchCriterios.addCriteriosEqualInteger("K.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
	    searchCriterios.addCriterios("K.tp_status", movimentoNotificacao, ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
	
	@Override
	public void delete(int cdLoteImpressao) throws Exception {	
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);	
			delete(cdLoteImpressao, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void delete(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    List<Lote> loteList = loteRepository.find(searchCriteriosLote(cdLoteImpressao), customConnection);
	    if(loteList.isEmpty()) {
			throw new DadosEmptyException("Lote não encontrado.");
		} 
	    List<LoteImpressaoAit> loteImpressaoAitList = loteImpressaoAitRepository.find(searchCriteriosLote(cdLoteImpressao), customConnection);
	    if(loteImpressaoAitList.isEmpty()) {
			throw new DadosEmptyException("Lote de AITs não encontrado.");
		} 
	    excluirArquivo(loteList.get(0).getCdArquivo(), cdLoteImpressao, customConnection);
	    excluirLote(loteList.get(0).getCdLote(), customConnection);
	    excluirLoteImpressaoAit(loteImpressaoAitList, customConnection);
	    excluirLoteImpressao(cdLoteImpressao, customConnection);
	    desvincularERestaurarEtiquetas(cdLoteImpressao, customConnection);
	}
	
	private SearchCriterios searchCriteriosLote(int cdLoteImpressao) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", cdLoteImpressao, true);
		return searchCriterios;
	}
	
	private void excluirArquivo(int cdArquivo, int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		List<LoteImpressaoArquivo> loteImpressaoArquivoList = loteImpressaoArquivoRepository.find(searchCriteriosLote(cdLoteImpressao), customConnection);
		if (!loteImpressaoArquivoList.isEmpty() && loteImpressaoArquivoList != null ) {
			for(LoteImpressaoArquivo loteImpressaoArquivo: loteImpressaoArquivoList) {
				this.loteImpressaoArquivoRepository.delete(loteImpressaoArquivo, customConnection);
				this.arquivoRepository.delete(loteImpressaoArquivo.getCdArquivo(), customConnection);
			}
		}
		this.arquivoRepository.delete(cdArquivo, customConnection);
	}
	
	private void excluirLote(int cdLote, CustomConnection customConnection) throws Exception {
		this.loteRepository.delete(cdLote, customConnection);
	}
	
	private void excluirLoteImpressaoAit(List<LoteImpressaoAit> loteImpressaoAitList, CustomConnection customConnection) throws Exception {
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
	    	loteImpressaoAitRepository.delete(loteImpressaoAit, customConnection);
	    }
	}
	
	private void excluirLoteImpressao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		this.loteImpressaoRepository.delete(cdLoteImpressao, customConnection);
	}
	
	private void desvincularERestaurarEtiquetas(int cdloteImpressao, CustomConnection customConnection) throws Exception {
		List<CorreiosEtiqueta> correiosEtiquetasList = correiosEtiquetaService.find(searchCriteriosLote(cdloteImpressao));
		if(correiosEtiquetasList.isEmpty()) {
			return;
		}
	    atualizarEtiquetas(correiosEtiquetasList, customConnection);
	}
	
	@Override
	public List<LoteImpressaoAit> excluirAitsLote(List<LoteImpressaoAit> loteImpressaoAitList, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			loteImpressaoAitList = excluirAitsLote(loteImpressaoAitList, cdUsuario, customConnection);
			customConnection.finishConnection();
			return loteImpressaoAitList;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public List<LoteImpressaoAit> excluirAitsLote(List<LoteImpressaoAit> aitListExcluidos, int cdUsuario, CustomConnection customConnection) throws Exception {
		excluirAitsLoteImpressao(aitListExcluidos, customConnection);
		desvincularERestaurarEtiquetaAits(aitListExcluidos, customConnection);
		List<LoteImpressaoAit> loteImpressaoAitList = atualizarLoteImpressaoAit(aitListExcluidos.get(0).getCdLoteImpressao(), customConnection);
	    return loteImpressaoAitList;
	}
	
	private List<LoteImpressaoAit> atualizarLoteImpressaoAit(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
	    List<LoteImpressaoAit> loteImpressaoAitList = loteImpressaoAitRepository.find(searchCriteriosLote(cdLoteImpressao), customConnection);
	    for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
	    	loteImpressaoAit.setStImpressao(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey());
	    	loteImpressaoAitRepository.update(loteImpressaoAit, customConnection);
	    }
	    return loteImpressaoAitList;
	}
	
	private void atualizarLoteImpressao(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
		loteImpressao.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_GERACAO.getKey());
    	loteImpressaoRepository.update(loteImpressao, customConnection);
	}
	
	private void excluirAitsLoteImpressao(List<LoteImpressaoAit> loteImpressaoAitList, CustomConnection customConnection) throws Exception {
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
	            loteImpressaoAitRepository.delete(loteImpressaoAit, customConnection);
		}
	}
	
	private void atualizarLote(Lote lote, CustomConnection customConnection) throws Exception {
	    lote.setCdArquivo(0);
	    loteRepository.update(lote, customConnection);
	}
	
	private void desvincularERestaurarEtiquetaAits(List<LoteImpressaoAit> loteImpressaoAitList, CustomConnection customConnection) throws Exception {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("cd_lote_impressao", loteImpressaoAitList.get(0).getCdLoteImpressao(), true);		
	    List<String> cdAitList = loteImpressaoAitList.stream()
	            .map(lote -> String.valueOf(lote.getCdAit()))
	            .collect(Collectors.toList());
	    searchCriterios.addCriterios("cd_ait", String.join(",", cdAitList), ItemComparator.IN, Types.INTEGER);

	    List<CorreiosEtiqueta> correiosEtiquetasList = correiosEtiquetaService.find(searchCriterios);
	    if (correiosEtiquetasList.isEmpty()) {
	        return;
	    }

	    atualizarEtiquetas(correiosEtiquetasList, customConnection);
	}
	
	private void atualizarEtiquetas(List<CorreiosEtiqueta> correiosEtiquetasList, CustomConnection customConnection) throws Exception {
	    for (CorreiosEtiqueta correiosEtiqueta : correiosEtiquetasList) {
	        correiosEtiqueta.setTpStatus(-1);
	        correiosEtiqueta.setCdLoteImpressao(-1);
	        correiosEtiqueta.setCdAit(-1);
	        correiosEtiqueta.setDtEnvio(null);
	        this.correiosEtiquetaRepository.update(correiosEtiqueta, customConnection);
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
		geracaoDocumentoSse.iniciarThreadGeracaoDocumentos(String.valueOf(cdLoteImpressao), new LoteGeracaoDocumentosTask(cdLoteImpressao, cdUsuario));
		getStatus(cdLoteImpressao, customConnection).getStDocumento();
	}
	
	@Override
	public void iniciarGeracaoDocumentosAit(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
	    try {
	        customConnection.initConnection(false); 
	        LoteImpressao loteImpressao = loteImpressaoRepository.get(cdLoteImpressao, customConnection);
	        AsyncListProcessor<LoteImpressao> listProcessor = new AsyncListProcessor<>();
	        List<LoteImpressao> listaLoteImpressao = Arrays.asList(loteImpressao);
	        int totalDocumentos = listaLoteImpressao.size();
	        final int[] progressoAtual = {0};

	        for (LoteImpressao item : listaLoteImpressao) {
	            listProcessor.process(
	                Arrays.asList(item),
	                ait -> {
	                    gerarDocumentoParaAit(item, cdUsuario, customConnection); 
	                    synchronized (progressoAtual) {
	                        progressoAtual[0]++;
	                        notificarProgresso(progressoAtual[0], totalDocumentos);
	                    }
	                }
	            );
	        }
	        customConnection.finishConnection(); 
	    } finally {
	        customConnection.closeConnection();
	    }
	}
	
	private void gerarDocumentoParaAit(LoteImpressao loteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
	    Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
	    DocumentGeneratorFactory.getStrategy(loteImpressao.getTpImpressao(), customConnection)
	        .generate(loteImpressao, lote.getIdLote(), customConnection);
	    getStatus(loteImpressao.getCdLoteImpressao(), customConnection).getStDocumento();
	}
	
	private byte[] gerarDocumentoParaAitViaUnica(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception {
	    IDocumentGeneratorStrategy generator = DocumentViaUnicaGeneratorFactory.createGenerator(loteImpressao);
		Lote lote = loteRepository.get(loteImpressao.getCdLote(), customConnection);
	    return generator.generate(loteImpressao, lote.getIdLote(), customConnection);
	}

	@Override
	public void getStatusGeracaoDocumentos(SseEventSink sseEventSink, Sse sse, int cdLoteImpressao) throws Exception {
	    GeracaoDocumentosSSE events = geracaoDocumentoSse.setSse(sseEventSink, sse);
	    boolean isConcluded = false;
	    while (!isConcluded) {
	    	
	    	if(!events.ativo()) {
	    		isConcluded = true;
	    	}
	    	
	        LoteImpressaoStatus loteStatus = getStatus(cdLoteImpressao, new CustomConnection());
	        events.setStatus(loteStatus).notificar();

	        if (loteStatus.getQtDocumentosGerados() == loteStatus.getTotalDocumentos()) {
	        	isConcluded = true;
	        }
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }
	}
	
	private void notificarProgresso(int progressoAtual, int totalDocumentos) throws Exception {
	    LoteImpressaoStatus loteStatus = new LoteImpressaoStatus();
	    loteStatus.setQtDocumentosGerados(progressoAtual);
	    loteStatus.setTotalDocumentos(totalDocumentos);
	}
	
	public LoteImpressaoStatus getStatus(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		customConnection.initConnection(false);
		try {
			LoteImpressao loteImpressao =  loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			List<LoteImpressaoAit> loteImpressaoAitsList = loteImpressaoAitRepository.findByCdLoteImpressao(cdLoteImpressao);
			int qtDocumentosGerados = 0;
			for (LoteImpressaoAit loteImpressaoAit: loteImpressaoAitsList) {
				if (loteImpressaoAit.getStImpressao() > LoteImpressaoAitSituacao.AGUARDANDO_GERACAO)
					qtDocumentosGerados++;
			}
			
			LoteImpressaoStatus status = new LoteImpressaoStatusBuilder(loteImpressao, loteImpressaoAitsList.size(), qtDocumentosGerados)
					.build();			
			customConnection.finishConnection();
			return status;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public byte[] imprimirLoteImpressao(int cdLoteImpressao) throws Exception {
		return imprimirLoteImpressao(cdLoteImpressao, new CustomConnection());
	}
	
	@Override
	public byte[] imprimirLoteImpressao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
			Arquivo arquivoLote = this.arquivoRepository.get(lote.getCdArquivo(), customConnection);
			if (lote.getCdArquivo() <= 0 || arquivoLote == null) {
				throw new ValidacaoException("O arquivo para download não foi encontrado.");
			}
			loteImpressao.setStLote(StatusLoteImpressaoEnum.IMPRESSO.getKey());
			customConnection.finishConnection();
			return arquivoLote.getBlbArquivo();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	@Override
	public PagedResponse<LoteImpressaoDTO> buscarLotesAits(LoteImpressaoSearch loteImpressaoSearch) throws ValidacaoException, Exception {
		Search<LoteImpressaoDTO> list = this.loteImpressaoRepository.findLoteAits(loteImpressaoSearch);
		if(list.getList(LoteImpressaoDTO.class).isEmpty()) {
			throw new ValidacaoException ("Não há AIT nesse Lote com esse filtro.");
		} 
		return new PagedResponse<>(list.getList(LoteImpressaoDTO.class), list.getRsm().getTotal());
	}
	
	@Override
	public LoteImpressaoDTO buscarLote(LoteImpressaoSearch loteImpressaoSearch) throws Exception, NoContentException{ 
		List<LoteImpressaoDTO> lote = this.loteImpressaoRepository.getLote(loteImpressaoSearch).getList(LoteImpressaoDTO.class);
		if(lote.isEmpty()) {
			throw new NoContentException("Nenhum Lote encontrado.");
		}
		return lote.get(0);
	}
	
	@Override
	public byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario) throws Exception {
		return gerarLoteNotificacaoNaiViaUnica(cdAit, cdUsuario, new CustomConnection());
	}
	
	private byte[] gerarLoteNotificacaoNaiViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			byte[] impressaoNai = new ProcessadorImpressaoNai(cdAit, cdUsuario).gerar(customConnection);
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
	
	private NipImpressaoDTO gerarLoteNotificacaoNipViaUnica(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			TipoStatusEnum tpNotificacao = getTipoNotificacaoNipAdvertencia(cdAit, customConnection);
			NipImpressaoDTO impressaoNIP = new ProcessadorImpressaoNip(cdAit, tpNotificacao, cdUsuario).gerar(customConnection);
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
	public LoteImpressao save(LoteImpressao loteImpressao, CustomConnection customConnection) throws Exception{
		try {
			if(loteImpressao == null)
				throw new ValidacaoException("Erro ao salvar. LoteImpressao é nulo.");
			customConnection.initConnection(true);
			if(loteImpressao.getCdLoteImpressao() == 0){
				loteImpressao.setCdLoteImpressao(loteImpressaoRepository.insert(loteImpressao, customConnection).getCdLoteImpressao());
			} else {
				loteImpressao = loteImpressaoRepository.update(loteImpressao, customConnection);
				customConnection.finishConnection();
				return loteImpressao;
			} if(loteImpressao.getAits()!=null && loteImpressao.getAits().size()>0) {
				for (LoteImpressaoAit loteImpressaoAit : loteImpressao.getAits()) {
					loteImpressaoAit.setCdLoteImpressao(loteImpressao.getCdLoteImpressao());
					criarLoteImpressaoAit(loteImpressao, loteImpressaoAit.getCdAit(), customConnection);
				}
			}
			customConnection.finishConnection();
			return loteImpressao;
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public LoteImpressao gerarLoteNotificacaoAitViaUnica(List<Ait> aitList, int cdUsuario, int tipoLote) throws ValidacaoException, Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
	        LoteImpressao loteImpressao = new LoteImpressaoGenerator().gerarLoteImpressao(aitList, cdUsuario, tipoLote, customConnection);
			customConnection.finishConnection();
			return loteImpressao;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void gerarDocsLoteAitViaUnica(LoteImpressao loteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			gerarDocumentoParaAitViaUnica(loteImpressao, customConnection);
			customConnection.finishConnection();
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public Boolean verificarAitsComMesmoLote(int cdAit, int tpImpressao) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		List<LoteImpressaoAit> loteImpressaoAit = loteImpressaoAitRepository.find(searchCriterios, new CustomConnection());
		if(loteImpressaoAit.isEmpty()) {
			return false;
		}
		int qtdLote = getPertencenteLote(loteImpressaoAit.get(0).getCdLoteImpressao(), tpImpressao);
		return qtdLote > 1;
	}
	
	@Override
	public byte[] imprimirLoteNotificacao(int cdLoteImpressao, CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(true);
			LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
			Lote lote = this.loteRepository.get(loteImpressao.getCdLote(), customConnection);
			Arquivo arquivoLote = this.arquivoRepository.get(lote.getCdArquivo(), customConnection);
			if (lote.getCdArquivo() <= 0 || arquivoLote == null) {
				throw new ValidacaoException("O arquivo para download não foi encontrado.");
			}
			loteImpressao.setStLote(LoteImpressaoAitSituacao.IMPRESSO);
			save(loteImpressao, customConnection);
			customConnection.finishConnection();
			return arquivoLote.getBlbArquivo();
		}
		finally{
			customConnection.closeConnection();
		}
	}
	
	private int getPertencenteLote(int cdLoteImpressao, int tpImpressao) throws Exception{
		SearchCriterios searchCriterios = new SearchCriterios(); 
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao);
		searchCriterios.addCriteriosEqualInteger("B.tp_impressao", tpImpressao);
		Search<LoteImpressaoAit> search = new SearchBuilder<LoteImpressaoAit>("mob_lote_impressao_ait A")
				.fields(" A.* ")
				.addJoinTable("JOIN mob_lote_impressao B ON (A.cd_lote_impressao = B.cd_lote_impressao)")
				.searchCriterios(searchCriterios)
				.build();
		List<LoteImpressaoAit> totalCdLoteImpressao = search.getList(LoteImpressaoAit.class);
		return totalCdLoteImpressao.size();
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
	public List<Ait> getAllAitsEmitirNAI() throws IllegalArgumentException, SQLException, Exception {
		return getAllAitsEmitirNAI (new CustomConnection());
	}
	
	public List<Ait> getAllAitsEmitirNAI (CustomConnection customConnection) throws IllegalArgumentException, SQLException, Exception {
		customConnection.initConnection(false);
		try{
			return new ListAitsCandidatosNaiFactory()
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NAI.getKey(), new SearchCriterios())
					.build(new Criterios(), customConnection.getConnection());
		}
		finally{
			customConnection.closeConnection();
		}
	}

	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNaiLote(List<Ait> listAitsNais, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GeraMovimentoNotificacaoFactory()
				.strategy(TipoLoteImpressaoEnum.LOTE_NAI.getKey())
				.gerarMovimentos(listAitsNais, cdUsuario);
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
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey(), new SearchCriterios(), customConnection)
					.build();
		}
		finally{
			customConnection.closeConnection();
		}
	}

	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoFimPrazoDefesa(List<Ait> listAits, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GeraMovimentoNotificacaoFactory()
				.strategy(TipoLoteImpressaoEnum.LOTE_FIM_PRAZO_DEFESA.getKey())
				.gerarMovimentos(listAits, cdUsuario);
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
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NIP.getKey(), new SearchCriterios(), customConnection)
					.build();
		}
		finally{
			customConnection.closeConnection();
		}
	}

	@Override
	public List<ServicoDetranDTO> gerarMovimentoNotificacaoNipLote(List<Ait> listAitsNips, int cdUsuario) throws Exception {
		List<ServicoDetranDTO> detranDTOs = new GeraMovimentoNotificacaoFactory()
				.strategy(TipoLoteImpressaoEnum.LOTE_NIP.getKey())
				.gerarMovimentos(listAitsNips, cdUsuario);
		return detranDTOs;
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

	private List<LoteImpressaoAit> reiniciarGeracaoDocumentos(int cdLoteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteImpressao, customConnection);
		Lote lote = loteRepository.get(loteImpressao.getCdLote(), customConnection);
		int cdArquivo = lote.getCdArquivo();
		atualizarLote(lote, customConnection);
		excluirArquivo(cdArquivo, cdLoteImpressao, customConnection);
		atualizarLoteImpressao(loteImpressao, customConnection);
		List<LoteImpressaoAit> loteImpressaoAit = atualizarLoteImpressaoAit(loteImpressao.getCdLoteImpressao(), customConnection);
		iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario, customConnection);
	    return loteImpressaoAit;
	}
	
	@Override
	public Arquivo pegarArquivoLote(int cdLoteNotificacao, int tpArquivo) throws Exception { 
		SearchCriterios searchCriterios = searchCriteriosArquivoLote(cdLoteNotificacao, tpArquivo);
		List<Arquivo> arquivoList = searchArquivoLote(searchCriterios).getList(Arquivo.class);
		if(arquivoList.isEmpty()) {
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
		Search<Arquivo> search = new SearchBuilder<Arquivo>("grl_arquivo A")
				.addField("A.cd_arquivo, A.dt_arquivamento, A.cd_tipo_arquivo, A.blb_arquivo")
				.addJoinTable(" JOIN mob_lote_impressao_arquivo B ON (B.cd_arquivo = A.cd_arquivo) ")
				.searchCriterios(criterios)
				.build();
		return search;
	}

	@Override
	public List<Ait> buscarAitLoteImpressao(int cdLoteImpressao, int tipoRecurso, CustomConnection customConnection)
			throws Exception {
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
	
	public void atualizarLotePrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, List<Ait> aits, List<LoteImpressaoAit> aitsFalhosNovoLote, CustomConnection customConnection) throws ValidacaoException, Exception {
		if (!aitsFalhosNovoLote.isEmpty()) {
			
			int tipoLote = aitsFalhosNovoLote.get(0).getStImpressao() == TipoLoteImpressaoEnum.NOVO_PRAZO_DEFESA.getKey() ?
					TipoStatusEnum.NAI_ENVIADO.getKey(): TipoStatusEnum.NIP_ENVIADA.getKey();

		    try {
		        this.managerLog.info("ATUALIZANDO LOTE", "Removendo AITs com falha do lote original.");
		        excluirLoteImpressaoAit(aitsFalhosNovoLote, customConnection);
		    } catch (Exception e) {
		        throw new Exception("Falha ao remover AITs do lote de impressão: " + alteraPrazoRecursoDTO.getCdLoteImpressao(), e);
		    }

		    CreateLoteImpressaoDTO createLoteImpressao = new CreateLoteImpressaoDTO();
		    createLoteImpressao.setTpImpressao(tipoLote);
		    createLoteImpressao.setCdCriador(alteraPrazoRecursoDTO.getCdUsuario());

		    List<CreateAitLoteImpressaoDTO> createLoteImpressaoDTOList = new ArrayList<>();
		    for (LoteImpressaoAit loteImpressaoAit : aitsFalhosNovoLote) {
		    	CreateAitLoteImpressaoDTO dto = new CreateAitLoteImpressaoDTO();
		        dto.setCdAit(loteImpressaoAit.getCdAit());
		        createLoteImpressaoDTOList.add(dto);
		    }
		    createLoteImpressao.setAits(createLoteImpressaoDTOList);

		    LoteImpressao novoLote = insert(createLoteImpressao, customConnection);
		    customConnection.finishConnection();

		    CustomConnection newCustomConnection = new CustomConnection();
		    iniciarGeracaoDocumentos(novoLote.getCdLoteImpressao(), alteraPrazoRecursoDTO.getCdUsuario(), newCustomConnection);			
		}
		if(aits.size() == aitsFalhosNovoLote.size()) 
			delete(alteraPrazoRecursoDTO.getCdLoteImpressao(), customConnection);
		else
			reiniciarGeracaoDocumentos(alteraPrazoRecursoDTO.getCdLoteImpressao(), alteraPrazoRecursoDTO.getCdUsuario(), customConnection);
	}
}
