package com.tivic.manager.mob.ecarta.services;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.ecarta.dtos.ArquivoServicoDTO;
import com.tivic.manager.mob.ecarta.dtos.ECartaItemSV;
import com.tivic.manager.mob.ecarta.relatorios.GeraDocumentoFactory;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoAitRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.ILoteImpressaoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.manager.mob.lote.impressao.builders.LoteImpressaoArquivoBuilder;
import com.tivic.manager.mob.lote.impressao.enums.LoteImpressaoSituacaoEnum;
import com.tivic.manager.util.manipulatefiles.IManipulateFolder;
import com.tivic.manager.util.manipulatefiles.PathFolderBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ArquivoServiceEDI implements IArquivoServiceEDI {
	private String path;
	private List<ECartaItemSV> items;
	private List<Thread> threads = new ArrayList<Thread>();
	private IManipulateFolder manipulateFolder;
	private ILoteImpressaoAitRepository loteImpressaoAitRepository;
	private CustomConnection customConnection;
	private ILoteImpressaoRepository loteImpressaoRepository;
	private IArquivoService arquivoService;
	private ILoteImpressaoArquivoRepository loteImpressaoArquivoRepository;
	private IParametroRepository parametroRepository;
	SseEventSink sseEventSink;
	Sse sse;
	LoteImpressaoStatus status;

	public ArquivoServiceEDI() throws Exception {
		this.manipulateFolder = (IManipulateFolder) BeansFactory.get(IManipulateFolder.class);
		this.loteImpressaoAitRepository = (ILoteImpressaoAitRepository) BeansFactory
				.get(ILoteImpressaoAitRepository.class);
		this.loteImpressaoRepository = (ILoteImpressaoRepository) BeansFactory.get(ILoteImpressaoRepository.class);
		arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteImpressaoArquivoRepository = (ILoteImpressaoArquivoRepository) BeansFactory
				.get(ILoteImpressaoArquivoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public ArquivoServiceEDI setSse(SseEventSink sseEventSink, Sse sse) {
		this.sseEventSink = sseEventSink;
		this.sse = sse;
		return this;
	}

	public ArquivoServiceEDI setStatus(LoteImpressaoStatus status) {
		this.status = status;
		return this;
	}

	@Override
	public void gerar(int cdLoteNotificacao, int cdUsuario) throws Exception {
		this.customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			criarPasta(cdLoteNotificacao);
			this.items = montarItems(cdLoteNotificacao);
			ArquivoServicoDTO arquivoZIP = criarArquivo(this.items, this.path);
			salvarArquivoLote(cdLoteNotificacao, arquivoZIP, cdUsuario);
			this.manipulateFolder.removeAllFiles(new File(this.path));
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

	public void notificar() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		if (sseEventSink.isClosed()) {
			return;
		}
		OutboundSseEvent sseEvent = sse.newEventBuilder().name("st_lote").data(mapper.writeValueAsString(status))
				.build();
		sseEventSink.send(sseEvent);
		Thread thread = getThread(String.valueOf(status.getCdLoteImpressao()));
		if (thread == null) {
			sseEventSink.close();
			return;
		}
		if (concluido()) {
			removeThread(thread);
			sseEventSink.close();
		}
		intervalo();
	}

	public boolean concluido() {
		return status.getQtDocumentosGerados() == status.getTotalDocumentos();
	}

	public boolean ativo() {
		return !sseEventSink.isClosed();
	}

	public void intervalo() throws InterruptedException {
		TimeUnit.SECONDS.sleep(1);
	}

	@Override
	public Thread getThread(String name) throws Exception {
		Optional<Thread> thread = threads.parallelStream().filter(t -> t.getName().equals(name)).findFirst();

		return thread.isPresent() ? thread.get() : null;
	}

	@Override
	public ArquivoServiceEDI removeThread(Thread thread) {
		thread.interrupt();
		this.threads.remove(thread);
		return this;
	}

	@Override
	public void iniciarThreadGeracaoArquivoServiceEDI(String threadGeracao, Runnable task) throws Exception {
		Thread threadGeracaoarquivoServiceEDI = new Thread(task);
		threadGeracaoarquivoServiceEDI.setName(threadGeracao);
		addThread(threadGeracaoarquivoServiceEDI);
		threadGeracaoarquivoServiceEDI.setUncaughtExceptionHandler(new FinalizaThreadsGeracaoArquivoServicoEDI());
		threadGeracaoarquivoServiceEDI.start();
	}

	private ArquivoServiceEDI addThread(Thread thread) {
		threads.add(thread);
		return this;
	}

	private void criarPasta(int cdLoteNotificacao) {
		this.path = ManagerConf.getInstance().get("TOMCAT_WORK_DIR",
				new PathFolderBuilder().add("").add("tivic").add("work").getPath())
				+ new PathFolderBuilder().add("E-Carta_" + cdLoteNotificacao).getPath();
		this.manipulateFolder.createFolder(this.path);
	}

	private List<ECartaItemSV> montarItems(int cdLoteNotificacao) throws Exception {
		List<ECartaItemSV> eCartaItemSVList = new ArrayList<ECartaItemSV>();
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteNotificacao, true);
		List<LoteImpressaoAit> loteImpressaoAitList = this.loteImpressaoAitRepository.find(searchCriterios,
				this.customConnection);
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteNotificacao, customConnection);
		for (LoteImpressaoAit loteImpressaoAit : loteImpressaoAitList) {
			loteImpressaoAit.setStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_IMPRESSAO);
			ECartaItemSV eCartaItemSV = new BuscaDadosItemSV().buscar(loteImpressaoAit.getCdAit(), cdLoteNotificacao,
					customConnection);
			eCartaItemSV.setBlbNotificacao(new GeraDocumentoFactory().getStrategy(loteImpressao.getTpDocumento())
					.gerar(loteImpressaoAit.getCdAit(), customConnection));
			loteImpressaoAitRepository.update(loteImpressaoAit, new CustomConnection());
			eCartaItemSVList.add(eCartaItemSV);
		}
		return eCartaItemSVList;
	}

	private ArquivoServicoDTO criarArquivo(List<ECartaItemSV> itens, String path) throws Exception {
		ArquivoServicoDTO arquivoZIP = new ECTArquivosZIP().gerarAquivoServico(itens, path);
		return arquivoZIP;
	}

	private void salvarArquivoLote(int cdLoteNotificacao, ArquivoServicoDTO arquivoZIP, int cdUsuario)
			throws Exception {
		String nomeArquivo = arquivoZIP.getNmArquivo();
		String nomeSemExtensao = nomeArquivo.replaceAll("\\.zip$", "");
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteNotificacao, true);
		LoteImpressao loteImpressao = this.loteImpressaoRepository.get(cdLoteNotificacao, this.customConnection);
		Arquivo arquivoNotificacao = new ArquivoBuilder()
				.setBlbArquivo(arquivoZIP
				.getArquivoServico())
				.setCdUsuario(cdUsuario)
				.setNmArquivo(nomeArquivo)
				.setNmDocumento(nomeSemExtensao)
				.setDtArquivamento(new GregorianCalendar())
				.setDtCriacao(new GregorianCalendar())
				.setCdTipoArquivo(parametroRepository.getValorOfParametroAsInt("TP_ARQUIVO_ARQUIVO_DE_SERVICO",
						this.customConnection))
				.build();
		this.arquivoService.save(arquivoNotificacao);
		loteImpressao.setCdArquivo(arquivoNotificacao.getCdArquivo());
		loteImpressao.setStLoteImpressao(LoteImpressaoSituacaoEnum.ARQUIVO_SERVICO_ECARTAS_DISPONIVEL.getKey());
		LoteImpressaoServices.save(loteImpressao);
		vincularArquivoLote(loteImpressao, arquivoNotificacao);
	}

	private void vincularArquivoLote(LoteImpressao loteImpressao, Arquivo arquivoNotificacao) throws Exception {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivoBuilder()
				.setCdArquivo(arquivoNotificacao.getCdArquivo()).setCdLoteImpressao(loteImpressao.getCdLoteImpressao())
				.build();
		loteImpressaoArquivoRepository.insert(loteImpressaoArquivo, this.customConnection);
	}
}
