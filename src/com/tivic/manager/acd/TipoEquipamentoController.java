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

import com.tivic.manager.acd.TipoEquipamento;
import com.tivic.manager.acd.TipoEquipamentoDTO;
import com.tivic.manager.acd.TipoEquipamentoServices;
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
@Api(value = "TipoEquipamento", tags = {"TipoEquipamento"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/equipamentos")
@Produces(MediaType.APPLICATION_JSON)
public class TipoEquipamentoController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma novo TipoEquipamento "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoEquipamento registrada", response = TipoEquipamento.class),
			@ApiResponse(code = 400, message = "TipoEquipamento possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "ETAPA a ser registrado", required = true) TipoEquipamento tipoEquipamento) {
		try {	
			tipoEquipamento.setCdTipoEquipamento(0);
			
			Result r = TipoEquipamentoServices.save(tipoEquipamento, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("TipoEquipamento possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoEquipamento)r.getObjects().get("TIPOEQUIPAMENTO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma TipoEquipamento",
			notes = "Considere id = cdTipoEquipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoEquipamento atualizada", response = TipoEquipamento.class),
			@ApiResponse(code = 400, message = "TipoEquipamento � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da TipoEquipamento a ser atualizado", required = true) @PathParam("id") int cdTipoEquipamento, 
			@ApiParam(value = "TipoEquipamento a ser atualizado", required = true) TipoEquipamento tipoEquipamento) {
		try {
			if(tipoEquipamento.getCdTipoEquipamento() == 0) 
				return ResponseFactory.badRequest("TipoEquipamento � nulo ou inv�lido");
			
			tipoEquipamento.setCdTipoEquipamento(cdTipoEquipamento);
			Result r = TipoEquipamentoServices.save(tipoEquipamento);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("TipoEquipamento � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((TipoEtapa)r.getObjects().get("TIPOEQUIPAMENTO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna TipoEquipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoEquipamento encontrado", response = TipoEquipamento[].class),
			@ApiResponse(code = 204, message = "TipoEquipamento n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id do tipoEquipamento") @QueryParam("id") String idTipoEquipamento,
			@ApiParam(value = "Nome do tipoEquipamento") @QueryParam("equipamento") String nmTipoEquipamento
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idTipoEquipamento != null) {
				criterios.add("id_tipo_equipamento", idTipoEquipamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmTipoEquipamento != null) {
				criterios.add("nm_tipo_equipamento", nmTipoEquipamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = TipoEquipamentoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum tipoEquipamento encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/tipoequipamento")
	@ApiOperation(
		value = "Fornece um TipoEquipamento dado o id indicado",
		notes = "Considere id = cdTipoEquipamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "TipoEquipamento encontrado", response = TipoEquipamentoDTO.class),
		@ApiResponse(code = 204, message = "N�o existe TipoEquipamento com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da Etapa") @PathParam("id") int cdTipoEquipamento) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoEquipamentoServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_etapa" : "cd_tipo_equipamento"), Integer.toString(cdTipoEquipamento), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe TipoEquipamento com o id indicado.");
			
			return ResponseFactory.ok(new TipoEquipamentoDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
