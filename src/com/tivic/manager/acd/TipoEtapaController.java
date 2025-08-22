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

import com.tivic.manager.acd.TipoEtapa;
import com.tivic.manager.acd.TipoEtapaDTO;
import com.tivic.manager.acd.TipoEtapaServices;
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
@Api(value = "Etapa", tags = {"TipoEtapa"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/etapas")
@Produces(MediaType.APPLICATION_JSON)
public class TipoEtapaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Etapa "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etapa registrada", response = TipoEtapa.class),
			@ApiResponse(code = 400, message = "Etapa possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "ETAPA a ser registrado", required = true) TipoEtapa etapa) {
		try {	
			etapa.setCdEtapa(0);
			
			Result r = TipoEtapaServices.save(etapa, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("ETAPA possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoEtapa)r.getObjects().get("ETAPA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma Etapa",
			notes = "Considere id = idEscolaridade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etapa atualizada", response = TipoEtapa.class),
			@ApiResponse(code = 400, message = "Etapa � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da ETAPA a ser atualizado", required = true) @PathParam("id") int cdEtapa, 
			@ApiParam(value = "ETAPA a ser atualizado", required = true) TipoEtapa etapa) {
		try {
			if(etapa.getCdEtapa() == 0) 
				return ResponseFactory.badRequest("ETAPA � nulo ou inv�lido");
			
			etapa.setCdEtapa(cdEtapa);
			Result r = TipoEtapaServices.save(etapa);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("ETAPA � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((TipoEtapa)r.getObjects().get("ETAPA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Etapas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etapa encontrado", response = TipoEtapa[].class),
			@ApiResponse(code = 204, message = "Etapa n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id da estapa") @QueryParam("id") String idEtapa,
			@ApiParam(value = "Nome da etapa") @QueryParam("etapa") String nmEtapa
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idEtapa != null) {
				criterios.add("id_etapa", idEtapa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmEtapa != null) {
				criterios.add("nm_etapa", nmEtapa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = TipoEtapaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum etapa encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/etapa")
	@ApiOperation(
		value = "Fornece um Etapa dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etapa encontrado", response = TipoEtapaDTO.class),
		@ApiResponse(code = 204, message = "N�o existe Etapa com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da Etapa") @PathParam("id") int cdEtapa) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoEtapaServices.find(crt.add((Util.isStrBaseAntiga() ? "cd_etapa" : "A.cd_etapa"), Integer.toString(cdEtapa), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe ESCOLARIDADE com o id indicado.");
			
			return ResponseFactory.ok(new TipoEtapaDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
