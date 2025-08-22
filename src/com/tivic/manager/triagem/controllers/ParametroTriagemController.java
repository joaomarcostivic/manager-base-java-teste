package com.tivic.manager.triagem.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.triagem.dtos.ParametroTriagemDTO;
import com.tivic.manager.triagem.services.IParametroTriagemService;
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
@Path("/v3/triagem/parametros")
@Produces(MediaType.APPLICATION_JSON)
public class ParametroTriagemController {
	
	private ManagerLog managerLog;
	private IParametroTriagemService parametroTriagemService;

	public ParametroTriagemController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.parametroTriagemService = (IParametroTriagemService) BeansFactory.get(IParametroTriagemService.class);
	}
	
	@GET
	@Path("/{nmParametro}")
	@ApiOperation(value = "Retorna o parametro usando o nome para a busca")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parametro encontrado com sucesso", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Parametro não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getValorOfParametroByName(@ApiParam(value = "Nome do parametro") @PathParam("nmParametro") String nmParametro) {
		try {
			ParametroTriagemDTO parametroTriagemDTO = this.parametroTriagemService.getValorOfParametroByName(nmParametro);
			return ResponseFactory.ok(parametroTriagemDTO);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
