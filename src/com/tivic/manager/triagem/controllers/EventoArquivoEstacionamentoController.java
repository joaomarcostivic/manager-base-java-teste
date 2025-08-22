package com.tivic.manager.triagem.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.services.arquivo.IEventoArquivoEstacionamentoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "triagem", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/triagem/eventos/estacionamento")
@Produces(MediaType.APPLICATION_JSON)
public class EventoArquivoEstacionamentoController {
	
	private ManagerLog managerLog;
	private IEventoArquivoEstacionamentoService eventoArquivoEstacionamentoService;

	public EventoArquivoEstacionamentoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		eventoArquivoEstacionamentoService = (IEventoArquivoEstacionamentoService) BeansFactory.get(IEventoArquivoEstacionamentoService.class);
	}
	
	@GET
	@Path("/arquivos/{cdEvento}")
	@ApiOperation(value = "Retorna a lista de arquivos por evento.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivos encontrados com sucesso.", response = EventoEquipamento.class),
			@ApiResponse(code = 204, message = "Nenhum arquivo encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getArquivosByEvento(@ApiParam(value = "Código do Evento") @PathParam("cdEvento") int cdEvento) {
		try {
			return ResponseFactory.ok(eventoArquivoEstacionamentoService.getArquivosByEvento(cdEvento));
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
