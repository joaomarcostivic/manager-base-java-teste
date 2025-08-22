package com.tivic.manager.mob;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.util.Result;

@Api(value = "RRD", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/rrds")
@Produces(MediaType.APPLICATION_JSON)
public class RrdController {
	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra um novo RRD"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "RRD registrado"),
			@ApiResponse(code = 400, message = "RRD possui algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "RRD a ser registrado", required = true) Rrd rrd) {
		try {
			rrd.setCdRrd(0);
			Result r = RrdServices.save(rrd);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("RRD possui algum parâmetro inválido", r.getMessage());
			}
			return ResponseFactory.ok(r.getObjects().get("Rrd"));
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Registra um novo RRD"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "RRD atualizado"),
			@ApiResponse(code = 400, message = "RRD possui algum parâmetro inválido"),
			@ApiResponse(code = 204, message = "RRD não encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do RRD a ser atualizado", required = true) @PathParam("id") int cdRrd,
			@ApiParam(value = "RRD a ser atualizado", required = true) Rrd rrd) {
		try {
			if(cdRrd == 0)
				return ResponseFactory.noContent("RRD não encontrado");
			
			rrd.setCdRrd(cdRrd);
			Result r = RrdServices.save(rrd);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("RRD possui algum parâmetro inválido", r.getMessage());
			}
			return ResponseFactory.ok(r.getObjects().get("Rrd"));
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	

}
