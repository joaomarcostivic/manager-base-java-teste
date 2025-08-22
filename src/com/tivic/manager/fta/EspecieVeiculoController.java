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

@Api(value = "EspeciesVeiculos", tags = {"fta"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/veiculos/especies")
public class EspecieVeiculoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova espécie de veículo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Espécie de Veículo registrada", response = EspecieVeiculo.class),
			@ApiResponse(code = 400, message = "A Espécie de Veículo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Espécie de Veículo a ser registrada", required = true) EspecieVeiculo especieVeiculo) {
		try {
			especieVeiculo.setCdEspecie(0);
			
			Result result = EspecieVeiculoServices.save(especieVeiculo);
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest("A Espécie de Veículo possui algum parâmetro inválido", result.getMessage());
			
			return ResponseFactory.ok((EspecieVeiculo)result.getObjects().get("ESPECIEVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma espécie de veículo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Espécie de Veículo atualizada", response = EspecieVeiculo.class),
			@ApiResponse(code = 400, message = "A Espécie de Veículo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(@ApiParam(value = "Espécie de Veículo a ser registrada", required = true) EspecieVeiculo especieVeiculo,
			@ApiParam(value = "Código da Espécie de Veículo") @PathParam("id") int cdEspecie) {
		try {
			especieVeiculo.setCdEspecie(cdEspecie);
			
			Result result = EspecieVeiculoServices.save(especieVeiculo);
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest("A Espécie de Veículo possui algum parâmetro inválido", result.getMessage());
			
			return ResponseFactory.ok((EspecieVeiculo)result.getObjects().get("ESPECIEVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma espécie de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Espécies de Veículo encontrada", response = EspecieVeiculo.class),
			@ApiResponse(code = 204, message = "Espécie de Veículo não localizada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Espécie de Veículo") @PathParam("id") int cdEspecie) {
		try {
			EspecieVeiculo especieVeiculo = EspecieVeiculoDAO.get(cdEspecie);
			if (especieVeiculo == null)
				return ResponseFactory.noContent("Nenhuma espécie de veiculo encontrada");
			
			return ResponseFactory.ok(especieVeiculo);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna espécies de veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Espécies de Veículo encontradas", response = EspecieVeiculo[].class),
			@ApiResponse(code = 204, message = "Nenhuma espécie de Veículo localizada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value  = "Descrição da Espécie de Veiculo") @QueryParam("dsEspecie") String dsEspecie,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(dsEspecie != null)
				criterios.add(new ItemComparator("ds_especie", dsEspecie, ItemComparator.LIKE_ANY, Types.VARCHAR));
			
			ResultSetMap rsm = EspecieVeiculoServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<EspecieVeiculo> especiesVeiculo = new ResultSetMapper<EspecieVeiculo>(rsm, EspecieVeiculo.class).toList();
			return ResponseFactory.ok(especiesVeiculo);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
}
