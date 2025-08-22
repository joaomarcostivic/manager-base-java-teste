package com.tivic.manager.grl;

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

import com.tivic.manager.grl.Escolaridade;
import com.tivic.manager.grl.EscolaridadeDTO;
import com.tivic.manager.grl.EscolaridadeServices;
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
@Api(value = "Escolaridade", tags = {"Escolaridade"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/escolaridades")
@Produces(MediaType.APPLICATION_JSON)
public class EscolaridadeController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Escolaridade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Escolaridade registrada", response = Escolaridade.class),
			@ApiResponse(code = 400, message = "Escolaridade possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "ESCOLARIDADE a ser registrado", required = true) Escolaridade escolaridade) {
		try {	
			escolaridade.setCdEscolaridade(0);
			
			Result r = EscolaridadeServices.save(escolaridade, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("ESCOLARIDADE possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((Escolaridade)r.getObjects().get("ESCOLARIDADE"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma ESCOLARIDADE",
			notes = "Considere id = idEscolaridade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "ESCOLARIDADE atualizado", response = Escolaridade.class),
			@ApiResponse(code = 400, message = "ESCOLARIDADE � nulo ou inv�lido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da ESCOLARIDADE a ser atualizado", required = true) @PathParam("id") int cdEscolaridade, 
			@ApiParam(value = "ESCOLARIDADE a ser atualizado", required = true) Escolaridade escolaridade) {
		try {
			if(escolaridade.getCdEscolaridade() == 0) 
				return ResponseFactory.badRequest("ESCOLARIDADE � nulo ou inv�lido");
			
			escolaridade.setCdEscolaridade(cdEscolaridade);
			Result r = EscolaridadeServices.save(escolaridade);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("ESCOLARIDADE � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((Escolaridade)r.getObjects().get("ESCOLARIDADE"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Escolaridades"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "ESCOLARIDADE encontrado", response = Escolaridade[].class),
			@ApiResponse(code = 204, message = "ESCOLARIDADE n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Id da escolaridade") @QueryParam("id") String idEscolaridade,
			@ApiParam(value = "Nome da escolaridade") @QueryParam("escolaridade") String nmEscolaridade,
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			if(idEscolaridade != null) {
				criterios.add("id_escolaridade", idEscolaridade, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmEscolaridade != null) {
				criterios.add("nm_escolaridade", nmEscolaridade, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = EscolaridadeServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum escolaridade encontrada");
			}
			
			List<Escolaridade> escolaridades = new ResultSetMapper<Escolaridade>(_rsm, Escolaridade.class).toList();
			
			return ResponseFactory.ok(escolaridades);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/getescolaridade/{id}")
	@ApiOperation(
		value = "Fornece um ESCOLARIDADE dado o id indicado",
		notes = "Considere id = idEscolaridade"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ESCOLARIDADE encontrado", response = EscolaridadeDTO.class),
		@ApiResponse(code = 204, message = "N�o existe ESCOLARIDADE com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id de ESCOLARIDADE") @PathParam("id") String idEscolaridade) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap _rsm = EscolaridadeServices.find(crt.add((Util.isStrBaseAntiga() ? "id_escolaridade" : "id_escolaridade"), idEscolaridade, ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!_rsm.next())
				return ResponseFactory.noContent("N�o existe ESCOLARIDADE com o id indicado.");
			
			return ResponseFactory.ok(new EscolaridadeDTO.Builder(_rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
