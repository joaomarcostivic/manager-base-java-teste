package com.tivic.manager.mob.veiculosmultavencida;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.veiculosmultavencida.builders.VeiculosMultaVencidaSearchBuilder;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Veiculos multa vencida", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})

@Path("/v3/mob/veiculosmultavencida")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VeiculosMultaVencidaController {
	
	private ManagerLog managerLog;
	private IVeiculosMultaVencidaService veiculosMultaVencidaService;
	
	public VeiculosMultaVencidaController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.veiculosMultaVencidaService = (IVeiculosMultaVencidaService) BeansFactory.get(IVeiculosMultaVencidaService.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna os veículos com multas vencidas")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Veículos com multas vencidas encontrados", response = VeiculosMultaVencidaDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum veículo encontrado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response find(
		@ApiParam(value = "Nome do proprietário") @QueryParam("nmProprietario") String nmProprietario,
		@ApiParam(value = "Número da placa") @QueryParam("nrPlaca") String nrPlaca,
		@ApiParam(value = "Quantidade de dias em atraso") @QueryParam("qtDiasAtraso") int qtDiasAtraso) {
		try {
			SearchCriterios searchCriterios = new VeiculosMultaVencidaSearchBuilder()
					.setNmProprietario(nmProprietario)
					.setNrPlaca(nrPlaca)
					.setQtDiasAtraso(qtDiasAtraso)
					.build();
			PagedResponse<VeiculosMultaVencidaDTO> lista = this.veiculosMultaVencidaService.find(searchCriterios);
			return ResponseFactory.ok(lista);
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/aits")
	@ApiOperation(value = "Retorna os AITs atrasadas de um veículo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs encontrados", response = VeiculosMultaVencidaDTO[].class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findAits(
			@ApiParam(value = "Nome do proprietário") @QueryParam("nmProprietario") String nmProprietario,
			@ApiParam(value = "Número da placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Quantidade de dias em atraso") @QueryParam("qtDiasAtraso") int qtDiasAtraso) {
		try {
			SearchCriterios searchCriterios = new VeiculosMultaVencidaSearchBuilder()
					.setNrPlaca(nrPlaca)
					.setQtDiasAtraso(qtDiasAtraso)
					.build();
			PagedResponse<AitVeiculoMultaVencidaDTO> lista = this.veiculosMultaVencidaService.findAits(searchCriterios);
			return ResponseFactory.ok(lista);
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao")
	@ApiOperation(value = "Imprime o relatório de veículos filtrados na tela", notes = "Impressão de relatório")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorio(
			@ApiParam(value = "Lista de AITs buscados na tela") List<VeiculosMultaVencidaDTO> aitsList) throws ValidacaoException {
		try {
			return ResponseFactory.ok(this.veiculosMultaVencidaService.imprimir(aitsList));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
