package com.tivic.manager.mob.lote.impressao.geracaodocumento;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

public interface IGeracaoDocumentosSSE {

	public GeracaoDocumentosSSE setSse(SseEventSink sseEventSink, Sse sse);
	public GeracaoDocumentosSSE removeThread(Thread thread);
	public Thread getThread(String name) throws Exception;
	public void iniciarThreadGeracaoDocumentos(String threadGeracao, Runnable task) throws Exception;

}
