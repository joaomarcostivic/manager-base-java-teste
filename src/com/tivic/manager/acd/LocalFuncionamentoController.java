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

import com.tivic.manager.acd.LocalFuncionamento;
import com.tivic.manager.acd.LocalFuncionamentoDTO;
import com.tivic.manager.acd.LocalFuncionamentoServices;
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
@Api(value = "LocalFuncionamento", tags = {"LocalFuncionamento"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/localfuncionamento")
@Produces(MediaType.APPLICATION_JSON)
public class LocalFuncionamentoController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova LocalFuncionamento "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LocalFuncionamento registrada", response = LocalFuncionamento.class),
			@ApiResponse(code = 400, message = "LocalFuncionamento possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "LocalFuncionamento a ser registrado", required = true) LocalFuncionamento localFuncionamento) {
		try {	
			localFuncionamento.setCdLocalFuncionamento(0);
			
			Result r = LocalFuncionamentoServices.save(localFuncionamento, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("LocalFuncionamento possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((LocalFuncionamento)r.getObjects().get("LOCALFUNCIONAMENTO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de um LocalFuncionamento",
			notes = "Considere id = idLocalFuncionamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LocalFuncionamento atualizada", response = LocalFuncionamento.class),
			@ApiResponse(code = 400, message = "LocalFuncionamento � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da LocalFuncionamento a ser atualizado", required = true) @PathParam("id") int cdLocalFuncionamento, 
			@ApiParam(value = "LocalFuncionamento a ser atualizado", required = true) LocalFuncionamento localFuncionamento) {
		try {
			if(localFuncionamento.getCdLocalFuncionamento() == 0) 
				return ResponseFactory.badRequest("LocalFuncionamento � nulo ou inv�lido");
			
			localFuncionamento.setCdLocalFuncionamento(cdLocalFuncionamento);
			Result r = LocalFuncionamentoServices.save(localFuncionamento);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("LocalFuncionamento � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((LocalFuncionamento)r.getObjects().get("LOCALFUNCIONAMENTO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna LocalFuncionamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LocalFuncionamento encontrado", response = LocalFuncionamento[].class),
			@ApiResponse(code = 204, message = "LocalFuncionamento n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id da estapa") @QueryParam("id") String idLocalFuncionamento,
			@ApiParam(value = "Nome da etapa") @QueryParam("localfuncionamento") String nmLocalFuncionamento
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idLocalFuncionamento != null) {
				criterios.add("id_local_funcionamento", idLocalFuncionamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmLocalFuncionamento != null) {
				criterios.add("nm_local_funcionamento", nmLocalFuncionamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = LocalFuncionamentoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum localFuncionamento encontrado");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/localfuncionamento")
	@ApiOperation(
		value = "Fornece um LocalFuncionamento dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "LocalFuncionamento encontrado", response = LocalFuncionamentoDTO.class),
		@ApiResponse(code = 204, message = "N�o existe LocalFuncionamento com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da Etapa") @PathParam("id") int cdLocalFuncionamento) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = LocalFuncionamentoServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_local_armazenamento" : "cd_local_funcionamento"), Integer.toString(cdLocalFuncionamento), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe ESCOLARIDADE com o id indicado.");
			
			return ResponseFactory.ok(new LocalFuncionamentoDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
