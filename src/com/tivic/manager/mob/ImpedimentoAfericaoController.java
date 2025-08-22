package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.CategoriaVeiculoServices;
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

@Api(value = "Motivo impedimento", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("v2/mob/impedimentoAfericao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImpedimentoAfericaoController {
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de motivos do impedimento das aferições das catracas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Motivos de impedimento encontradas", response = ImpedimentoAfericao[].class),
			@ApiResponse(code = 204, message = "Motivos não encontradas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(Connection connect) {
		try {
						
			ResultSetMap rsm = ImpedimentoAfericaoServices.getAll(connect);
			
			
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum motivo de impedimento encontrado");
			
			return ResponseFactory.ok(new ResultSetMapper<ImpedimentoAfericao>(rsm, ImpedimentoAfericao.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}

