package com.tivic.manager.grl.parametro;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
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

@Api(value = "parametro", tags = { "grl" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/grl/parametros")
@Produces(MediaType.APPLICATION_JSON)
public class ParametroController {
	private IParametroService parametroService;
	private ManagerLog managerLog;

	public ParametroController() throws Exception {
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/int/{nmParametro}")
	@ApiOperation(value = "Retorna o valor do parametro em formaro de número")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parametro encontrado com sucesso", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Parametro não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getValorOfParametroAsInt(@ApiParam(value = "Nome do parametro") @PathParam("nmParametro") String nmParametro) {
		try {
			return ResponseFactory.ok(parametroService.getValorOfParametroAsInt(nmParametro));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/string/{nmParametro}")
	@ApiOperation(value = "Retorna o valor do parametro em formato de string")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parametro encontrado com sucesso", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Parametro não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getValorOfParametroAsString(@ApiParam(value = "Nome do parametro") @PathParam("nmParametro") String nmParametro) {
		try {
			return ResponseFactory.ok(parametroService.getValorOfParametroAsString(nmParametro));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
}
