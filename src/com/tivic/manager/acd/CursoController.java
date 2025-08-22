package com.tivic.manager.acd;

import java.sql.Types;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.RestData;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

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

@Api(value = "Curso", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/curso")
@Produces(MediaType.APPLICATION_JSON)
public class CursoController {


	@GET
	@Path("/{cdCurso}")
	@ApiOperation(
			value = "Busca uma curso"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Curso buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdCurso") int cdCurso, @QueryParam("cascade") boolean cascade) {
		try {
			if(cdCurso <= 0) 
				return ResponseFactory.badRequest("Código da curso é nulo ou inválido");
			
			CursoDTO cursoDto = new CursoDTO.Builder(cdCurso, cascade).build();
			
			if(cursoDto == null){
				return ResponseFactory.noContent("Nenhuma curso encontrada");
			}
			
			return ResponseFactory.ok(cursoDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdInstituicao}/getallcursosofinstituicao")
	@ApiOperation(
			value = "Busca uma curso"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Curso buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdInstituicao") int cdInstituicao) {
		try {
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("C�digo da instituicao � nulo ou inv�lido");
			
			ResultSetMap rsm = CursoServices.getAllCursosOf(cdInstituicao);
			
			if(rsm == null){
				return ResponseFactory.noContent("Nenhuma curso encontrado");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Cursos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso encontrado", response = Curso[].class),
			@ApiResponse(code = 204, message = "Curso n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome da etapa") @QueryParam("etapa") String nmEtapa,
			@ApiParam(value = "Nome do curso") @QueryParam("curso") String nmCurso
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(nmEtapa != null) {
				criterios.add("nm_etapa", nmEtapa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmCurso != null) {
				criterios.add("nm_curso", nmCurso, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = CursoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum curso encontrado");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/cursosofertados")
	@ApiOperation(
			value = "Retorna Cursos Ofertados"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso encontrado", response = Curso[].class),
			@ApiResponse(code = 204, message = "Curso n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getCursosOfertados(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome da etapa") @QueryParam("etapa") String nmEtapa,
			@ApiParam(value = "Nome do curso") @QueryParam("curso") String nmCurso
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(nmEtapa != null) {
				criterios.add("nm_etapa", nmEtapa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmCurso != null) {
				criterios.add("nm_curso", nmCurso, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = CursoServices.findCursosOfertados(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum curso encontrado");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	
	
	
//ANTIGOS	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Curso curso){
		try {
			Result result = CursoServices.save(curso);
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
	public String remove(Curso curso){
		try {
			Result result = CursoServices.remove(curso.getCdCurso());
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
			ResultSetMap rsm = CursoServices.getAll();
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
			ResultSetMap rsm = CursoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findcursosofertados")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findCursosOfertados(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = CursoServices.findCursosOfertados(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallcursosof")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllCursosOf(RestData restData) {
		try {
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = CursoServices.getAllCursosOf(cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallcursosofcomturmas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllCursosOfComTurmas(RestData restData) {
		try {
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			boolean incluirTurmas = Boolean.parseBoolean(String.valueOf(restData.getArg("incluirTurmas")));
			
			ResultSetMap rsm = CursoServices.getAllCursosOfComTurmas(cdInstituicao, incluirTurmas);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
