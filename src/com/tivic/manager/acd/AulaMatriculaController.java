package com.tivic.manager.acd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.util.Result;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

import com.tivic.sol.report.ReportServices;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "AulaMatricula", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/aulamatricula/")
@Produces(MediaType.APPLICATION_JSON)
public class AulaMatriculaController {

	
	@GET
	@Path("/")
	@ApiOperation(
			value = "Busca as aulas matrícula a partir de uma aula"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das aulas matricula"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAll(
		@ApiParam(value = "Código da aula", required = true) @QueryParam("cdAula") int cdAula
	) {
		try {
			if(cdAula <= 0) 
				return ResponseFactory.badRequest("Código da aula é nulo ou inválido");
			
			ResultSetMap rsm = AulaMatriculaServices.getAllByAula(cdAula);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhuma aula matricula encontrada");
			}
			
			List<AulaMatriculaDTO> aulasMatriculas = new AulaMatriculaDTO.ListBuilder(rsm).setMatricula(rsm).build();
			return ResponseFactory.ok(aulasMatriculas);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/print/frequencia")
	@ApiOperation(
			value = "Busca o relatorio de frequencia de uma aula"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório de frequencia"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("application/pdf")
	public Response imprimir(@QueryParam("cdAula") int cdAula){
		try {	    	
			if(cdAula <= 0) 
				return ResponseFactory.badRequest("Código da aula é nulo ou inválido");
			
			Aula aula = AulaDAO.get(cdAula);
			if(aula == null){
				return ResponseFactory.noContent("Nenhuma aula encontrada");
			}
			
			Oferta oferta = OfertaDAO.get(aula.getCdOferta());
			Turma turma = TurmaDAO.get(oferta.getCdTurma());
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao());
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoServices.getPeriodoAtualOfSecretaria();
			Curso curso = CursoDAO.get(turma.getCdCurso());
			ResultSetMap rsmAulaMatricula = AulaMatriculaServices.getAllByAula(cdAula);
			
			HashMap<String, Object> paramns = new HashMap<String, Object>();
			paramns.put("NM_INSTITUICAO", instituicao.getNmPessoa());
			paramns.put("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo());
			paramns.put("NM_CURSO", curso.getNmProdutoServico());
			paramns.put("NM_TURMA", turma.getNmTurma());
			paramns.put("NM_TURNO", TurmaServices.tiposTurno[turma.getTpTurno()]);
			paramns.put("QT_VAGAS", turma.getQtVagas());
			paramns.put("DT_AULA", Util.convCalendarString3(aula.getDtAula()));
			paramns.put("QT_MATRICULADOS", rsmAulaMatricula.size());
							
			if(paramns.get("LOGO_1") != null)
				paramns.put("LOGO_1", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_1")));
			if(paramns.get("LOGO_2") != null)
				paramns.put("LOGO_2", ImagemServices.getArrayBytesFromArrayList((ArrayList<Byte>)paramns.get("LOGO_2")));
			
			byte[] print = ReportServices.getPdfReport("acd/frequencia", paramns, rsmAulaMatricula);			
									
			return ResponseFactory.ok(print);
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
	public String save(AulaMatricula aulaMatricula){
		try {
			Result result = AulaMatriculaServices.save(aulaMatricula);
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
	public String remove(AulaMatricula aulaMatricula){
		try {
			Result result = AulaMatriculaServices.remove(aulaMatricula.getCdAula(), aulaMatricula.getCdMatricula());
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
			ResultSetMap rsm = AulaMatriculaServices.getAll();
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
			ResultSetMap rsm = AulaMatriculaServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	
	
}
