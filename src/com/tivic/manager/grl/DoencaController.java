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

import com.tivic.manager.grl.Doenca;
import com.tivic.manager.grl.DoencaDTO;
import com.tivic.manager.grl.DoencaServices;
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
@Api(value = "Doenca", tags = {"Doenca"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/doencas")
@Produces(MediaType.APPLICATION_JSON)
public class DoencaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Doenca"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Doenca registrada", response = Doenca.class),
			@ApiResponse(code = 400, message = "Doenca possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "DOENCA a ser registrado", required = true) Doenca doenca) {
		try {	
			doenca.setCdDoenca(0);
			
			Result r = DoencaServices.save(doenca, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("DOENCA possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((Doenca)r.getObjects().get("ALERGIA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma DOENCA",
			notes = "Considere id = idDoenca"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT atualizado", response = Doenca.class),
			@ApiResponse(code = 400, message = "AIT � nulo ou inv�lido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da DOENCA a ser atualizado", required = true) @PathParam("id") int cdDoenca, 
			@ApiParam(value = "DOENCA a ser atualizado", required = true) Doenca doenca) {
		try {
			if(doenca.getCdDoenca() == 0) 
				return ResponseFactory.badRequest("DOENCA � nulo ou inv�lido");
			
			doenca.setCdDoenca(cdDoenca);
			Result r = DoencaServices.save(doenca);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("DOENCA � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((Doenca)r.getObjects().get("DOENCA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Doencas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "DOENCA encontrado", response = Doenca[].class),
			@ApiResponse(code = 204, message = "DOENCA n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Id da Alergia") @QueryParam("id") String idDoenca,
			@ApiParam(value = "Nome da alergia") @QueryParam("doenca") String nmDoenca,
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			if(idDoenca != null) {
				criterios.add("id_doenca", idDoenca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmDoenca != null) {
				criterios.add("nm_doenca", nmDoenca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = DoencaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum alergia encontrada");
			}
			
			List<Doenca> doencas = new ResultSetMapper<Doenca>(_rsm, Doenca.class).toList();
			
			return ResponseFactory.ok(doencas);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/getdoenca/{idDoenca}")
	@ApiOperation(
		value = "Fornece um DOENCA dado o id indicado",
		notes = "Considere id = idDoenca"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ALERGIA encontrado", response = DoencaDTO.class),
		@ApiResponse(code = 204, message = "N�o existe ALERGIA com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id de DOENCA") @PathParam("idDoenca") String idDoenca) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap _rsm = DoencaServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_doenca" : "id_doenca"), idDoenca, ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!_rsm.next())
				return ResponseFactory.noContent("N�o existe DOENCA com o id indicado.");
			
			return ResponseFactory.ok(new DoencaDTO.Builder(_rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
