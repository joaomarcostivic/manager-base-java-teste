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

@Api(value = "TiposVeiculos", tags = {"fta"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/veiculos/tipos")
public class TipoVeiculoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo tipo de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo de Veículo registrado", response = TipoVeiculo.class),
			@ApiResponse(code = 400, message = "O tipo de Veículo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Tipo de Veiculo a ser registrado", required = true) TipoVeiculo tipoVeiculo) {
		try {
			tipoVeiculo.setCdTipoVeiculo(0);
			Result result = TipoVeiculoServices.save(tipoVeiculo);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest("O tipo de Veículo possui algum parâmetro inválido", result.getMessage());
			}
			
			return ResponseFactory.ok((TipoVeiculo)result.getObjects().get("TIPOVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um tipo de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo de Veículo atualizado", response = TipoVeiculo.class),
			@ApiResponse(code = 400, message = "O tipo de Veículo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "Tipo de Veiculo a ser registrado") TipoVeiculo tipoVeiculo,
			@ApiParam(value = "Código do Tipo de Veículo") @PathParam("id") int cdTipoVeiculo) {
		try {
			tipoVeiculo.setCdTipoVeiculo(cdTipoVeiculo);
			
			Result result = TipoVeiculoServices.save(tipoVeiculo);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest("O tipo de Veículo possui algum parâmetro inválido", result.getMessage());
			}
			
			return ResponseFactory.ok((TipoVeiculo)result.getObjects().get("TIPOVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um tipo de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo de Veículo encontrado", response = TipoVeiculo.class),
			@ApiResponse(code = 204, message = "Tipo de Veículo não localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código do Tipo de Veículo") @PathParam("id") int cdTipoVeiculo) {
		try {
			TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get(cdTipoVeiculo);
			if (tipoVeiculo == null)
				return ResponseFactory.noContent("Nenhum tipo de veiculo encontrado");
			return ResponseFactory.ok(tipoVeiculo);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna tipos de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipos de Veículo encontrados", response = TipoVeiculo[].class),
			@ApiResponse(code = 204, message = "Nenhum tipo de Veículo localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value  = "Nome do Tipo de Veiculo") @QueryParam("nmTipoVeiculo") String nmTipoVeiculo,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(nmTipoVeiculo != null) {
				criterios.add(new ItemComparator("nm_tipo_veiculo", nmTipoVeiculo, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			ResultSetMap rsm = TipoVeiculoServices.find(criterios);
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<TipoVeiculo> tiposVeiculo = new ResultSetMapper<TipoVeiculo>(rsm, TipoVeiculo.class).toList();
			return ResponseFactory.ok(tiposVeiculo);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
}
