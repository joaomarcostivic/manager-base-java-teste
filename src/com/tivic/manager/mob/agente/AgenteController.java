package com.tivic.manager.mob.agente;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Agente;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
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

@Api(value = "Agente", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/agente")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AgenteController {
	private IAgenteService agenteService;
	private ManagerLog managerLog;
	
	public AgenteController() throws Exception {
		agenteService = (IAgenteService) BeansFactory.get(IAgenteService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@DELETE
	@Path("/delete/{cdAgente}")
	@ApiOperation(value = "Exclusão de Agente")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Agente excluido", response = Agente.class),
			@ApiResponse(code = 400, message = "Erro ao excluir Agente", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response delete(
			@ApiParam(value = "Código do Agente") @PathParam("cdAgente") int cdAgente) {
		try {
			agenteService.delete(cdAgente);
			return ResponseFactory.noContent();
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
