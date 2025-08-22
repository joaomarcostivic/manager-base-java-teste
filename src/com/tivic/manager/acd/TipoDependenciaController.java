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

import com.tivic.manager.acd.TipoDependencia;
import com.tivic.manager.acd.TipoDependenciaDTO;
import com.tivic.manager.acd.TipoDependenciaServices;
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
@Api(value = "TipoDependencia", tags = {"TipoDependencia"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/tipodependencia")
@Produces(MediaType.APPLICATION_JSON)
public class TipoDependenciaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova TipoDependencia "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoDependencia registrada", response = TipoDependencia.class),
			@ApiResponse(code = 400, message = "TipoDependencia possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TipoDependencia a ser registrado", required = true) TipoDependencia tipoDependencia) {
		try {	
			tipoDependencia.setCdTipoDependencia(0);
			
			Result r = TipoDependenciaServices.save(tipoDependencia, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("ETAPA possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoDependencia)r.getObjects().get("TIPODEPENDENCIA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma TipoDependencia",
			notes = "Considere id = idTipoDependencia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoDependencia atualizada", response = TipoDependencia.class),
			@ApiResponse(code = 400, message = "TipoDependencia � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da TipoDependencia a ser atualizado", required = true) @PathParam("id") int cdTipoDependencia, 
			@ApiParam(value = "TipoDependencia a ser atualizado", required = true) TipoDependencia tipoDependencia) {
		try {
			if(tipoDependencia.getCdTipoDependencia() == 0) 
				return ResponseFactory.badRequest("TipoDependencia � nulo ou inv�lido");
			
			tipoDependencia.setCdTipoDependencia(cdTipoDependencia);
			Result r = TipoDependenciaServices.save(tipoDependencia);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("TipoDependencia � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((TipoDependencia)r.getObjects().get("TIPODEPENDENCIA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna TipoDependencia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoDependencia encontrado", response = TipoDependencia[].class),
			@ApiResponse(code = 204, message = "TipoDependencia n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id da estapa") @QueryParam("id") String idTipoDependencia,
			@ApiParam(value = "Nome da etapa") @QueryParam("tipodependencia") String nmTipoDependencia
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idTipoDependencia != null) {
				criterios.add("id_tipo_dependencia", idTipoDependencia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmTipoDependencia != null) {
				criterios.add("nm_tipo_dependencia", nmTipoDependencia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = TipoDependenciaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum etapa encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/tipodependencia")
	@ApiOperation(
		value = "Fornece um TipoDependencia dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "TipoDependencia encontrado", response = TipoDependenciaDTO.class),
		@ApiResponse(code = 204, message = "N�o existe Etapa com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da Etapa") @PathParam("id") int cdTipoDependencia) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoDependenciaServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_tipo_dependencia" : "cd_tipo_dependencia"), Integer.toString(cdTipoDependencia), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe ESCOLARIDADE com o id indicado.");
			
			return ResponseFactory.ok(new TipoDependenciaDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
