package com.tivic.manager.mob.processamento.sincronizacao;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.processamento.ProcessamentoStatusDTO;
import com.tivic.sol.connection.CustomConnection;

public interface ISincronizacaoService {
	public void sincronizar(SseEventSink eventSink, Sse sse) throws Exception;
	public void sincronizar(SseEventSink eventSink, Sse sse, CustomConnection customConnectionRadar) throws Exception;
	public ProcessamentoStatusDTO getStatus() throws Exception;
	public ProcessamentoStatusDTO getStatus(CustomConnection customConnection) throws Exception;
}
