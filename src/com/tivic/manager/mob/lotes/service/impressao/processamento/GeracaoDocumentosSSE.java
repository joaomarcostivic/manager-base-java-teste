package com.tivic.manager.mob.lotes.service.impressao.processamento;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;

public class GeracaoDocumentosSSE implements IGeracaoDocumentosSSE {

    private final ThreadManager threadManager;
    private SseEventSink sseEventSink;
    private Sse sse;
    private LoteImpressaoStatus status;

    public GeracaoDocumentosSSE() {
        this.threadManager = new ThreadManager();
    }

    public GeracaoDocumentosSSE(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    @Override
    public GeracaoDocumentosSSE setSse(SseEventSink sseEventSink, Sse sse) {
        this.sseEventSink = sseEventSink;
        this.sse = sse;
        return this;
    }

    public GeracaoDocumentosSSE setStatus(LoteImpressaoStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public void notificar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        if (!sseEventSink.isClosed()) {
            OutboundSseEvent sseEvent = sse.newEventBuilder()
                    .name("st_lote")
                    .data(mapper.writeValueAsString(status))
                    .build();
            sseEventSink.send(sseEvent);

            Thread thread = getThread(String.valueOf(status.getCdLoteImpressao()));
            if (thread == null) {
                sseEventSink.close();
                return;
            }
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
    public GeracaoDocumentosSSE removeThread(Thread thread) {
        threadManager.removeThread(thread);
        return this;
    }

    @Override
    public Thread getThread(String name) {
        return threadManager.getThread(name);
    }

    @Override
    public void iniciarThreadGeracaoDocumentos(String threadGeracao, Runnable task) throws Exception {
        threadManager.iniciarThread(threadGeracao, task, new FinalizaThreadsGeracaoLote());
    }
}

