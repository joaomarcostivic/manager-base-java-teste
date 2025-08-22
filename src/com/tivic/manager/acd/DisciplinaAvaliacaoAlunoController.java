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

@Api(value = "DisciplinaAvaliacaoAluno", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/acd/disciplinaavaliacaoaluno/")
@Produces(MediaType.APPLICATION_JSON)
public class DisciplinaAvaliacaoAlunoController {

	

	@GET
	@Path("/")
	@ApiOperation(
			value = "Busca as disciplinas avaliacao aluno"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado das disciplinas avaliacao aluno"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getBy(
		@ApiParam(value = "Código da oferta avaliação", required = true) @QueryParam("cdOfertaAvaliacao") int cdOfertaAvaliacao, 
		@ApiParam(value = "Código da oferta", required = true) @QueryParam("cdOferta") int cdOferta
	) {
		try {
			if(cdOfertaAvaliacao <= 0) 
				return ResponseFactory.badRequest("Código da oferta avaliação é nulo ou inválido");
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.getAllByOfertaAvaliacao(cdOfertaAvaliacao, cdOferta);
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Nenhuma disciplina avaliação aluno encontrada");
			}
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdMatricula}/{cdOfertaAvaliacao}/{cdOferta}")
	@ApiOperation(
			value = "Busca as disciplinas avaliacao aluno"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Resultado da disciplina avaliacao aluno"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido ou faltando"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(
		@ApiParam(value = "Código da matrícula", required = true) @PathParam("cdMatricula") int cdMatricula, 
		@ApiParam(value = "Código da oferta avaliação", required = true) @PathParam("cdOfertaAvaliacao") int cdOfertaAvaliacao, 
		@ApiParam(value = "Código da oferta", required = true) @PathParam("cdOferta") int cdOferta
	) {
		try {
			if(cdMatricula <= 0) 
				return ResponseFactory.badRequest("Código da matrícula é nulo ou inválido");
			if(cdOfertaAvaliacao <= 0) 
				return ResponseFactory.badRequest("Código da oferta avaliação é nulo ou inválido");
			if(cdOferta <= 0) 
				return ResponseFactory.badRequest("Código da oferta é nulo ou inválido");
			
			
			DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = DisciplinaAvaliacaoAlunoDAO.get(cdMatricula, cdOfertaAvaliacao, cdOferta);
			if(disciplinaAvaliacaoAluno == null){
				return ResponseFactory.noContent("Nenhuma disciplina avaliação aluno encontrada");
			}
			return ResponseFactory.ok(disciplinaAvaliacaoAluno);
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
	public String save(RestData restData){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno = objectMapper.convertValue(restData.getArg("disciplinaAvaliacaoAluno"), DisciplinaAvaliacaoAluno.class);
			DisciplinaAvaliacaoAluno disciplinaAvaliacaoAlunoAntiga = objectMapper.convertValue(restData.getArg("disciplinaAvaliacaoAlunoAntiga"), DisciplinaAvaliacaoAluno.class);
			int cdUsuario = Integer.parseInt(String.valueOf(restData.getArg("cdUsuario")));
			
			Result result = DisciplinaAvaliacaoAlunoServices.save(disciplinaAvaliacaoAluno, disciplinaAvaliacaoAlunoAntiga, cdUsuario);
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
	public String remove(DisciplinaAvaliacaoAluno disciplinaAvaliacaoAluno){
		try {
			Result result = DisciplinaAvaliacaoAlunoServices.remove(disciplinaAvaliacaoAluno.getCdMatricula(), disciplinaAvaliacaoAluno.getCdOfertaAvaliacao(), disciplinaAvaliacaoAluno.getCdOferta());
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{cdOfertaAvaliacao}/{cdOferta}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String getAllByOfertaAvaliacao(@PathParam("cdOfertaAvaliacao") int cdOfertaAvaliacao, @PathParam("cdOferta") int cdOferta) {
		try {
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.getAllByOfertaAvaliacao(cdOfertaAvaliacao, cdOferta);
			return new JSONObject(rsm).toString();
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
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.getAll();
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@POST
	@Path("/getallbyofertaavaliacao")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllByOfertaAvaliacao(RestData restData){
		try {
			
			int cdOfertaAvaliacao = Integer.parseInt(String.valueOf(restData.getArg("cdOfertaAvaliacao")));
			int cdOferta = Integer.parseInt(String.valueOf(restData.getArg("cdOferta")));
			
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.getAllByOfertaAvaliacao(cdOfertaAvaliacao, cdOferta);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/getallbymatricula/{cdMatricula}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllByMatricula(@PathParam("cdMatricula") int cdMatricula){
		try {
			
			
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.getAllByMatricula(cdMatricula, null);
			return new JSONObject(rsm).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	

	@GET
	@Path("/getallbymatriculaunidadedisciplina/{cdOferta}/{cdUnidade}/{cdDisciplina}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllByMatriculaUnidadeDisciplina(@PathParam("cdOferta") int cdOferta,@PathParam("cdUnidade") int cdUnidade,@PathParam("cdDisciplina") int cdDisciplina ){
		try {
			
			
			ResultSetMap pstmt = DisciplinaAvaliacaoAlunoServices.getAllByMatriculaUnidadeDisciplinaAluno(cdOferta, cdUnidade, cdDisciplina, null);
			//return new JSONObject(pstmt).toString();
			return Util.rsmToJSON(pstmt);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	@GET
	@Path("/getmediabyturma/{cdMatricula}/{cdTurma}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getMediaByTurma(@PathParam("cdMatricula") int cdMatricula,@PathParam("cdTurma") int cdTurma){
		try {
			
			
			float rsm = DisciplinaAvaliacaoAlunoServices.getMediaNotaByTurma(cdMatricula,cdTurma,null);
			return new JSONObject(rsm).toString();
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
			ResultSetMap rsm = DisciplinaAvaliacaoAlunoServices.find(criterios);
			return Util.rsmToJSON(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

}
