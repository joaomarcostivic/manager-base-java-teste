package com.tivic.manager.mob.ait.relatorios;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Relatorio de recurso Jari", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/ait/relatorios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioRecursoController {
	IRelatorioRecursoServices relatorioRecursoServices;
	
	public RelatorioRecursoController() throws Exception{
		this.relatorioRecursoServices = (IRelatorioRecursoServices) BeansFactory.get(IRelatorioRecursoServices.class);
	}
	
	@GET
	@Path("/recursojari/{cdAit}")
	@ApiOperation(value = "Retorna dados de uma solicitação de cartão documento")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "solicitação encontrada"),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces("application/pdf")
	public Response getReportJari(
			@ApiParam( value = "Código da AIT" ) @PathParam( "cdAit" ) @DefaultValue("-1") int cdAit
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.CD_AIT", cdAit, cdAit > -1);
			searchCriterios.setQtLimite(1);
			Report report = relatorioRecursoServices.reportRecursoJari(searchCriterios);
			
			return ResponseFactory.ok(report.getReportPdf("mob/relatorio_recurso_jari"));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	} 
}
