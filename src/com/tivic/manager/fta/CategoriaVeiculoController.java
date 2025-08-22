package com.tivic.manager.fta;

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

@Api(value = "Categoria", tags = {"fta"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/veiculo/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoriaVeiculoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova categoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria registrada", response = CategoriaVeiculo.class),
			@ApiResponse(code = 400, message = "Categoria inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Categoria a ser registrada") CategoriaVeiculo categoria) {
		try {			
			categoria.setCdCategoria(0);
			
			Result result = CategoriaVeiculoServices.save(categoria);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((CategoriaVeiculo)result.getObjects().get("CATEGORIAVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma categoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria atualizada", response = CategoriaVeiculo.class),
			@ApiResponse(code = 400, message = "Categoria inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Código da Categoria") @PathParam("id") int cdCategoria,
			@ApiParam(value = "Categoria a ser atualizada") CategoriaVeiculo categoria) {
		try {			
			categoria.setCdCategoria(cdCategoria);
			
			Result result = CategoriaVeiculoServices.save(categoria);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((CategoriaVeiculo)result.getObjects().get("CATEGORIAVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma Categoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categoria encontrada", response = CategoriaVeiculo.class),
			@ApiResponse(code = 204, message = "Categoria não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código da Categoria") @PathParam("id") int cdCategoria) {
		try {
			CategoriaVeiculo categoria = CategoriaVeiculoServices.get(cdCategoria);
			
			if(categoria==null) 
				return ResponseFactory.noContent("Nenhuma cateorgia encontrada");			
			
			return ResponseFactory.ok(categoria);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Categorias"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Categorias encontradas", response = CategoriaVeiculo[].class),
			@ApiResponse(code = 204, message = "Categorias não encontradas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome da Categoria") @QueryParam("nmCategoria") String nmCategoria) {
		try {
			Criterios criterios = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			if(nmCategoria != null)
				criterios.add("nm_categoria", nmCategoria, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			ResultSetMap rsm = CategoriaVeiculoServices.find(criterios);
			
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma categoria encontrada");
			
			return ResponseFactory.ok(new ResultSetMapper<CategoriaVeiculo>(rsm, CategoriaVeiculo.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
