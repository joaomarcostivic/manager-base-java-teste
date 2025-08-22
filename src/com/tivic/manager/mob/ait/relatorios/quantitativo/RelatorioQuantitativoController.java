package com.tivic.manager.mob.ait.relatorios.quantitativo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Relatório quantitativo de movimentos", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/ait/relatorios/quantitativo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioQuantitativoController {
	
	IRelatorioQuantitativoService relatorioQuantitativoService;
	private ManagerLog managerLog;

	public RelatorioQuantitativoController() throws Exception {
		relatorioQuantitativoService = (IRelatorioQuantitativoService) BeansFactory.get(IRelatorioQuantitativoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna relatório quantitativo de movimentos registrado ao Detran."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultato quantitativo encontrado.", response = RelatorioQuantitativoDTO[].class),
			@ApiResponse(code = 204, message = "Resultato quantitativo não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response findQuantitativo(
			@ApiParam(value = "Data inicial de movimento") @QueryParam("dtMovimentoInicial") String dtMovimentoInicial,
			@ApiParam(value = "Data final de movimento") @QueryParam("dtMovimentoFinal") String dtMovimentoFinal,
			@QueryParam("page") Integer nrPagina,
			@QueryParam("limit") Integer nrLimite
	) {
		try {	
			RelatorioQuantitativoSearch relatorioQuantitativoSearch = new RelatorioQuantitativoSearchBuilder()
					.setDtMovimentoInicial(dtMovimentoInicial)
					.setDtMovimentoFinal(dtMovimentoFinal)
					.build();
			return ResponseFactory.ok(relatorioQuantitativoService.buscarRelatorioQuantitativo(relatorioQuantitativoSearch));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/geracao")
	@ApiOperation(
			value = "Gera relatório quantitativo para impressão."
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Relatório encontrado.", response = RelatorioQuantitativoDTO[].class),
			@ApiResponse(code = 204, message = "Relatório não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	@Produces({ "application/pdf", "application/json" })
	public Response gerarRelatorioAits(
			@ApiParam(value = "Data inicial de movimento") @QueryParam("dtMovimentoInicial") String dtMovimentoInicial,
			@ApiParam(value = "Data final de movimento") @QueryParam("dtMovimentoFinal") String dtMovimentoFinal,
			@QueryParam("page") Integer nrPagina,
			@QueryParam("limit") Integer nrLimite
			) {
		try {
			RelatorioQuantitativoSearch relatorioQuantitativoSearch = new RelatorioQuantitativoSearchBuilder()
					.setDtMovimentoInicial(dtMovimentoInicial)
					.setDtMovimentoFinal(dtMovimentoFinal)
					.build();
			return ResponseFactory.ok(relatorioQuantitativoService.gerar(relatorioQuantitativoSearch));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
