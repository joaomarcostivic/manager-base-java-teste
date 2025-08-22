package com.tivic.manager.mob.concessaoveiculo;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.ConcessaoVeiculo;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "ConcessaoVeiculo", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/mob/concessoesveiculos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConcessaoVeiculoController {

	private ConcessaoVeiculoService concessaoVeiculoService;

	public ConcessaoVeiculoController() {
		this.concessaoVeiculoService = new ConcessaoVeiculoService();
	}

	@POST
	@Path("/")
	@ApiOperation(value = "Registra uma nova concessao veiculo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Veiculo registrado", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 400, message = "Veiculo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response create(@ApiParam(value = "ConcessaoVeiculo a ser registrado") ConcessaoVeiculoDTO dto) {
		try {
			dto.getConcessaoVeiculo().setCdConcessaoVeiculo(0);
			dto.getPessoa().setCdPessoa(0);
			dto.getPessoaEndereco().setCdPessoa(0);
			concessaoVeiculoService.save(dto);
			return ResponseFactory.ok(dto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Atualiza uma concessao veiculo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Veiculo atualizado", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 400, message = "Veiculo invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response update(
			@ApiParam(value = "Codigo da concessao veiculo a ser atualizada") @PathParam("id") int cdConcessaoVeiculo,
			@ApiParam(value = "ConcessaoVeiculo a ser atualizada.") ConcessaoVeiculoDTO dto) {
		try {
			dto.getConcessaoVeiculo().setCdConcessaoVeiculo(cdConcessaoVeiculo);
			concessaoVeiculoService.save(dto);
			return ResponseFactory.ok(dto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}/desvincular")
	@ApiOperation(value = "Altera a situação do veículo para desvinculado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Situação atualizada", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 400, message = "Veiculo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response desvincular(@ApiParam(value = "Situação do veículo a ser registrado") ConcessaoVeiculoDTO dto) {
		try {
			concessaoVeiculoService.desvincularVeiculo(dto, null);
			return ResponseFactory.ok(dto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
