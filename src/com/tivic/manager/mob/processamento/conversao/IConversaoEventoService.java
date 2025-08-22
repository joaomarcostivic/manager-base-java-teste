package com.tivic.manager.mob.processamento.conversao;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.processamento.ProcessamentoStatusDTO;
import com.tivic.manager.mob.processamento.conversao.dtos.GrupoEventoDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IConversaoEventoService {
	public void converter(SseEventSink eventSink, Sse sse, int cdUsuario) throws Exception;
	public void converter(SseEventSink eventSink, Sse sse, int cdUsuario, CustomConnection customConnectionRadar) throws Exception;
	public ProcessamentoStatusDTO getStatus() throws Exception;
	public ProcessamentoStatusDTO getStatus(CustomConnection customConnection) throws Exception;
	public PagedResponse<GrupoEventoDTO> getNaoConvertidosAgrupados(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<GrupoEventoDTO> getNaoConvertidosAgrupados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public PagedResponse<EventoEquipamento> getNaoConvertidos(SearchCriterios searchCriterios) throws Exception;
	public PagedResponse<EventoEquipamento> getNaoConvertidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
}
