package com.tivic.manager.mob.linharota;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Linha;
import com.tivic.manager.mob.LinhaRota;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "LinhaRota", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
	})
@Path("/v3/mob/linharotas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LinhaRotaController {

	ILinhaRotaService linhaRotaService;

	public LinhaRotaController() {
		linhaRotaService = new LinhaRotaService();
	}
	
	@DELETE
	@Path("/{cdLinha}/{cdRota}")
	@ApiOperation(
			value = "Remove uma LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Rota removida com sucesso", response = Linha.class),
			@ApiResponse(code = 400, message = "Rota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response remove(
			@ApiParam(value = "Código da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Código da Rota") @PathParam("cdRota") int cdRota) {
		try {
			System.out.println("entrou");
			LinhaRota linhaRota = linhaRotaService.remove(cdLinha, cdRota);
			return ResponseFactory.ok(linhaRota);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/inactivate")
	@ApiOperation(
			value = "Inativa uma LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Rota inativada com sucesso", response = Linha.class),
			@ApiResponse(code = 400, message = "Rota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response inactivate(@ApiParam(value = "LinhaRota a ser registrado") LinhaRota linhaRota) {
		try {
			linhaRota = linhaRotaService.inactivate(linhaRota);
			return ResponseFactory.ok(linhaRota);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/activate")
	@ApiOperation(
			value = "Ativa uma LinhaRota"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Rota ativada com sucesso", response = Linha.class),
			@ApiResponse(code = 400, message = "Rota inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response activate(@ApiParam(value = "LinhaRota a ser registrado") LinhaRota linhaRota) {
		try {
			linhaRota = linhaRotaService.activate(linhaRota);
			return ResponseFactory.ok(linhaRota);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
