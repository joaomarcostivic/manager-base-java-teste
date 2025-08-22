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

import com.tivic.manager.grl.LogradouroBairro;
import com.tivic.manager.grl.LogradouroBairroServices;
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

@Api(value = "Matricula", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/matricula")
@Produces(MediaType.APPLICATION_JSON)
public class MatriculaController {
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Instituicoes"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Instituicao encontrado", response = Matricula[].class),
			@ApiResponse(code = 204, message = "Instituicao n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("1550") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome da instituicao") @QueryParam("aluno") String nmPessoa,
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("1") @QueryParam("lgPeriodoAtual") int lgPeriodoAtual
		
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(nmPessoa != null) {
				criterios.add("D.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
				
			if(lgPeriodoAtual> 0) {
				criterios.add("lgPeriodoAtual", Integer.toString(lgPeriodoAtual), ItemComparator.EQUAL, Types.INTEGER);
			}	
			
			ResultSetMap rsm = MatriculaServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Instituicao encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{cdMatricula}/matricula")
	@ApiOperation(
		value = "Fornece uma Matricula dado o id indicado",
		notes = "Considere id = idLogradouro"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Matricula encontrado", response = LogradouroBairro[].class),
		@ApiResponse(code = 204, message = "N�o existe Matricula com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveByCdLogradouro(@ApiParam(value = "Codigo da Matricula") @PathParam("cdMatricula") int cdMatricula) {
		try {	
			
			Criterios crt = new Criterios();
			ResultSetMap rsm = MatriculaServices.find(crt.add((Util.isStrBaseAntiga() ? "A.cd_matricula" : "cd_matricula"), Integer.toString(cdMatricula), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe LogradouroBairro com o id indicado.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	
	
	
	
	
	@GET
	@Path("/{cdInstituicao}/reaproveitamentomatriculas")
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
	public static Response reaproveitamentoMatriculas(@ApiParam(value = "C�digo da turma", required = true) @PathParam("cdInstituicao") int cdInstituicao) {
		try {
			if(cdInstituicao <= 0) 
				return ResponseFactory.badRequest("C�digo da turma � nulo ou inv�lido");
			
			if(InstituicaoDAO.get(cdInstituicao) == null){
				return ResponseFactory.noContent("Instiuicao inexistente");
			}
			
			Result result = MatriculaServices.reaproveitamentoMatriculas(cdInstituicao);
			
			if(result.getCode() < 0){
				return ResponseFactory.noContent("N�o foi possivel fazer o reaproveitamento");
			}
			
			
			return ResponseFactory.ok(result);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	
	@DELETE
	@Path("{cdMatricula}/removeMatricula")
	@ApiOperation(
			value = "Apaga um Curso vinculado a uma matricula"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Matricula apagado"),
			@ApiResponse(code = 400, message = "Matricula possui algum par�metro inv�lido"),
			@ApiResponse(code = 204, message = "Matricula n�o encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response deleteCursoFromInstituicao(
			@ApiParam(value = "Cd da matricula", required = true) @PathParam("cdMatricula") int cdMatricula) {
		try {
			Result r = MatriculaServices.remove(cdMatricula);
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok("Curso from instituicao apagado");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
		
	}

}
