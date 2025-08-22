package com.tivic.manager.mob.processamento.sincronizacao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v3/sincronizacao")
@Produces(MediaType.APPLICATION_JSON)
public class SincronizacaoController {

	private ISincronizacaoService sincronizacaoService;
	private ManagerLog managerLog;
	
	public SincronizacaoController() throws Exception {
		this.sincronizacaoService = (ISincronizacaoService) BeansFactory.get(ISincronizacaoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/sincronizar")
	@ApiOperation(value = "Sincroniza os eventos do radar com o cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos sincronizados com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Status da sincronização de eventos não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void sincronizar(@Context SseEventSink sseEventSink, @Context Sse sse) throws Exception {
		try {
			sincronizacaoService.sincronizar(sseEventSink, sse);
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception(e.getMessage());
		}
	}
	
	@GET
	@Path("/status")
	@ApiOperation(value = "Sincroniza os eventos do radar com o cliente")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos sincronizados com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Status da sincronização de eventos não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getStatus() {
		try {
			return ResponseFactory.ok(sincronizacaoService.getStatus());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}