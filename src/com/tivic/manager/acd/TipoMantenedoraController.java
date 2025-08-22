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

import com.tivic.manager.acd.TipoMantenedora;
import com.tivic.manager.acd.TipoMantenedoraDTO;
import com.tivic.manager.acd.TipoMantenedoraServices;
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
@Api(value = "TipoMantenedora", tags = {"TipoMantenedora"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/tipomantenedora")
@Produces(MediaType.APPLICATION_JSON)
public class TipoMantenedoraController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova TipoMantenedora "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoMantenedora registrada", response = TipoMantenedora.class),
			@ApiResponse(code = 400, message = "TipoMantenedora possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TipoMantenedora a ser registrado", required = true) TipoMantenedora tipoMantenedora) {
		try {	
			tipoMantenedora.setCdTipoMantenedora(0);
			
			Result r = TipoMantenedoraServices.save(tipoMantenedora, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("TipoMantenedora possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoMantenedora)r.getObjects().get("TIPOMANTENEDORA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma TipoMantenedora",
			notes = "Considere id = idTipoMantenedora"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoMantenedora atualizada", response = TipoMantenedora.class),
			@ApiResponse(code = 400, message = "TipoMantenedora � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da ETAPA a ser atualizado", required = true) @PathParam("id") int cdTipoMantenedora, 
			@ApiParam(value = "ETAPA a ser atualizado", required = true) TipoMantenedora tipoMantenedora) {
		try {
			if(tipoMantenedora.getCdTipoMantenedora() == 0) 
				return ResponseFactory.badRequest("TipoMantenedora � nulo ou inv�lido");
			
			tipoMantenedora.setCdTipoMantenedora(cdTipoMantenedora);
			Result r = TipoMantenedoraServices.save(tipoMantenedora);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("TipoMantenedora � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((TipoMantenedora)r.getObjects().get("TIPOMANTENEDORA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna TipoMantendora"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoMantendora encontrado", response = TipoMantenedora[].class),
			@ApiResponse(code = 204, message = "TipoMantendora n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id da estapa") @QueryParam("id") String idTipoMantenedora,
			@ApiParam(value = "Nome da etapa") @QueryParam("tipomantenedora") String nmTipoMantenedora
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idTipoMantenedora != null) {
				criterios.add("id_tipo_mantenedora", idTipoMantenedora, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmTipoMantenedora != null) {
				criterios.add("nm_tipo_mantenedora", nmTipoMantenedora, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = TipoMantenedoraServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum mantenedora encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/tipomantenedora")
	@ApiOperation(
		value = "Fornece um TipoMantenedora dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "TipoMantenedora encontrado", response = TipoMantenedoraDTO.class),
		@ApiResponse(code = 204, message = "N�o existe TipoMantenedora com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da TipoMantenedora") @PathParam("id") int cdTipoMantenedora) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoMantenedoraServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_tipo_mantenedora" : "cd_tipo_mantenedora"), Integer.toString(cdTipoMantenedora), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe TipoMantenedora com o id indicado.");
			
			return ResponseFactory.ok(new TipoMantenedoraDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
