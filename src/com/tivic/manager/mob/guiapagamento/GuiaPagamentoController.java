package com.tivic.manager.mob.guiapagamento;

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

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.guiapagamento.geraguiapagamento.IGeraGuiaPagamento;
import com.tivic.manager.util.pagination.PagedResponse;
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
import sol.util.Result;

@Api(value = "Guia de pagamento", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/guiapagamento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GuiaPagamentoController {

	private IGuiaPagamentoService guiaPagamentoService;
	private IGeraGuiaPagamento geraGuiaPagamento;
	private ManagerLog managerLog;
	
	public GuiaPagamentoController() throws Exception {
		this.guiaPagamentoService = (IGuiaPagamentoService) BeansFactory.get(IGuiaPagamentoService.class);
		this.geraGuiaPagamento = (IGeraGuiaPagamento) BeansFactory.get(IGeraGuiaPagamento.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Infrações"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infrações encontradas", response = Infracao[].class),
			@ApiResponse(code = 204, message = "Nenhuma infração encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response findGuiaPagamento(@ApiParam(value = "Número de AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Número do CPF") @QueryParam("nrCpfCnpjProprietario") String nrCpfCnpjProprietario,
			@ApiParam(value = "Placa do veículo") @QueryParam("nrPlaca") String nrPlaca,
			@QueryParam("limit") int limit,
			@QueryParam("page") int page
			) {
		try {
			SearchCriterios searchCriterios = new GuiaPagamentoSearchBuilder()
					.setIdAit(idAit)
					.setNrCpfCnpjProprietario(nrCpfCnpjProprietario)
					.setNrPlaca(nrPlaca)
					.setLimit(limit, page)
					.build();
			PagedResponse<Ait> pagedAit = this.guiaPagamentoService.findGuiaPagamento(searchCriterios);
			return ResponseFactory.ok(pagedAit);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/impressao")
	@ApiOperation(value = "Gera guia de pagamento")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "As guias possuem algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarGuiaPagamento(
			@ApiParam(value = "Ait´s para geração de guia de pagamento") List<Ait> ait) {
		try {
			byte[] arquivoGuiaPagamento = this.geraGuiaPagamento.gerarGuiaPagamento(ait);
			return ResponseFactory.ok(arquivoGuiaPagamento);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/impressao/np-ficticia")
	@ApiOperation(
			value = "Gera a impressão de uma NP com dados fictícios"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Documento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarNpFicticia() {
		try {
			return ResponseFactory.ok(this.guiaPagamentoService.gerarNpFicticia());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
