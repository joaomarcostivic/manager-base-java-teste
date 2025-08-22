package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

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

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.sol.report.ReportServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Turma", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/turma")
@Produces(MediaType.APPLICATION_JSON)
public class TurmaController {


	@GET
	@Path("/{cdTurma}")
	@ApiOperation(
			value = "Busca uma determinada turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Turma buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdTurma") int cdTurma) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			
			TurmaDTO turmaDto = new TurmaDTO.Builder(cdTurma).build();
			
			if(turmaDto == null){
				return ResponseFactory.noContent("Nenhuma turma encontrada");
			}
			
			return ResponseFactory.ok(turmaDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@GET
	@Path("/{cdTurma}/alunos")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAlunos(@ApiParam(value = "Código da turma", required = true) @PathParam("cdTurma") int cdTurma) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = TurmaServices.getAlunosOf(cdTurma);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma não está vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(new AulaMatriculaDTO.ListBuilder(rsm).setMatricula(rsm).build());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdTurma}/alunosturma")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAlunosOfTurma(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurma") int cdTurma) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = TurmaServices.getAlunosOf(cdTurma);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdTurma}/atividadecomplementar")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAtividadeComplementar(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurma") int cdTurma) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = TurmaServices.getAtividadeComplementarOf(cdTurma);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdTurma}/{cdAtividadeComplementar}/addatividadecomplementar")
	@ApiOperation(
			value = "Adiciona atividades complementares a turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response addAtividadeComplementar(
			@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurma") int cdTurma,
			@ApiParam(value = "C�digo da atividade complementar", required = true) @PathParam("cdAtividadeComplementar") int cdAtividadeComplementar
			) {
		try {
			if(cdTurma <= 0 && cdAtividadeComplementar <=0) 
				return ResponseFactory.badRequest("C�digo da turma ou codigo da atividade complementar � nulo ou inv�lido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			Result r = TurmaServices.addAtividadeComplementar(cdTurma, cdAtividadeComplementar);
			
			if(r.getCode() < 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhuma atividadecomplementar");
			}
			
			return ResponseFactory.ok(r);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdTurma}/disciplinas")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getDisciplinasTurma(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurma") int cdTurma) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			ResultSetMap rsm = TurmaServices.getOfertasByTurma(cdTurma);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdAluno}/{cdTurma}/{cdTurmaDestino}/{cdPeriodoLetivo}/{cdUsuario}/remanejaraluno")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado dos alunos da turma"),
		@ApiResponse(code = 400, message = "A busca teve algum par�metro inv�lido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response remanejarAluno(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdAluno") int cdAluno,
			@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurma") int cdTurma,
			@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdTurmaDestino") int cdTurmaDestino,
			@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdPeriodoLetivo") int cdPeriodoLetivo,
			@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdUsuario") int cdUsuario) {
		try {
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(TurmaDAO.get(cdTurma) == null){
				return ResponseFactory.noContent("Turma inexistente");
			}
			
			Result r = TurmaServices.remanejarAluno(cdAluno, cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario);
			
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok(r);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Turma"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Turma encontrado", response = Turma[].class),
			@ApiResponse(code = 204, message = "Turma n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("qtLimite") int qtLimite,
			@ApiParam(value = "Nome da turma") @QueryParam("turma") String nmTurma,
			@ApiParam(value = "Nome da turma") @QueryParam("turma") int stTurma
		) {
		try {
			
			Criterios criterios = new Criterios("qtLimite", Integer.toString(qtLimite), 0, 0);
			

			if(nmTurma != null) {
				criterios.add("A.nm_turma", nmTurma, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			if(stTurma > -1) {
				criterios.add("A.st_turma", Integer.toString(stTurma), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			ResultSetMap rsm = TurmaServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Instituicao encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@DELETE
	@Path("{cdTurma}/removeturma")
	@ApiOperation(
			value = "Apaga uma Turma"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Turma apagado"),
			@ApiResponse(code = 400, message = "Turma possui algum par�metro inv�lido"),
			@ApiResponse(code = 204, message = "Turma n�o encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response deleteTurma(
			@ApiParam(value = "Id da Turma", required = true) @PathParam("cdTurma") int cdTurma) {
		try {
			Result r = TurmaServices.remove(cdTurma);
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok("Turma foi apagada");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
		
	}
	
	
//  ANTIGOS	
	
	@PUT
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String save(Turma turma){
		try {
			Result result = TurmaServices.save(turma, null, null);
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
	public String remove(Turma turma){
		try {
			Result result = TurmaServices.remove(turma.getCdTurma());
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
			ResultSetMap rsm = TurmaServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/alunos/{cdTurma}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAlunosOf(@PathParam("cdTurma") int cdTurma) {
		try {
			ResultSetMap rsm = TurmaServices.getAlunosOf(cdTurma);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/alunosdisciplina/{cdTurma}/{cdDisciplina}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAlunosByTurmaDisciplina(@PathParam("cdTurma") int cdTurma, @PathParam("cdDisciplina") int cdDisciplina) {
		try {
			ResultSetMap rsm = TurmaServices.getAlunosByTurmaDisciplina(cdTurma, cdDisciplina, null);
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
			ResultSetMap rsm = TurmaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findallpossiveis")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAllPossiveis(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = TurmaServices.findAllPossiveis(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/findallpossiveisbycursos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String findAllPossiveisByCursos(ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = TurmaServices.findAllPossiveis(criterios);
			
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("RSM_TURMAS", rsm);
			register.put("NM_TITULO", "Turmas possíveis");
			rsmFinal.addRegister(register);
			
			return Util.rsmToJSON(rsmFinal);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallpossiveis")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllPossiveis(RestData restData) {
		try {
			
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCursoTurma = Integer.parseInt(String.valueOf(restData.getArg("cdCursoTurma")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdMatricula = Integer.parseInt(String.valueOf(restData.getArg("cdMatricula")));
			int incluirTransferencia = Integer.parseInt(String.valueOf(restData.getArg("incluirTransferencia")));
			
			ResultSetMap rsm = TurmaServices.getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, incluirTransferencia);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallpossiveisbymatriculas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllPossiveisByMatriculas(RestData restData) {
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			int cdCursoTurma = Integer.parseInt(String.valueOf(restData.getArg("cdCursoTurma")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			ArrayList<Integer> matriculas = objectMapper.convertValue(restData.getArg("matriculas"), ArrayList.class);
			 
			ResultSetMap rsm = TurmaServices.getAllPossiveisByMatriculas(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, matriculas);
			
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/remanejaraluno")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String remanejarAluno(RestData restData) {
		try {
			
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdTurmaDestino = Integer.parseInt(String.valueOf(restData.getArg("cdTurmaDestino")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			 
			Result result = TurmaServices.remanejarAluno(cdAluno, cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/trocaraluno")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String trocarAluno(RestData restData) {
		try {
			
			int cdAlunoTroca = Integer.parseInt(String.valueOf(restData.getArg("cdAlunoTroca")));
			int cdTurmaTroca = Integer.parseInt(String.valueOf(restData.getArg("cdTurmaTroca")));
			int cdAlunoTrocado = Integer.parseInt(String.valueOf(restData.getArg("cdAlunoTrocado")));
			int cdTurmaTrocado = Integer.parseInt(String.valueOf(restData.getArg("cdTurmaTrocado")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			 
			Result result = TurmaServices.trocarAluno(cdAlunoTroca, cdTurmaTroca, cdAlunoTrocado, cdTurmaTrocado, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@POST
	@Path("/remanejaralunos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String remanejarAlunos(RestData restData) {
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			ArrayList<Integer> matriculas = objectMapper.convertValue(restData.getArg("matriculas"), ArrayList.class);
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			int cdTurmaDestino = Integer.parseInt(String.valueOf(restData.getArg("cdTurmaDestino")));
			int cdPeriodoLetivo = Integer.parseInt(String.valueOf(restData.getArg("cdPeriodoLetivo")));
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			 
			Result result = TurmaServices.remanejarAlunos(matriculas, cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getlistaturma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getListaTurma(RestData restData) {
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			
			Turma turma = TurmaDAO.get(cdTurma);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo());
			Curso curso = CursoDAO.get(turma.getCdCurso());
			
			Result result = TurmaServices.getListaTurma(cdTurma);
			ResultSetMap rsm = (ResultSetMap)result.getObjects().get("rsm");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
			params.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			params.put("NM_CURSO", curso.getNmProdutoServico());
			params.put("NM_TURMA", turma.getNmTurma());
			params.put("NM_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
			params.put("QT_VAGAS", turma.getQtVagas());
			params.put("QT_MATRICULADOS", rsm.size());
			params.put("TXT_OFERTAS", result.getObjects().get("txtOfertas"));
			
			JasperPrint print = ReportServices.getJasperPrint(null, "acd/lista_alunos_com_oferta", params, rsm, null);
			
			File directory = new File(ContextManager.getRealPath() + "/turmas");
			if (!directory.exists()){
				directory.mkdir();
            }
			
			directory = new File(ContextManager.getRealPath() + "/turmas/"+instituicaoPeriodo.getNmPeriodoLetivo());
			if (!directory.exists()){
				directory.mkdir();
            }
			
			File pdf = new File(ContextManager.getRealPath() + "/turmas/"+instituicaoPeriodo.getNmPeriodoLetivo()+"/turma"+cdTurma+".pdf");
			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
			pdfOutPut.close();
			//Result result = new Result(1, "", "caminhoPdf", "http://edf.pmvc.ba.gov.br//edf//requerimento//requerimento"+cdMatricula+".pdf");
			result = new Result(1, "");
			result.addObject("caminhoPdf", "//edf//turmas//"+instituicaoPeriodo.getNmPeriodoLetivo()+"//turma"+cdTurma+".pdf");
			result.addObject("rsm", rsm);
			//Result result = new Result(1, "", "caminhoPdf", "http://localhost:8080//edf//requerimento//requerimento"+cdMatricula+".pdf");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getresumorendimentobyturmas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getResumoRendimentoByTurmas(RestData restData) {
		try {
			
			int cdCirculo = Integer.parseInt(String.valueOf(restData.getArg("cdCirculo")));
			int cdInstituicao = Integer.parseInt(String.valueOf((restData.getArg("cdInstituicao") == null ? "0" : restData.getArg("cdInstituicao"))));
			 
			Result result = TurmaServices.getResumoRendimentoByTurmas(cdCirculo, cdInstituicao);
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getresumorendimentobydisciplinacursos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getResumoRendimentoByDisciplinaCursos(RestData restData) {
		try {
			
			int cdCirculo = Integer.parseInt(String.valueOf(restData.getArg("cdCirculo")));
			int cdInstituicao = Integer.parseInt(String.valueOf((restData.getArg("cdInstituicao") == null ? "0" : restData.getArg("cdInstituicao"))));
			 
			Result result = TurmaServices.getResumoRendimentoByDisciplinaCursos(cdCirculo, cdInstituicao);
			
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@POST
	@Path("/getofertasbyturma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getOfertasByTurma(RestData restData) {
		try {
			
			int cdTurma = Integer.parseInt(String.valueOf(restData.getArg("cdTurma")));
			 
			ResultSetMap rsm = TurmaServices.getOfertasByTurma(cdTurma);

			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/tiposturno")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getTiposTurno() {
		try {
			String[] tiposTurno = TurmaServices.tiposTurno;
			JSONArray turnosJson = new JSONArray();
			for(int indice=0; tiposTurno.length > indice; indice++) {
				turnosJson.put(new JSONObject("{\"tpTurno\": \""+indice+"\", nmTurno:\""+tiposTurno[indice]+"\"}").toString());
			}
			
			return Response.ok(turnosJson.toString()).build();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Response.serverError().build();
		}
	}
	

}
