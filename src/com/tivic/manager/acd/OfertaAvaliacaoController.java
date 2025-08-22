package com.tivic.manager.acd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.ConfManager;
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

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import com.tivic.manager.acd.boletim.Boletim;
import com.tivic.manager.acd.boletim.BoletimServices;
import com.tivic.sol.report.ReportServices;
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

@Api(value = "OfertaAvaliação", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/ofertaavaliacao/")
@Produces(MediaType.APPLICATION_JSON)
public class OfertaAvaliacaoController {


	@GET
	@Path("/{cdOfertaAvaliacao}/{cdOferta}")
	@ApiOperation(
			value = "Busca uma oferta avaliação"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado da Oferta Avaliação"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Oferta Avaliação não encontrada"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(
		@ApiParam(value = "Código da oferta avaliação") @PathParam("cdOfertaAvaliacao") int cdOfertaAvaliacao, 
		@ApiParam(value = "Código da oferta") @PathParam("cdOferta") int cdOferta
	){
		try {
			if(cdOfertaAvaliacao <= 0) 
				return ResponseFactory.badRequest("Código da oferta avaliação é nulo ou inválido");
			
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta);
			if(ofertaAvaliacao == null){
				return ResponseFactory.noContent("Oferta Avaliação não encontrado");
			}
			
			return ResponseFactory.ok(ofertaAvaliacao);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}

	@GET
	@Path("/")
	@ApiOperation(
			value = "Busca uma oferta avaliação"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado da Oferta Avaliação"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getBy(
		@ApiParam(value = "Código da oferta") @PathParam("cdOferta") int cdOferta,
		@ApiParam(value = "Código da unidade") @PathParam("cdUnidade") int cdUnidade
	){
		try {
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta avaliação é nulo ou inválido");
			
			if(cdUnidade <= 0) 
				return ResponseFactory.badRequest("Código da unidade é nulo ou inválido");
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByOferta(cdOferta, cdUnidade);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhum registro encontrado");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Cadastra uma oferta avaliação"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Oferta Avaliação cadastrada"),
		@ApiResponse(code = 400, message = "Oferta Avaliação não pode ser salvo"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(OfertaAvaliacao ofertaAvaliacao){
		try {
			Result result = OfertaAvaliacaoServices.save(ofertaAvaliacao, 0);
			if(result.getCode() > 0)
				return ResponseFactory.ok(ofertaAvaliacao);
			else
				return ResponseFactory.badRequest(result.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/boletim")
	@ApiOperation(
			value = "Busca os alunos de uma turma"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado do boletim do aluno"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	public static Response getBoletim(
			@ApiParam(value = "Código do professor") @QueryParam("cdProfessor") int cdProfessor,
			@ApiParam(value = "Código da turma") @QueryParam("cdTurma") int cdTurma,
			@ApiParam(value = "Código da matrícula do aluno", required = true) @QueryParam("cdMatricula") int cdMatricula
		) {
		try {
			
			if(cdProfessor <= 0) 
				return ResponseFactory.badRequest("Código do professor é nulo ou inválido");
			
			if(cdTurma <= 0) 
				return ResponseFactory.badRequest("Código da turma é nulo ou inválido");
			
			if(cdMatricula <= 0) 
				return ResponseFactory.badRequest("Código da matrícula do aluno é nulo ou inválido");
			
			BoletimServices boletimServices = new BoletimServices();
			
			Boletim boletim = boletimServices.loadBoletim(cdProfessor, cdTurma, cdMatricula);
			
			if(boletim == null){
				return ResponseFactory.noContent("Boletim não encontrado");
			}
			
			return ResponseFactory.ok(boletim);
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	

	@DELETE
	@Path("/{cdOfertaAvaliacao}/{cdOferta}")
	@ApiOperation(
			value = "Remove uma oferta avaliação"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Mensagem de confirmação da remoção"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("cdOfertaAvaliacao") int cdOfertaAvaliacao, @PathParam("cdOferta") int cdOferta){
		try {
			if(cdOfertaAvaliacao <= 0) 
				return ResponseFactory.badRequest("Código da oferta avaliação é nulo ou inválido");
			
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta);
			if(ofertaAvaliacao == null){
				return ResponseFactory.noContent("Oferta Avaliação não encontrado");
			}
			
			Result result = OfertaAvaliacaoServices.remove(cdOfertaAvaliacao, cdOferta, true);
			if(result.getCode() > 0){
				return ResponseFactory.ok("Remoção de oferta avaliação realizada com sucesso");
			}
			
			return ResponseFactory.badRequest(result.getMessage());
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}

	
	
	
//  ANTIGOS

	@PUT
	@Path("/notas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveNotas(RestData restData){
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			OfertaAvaliacao ofertaAvaliacao = objectMapper.convertValue(restData.getArg("ofertaAvaliacao"), OfertaAvaliacao.class);
			ArrayList registerDisciplinaAvaliacaoAluno = objectMapper.convertValue(restData.getArg("disciplinasAvaliacaoAluno"), ArrayList.class);
			ArrayList<DisciplinaAvaliacaoAluno> disciplinasAvaliacaoAluno = new ArrayList<DisciplinaAvaliacaoAluno>();
			for(Object item : registerDisciplinaAvaliacaoAluno){
				disciplinasAvaliacaoAluno.add(objectMapper.convertValue(item, DisciplinaAvaliacaoAluno.class));
			}
			
			Result result = OfertaAvaliacaoServices.save(ofertaAvaliacao, null, disciplinasAvaliacaoAluno, 0);
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
			ResultSetMap rsm = OfertaAvaliacaoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyofertaunidade")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOfertaUnidade(RestData restData) {
		try {
			
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta"))); 
			int cdUnidade = Integer.parseInt(String.valueOf(restData.getArg("cdUnidade")));
			int cdMatriculaDisciplina = (restData.getArg("cdMatriculaDisciplina") != null ? Integer.parseInt(String.valueOf(restData.getArg("cdMatriculaDisciplina"))) : 0);
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByOfertaUnidade(cdOferta, cdUnidade, cdMatriculaDisciplina);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/getallbyofertaunidade/{cdOferta}/{cdUnidade}/{cdMatriculaDisciplina}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOfertaUnidadeAluno(@PathParam("cdOferta") int cdOferta, @PathParam("cdUnidade") int cdUnidade,@PathParam("cdMatriculaDisciplina") int cdMatriculaDisciplina) {
		try {
		
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByOfertaUnidade(cdOferta, cdUnidade, cdMatriculaDisciplina);
			return new JSONObject(rsm).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	@POST
	@Path("/getallbyprofessor")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByProfessor(RestData restData) {
		try {
			
			int cdProfessor = Integer.parseInt(String.valueOf(restData.getArg("cdProfessor")));
			int cdInstituicao = Integer.parseInt(String.valueOf(restData.getArg("cdInstituicao")));
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByProfessor(cdProfessor, cdInstituicao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyaluno")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByAluno(RestData restData) {
		try {
			
			int cdAluno = Integer.parseInt(String.valueOf(restData.getArg("cdAluno")));
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByAluno(cdAluno);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/getallbyaluno/{cdAluno}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByAluno(@PathParam("cdAluno") int cdAluno) {
		try {
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByAluno(cdAluno,null);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/boletim/{cdAluno}/{cdProfessor}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getBoletimByAluno(@PathParam("cdAluno") int cdAluno, @PathParam("cdProfessor") int cdProfessor) {
		try {
			
			Result result = OfertaAvaliacaoServices.getBoletimByAluno(cdAluno, cdProfessor, null);
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
			ResultSetMap rsm = OfertaAvaliacaoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	
	@GET
	@Path("/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOferta(@PathParam("cdOferta") int cdOferta, @QueryParam("lgAtividade") int lgAtividade, @QueryParam("cdUnidade") int cdUnidade){
		try {
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllByOferta(cdOferta, cdUnidade);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/getnotas/{cdTurma}/{cdDisciplina}/{cdOferta}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getNotas(@PathParam("cdTurma") int cdTurma, @PathParam("cdDisciplina") int cdDisciplina, @PathParam("cdOferta") int cdOferta){
		try {
			
			ResultSetMap rsmAlunos = OfertaAvaliacaoServices.getConceitos(cdTurma, cdDisciplina, cdOferta);
			return Util.rsmToJSON(rsmAlunos);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallocorrenciasbyofertaavaliacao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllOcorrenciasByOfertaAvaliacao(RestData restData){
		try {
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao = Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getAllOcorrenciasByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/aplicar")
	@Produces(MediaType.APPLICATION_JSON)
	public static String aplicar(RestData restData) {
		try {
			
			int cdOferta= Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao= Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			
			Result result = OfertaAvaliacaoServices.aplicar(cdOfertaAvaliacao, cdOferta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/fecharavaliacao")
	@Produces(MediaType.APPLICATION_JSON)
	public static String fecharAvaliacao(RestData restData) {
		try {
			
			int cdOferta= Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao= Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			
			Result result = OfertaAvaliacaoServices.fecharAvaliacao(cdOfertaAvaliacao, cdOferta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/abriravaliacao")
	@Produces(MediaType.APPLICATION_JSON)
	public static String abrirAvaliacao(RestData restData) {
		try {
			
			int cdOferta= Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao= Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			Result result = OfertaAvaliacaoServices.abrirAvaliacao(cdOfertaAvaliacao, cdOferta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/cancelar")
	@Produces(MediaType.APPLICATION_JSON)
	public static String cancelar(RestData restData) {
		try {
			
			int cdOferta= Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			int cdOfertaAvaliacao= Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			
			Result result = OfertaAvaliacaoServices.cancelar(cdOfertaAvaliacao, cdOferta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/impressaoatividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public static String impressaoAtividade(RestData restData) {
		try {
			
			ConfManager conf = Util.getConfManager();
			String reportPath = conf.getProps().getProperty("REPORT_PATH");
			
			int cdOfertaAvaliacao = Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			OfertaAvaliacao ofertaAvaliacao = OfertaAvaliacaoDAO.get(cdOfertaAvaliacao, cdOferta);
			CursoUnidade cursoUnidade = CursoUnidadeDAO.get(ofertaAvaliacao.getCdUnidade(), ofertaAvaliacao.getCdCurso());
			Oferta oferta = OfertaDAO.get(cdOferta);
			Disciplina disciplina = DisciplinaDAO.get(oferta.getCdDisciplina());
			Turma turma = TurmaDAO.get(oferta.getCdTurma());
			Curso curso = CursoDAO.get(turma.getCdCurso());
			Professor professor = ProfessorDAO.get(oferta.getCdProfessor());
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = null;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao());
			if(rsmPeriodoAtual.next()){
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"));
			}
			
			ResultSetMap rsm = OfertaAvaliacaoServices.getImpressaoAtividade(cdOfertaAvaliacao, cdOferta);
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			params.put("NM_UNIDADE", cursoUnidade.getNmUnidade());
			params.put("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
			params.put("CL_TURMA", curso.getNmProdutoServico() + " - " + turma.getNmTurma());
			params.put("CL_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
			params.put("NM_DISCIPLINA", disciplina.getNmProdutoServico());
			params.put("NM_PROFESSOR", professor.getNmPessoa());
			params.put("SUBREPORT_DIR", ContextManager.getRealPath() + reportPath);
			
			JasperPrint print = ReportServices.getJasperPrint(null, "acd/atividade", params, rsm, null);
			
			File directory = new File(ContextManager.getRealPath() + "/atividades");
			if (!directory.exists()){
				directory.mkdir();
            }
			
			File pdf = new File(ContextManager.getRealPath() + "/atividades/atividade"+cdOfertaAvaliacao+cdOferta+".pdf");
			FileOutputStream pdfOutPut = new FileOutputStream(pdf);
			JasperExportManager.exportReportToPdfStream(print, pdfOutPut);
			pdfOutPut.close();
			//Result result = new Result(1, "", "caminhoPdf", "http://edf.pmvc.ba.gov.br//edf//requerimento//requerimento"+cdMatricula+".pdf");
			Result result = new Result(1, "");
			result.addObject("caminhoPdf", "//edf//atividades//atividade"+cdOfertaAvaliacao+cdOferta+".pdf");
			result.addObject("rsm", rsm);
			//Result result = new Result(1, "", "caminhoPdf", "http://localhost:8080//edf//requerimento//requerimento"+cdMatricula+".pdf");
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
}
