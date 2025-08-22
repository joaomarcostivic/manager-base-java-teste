package com.tivic.manager.grl;

import java.sql.Types;

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

@Api(value = "Cidades", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/cidades")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CidadeController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Cidade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cidade registrada", response = Cidade.class),
			@ApiResponse(code = 400, message = "Cidade inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Órgão a ser registrado") Cidade cidade) {
		try {			
			cidade.setCdCidade(0);
			
			Result result = CidadeServices.save(cidade);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Cidade)result.getObjects().get("CIDADE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma Cidade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cidade atualizada", response = Cidade.class),
			@ApiResponse(code = 400, message = "Cidade inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Cidade") @PathParam("id") int cdCidade,
			@ApiParam(value = "Cidade a ser atualizada") Cidade cidade) {
		try {			
			cidade.setCdCidade(cdCidade);
			
			Result result = CidadeServices.save(cidade);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Cidade)result.getObjects().get("CIDADE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma Cidade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cidade encontrada", response = Cidade.class),
			@ApiResponse(code = 204, message = "Cidade não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Cidade") @PathParam("id") int cdCidade) {
		try {
			ResultSetMap cidades = CidadeServices.getCidadeDTO(cdCidade);
			if(!cidades.next()) 
				return ResponseFactory.noContent("Nenhuma cidade encontrada");			
			
			CidadeDTO cidade = new CidadeDTO.Builder(cidades.getInt("cd_cidade"), true).build();
			return ResponseFactory.ok(cidade);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Cidades"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cidade encontrada", response = Cidade[].class),
			@ApiResponse(code = 204, message = "Cidade não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome da Cidade") @QueryParam("cidade") String nmCidade,
			@ApiParam(value = "Nome do Estado") @QueryParam("estado") String nmEstado,
			@ApiParam(value = "Id. IBGE") @QueryParam("ibge") String idIbge) {
		try {
			Criterios criterios = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			
			if(nmCidade != null)
				criterios.add("A.nm_cidade", nmCidade, ItemComparator.LIKE_BEGIN, Types.VARCHAR);
			if(nmEstado != null)
				criterios.add("B.nm_estado", nmEstado, ItemComparator.LIKE_ANY, Types.VARCHAR);
			if(idIbge != null)
				criterios.add("A.id_ibge", idIbge, ItemComparator.LIKE, Types.VARCHAR);
			
			ResultSetMap rsm = CidadeServices.find(criterios);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma cidade encontrada");
			
			return ResponseFactory.ok(new ResultSetMapper<Cidade>(rsm, Cidade.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	
	@GET
	@Path("/{id}/bairros")
	@ApiOperation(
			value = "Retorna os Bairros de uma cidade"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cidade encontrada", response = Cidade.class),
			@ApiResponse(code = 204, message = "Cidade não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getBairros(@ApiParam(value = "Código da Cidade") @PathParam("id") int cdCidade) {
		try {			
			ResultSetMap bairros = BairroServices.find(new Criterios("A.cd_cidade", String.valueOf(cdCidade), ItemComparator.EQUAL, Types.INTEGER));
			if(!bairros.next()) 
				return ResponseFactory.noContent("Nenhum bairro encontrado");			
			
			return ResponseFactory.ok(bairros);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
