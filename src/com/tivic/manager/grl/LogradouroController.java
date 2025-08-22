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

import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroDTO;
import com.tivic.manager.grl.LogradouroServices;
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
@Api(value = "Logradouro", tags = {"Logradouro"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/logradouros")
@Produces(MediaType.APPLICATION_JSON)
public class LogradouroController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma novo Logradouro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Logradouro registrada", response = Logradouro.class),
			@ApiResponse(code = 400, message = "Logradouro possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Logradouro a ser registrado", required = true) Logradouro logradouro) {
		try {	
			logradouro.setCdLogradouro(0);
			
			Result r = LogradouroServices.save(logradouro, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("LOGRADOURO possui algum par�metro inválido", r.getMessage());
			}
			
			return ResponseFactory.ok((Logradouro)r.getObjects().get("LOGRADOURO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma LOGRADOURO",
			notes = "Considere id = idLogradouro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LOGRADOURO atualizado", response = Logradouro.class),
			@ApiResponse(code = 400, message = "LOGRADOURO é nulo ou inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da LOGRADOURO a ser atualizado", required = true) @PathParam("id") int cdLogradouro, 
			@ApiParam(value = "LOGRADOURO a ser atualizado", required = true) Logradouro logradouro) {
		try {
			if(logradouro.getCdLogradouro() == 0) 
				return ResponseFactory.badRequest("LOGRADOURO é nulo ou inválido");
			
			logradouro.setCdLogradouro(cdLogradouro);
			Result r = LogradouroServices.save(logradouro);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("LOGRADOURO é nulo ou inválido", r.getMessage());
			
			return ResponseFactory.ok((Logradouro)r.getObjects().get("LOGRADOURO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Logradouros"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "LOGRADOURO encontrado", response = LogradouroDTO[].class),
			@ApiResponse(code = 204, message = "LOGRADOURO não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getAll(@ApiParam(value = "Id do logradouro") @QueryParam("id") String idLogradouro,
			@ApiParam(value = "Nome do logradouro") @QueryParam("nmLogradouro") String nmLogradouro,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			if(idLogradouro != null) {
				criterios.add("id_logradouro", idLogradouro, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmLogradouro != null) {
				criterios.add("A.nm_logradouro", nmLogradouro, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap rsm = LogradouroServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum logradouro encontrado");
			}
			
			List<Logradouro> logradouros = new ResultSetMapper<Logradouro>(rsm, Logradouro.class).toList();
			
			List<LogradouroDTO> logradouroDTO = new ArrayList<LogradouroDTO>();

			logradouroDTO = this.CreateListGetAll(logradouros);			

			return ResponseFactory.ok(logradouroDTO);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
		
	}

	public static List<LogradouroDTO> CreateListGetAll(List<Logradouro> logradouros){
		List<LogradouroDTO> logradourosDTO = new ArrayList<LogradouroDTO>();

		for(Logradouro logradouro : logradouros) {
			LogradouroDTO logradouroDTO = new LogradouroDTO.Builder(logradouro).getBairro().build(); 
			logradourosDTO.add(logradouroDTO);
		}

		return logradourosDTO;
	}	
	
	@GET
	@Path("/{id}/getlogradouro")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = idLogradouro"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "LOGRADOURO encontrado", response = LogradouroDTO.class),
		@ApiResponse(code = 204, message = "Não existe logradouro no id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id de LOGRADOURO") @PathParam("id") int cdLogradouro) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = LogradouroServices.find(crt.add((Util.isStrBaseAntiga() ? "A.cd_logradouro" : "cd_logradouro"), Integer.toString(cdLogradouro), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("Não existe logradouro no id indicado");
			
			return ResponseFactory.ok(new LogradouroDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	

	@GET
	@Path("/{id}/bairros")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = idLogradouro"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ESCOLARIDADE encontrado", response = LogradouroBairro[].class),
		@ApiResponse(code = 204, message = "Não existe logradouro no id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveByCdLogradouro(@ApiParam(value = "id de LOGRADOURO") @PathParam("id") int cdLogradouro) {
		try {	
			
			Criterios crt = new Criterios();
			ResultSetMap rsm = LogradouroBairroServices.find(crt.add((Util.isStrBaseAntiga() ? "A.cd_logradouro" : "cd_logradouro"), Integer.toString(cdLogradouro), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("Não existe logradouro bairro no id indicado");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	
	


}
