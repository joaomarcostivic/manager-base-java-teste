package com.tivic.manager.grl.equipamento.report;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Concessao;
import com.tivic.sol.report.ReportServices;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Reletório de equipamentos", tags = { "grl" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/grl/equipamentos/relatorio")
@Consumes(MediaType.APPLICATION_JSON)
public class ReportEquipamentoController {

	ReportEquipamentoService reportEquipamentoService;
	
	public ReportEquipamentoController() throws Exception {
		reportEquipamentoService = (ReportEquipamentoService) BeansFactory.get(ReportEquipamentoService.class);
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Retorna a lista de equipamentos"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Equipamentos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum equipamento encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response find (
		@ApiParam(value = "Situacao do equipamento") @QueryParam("stEquipamento") @DefaultValue("-1") int stEquipamento,
		@ApiParam(value = "Tipo de equipamento") @QueryParam("tpEquipamento") @DefaultValue("-1") int tpEquipamento,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.TP_EQUIPAMENTO", tpEquipamento, tpEquipamento > -1); 
			searchCriterios.addCriteriosEqualInteger("A.ST_EQUIPAMENTO", stEquipamento, stEquipamento > -1);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			searchCriterios.setOrderBy("A.NM_EQUIPAMENTO");
			return ResponseFactory.ok(reportEquipamentoService.find(searchCriterios));
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/pdf")
	@ApiOperation(
		value = "Fornece um relatório de equipamentos"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Relatório gerado", response = Concessao.class),
		@ApiResponse(code = 204, message = "Nenhum equipamento encontrado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response buildReport(
			@ApiParam(value = "Situacao do equipamento") @QueryParam("stEquipamento") @DefaultValue("-1") int stEquipamento,
			@ApiParam(value = "Tipo de equipamento") @QueryParam("tpEquipamento") @DefaultValue("-1") int tpEquipamento
		) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.TP_EQUIPAMENTO", tpEquipamento, tpEquipamento > -1); 
			searchCriterios.addCriteriosEqualInteger("A.ST_EQUIPAMENTO", stEquipamento, stEquipamento > -1);
			Report report = reportEquipamentoService.buildReport(searchCriterios);
			return ResponseFactory.ok(ReportServices.getPdfReport("grl/equipamento", report.getParams(), report.getRsm()));
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
}
