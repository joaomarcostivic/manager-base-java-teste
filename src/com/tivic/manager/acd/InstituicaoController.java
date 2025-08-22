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

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.geo.Ponto;
import com.tivic.manager.grl.LogradouroDTO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.mob.RrdServices;
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

@Api(value = "Instituição", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/instituicao")
@Produces(MediaType.APPLICATION_JSON)
public class InstituicaoController {

	

	@GET
	@Path("/{cdInstituicao}")
	@ApiOperation(
			value = "Busca uma instituição"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Instituição buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdInstituicao") int cdInstituicao, @QueryParam("cascade") boolean cascade) {
		try {
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("Código da instituição é nulo ou inválido");
			
			InstituicaoDTO instituicaoDto = new InstituicaoDTO.Builder(cdInstituicao, cascade).build();
			
			if(instituicaoDto == null){
				return ResponseFactory.noContent("Nenhuma instituicao encontrada");
			}
			
			return ResponseFactory.ok(instituicaoDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Instituicoes"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Instituicao encontrado", response = Instituicao[].class),
			@ApiResponse(code = 204, message = "Instituicao n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("qtLimite") int qtLimite,
			//@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("2") @QueryParam("cdInstituicaoSuperior") int cdInstituicaoSuperior,
			@ApiParam(value = "Nome da instituicao") @QueryParam("instituicao") String nmPessoa
		) {
		try {
			
			Criterios criterios = new Criterios();
			

			if(nmPessoa != null && !nmPessoa.equals("")) {
				criterios.add("L.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			ResultSetMap rsm = InstituicaoServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Instituicao encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdInstituicao}/{cdPeriodoLetivo}/getallmatriculasativas")
	@ApiOperation(
			value = "Busca todas as matriculas ativas de uma instiui��o em um periodo letivo"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAllMatriculasAtivas(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdInstituicao") int cdInstituicao,
			@ApiParam(value = "C�digo do Periodo Letivo", required = true) @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo
			
			) {
		try {
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(InstituicaoDAO.get(cdInstituicao) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = InstituicaoServices.getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Matriculas n�o encontradas");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdInstituicao}/{cdPeriodoLetivo}/getAllTurmasByInstituicaoPeriodo")
	@ApiOperation(
			value = "Busca todas as turmas de uma instiui��o em um periodo"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getDisciplinasTurma(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdInstituicao") int cdInstituicao,
			@ApiParam(value = "C�digo do Periodo Letivo", required = true) @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo
			) {
		try {
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(InstituicaoDAO.get(cdInstituicao) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(cdInstituicao,cdPeriodoLetivo);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@DELETE
	@Path("{cdInstituicao}/{cdCurso}/{cdPeriodoLetivo}/{cdUsuario}/removecursofrominstituicao")
	@ApiOperation(
			value = "Apaga um Curso vinculado a uma instituicao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Curso apagado"),
			@ApiResponse(code = 400, message = "Curso possui algum par�metro inv�lido"),
			@ApiResponse(code = 204, message = "Curso n�o encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response deleteCursoFromInstituicao(
			@ApiParam(value = "Id do cdInstituicao", required = true) @PathParam("cdInstituicao") int cdInstituicao,
			@ApiParam(value = "Id do cdCurso", required = true) @PathParam("cdCurso") int cdCurso,
			@ApiParam(value = "Id do cdPeriodoLetivo", required = true) @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo,
			@ApiParam(value = "Id do cdUsuario", required = true) @PathParam("cdUsuario") int cdUsuario) {
		try {
			Result r = InstituicaoServices.dropCursoFromInstituicao(cdInstituicao, cdCurso, cdPeriodoLetivo, cdUsuario);
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok("Curso from instituicao apagado");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
		
	}
	
	
	
	
	//ANTIGOS
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(RestData restData){
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			Instituicao instituicao = objectMapper.convertValue(restData.getArg("instituicao"), Instituicao.class);
			InstituicaoEducacenso instituicaoEducacenso = objectMapper.convertValue(restData.getArg("instituicaoEducacenso"), InstituicaoEducacenso.class);
			PessoaEndereco pessoaEndereco = objectMapper.convertValue(restData.getArg("pessoaEndereco"), PessoaEndereco.class);
			Ponto ponto = objectMapper.convertValue(restData.getArg("pontoLocalizacao"), Ponto.class);
			int cdUsuario = objectMapper.convertValue(restData.getArg("cdUsuario"), Integer.class);
			
			Result result = InstituicaoServices.save(instituicao, instituicaoEducacenso, pessoaEndereco, ponto, cdUsuario);
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
	public String remove(Instituicao instituicao){
		try {
			Result result = InstituicaoServices.remove(instituicao.getCdInstituicao());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAllRest() {
		try {
			ResultSetMap rsm = InstituicaoServices.getAll();
			JSONArray instituicoesJson = new JSONArray();
			while(rsm.next()) {
				instituicoesJson.put(new JSONObject(InstituicaoDAO.get(rsm.getInt("cd_instituicao")).toORM()));
			}
			return Response.ok(instituicoesJson.toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = InstituicaoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getAllAtivas")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllAtivas() {
		try {
			ResultSetMap rsm = InstituicaoServices.getAllAtivas();
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
			ResultSetMap rsm = InstituicaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findativas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAtivas(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = InstituicaoServices.findAtivas(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallperiodosletivos")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllPeriodosLetivos(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = InstituicaoServices.getAllPeriodosLetivos(cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallmatriculasativas")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllMatriculasAtivas(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdTurma = 0;
			if(restData.getArg("cdTurma") != null)
				cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdCurso = 0;
			if(restData.getArg("cdCurso") != null)
				cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			
			String nmAluno = "";
			if(restData.getArg("nmAluno") != null)
				nmAluno = String.valueOf(restData.getArg("nmAluno"));
			
			boolean buscarSuperior = Boolean.parseBoolean(String.valueOf(restData.getArg("buscarSuperior")));
			boolean buscarSimplificada = Boolean.parseBoolean(String.valueOf(restData.getArg("buscarSimplificada")));
			boolean buscarAtiva = Boolean.parseBoolean(String.valueOf(restData.getArg("buscarAtiva")));
			
			ResultSetMap rsm = InstituicaoServices.getAllMatriculasAtivas(cdInstituicao, cdPeriodoLetivo, cdTurma, cdCurso, buscarSuperior, buscarSimplificada, buscarAtiva, nmAluno);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getLocalizacao")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getLocalizacao(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = InstituicaoServices.getLocalizacao(cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getresumo")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getResumo(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			Result result = InstituicaoServices.getResumo(cdInstituicao);
			return new JSONObject(result).toString();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getresumorede")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getResumoRede() {
		try {
			
			Result result = InstituicaoServices.getResumoRede();
			return new JSONObject(result).toString();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbycirculo")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByCirculo(RestData restData) {
		try {
			int cdCirculo = Integer.parseInt(String.valueOf(restData.getArg("cdCirculo")));
			ResultSetMap rsm = InstituicaoServices.getAllByCirculo(cdCirculo);
			return Util.rsmToJSON(rsm);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallturmasbyinstituicaoperiodosimplificado")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllTurmasByInstituicaoPeriodoSimplificado(RestData restData) {
		try {
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = 0;
			ResultSetMap rsm = InstituicaoServices.getAllTurmasByInstituicaoPeriodoSimplificado(cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@GET
	@Path("/{cdInstituicao}/periodoatual")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getPeriodoAtual(@PathParam("cdInstituicao") int cdInstituicao) {
		try {
			InstituicaoPeriodo instituicaoPeriodo = new InstituicaoPeriodo();
			ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao);
			if(rsm.next()) {
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"));
			}
			rsm.beforeFirst();
			
			return Response.ok(new JSONObject(instituicaoPeriodo.toORM())).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("/{cdInstituicao}/periodorecente")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getPeriodoRecente(@PathParam("cdInstituicao") int cdInstituicao) {
		try {
			InstituicaoPeriodo instituicaoPeriodo = new InstituicaoPeriodo();
			ResultSetMap rsm = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao);
			if(rsm.next()) {
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"));
			}
			rsm.beforeFirst();
			
			return Response.ok(new JSONObject(instituicaoPeriodo.toORM())).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	

}
