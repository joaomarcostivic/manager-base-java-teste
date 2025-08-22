package com.tivic.manager.acd;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.acd.CursoEtapa;
import com.tivic.manager.acd.CursoEtapaDTO;
import com.tivic.manager.acd.CursoEtapaServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;
@Api(value = "CursoEtapa", tags = {"CursoEtapa"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/cursoetapa")
@Produces(MediaType.APPLICATION_JSON)
public class CursoEtapaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Etapa "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "CursoEtapa registrada", response = CursoEtapa.class),
			@ApiResponse(code = 400, message = "CursoEtapa possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "CursoEtapa a ser registrado", required = true) CursoEtapa cursoEtapa) {
		try {	
			cursoEtapa.setCdCursoEtapa(0);
			
			Result r = CursoEtapaServices.save(cursoEtapa, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("CursoEtapa possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((CursoEtapa)r.getObjects().get("CURSOETAPA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma CursoEtapa",
			notes = "Considere id = idEscolaridade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "CursoEtapa atualizada", response = CursoEtapa.class),
			@ApiResponse(code = 400, message = "CursoEtapa � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da CursoEtapa a ser atualizado", required = true) @PathParam("id") int cdCursoEtapa, 
			@ApiParam(value = "CursoEtapa a ser atualizado", required = true) CursoEtapa cursoEtapa) {
		try {
			if(cursoEtapa.getCdCursoEtapa() == 0) 
				return ResponseFactory.badRequest("CursoEtapa � nulo ou inv�lido");
			
			cursoEtapa.setCdCursoEtapa(cdCursoEtapa);
			Result r = CursoEtapaServices.save(cursoEtapa);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("CursoEtapa � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((CursoEtapa)r.getObjects().get("CURSOETAPA"));
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
			@ApiResponse(code = 200, message = "Etapa encontrado", response = CursoEtapa[].class),
			@ApiResponse(code = 204, message = "Etapa n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome do curso") @QueryParam("curso") String nmCurso
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			
			if(nmCurso != null) {
				criterios.add("nm_curso", nmCurso, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = CursoEtapaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum etapa encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/cursoetapa")
	@ApiOperation(
		value = "Fornece um CursoEtapa dado o id indicado",
		notes = "Considere id = cdCursoEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "CursoEtapa encontrado", response = CursoEtapa[].class),
		@ApiResponse(code = 204, message = "N�o existe CursoEtapa com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da Etapa") @PathParam("id") int cdEtapa) {
		try {	
			
			ResultSetMap rsm = CursoEtapaServices.getAllByEtapa(cdEtapa);
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe CursoEtapa com o id indicado.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
