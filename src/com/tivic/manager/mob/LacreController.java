package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

@Api(value = "Lacre", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/lacres")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LacreController {
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Retorna uma lista de lacres"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lacres encontrados"),
		@ApiResponse(code = 204, message = "Nenhum lacre"),
		@ApiResponse(code = 204, message = "Não existe lacres com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response find (
		@ApiParam(value = "Quantidade de paginas") @QueryParam("page") int nrPagina,
		@ApiParam(value = "Quantidade de registros") @QueryParam("limit") int nrLimite,
		@ApiParam(value = "Codigo do lacre") @QueryParam("idLacre") String idLacre,
		@ApiParam(value = "Situacao do lacre") @QueryParam("stLacre") String stLacre,
		@ApiParam(value = "Identificador da serie") @QueryParam("idSerie") String idSerie
	) {
		try {
			Criterios crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(idLacre != null) {
				crt.add("A.id_lacre", idLacre, ItemComparator.LIKE_ANY, Types.VARCHAR);				
			}
			if(stLacre != null) {
				crt.add("A.st_lacre", stLacre, ItemComparator.EQUAL, Types.INTEGER);				
			}

			if(idSerie != null) {
				crt.add("A.id_serie", idSerie, ItemComparator.LIKE, Types.VARCHAR);				
			}
			ResultSetMap rsm = LacreServices.find(crt);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum lacre encontrado");
			
			return ResponseFactory.ok(new ResultSetMapper<Lacre>(rsm, Lacre.class));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um lacre"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre encontrado", response = Lacre.class),
			@ApiResponse(code = 204, message = "Lacre não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Codigo da Lacre") @PathParam("id") int cdLacre) {
		try {
			if(cdLacre == 0) 
				return ResponseFactory.badRequest("Lacre é nulo ou invalido");
			
			Lacre lacre = LacreDAO.get(cdLacre);
			
			if(lacre==null) 
				return ResponseFactory.noContent("Nenhum lacre encontrado");
			
			return ResponseFactory.ok(lacre);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/ultimo/{nrPrefixo}")
	@ApiOperation(
			value= "Retornar um lacre pelo o IdLacre"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre encontrado", response = Lacre.class),
			@ApiResponse(code = 204, message = "Lacre não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	
	public static Response getIdLacre(@ApiParam(value = "Ultimo lacre do prefixo ") @PathParam("nrPrefixo") int nrPrefixo) {
		try {
			if(nrPrefixo == 0)
				return ResponseFactory.badRequest("Lacre nulo ou invalido");
			
			Lacre result = LacreServices.getLastLacre(nrPrefixo);
			
			if(result == null)
				return ResponseFactory.noContent("Nenhum lacre encontrado");
				
			return ResponseFactory.ok(result);
						
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	} 
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo lacre"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre registrado", response = Lacre.class),
			@ApiResponse(code = 400, message = "Lacre possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Lacre a ser registrado", required = true) Lacre lacre) {
		try {	
			
			lacre.setCdLacre(0);
			
			Result r = LacreServices.save(lacre);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("Lacre possui algum parametro invalido", r.getMessage());
			}
			
			return ResponseFactory.ok((Lacre)r.getObjects().get("LACRE"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/lote")
	@ApiOperation(
			value = "Registra um lacre em lote"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre registrado", response = Lacre.class),
			@ApiResponse(code = 400, message = "Lacre possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Lacre a ser registrado", required = true) ArrayList<Lacre> lacres) {
		try {	
			
			Result r = LacreServices.saveLote(lacres);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("Lacre possui algum parametro invalido", r.getMessage());
			}
			
			return ResponseFactory.ok(r);
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um lacre"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre atualizado", response = Lacre.class),
			@ApiResponse(code = 400, message = "Lacre invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo do Lacre") @PathParam("id") int cdLacre,
			@ApiParam(value = "Lacre a ser atualizado") Lacre lacre) {
		try {			
			lacre.setCdLacre(cdLacre);
			
			Result result = LacreServices.save(lacre);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Lacre)result.getObjects().get("LACRE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga um lacre",
		notes = "Considere id = cdLacre"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lacre apagado"),
			@ApiResponse(code = 400, message = "Lacre possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "Lacre nao encontrado"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
	})
	public static Response delete(@ApiParam(value = "Id do lacre a ser apagado", required = true) @PathParam("id") int cdLacre) {
		try {
			Result r = LacreServices.remove(cdLacre);
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok("Lacre apagado");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
