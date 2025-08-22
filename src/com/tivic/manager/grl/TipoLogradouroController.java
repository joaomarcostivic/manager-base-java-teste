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
@Path("/v2/grl/tipologradouro")
@Produces(MediaType.APPLICATION_JSON)
public class TipoLogradouroController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma novo TipoLogradouro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoLogradouro registrada", response = TipoLogradouro.class),
			@ApiResponse(code = 400, message = "TipoLogradouro possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TIPOLOGRADOURO a ser registrado", required = true) TipoLogradouro tipoLogradouro) {
		try {	
			tipoLogradouro.setCdTipoLogradouro(0);
			
			Result r = TipoLogradouroServices.save(tipoLogradouro, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("TIPOLOGRADOURO possui algum parâmetro inválido", r.getMessage());
			}
			
			return ResponseFactory.ok((TipoLogradouro)r.getObjects().get("TIPOLOGRADOURO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma TIPOLOGRADOURO",
			notes = "Considere id = idTipoLogradouro"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "TipoLogradouro atualizado", response = TipoLogradouro.class),
			@ApiResponse(code = 400, message = "TipoLogradouro é nulo ou inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da LOGRADOURO a ser atualizado", required = true) @PathParam("id") int cdTipoLogradouro, 
			@ApiParam(value = "Logradouro a ser atualizado", required = true) TipoLogradouro tipoLogradouro) {
		try {
			if(tipoLogradouro.getCdTipoLogradouro() == 0) 
				return ResponseFactory.badRequest("LOGRADOURO é nulo ou inválido");
			
			tipoLogradouro.setCdTipoLogradouro(cdTipoLogradouro);
			Result r = TipoLogradouroServices.save(tipoLogradouro);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("LOGRADOURO é nulo ou inválido", r.getMessage());
			
			return ResponseFactory.ok((TipoLogradouro)r.getObjects().get("ESCOLARIDADE"));
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
			@ApiResponse(code = 200, message = "LOGRADOURO encontrado", response = Logradouro[].class),
			@ApiResponse(code = 204, message = "LOGRADOURO não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value = "Id do logradouro") @QueryParam("id") String cdTipoLogradouro,
			@ApiParam(value = "Nome do tipo logradouro") @QueryParam("nmTipoLogradouro") String nmTipoLogradouro,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
						
			ArrayList<ItemComparator> criterios = new ArrayList< ItemComparator >();
			if(nmTipoLogradouro != null) {
				criterios.add( new ItemComparator( "nm_tipo_logradouro", nmTipoLogradouro, ItemComparator.LIKE_ANY, Types.VARCHAR ));
			}			
			
			ResultSetMap rsm = TipoLogradouroServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum logradouro encontrado.");
			}
			
			List<TipoLogradouro> tipoLogradouros = new ResultSetMapper<TipoLogradouro>(rsm, TipoLogradouro.class).toList();
			
			return ResponseFactory.ok(tipoLogradouros);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/tipos")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = idLogradouro"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "TipoLogradouro encontrado", response = TipoLogradouro[].class),
		@ApiResponse(code = 204, message = "Não existe TipoLogradouro com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response getAll() {
		try {	
	
			ResultSetMap rsm = TipoLogradouroServices.getAll();
			if(!rsm.next())
				return ResponseFactory.noContent("nenhum tipologradouro encontrado.");
			
			return ResponseFactory.ok(rsm); 
			
		} catch(Exception e) {			
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	
	@GET
	@Path("/gettipologradouro/{id}")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = idLogradouro"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Logradouro encontrado", response = TipoLogradouroDTO.class),
		@ApiResponse(code = 204, message = "Não existe logradouro com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id de LOGRADOURO") @PathParam("id") int cdTipoLogradouro) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = TipoLogradouroServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_logradouro" : "cd_tipo_logradouro"), Integer.toString(cdTipoLogradouro), ItemComparator.EQUAL, Types.VARCHAR));
			if(!rsm.next())
				return ResponseFactory.noContent("Não existe logradouro com o id indicado.");
			
			return ResponseFactory.ok(new TipoLogradouroDTO.Builder(rsm.getRegister()).build());
			
			
			/*Logradouro logradouro = LogradouroDAO.get(cdLogradouro, null);
			 if(logradouro == null)
					return ResponseFactory.noContent("Não existe imagem com o id indicado.");
				
				return ResponseFactory.ok(logradouro);*/
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	


}
