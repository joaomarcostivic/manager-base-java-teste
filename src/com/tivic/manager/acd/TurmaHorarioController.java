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

@Api(value = "TurmaHorario", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/turmahorario")
@Produces(MediaType.APPLICATION_JSON)
public class TurmaHorarioController {
	
	@GET
	@Path("/{cdTurma}/turmahorarios")
	@ApiOperation(
			value = "Buscar os horarios de uma turma"
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
			
			ResultSetMap rsm = TurmaHorarioServices.getAllByTurma(cdTurma);
			
			if(rsm.size() == 0){
				return ResponseFactory.noContent("Esta turma n�o est� vinculada a nenhum aluno");
			}
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}

}
