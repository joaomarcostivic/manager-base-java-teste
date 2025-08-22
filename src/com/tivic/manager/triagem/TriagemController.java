package com.tivic.manager.triagem;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.triagem.dtos.GrupoEventoTriagemDTO;
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
public class TriagemController {
	
	private ManagerLog managerLog;
	private ITriagemService triagemService;

	public TriagemController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		triagemService = (ITriagemService) BeansFactory.get(ITriagemService.class);
	}
		
	@GET
	@Path("/grupos")
	@ApiOperation(value = "Retorna a lista de grupos de eventos.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados com sucesso.", response = GrupoEventoTriagemDTO.class),
			@ApiResponse(code = 204, message = "Nenhum evento encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response findGrupos(@ApiParam(value = "Nome do parametro") @PathParam("nmParametro") String nmParametro) {
		try {
			return ResponseFactory.ok(triagemService.findGrupos());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
