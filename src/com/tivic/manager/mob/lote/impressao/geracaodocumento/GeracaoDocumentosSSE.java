package com.tivic.manager.mob.lote.impressao.geracaodocumento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;

public class GeracaoDocumentosSSE implements IGeracaoDocumentosSSE {
	
	List<Thread> threads = new ArrayList<Thread>();
	SseEventSink sseEventSink;
	Sse sse;
	LoteImpressaoStatus status;
	
	public GeracaoDocumentosSSE() { } 
	
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
	
	public void notificar() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		if(!sseEventSink.isClosed()) {
			OutboundSseEvent sseEvent = sse.newEventBuilder()
					.name("st_lote")
					.data(mapper.writeValueAsString(status))
					.build();
			
			sseEventSink.send(sseEvent);
			
			Thread thread = getThread(String.valueOf(status.getCdLoteImpressao()));
			
			if(thread == null) {
				sseEventSink.close();
				return;
			}
			
			if(concluido()) {
				removeThread(thread);
				sseEventSink.close();
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
		thread.interrupt();
		threads.remove(thread);
		return this;
	}
	
	@Override
	public Thread getThread(String name) throws Exception {
		Optional<Thread> thread = threads.parallelStream().filter(t -> t.getName().equals(name)).findFirst();
		
		return thread.isPresent() ? thread.get() : null;
	}
	
	@Override
	public void iniciarThreadGeracaoDocumentos(String threadGeracao, Runnable task) throws Exception {
	    Thread threadGeracaoDocumento = new Thread(task);
	    threadGeracaoDocumento.setName(threadGeracao);
	    addThread(threadGeracaoDocumento);
	    threadGeracaoDocumento.setUncaughtExceptionHandler(new FinalizaThreadsGeracaoLote());
	    threadGeracaoDocumento.start();
    }	

	private GeracaoDocumentosSSE addThread(Thread thread) {
		threads.add(thread);
		return this;
	}
	
}
