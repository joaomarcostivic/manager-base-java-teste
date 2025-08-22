package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
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

@Api(value = "InfracaoMotivo", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/infracao-motivo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfracaoMotivoController {
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Busca todas as Infrações motivo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Infração motivo encontrada", response = AitImagem.class),
		@ApiResponse(code = 204, message = "Nao existe InfraçãoMotivo com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response getAll() {
		try {	

			ResultSetMap rsm = InfracaoMotivoServices.getAll();
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe AIT com o id indicado.");
			
			return ResponseFactory.ok(rsm);
				
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}

}
