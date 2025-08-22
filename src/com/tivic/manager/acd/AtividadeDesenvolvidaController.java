package com.tivic.manager.acd;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "AtividadeDesenvolvida", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/atividadedesenvolvida/")
@Produces(MediaType.APPLICATION_JSON)
public class AtividadeDesenvolvidaController {

	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Cadastra uma atividade desenvolvida"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Atividade Desenvolvida cadastrada"),
		@ApiResponse(code = 400, message = "Atividade Desenvolvida não pode ser salvo"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(AtividadeDesenvolvida atividadeDesenvolvida){
		try {
			Result result = AtividadeDesenvolvidaServices.save(atividadeDesenvolvida);
			if(result.getCode() > 0)
				return ResponseFactory.ok(atividadeDesenvolvida);
			else
				return ResponseFactory.badRequest(result.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/{cdAtividadeDesenvolvida}")
	@ApiOperation(
			value = "Busca uma atividade desenvolvida"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado da Atividade Desenvolvida"),
		@ApiResponse(code = 204, message = "Atividade Desenvolvida não encontrada"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdAtividadeDesenvolvida") int cdAtividadeDesenvolvida){
		try {
			AtividadeDesenvolvida atividadeDesenvolvida = AtividadeDesenvolvidaDAO.get(cdAtividadeDesenvolvida);
			
			if(atividadeDesenvolvida == null)
				return ResponseFactory.noContent("Atividade não encontrada");
				
			return ResponseFactory.ok(atividadeDesenvolvida);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	
	
	
//	ANTIGOS
	
	@DELETE
	@Path("/{cdAtividadeDesenvolvida}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(@PathParam("cdAtividadeDesenvolvida") int cdAtividadeDesenvolvida){
		try {
			Result result = AtividadeDesenvolvidaServices.remove(cdAtividadeDesenvolvida);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = AtividadeDesenvolvidaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/find")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String find(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AtividadeDesenvolvidaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}