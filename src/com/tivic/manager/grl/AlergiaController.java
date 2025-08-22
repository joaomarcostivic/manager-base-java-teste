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

import com.tivic.manager.grl.Alergia;
import com.tivic.manager.grl.AlergiaDTO;
import com.tivic.manager.grl.AlergiaServices;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteServices;
import com.tivic.manager.mob.AitDTO;
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
@Api(value = "Alergia", tags = {"Alergia"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/alergias")
@Produces(MediaType.APPLICATION_JSON)
public class AlergiaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Alergia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alergia registrada", response = Alergia.class),
			@ApiResponse(code = 400, message = "Alergia possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "ALERGIA a ser registrado", required = true) Alergia alergia) {
		try {	
			alergia.setCdAlergia(0);
			
			Result r = AlergiaServices.save(alergia, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("ALERGIA possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((Alergia)r.getObjects().get("ALERGIA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de um ALERGIA",
			notes = "Considere id = cdAit"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT atualizado", response = Alergia.class),
			@ApiResponse(code = 400, message = "AIT � nulo ou inv�lido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da ALERGIA a ser atualizado", required = true) @PathParam("id") int cdAlergia, 
			@ApiParam(value = "ALERGIA a ser atualizado", required = true) Alergia alergia) {
		try {
			if(alergia.getCdAlergia() == 0) 
				return ResponseFactory.badRequest("ALERGIA � nulo ou inv�lido");
			
			alergia.setCdAlergia(cdAlergia);
			Result r = AlergiaServices.save(alergia);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("ALERGIA � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((Alergia)r.getObjects().get("ALERGIA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Alergias"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Alergia encontrado", response = Alergia[].class),
			@ApiResponse(code = 204, message = "Alergia n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Id da Alergia") @QueryParam("id") String idAlergia,
			@ApiParam(value = "Nome da alergia") @QueryParam("alergia") String nmAlergia,
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			if(idAlergia != null) {
				criterios.add("id_alergia", idAlergia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmAlergia != null) {
				criterios.add("nm_alergia", nmAlergia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = AlergiaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum alergia encontrada");
			}
			
			List<Alergia> alergias = new ResultSetMapper<Alergia>(_rsm, Alergia.class).toList();
			
			return ResponseFactory.ok(alergias);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/getalergia/{idAlergia}")
	@ApiOperation(
		value = "Fornece um ALERGIA dado o id indicado",
		notes = "Considere id = cdAlergia"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ALERGIA encontrado", response = AlergiaDTO.class),
		@ApiResponse(code = 204, message = "N�o existe ALERGIA com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id de ALERGIA") @PathParam("idAlergia") String idAlergia) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap _rsm = AlergiaServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_alergia" : "id_alergia"), idAlergia, ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!_rsm.next())
				return ResponseFactory.noContent("N�o existe ALERGIA com o id indicado.");
			
			return ResponseFactory.ok(new AlergiaDTO.Builder(_rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
