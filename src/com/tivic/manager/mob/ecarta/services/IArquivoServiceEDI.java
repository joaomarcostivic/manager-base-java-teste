package com.tivic.manager.mob.ecarta.services;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

public interface IArquivoServiceEDI {
	void gerar(int cdLoteNotificacao, int cdUsuario) throws Exception;

	ArquivoServiceEDI removeThread(Thread thread);

	void iniciarThreadGeracaoArquivoServiceEDI(String threadGeracao, Runnable task) throws Exception;

	ArquivoServiceEDI setSse(SseEventSink sseEventSink, Sse sse);

	Thread getThread(String name) throws Exception;
}
