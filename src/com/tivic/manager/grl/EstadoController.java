package com.tivic.manager.grl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Estados", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/estados")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EstadoController {
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Estado"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Estado encontrado", response = Estado.class),
			@ApiResponse(code = 204, message = "Estado não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Estado") @PathParam("id") int cdEstado) {
		try {
			Estado estado = EstadoDAO.get(cdEstado);
			if(estado == null) {
				return ResponseFactory.noContent("Nenhum estado encontrado");
			}
			
			return ResponseFactory.ok(estado);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Estados"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Estado encontrado", response = Estado[].class),
			@ApiResponse(code = 204, message = "Nenhum estado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll() {
		try {
			return ResponseFactory.ok(new ResultSetMapper<Estado>(EstadoServices.getAll(), Estado.class).toList());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
