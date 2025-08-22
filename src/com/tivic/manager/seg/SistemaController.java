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

@Api( value = "Sistemas", tags = {"seg"}, authorizations = {
		@Authorization( value = "Bearer Auth", scopes = {
				@AuthorizationScope( scope = "Bearer", description = "JWT Token")
		})
})

@Path("/v2/seg/sistemas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SistemaController {
	
	@GET
	@Path("")
	@ApiOperation( value = "Retorna lista de sistemas")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Sistemas encontrados", response = Sistema.class),
			@ApiResponse( code = 400, message = "Nenhum sistema encontrado", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response getAll() {
		try {			
			ResultSetMap rsm = SistemaServices.getAll();
			
			if(rsm == null || rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Nenhum sistema encontrado.");
			
			ResultSetMapper<Sistema> rsmConv = new ResultSetMapper<Sistema>(rsm, Sistema.class);
			
			return ResponseFactory.ok(rsmConv);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation( value = "Retorna registro de sistema")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Sistemas encontrados", response = Sistema.class),
			@ApiResponse( code = 400, message = "Nenhum sistema encontrado", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response get(@PathParam("id") int cdSistema) {
		try {			
			Sistema sistema = SistemaServices.getByCod(cdSistema);
			
			if(sistema == null || sistema.getCdSistema() <= 0)
				return ResponseFactory.badRequest("Nenhum sistema encontrado.");
			
			return ResponseFactory.ok(sistema);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/find")
	@ApiOperation( value = "Retorna lista de sistemas filtrada")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Sistemas encontrados", response = Sistema.class),
			@ApiResponse( code = 400, message = "Nenhum sistema encontrado", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class)
	})
	public static Response find(ArrayList<ItemComparator> criterios) {
		try {			
			ResultSetMap rsm = SistemaServices.find(criterios);
			
			if(rsm == null || rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Nenhum sistema encontrado.");
			
			ResultSetMapper<Sistema> rsmConv = new ResultSetMapper<Sistema>(rsm, Sistema.class);
			
			return ResponseFactory.ok(rsmConv);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation( value = "Registra novo sistema" )
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Sistema registrado", response = Sistema.class ),
			@ApiResponse( code = 400, message = "Sistema Inválido", response = ResponseBody.class ),
			@ApiResponse( code = 500, message = "Erro durante o processo de requisição", response = ResponseBody.class )
	})
	public static Response create (Sistema sistema) {
		try {
			Result result = SistemaServices.save(sistema);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok(result.getObjects().get("SISTEMA"));			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	

}
