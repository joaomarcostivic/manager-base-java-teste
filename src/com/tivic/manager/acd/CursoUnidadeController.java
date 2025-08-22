package com.tivic.manager.acd;

import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
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
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "CursoUnidade", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/cursounidade/")
@Produces(MediaType.APPLICATION_JSON)
public class CursoUnidadeController {

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getBy(
		@ApiParam(value = "Código do curso", required = true) @QueryParam("cdCurso") int cdCurso
	) {
		try {
			if(cdCurso <= 0) 
				return ResponseFactory.badRequest("Código do curso é nulo ou inválido");
			
			ResultSetMap rsm = CursoUnidadeServices.getAllByCurso(cdCurso);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhum curso encontrado");
			}
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
		
	}
	

	@GET
	@Path("/{cdUnidade}/{cdCurso}")
	@ApiOperation(
			value = "Busca um curso unidade"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado do Curso Unidade"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Curso Unidade não encontrado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(
		@ApiParam(value = "Código da unidade") @PathParam("cdUnidade") int cdUnidade, 
		@ApiParam(value = "Código do curso") @PathParam("cdCurso") int cdCurso
	){
		try {
			if(cdUnidade <= 0) 
				return ResponseFactory.badRequest("Código da unidade é nulo ou inválido");
			
			if(cdCurso <= 0) 
				return ResponseFactory.badRequest("Código do curso é nulo ou inválido");
			
			CursoUnidade cursoUnidade = CursoUnidadeDAO.get(cdUnidade, cdCurso);
			if(cursoUnidade == null){
				return ResponseFactory.noContent("Curso Unidade não encontrado");
			}
			
			return ResponseFactory.ok(cursoUnidade);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	
	
	
//  ANTIGOS	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(CursoUnidade cursoUnidade){
		try {
			Result result = CursoUnidadeServices.save(cursoUnidade);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@DELETE
	@Path("/remove")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String remove(CursoUnidade cursoUnidade){
		try {
			Result result = CursoUnidadeServices.remove(cursoUnidade.getCdUnidade(), cursoUnidade.getCdCurso());
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
			ResultSetMap rsm = CursoUnidadeServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbycurso")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByCurso(RestData restData) {
		try {
			
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			ResultSetMap rsm = CursoUnidadeServices.getAllByCurso(cdCurso);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/curso/{cdCurso}")
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllByCurso(@PathParam("cdCurso") int cdCurso) {
		try {
			ResultSetMap rsm = CursoUnidadeServices.getAllByCurso(cdCurso);
			return ResponseFactory.ok(rsm);
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
			ResultSetMap rsm = CursoUnidadeServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
