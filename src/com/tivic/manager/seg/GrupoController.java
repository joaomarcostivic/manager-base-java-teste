package com.tivic.manager.seg;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

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

@Api( value = "Grupos", tags = {"seg"}, authorizations = {
		@Authorization( value = "Bearer Auth", scopes = {
				@AuthorizationScope( scope = "Bearer", description = "JWT Token")
		})
})

@Path("/v2/seg/grupos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GrupoController {
	
	@GET
	@Path("")
	@ApiOperation( value = "Retorna lista de grupos")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Grupos encontrados", response = Grupo.class),
			@ApiResponse( code = 400, message = "Nenhum grupo encontrado", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response getAll() {
		
		try {
			ResultSetMap rsm = GrupoServices.getAll();
			
			ResultSetMapper<Grupo>  rsmConv = new ResultSetMapper<Grupo>(rsm, Grupo.class);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.badRequest("Não foram encontrados grupos");
			
			return ResponseFactory.ok(rsmConv);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

	@POST
	@Path("")
	@ApiOperation( value = "Registra novo grupo")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Grupo registrado", response = Grupo.class),
			@ApiResponse( code = 400, message = "Grupo inválido", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response create(@ApiParam( value = "Grupo a ser registrado" ) Grupo grupo) {
		
		try {
			Result result = GrupoServices.save(grupo);
						
			if(result.getCode() == 0)
				return ResponseFactory.badRequest("Grupo inválido");
			
			return ResponseFactory.ok(result.getObjects().get("GRUPO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/{id}")
	@ApiOperation( value = "Atualiza registro de grupo")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Grupo atualizado", response = Grupo.class),
			@ApiResponse( code = 400, message = "Grupo inválido", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response update(@ApiParam( value = "id" ) @PathParam("id") int cdGrupo, 
								  @ApiParam( value = "Grupo a ser atualizado" ) Grupo grupo) {
		
		try {
			
			grupo.setCdGrupo(cdGrupo);
			Result result = GrupoServices.save(grupo);
						
			if(result.getCode() == 0)
				return ResponseFactory.badRequest("Grupo inválido");
			
			return ResponseFactory.ok(result.getObjects().get("GRUPO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

}
