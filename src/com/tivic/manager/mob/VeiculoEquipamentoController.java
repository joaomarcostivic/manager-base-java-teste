package com.tivic.manager.mob;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.veiculoequipamento.VeiculoEquipamentoService;
import com.tivic.manager.validation.Validators;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.util.Result;

@Api(value = "Equipamentos", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/veiculoequipamento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VeiculoEquipamentoController {

   VeiculoEquipamentoService veiculoEquipamentoService;
	
	public VeiculoEquipamentoController() {
		veiculoEquipamentoService = new VeiculoEquipamentoService();
	}

	@POST
	@Path("")
	@ApiOperation(
			value = "Cria um vinculo veiculoEquipamento, com a inserção de um novo equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "VeiculoEquipamento criado", response = VeiculoEquipamento.class),
			@ApiResponse(code = 400, message = "Dados de instituição são nulos ou inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insert(
			@ApiParam(value = "VeiculoEquipamento a ser criado", required = true) VeiculoEquipamentoDTO veiculoEquipamentoDto
	){
		try {
			veiculoEquipamentoDto.populate();
			if(com.tivic.manager.grl.EquipamentoServices.validEquipamentoInsert(veiculoEquipamentoDto.getEquipamento()).getCode() == -1)
				return ResponseFactory.badRequest("Equipamento já existe");
			Validators<VeiculoEquipamentoDTO> validators = this.getUpdateValidators(veiculoEquipamentoDto);
			VeiculoEquipamentoDTO veiculoEquipamento = veiculoEquipamentoService.insert(veiculoEquipamentoDto, validators);
			return ResponseFactory.ok(veiculoEquipamento);
		} catch(ValidationException e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no servidor. Contate os desenvolvedores");
		} 
	}
	
	private Validators<VeiculoEquipamentoDTO> getUpdateValidators(VeiculoEquipamentoDTO veiculoEquipamentoDto) {
		return new Validators<VeiculoEquipamentoDTO>(veiculoEquipamentoDto);
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento atualizado", response = Equipamento.class),
			@ApiResponse(code = 400, message = "Equipamento inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento,
			@ApiParam(value = "Equipamento a ser atualizado") VeiculoEquipamentoDTO veiculoEquipamentoDto) {
		try {			
			veiculoEquipamentoDto.getEquipamento().setCdEquipamento(cdEquipamento);

			Result result = VeiculoEquipamentoServices.save(veiculoEquipamentoDto.getVeiculoEquipamento(), veiculoEquipamentoDto.getEquipamento());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Equipamento)result.getObjects().get("EQUIPAMENTO"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/{cdVeiculo}")
	@ApiOperation(
			value = "Retorna um Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento encontrado", response = VeiculoEquipamentoDTO.class),
			@ApiResponse(code = 204, message = "Equipamento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(
			@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento,
			@ApiParam(value = "Codigo do Veiculo") @PathParam("cdVeiculo") int cdVeiculo){
		try {
			VeiculoEquipamentoDTO equipamento = VeiculoEquipamentoServices.get(cdEquipamento, cdVeiculo);
			
			if(equipamento == null)
				return ResponseFactory.noContent("Nenhum equipamento encontrado");
			
			return ResponseFactory.ok(equipamento);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/{cdEquipamento}/{cdVeiculo}/{cdInstalacao}")
	@ApiOperation(
			value = "Remove um vinculo do veiculoequipamento"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Veiculo Equipamento removido"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remove(@PathParam("cdEquipamento") int cdEquipamento, @PathParam("cdVeiculo") int cdVeiculo,@PathParam("cdInstalacao") int cdInstalacao) {
		try {
			if(cdEquipamento <= 0) 
				return ResponseFactory.badRequest("Código do equipamento é nulo ou inválido");
			if(cdVeiculo <= 0) 
				return ResponseFactory.badRequest("Código do veiculo é nulo ou inválido");
			if(cdInstalacao <= 0) 
				return ResponseFactory.badRequest("Código da isntalação é nulo ou inválido");
			
			VeiculoEquipamentoServices.remove(cdEquipamento, cdVeiculo, cdInstalacao);
			
			return ResponseFactory.ok("");
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no servidor. Contate os desenvolvedores");
		} 
	}

}
