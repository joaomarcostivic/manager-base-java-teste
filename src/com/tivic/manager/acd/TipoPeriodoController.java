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

import com.tivic.manager.acd.TipoPeriodo;
import com.tivic.manager.acd.TipoPeriodoDTO;
import com.tivic.manager.acd.TipoPeriodoServices;
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
@Api(value = "TipoPeriodo", tags = {"TipoPeriodo"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/tipoperiodo")
@Produces(MediaType.APPLICATION_JSON)
public class TipoPeriodoController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova TipoPeriodo "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoPeriodo registrada", response = TipoPeriodo.class),
			@ApiResponse(code = 400, message = "TipoPeriodo possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TipoPeriodo a ser registrado", required = true) TipoPeriodo tipoPeriodo) {
		try {	
			tipoPeriodo.setCdTipoPeriodo(0);
			
			Result r = TipoPeriodoServices.save(tipoPeriodo, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("TipoPeriodo possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoPeriodo)r.getObjects().get("TIPOPERIODO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma TipoPeriodo",
			notes = "Considere id = cdTipoPeriodo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoPeriodo atualizada", response = TipoPeriodo.class),
			@ApiResponse(code = 400, message = "TipoPeriodo � nulo ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da TipoPeriodo a ser atualizado", required = true) @PathParam("id") int cdTipoPeriodo, 
			@ApiParam(value = "TipoPeriodo a ser atualizado", required = true) TipoPeriodo tipoPeriodo) {
		try {
			if(tipoPeriodo.getCdTipoPeriodo() == 0) 
				return ResponseFactory.badRequest("TipoPeriodo � nulo ou inv�lido");
			
			tipoPeriodo.setCdTipoPeriodo(cdTipoPeriodo);
			Result r = TipoPeriodoServices.save(tipoPeriodo);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("TipoPeriodo � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((TipoPeriodo)r.getObjects().get("TIPOPERIODO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna TipoPeriodo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoPeriodo encontrado", response = TipoPeriodo[].class),
			@ApiResponse(code = 204, message = "TipoPeriodo n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id do  tipoPeriodo") @QueryParam("id") String idTipoPeriodo,
			@ApiParam(value = "Nome do tipoPeriodo") @QueryParam("tipoperiodo") String nmTipoPeriodo
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idTipoPeriodo != null) {
				criterios.add("id_tipo_periodo", idTipoPeriodo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmTipoPeriodo != null) {
				criterios.add("nm_tipo_periodo", nmTipoPeriodo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = TipoPeriodoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum periodo encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/tipoperiodo")
	@ApiOperation(
		value = "Fornece um TipoPeriodo dado o id indicado",
		notes = "Considere id = cdTipoPeriodo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AtividadeComplementar encontrado", response = TipoPeriodoDTO.class),
		@ApiResponse(code = 204, message = "N�o existe AtividadeComplementar com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da TipoPeriodo") @PathParam("id") int cdTipoPeriodo) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoPeriodoServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_tipo_periodo" : "cd_tipo_periodo"), Integer.toString(cdTipoPeriodo), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe TipoPeriodo com o id indicado.");
			
			return ResponseFactory.ok(new TipoPeriodoDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
