package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaDTO;
import com.tivic.manager.grl.PessoaJuridicaDTO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseFactory;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Aula", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/aula/")
@Produces(MediaType.APPLICATION_JSON)
public class AulaController {


	@GET
	@Path("/{cdAula}")
	@ApiOperation(
			value = "Busca uma aula"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Aula buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Aula não encontrada"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdAula") int cdAula) {
		try {
			if(cdAula <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			AulaDTO aulaDto = new AulaDTO.Builder(cdAula).build();
			if(aulaDto == null){
				return ResponseFactory.noContent("Nenhuma aula encontrada");
			}
			
			return ResponseFactory.ok(aulaDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	
	
	@DELETE
	@Path("/{cdAula}")
	@ApiOperation(
			value = "Remove uma aula"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resposta de aula removida com sucesso"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Aula não encontrada"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remove(
		@ApiParam(value = "Código da aula", required = true) @PathParam("cdAula") int cdAula
	){
		try {
			if(cdAula <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			
			if(AulaDAO.get(cdAula) == null){
				return ResponseFactory.noContent("Nenhuma aula encontrada");
			}
			
			Result result = AulaServices.remove(cdAula, true);
			if(result.getCode() <= 0){
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok("Aula removida com sucesso");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}

	@POST
	@Path("/")
	@ApiOperation(
			value = "Adiciona uma aula"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Aula adicionada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(AulaCreateDTO aulaCreateDto){
		try {
			Aula aula = aulaCreateDto.aula;
			ArrayList<AulaMatricula> aulasMatriculas = aulaCreateDto.aulasMatriculas;
			
			if(aula == null) 
				return ResponseFactory.badRequest("Aula é nula ou inválida");
			if(aulasMatriculas == null) 
				return ResponseFactory.badRequest("Aulas matrícula é nula ou inválida");
			
			Result result = AulaServices.save(aula, aulasMatriculas, null);
			if(result.getCode() <= 0){
				return ResponseFactory.badRequest(result.getMessage());
			}
			return ResponseFactory.ok(aulaCreateDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}

	
	
//  ANTIGOS	
	
	@POST
	@Path("/getAll")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAll() {
		try {
			ResultSetMap rsm = AulaServices.getAll();
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
			ResultSetMap rsm = AulaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getaulasbyalunoperiodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAulasByAlunoPeriodo(RestData restData) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int tpPeriodo = 0;//Mensal
			Date data = objectMapper.convertValue(restData.getArg("data"), Date.class);
			
			ResultSetMap rsm = AulaServices.getAulasByAlunoPeriodo(cdAluno, cdInstituicao, tpPeriodo, Util.convDateToCalendar(data));
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getaulasbyprofessorperiodo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAulasByProfessorPeriodo(RestData restData) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int tpPeriodo = 0;//Mensal
			Date data = objectMapper.convertValue(restData.getArg("data"), Date.class);
			
			ResultSetMap rsm = AulaServices.getAulasByProfessorPeriodo(cdProfessor, cdInstituicao, tpPeriodo, Util.convDateToCalendar(data));
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getaula")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAula(RestData restData) {
		try {
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdHorario = 0;
			if(restData.getArg("cdHorario") != null)
				cdHorario = Integer.parseInt(String.valueOf(restData.getArg("cdHorario")));
			
			GregorianCalendar dtAula = Util.convStringToCalendar3(String.valueOf(restData.getArg("dtAula")));
			Result result = AulaServices.getAula(cdProfessor, cdDisciplina, cdTurma, cdHorario, dtAula);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/oferta/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getByOferta(@PathParam("cdOferta") int cdOferta) {
		try {
			ResultSetMap rsm = AulaServices.getAllByOferta(cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getaulasbyoferta/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAulasByOferta(@PathParam("cdOferta") int cdOferta) {
		try {
			ResultSetMap rsm = AulaServices.getAllByOferta(cdOferta);
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getaulasbyturma/{cdTurma}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAulasByTurma(@PathParam("cdTurma") int cdTurma) {
		try {
			ResultSetMap rsm = AulaServices.getAllByTurma(cdTurma,null);
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	@POST
	@Path("/getallbyoferta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOferta(RestData restData) {
		try {
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			ResultSetMap rsm = AulaServices.getAllByOferta(cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/fechar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String fechar(RestData restData) {
		try {
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			
			Result result = AulaServices.fechar(cdAula);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/abrir")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String abrir(RestData restData) {
		try {
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			
			Result result = AulaServices.abrir(cdAula);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getofertaavaliacaobydata")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOfertaAvaliacaoByData(RestData restData) {
		
		Connection connect = Conexao.conectar();
		
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdCurso = Integer.parseInt(String.valueOf(restData.getArg("cdCurso")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			
			GregorianCalendar dtAula = Util.convStringToCalendar3(String.valueOf(restData.getArg("dtAula")));
			
			ResultSetMap rsm = AulaServices.getOfertaAvaliacaoByData(cdTurma, cdCurso, cdProfessor, cdDisciplina, dtAula);
			while( rsm.next() ){
				
				GregorianCalendar dtAvaliacao = rsm.getGregorianCalendar("dt_avaliacao");
				if(dtAvaliacao != null)
					rsm.setValueToField("DT_NM_AVALIACAO", (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.DAY_OF_MONTH))) > 9 ? dtAvaliacao.get(Calendar.DAY_OF_MONTH) : "0" + dtAvaliacao.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtAvaliacao.get(Calendar.MONTH))) + 1)) + "/" + dtAvaliacao.get(Calendar.YEAR));
				
				rsm.setValueToField("CL_ST_AVALIACAO", OfertaAvaliacaoServices.situacaoOfertaAvaliacao[rsm.getInt("ST_AVALIACAO")]);
				
				rsm.setValueToField("CL_VL_PESO", Util.formatNumber(rsm.getDouble("vl_peso"), 2));
				
				
				float vlTotal = 0;
				int quantNotas = 0;
				ResultSetMap rsmDisciplinaAvaliacaoAluno = new ResultSetMap(connect.prepareStatement("SELECT * " +
																							 "			FROM acd_disciplina_avaliacao_aluno  " +
																							 "		  WHERE cd_oferta = " + rsm.getInt("cd_oferta") + 
																							 "			AND cd_oferta_avaliacao = " + rsm.getInt("cd_oferta_avaliacao")).executeQuery());
				while(rsmDisciplinaAvaliacaoAluno.next()){
					vlTotal += rsmDisciplinaAvaliacaoAluno.getFloat("VL_CONCEITO");
					quantNotas++;
				}
				rsmDisciplinaAvaliacaoAluno.beforeFirst();
				
				rsm.setValueToField("CL_VL_MEDIA_TURMA", Util.formatNumber((vlTotal/(quantNotas == 0 ? 1 : quantNotas)), 2));
				
			}
			rsm.beforeFirst();
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	@POST
	@Path("/getaulasabertas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAulasAbertas(RestData restData) {
		try {
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdDisciplina = (restData.getArg("cdDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina"))) : 0);
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			
			ResultSetMap rsm = AulaServices.getAulasAbertas(cdTurma, cdProfessor, cdDisciplina);

			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/getaulascanceladas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAulasCanceladas(RestData restData) {
		try {
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdDisciplina = Integer.parseInt(String.valueOf(restData.getArg("cdDisciplina")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			boolean retirarRepostas = Boolean.parseBoolean(String.valueOf(restData.getArg("retirarRepostas")));
			
			ResultSetMap rsm = AulaServices.getAulasCanceladas(cdTurma, cdProfessor, cdDisciplina, retirarRepostas);

			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/cancelar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String cancelar(RestData restData) {
		try {
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			String txtMotivoCancelamento = String.valueOf(restData.getArg("txtMotivoCancelamento"));
			
			Result result = AulaServices.cancelar(cdAula, txtMotivoCancelamento);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/copiarplanejamento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String copiarPlanejamento(RestData restData) {
		try {
			int cdAula = Integer.parseInt(String.valueOf(restData.getArg("cdAula")));
			
			Result result = AulaServices.copiarPlanejamento(cdAula);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getobservacoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getObservacoes(RestData restData) {
		try {
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			
			ResultSetMap rsm = AulaServices.getObservacoes(cdAluno, cdProfessor);

			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Path("/getfaltantes/{cdAula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getFaltantes(@PathParam("cdAula") int cdAula) {
		try {
			
			int qtFaltantes = 0;
			ResultSetMap rsm = AulaMatriculaServices.getAllByAula(cdAula);
			while(rsm.next()) {
				if(rsm.getInt("lg_presenca")==0) {
					qtFaltantes++;
				}
			}
			
			return new JSONObject("{\"qtFaltantes\": "+qtFaltantes+"}").toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	

	@GET
	@Path("/situacoes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getSituacoes() {
		try {
			String[] situacoes = AulaServices.situacoesAula;
			JSONArray situacoesJson = new JSONArray();
			for(int indice=0; situacoes.length > indice; indice++) {
				situacoesJson.put(new JSONObject("{\"stAula\": \""+indice+"\", nmSituacaoAula:\""+situacoes[indice]+"\"}").toString());
			}
			
			return Response.ok(situacoesJson.toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	


	
}
