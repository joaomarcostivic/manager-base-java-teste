package com.tivic.manager.acd;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

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

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.acd.boletim.Boletim;
import com.tivic.manager.acd.boletim.BoletimServices;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Oferta", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/oferta/")
@Produces(MediaType.APPLICATION_JSON)
public class OfertaController {


	@GET
	@Path("/{cdOferta}")
	@ApiOperation(
			value = "Busca uma oferta"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "A oferta buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdOferta") int cdOferta) {
		try {
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			
			OfertaDTO ofertaDto = new OfertaDTO.Builder(cdOferta).build();
			
			if(ofertaDto == null){
				return ResponseFactory.noContent("Nenhuma oferta encontrada");
			}
			
			return ResponseFactory.ok(ofertaDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/")
	@ApiOperation(
			value = "Busca as atividades desenvolvidas de uma oferta"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das ofertas"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getBy(
		@ApiParam(value = "Código da turma", required = true) @QueryParam("cdTurma") int cdTurma, 
		@ApiParam(value = "Código do professor") @QueryParam("cdProfessor") int cdProfessor, 
		@ApiParam(value = "Código da disciplina") @QueryParam("cdDisciplina") int cdDisciplina, 
		@ApiParam(value = "Código da situação da oferta") @QueryParam("stOferta") int stOferta
	) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			criterios.add(new ItemComparator("A.CD_TURMA", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
			
			if(cdProfessor > 0)
				criterios.add(new ItemComparator("cdProfessor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdDisciplina > 0)
				criterios.add(new ItemComparator("A.CD_DISCIPLINA", "" + cdDisciplina, ItemComparator.EQUAL, Types.INTEGER));
			if(stOferta == OfertaServices.ST_ATIVO || stOferta == OfertaServices.ST_INATIVO)
				criterios.add(new ItemComparator("A.ST_OFERTA", "" + stOferta, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = OfertaServices.find(criterios);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhuma oferta encontrada");
			}
			
			//:TODO Modificar para Factory quando tiver
			JSONArray array = new JSONArray();
			while(rsm.next()){
				array.put(new JSONObject(Oferta.fromRegister(rsm.getRegister())));
			}
			return ResponseFactory.ok(array);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/{cdOferta}/atividadesdesenvolvidas")
	@ApiOperation(
			value = "Busca as atividades desenvolvidas de uma oferta"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das atividades desenvolvidas"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAtividadeDesenvolvidas(
			@ApiParam(value = "Código da oferta", required = true) @PathParam("cdOferta") int cdOferta
		){
		try {
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			ResultSetMap rsm = AtividadeDesenvolvidaServices.getAllByOferta(cdOferta);	
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhuma atividade desenvolvida encontrada");
			}
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/{cdOferta}/aulas")
	@ApiOperation(
			value = "Busca as aulas de uma oferta"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das aulas"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAulas(
			@ApiParam(value = "Código da oferta", required = true) @PathParam("cdOferta") int cdOferta
		) {
		try {
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			ResultSetMap rsm = AulaServices.getAllByOferta(cdOferta);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhuma aula encontrada");
			}
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/disciplinas")
	@ApiOperation(
			value = "Busca as disciplinas de um professor/turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das disciplinas"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getDisciplinas(
			@ApiParam(value = "Código do professor", required = true) @QueryParam("cdProfessor") int cdProfessor, 
			@ApiParam(value = "Código da turma", required = true) @QueryParam("cdTurma") int cdTurma
		) {
		try {
			if(cdProfessor <= 0) 
				return ResponseFactory.badRequest("Código do professor é nulo ou inválido");
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			ResultSetMap rsm = OfertaServices.getDisciplinasByProfessorPeriodo(cdProfessor, cdTurma);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Este professor não está vinculado a nenhuma turma");
			}
			
			//:TODO Modificar para Factory quando tiver
			JSONArray array = new JSONArray();
			while(rsm.next()){
				array.put(new JSONObject(Disciplina.fromRegister(rsm.getRegister())));
			}
			return ResponseFactory.ok(array);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	
	
	
//	ANTIGOS
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Oferta oferta){
		try {
			Result result = OfertaServices.save(oferta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@PUT
	@Path("/saveofertas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveOfertas(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Oferta oferta = objectMapper.convertValue(restData.getArg("oferta"), Oferta.class);
			ArrayList<Integer> codigosDisciplina = objectMapper.convertValue(restData.getArg("codigosDisciplina"), ArrayList.class);
			
			ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
			
			for(int codDisciplina : codigosDisciplina){
				Oferta ofertaCopy = (Oferta)oferta.clone();
				ofertaCopy.setCdDisciplina(codDisciplina);
				ofertas.add(ofertaCopy);
			}
			
			Result result = OfertaServices.save(ofertas, true);
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
	public String remove(Oferta oferta){
		try {
			Result result = OfertaServices.remove(oferta.getCdOferta());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	
	@POST
	@Path("/inativar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String inativar(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Oferta oferta = objectMapper.convertValue(restData.getArg("oferta"), Oferta.class);
			int cdPessoa = Integer.parseInt(String.valueOf(restData.getArg("cdPessoa")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = OfertaServices.inativar(oferta.getCdOferta(), cdPessoa, cdUsuario);
			return new JSONObject(result).toString();
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
			ResultSetMap rsm = OfertaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/findperiodoatual")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findPeriodoAtual(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = OfertaServices.findPeriodoAtual(cdProfessor, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/disciplinasaluno/{cdTurma}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDisciplinasAluno(@PathParam("cdTurma") int cdTurma) {
		try {
			ResultSetMap rsm = OfertaServices.getAllByTurma(cdTurma);
			return new JSONObject(rsm).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getdisciplinasbyprofessorperiodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getDisciplinasByProfessorPeriodo(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			
			ResultSetMap rsm = OfertaServices.getDisciplinasByProfessorPeriodo(cdProfessor, cdTurma);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findcursosturmasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findCursosTurmasByProfessor(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			
			ResultSetMap rsm = OfertaServices.findCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/findturmasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findTurmasByProfessor(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			
			ResultSetMap rsm = OfertaServices.findTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findturmasdisciplinasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findTurmasDisciplinasByProfessor(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			
			ResultSetMap rsm = OfertaServices.findTurmasDisciplinasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/finddisciplinascursosturmasbyprofessor")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findDisciplinasCursosTurmasByProfessor(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			
			ResultSetMap rsm = OfertaServices.findDisciplinasCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/finddisciplinascursosturmasbyaluno")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findDisciplinasCursosTurmasByAluno(RestData restData) {
		try {
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			
			ResultSetMap rsm = OfertaServices.findDisciplinasCursosTurmasByAluno(cdAluno, cdInstituicao, cdPeriodoLetivo);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/finddisciplinascursosturmasbyaluno/{cdAluno}/{cdInstituicao}/{cdPeriodoLetivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAlunoOfertaMobile(@PathParam("cdAluno") int cdAluno, @PathParam("cdInstituicao") int cdInstituicao, @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo) {
		try {
			
			ResultSetMap rsm = OfertaServices.findDisciplinasCursosTurmasByProfessor(cdAluno, cdInstituicao, cdPeriodoLetivo, null);
			 System.out.println(rsm);
			 return new JSONObject(rsm).toString();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	

	@GET
	@Path("/finddisciplinascursosturmasbyaprofessor/{cdProfessor}/{cdInstituicao}/{cdPeriodoLetivo}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String finddisciplinascursosturmasbyaprofessor(@PathParam("cdProfessor") int cdProfessor, @PathParam("cdInstituicao") int cdInstituicao, @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo) {
		try {
			
			ResultSetMap rsmDisciplinas = OfertaServices.findDisciplinasCursosTurmasByProfessor(cdProfessor, cdInstituicao, cdPeriodoLetivo, null);
			 System.out.println(rsmDisciplinas);
			 return Util.rsmToJSON(rsmDisciplinas);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/getallbyaluno/{cdPessoa}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByAluno(@PathParam("cdPessoa") int cdPessoa) {
		try {
			
			ResultSetMap rsm = OfertaServices.getAllByPessoa(cdPessoa);
			 System.out.println(rsm);
			 return new JSONObject(rsm).toString();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbypessoa")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByPessoa(RestData restData) {
		try {
			int cdPessoa = Integer.parseInt(String.valueOf(restData.getArg("cdPessoa")));
			
			ResultSetMap rsm = OfertaServices.getAllByPessoa(cdPessoa);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyturma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByTurma(RestData restData) {
		try {
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			
			ResultSetMap rsm = OfertaServices.getAllByTurma(cdTurma);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/getalunosbyoferta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAlunosByOferta(RestData restData) {
		try {
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			ResultSetMap rsm = OfertaServices.getAlunosByOferta(cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getatividadesbyoferta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAtividadesByOferta(RestData restData) {
		try {
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			ResultSetMap rsm = OfertaServices.getAtividadesByOferta(cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getallbyinstituicao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAllByInstituicao(@QueryParam("cdInstituicao") int cdInstituicao) {
		try {
			ArrayList<Oferta> ofertas = new ArrayList<Oferta>();
			ResultSetMap rsm = OfertaServices.getAllByInstituicao(cdInstituicao);
			while(rsm.next()) {
				ofertas.add(OfertaDAO.get(rsm.getInt("cd_oferta")));
			}
			return Response.ok(new JSONArray(ofertas).toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("/avaliacoes/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAvaliacoes(@PathParam("cdOferta") int cdOferta) {
		try {
			ArrayList<OfertaAvaliacao> avaliacoes = new ArrayList<OfertaAvaliacao>();
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByOferta(cdOferta, 0);
			while(rsm.next()) {
				avaliacoes.add(OfertaAvaliacaoDAO.get(rsm.getInt("cd_oferta_avaliacao"), rsm.getInt("cd_oferta")));
			}
			return Response.ok(new JSONArray(avaliacoes).toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	
	@GET
	@Path("/atividadesdesenvolvidas/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAtividadesDesenvolvidas(@PathParam("cdOferta") int cdOferta) {
		try {
			ArrayList<AtividadeDesenvolvida> atividadesDesenvolvidas = new ArrayList<AtividadeDesenvolvida>();
			ResultSetMap rsm = AtividadeDesenvolvidaServices.getAllByOferta(cdOferta);
			while(rsm.next()) {
				atividadesDesenvolvidas.add(AtividadeDesenvolvidaDAO.get(rsm.getInt("cd_atividade_desenvolvida")));
			}
			return Response.ok(new JSONArray(atividadesDesenvolvidas).toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
}
