package com.tivic.manager.acd;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.acd.InstituicaoCirculo;
import com.tivic.manager.acd.InstituicaoCirculoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "InstituicaoCirculo", tags = {"InstituicaoCirculo"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/instituicaocirculo")
@Produces(MediaType.APPLICATION_JSON)
public class InstituicaoCirculoController {

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova InstituicaoCirculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "InstituicaoCirculo registrada", response = InstituicaoCirculo.class),
			@ApiResponse(code = 400, message = "InstituicaoCirculo possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "PESSOAJURIDICA a ser registrado", required = true) InstituicaoCirculo instituicaoCirculo) {
		try {	
			instituicaoCirculo.setCdCirculo(0);
			
			Result r = InstituicaoCirculoServices.save(instituicaoCirculo);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("InstituicaoCirculo possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((InstituicaoCirculo)r.getObjects().get("InstituicaoCirculo"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma InstituicaoCirculo",
			notes = "Considere id = cdPessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "InstituicaoCirculo atualizado", response = InstituicaoCirculo.class),
			@ApiResponse(code = 400, message = "InstituicaoCirculo � nulo ou inv�lido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da PESSOAJURIDICA a ser atualizado", required = true) @PathParam("id") int cdCirculo, 
			@ApiParam(value = "PESSOAJURIDICA a ser atualizado", required = true) InstituicaoCirculo instituicaoCirculo) {
		try {
			if(instituicaoCirculo.getCdCirculo() == 0) 
				return ResponseFactory.badRequest("InstituicaoCirculo � nulo ou inv�lido");
			
			instituicaoCirculo.setCdCirculo(cdCirculo);
			Result r = InstituicaoCirculoServices.save(instituicaoCirculo);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("InstituicaoCirculo � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((InstituicaoCirculo)r.getObjects().get("InstituicaoCirculo"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}

}
