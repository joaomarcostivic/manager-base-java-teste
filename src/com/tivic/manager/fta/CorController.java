package com.tivic.manager.fta;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

@Api(value = "Cor", tags = {"fta"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/cor")
public class CorController {
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova cor"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cor registrada", response = Cor.class),
			@ApiResponse(code = 400, message = "A cor possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Cor a ser registrado", required = true) Cor cor) {
		try {	
			cor.setCdCor(0);
			
			Result r = CorServices.save(cor);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("A cor possui algum parâmetro inválido", r.getMessage());
			}
			
			return ResponseFactory.ok((Cor)r.getObjects().get("COR"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma nova cor"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cor atualizada", response = Cor.class),
			@ApiResponse(code = 400, message = "A cor possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(@ApiParam(value = "Código da Cor") @PathParam("id") int cdCor,
			@ApiParam(value = "Cor a ser registrado", required = true) Cor cor) {
		try {
			cor.setCdCor(cdCor);
			Result result = CorServices.save(cor);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest("A cor possui algum parâmetro inválido", result.getMessage());
			}
			
			return ResponseFactory.ok((Cor)result.getObjects().get("COR"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma cor"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cor atualizada", response = Cor.class),
			@ApiResponse(code = 204, message = "Cor não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Cor") @PathParam("id") int cdCor) {
		try {
			Cor cor = CorDAO.get(cdCor);
			if(cor == null)
				return ResponseFactory.noContent("Nenhuma cor encontrado");
			return ResponseFactory.ok(cor);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna cores"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cores encontradas", response = Cor[].class),
			@ApiResponse(code = 204, message = "Nenhuma cor encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value  = "Nome da Cor") @QueryParam("nmCor") String nmCor,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(nmCor != null) {
				criterios.add(new ItemComparator("nm_cor", nmCor, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			ResultSetMap rsm = CorServices.find(criterios);
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<Cor> cores = new ResultSetMapper<Cor>(rsm, Cor.class).toList();
			return ResponseFactory.ok(cores);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
}
