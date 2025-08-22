package com.tivic.manager.mob.concessao.relatorio;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Concessao;
import com.tivic.manager.mob.ConcessaoDTO;
import com.tivic.sol.report.ReportServices;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ResultSetMap;

@Api(value = "Reletório Horário Aferição", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/relatorioconcessoes")
@Consumes(MediaType.APPLICATION_JSON)
public class RelatorioConcessaoController {
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Retorna a lista de Concessões"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessões encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma concessão"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response find (
		@ApiParam(value = "Situacao da concessão") @QueryParam("stConcessao") @DefaultValue("-1") int stConcessao,
		@ApiParam(value = "Tipo da concessão") @QueryParam("tpConcessao") @DefaultValue("-1") int tpConcessao,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.TP_CONCESSAO", tpConcessao, tpConcessao > -1); 
			searchCriterios.addCriteriosEqualInteger("A.ST_CONCESSAO", stConcessao, stConcessao > -1);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			searchCriterios.setOrderBy("A.CD_CONCESSAO DESC");
			
			ResultSetMap rsm = RelatorioConcessaoServices.find(searchCriterios);
			return ResponseFactory.ok(new RelatorioConcessaoDTO.ListBuilder(rsm, rsm.getTotal()).build());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/quantitativo")
	@ApiOperation(
		value = "Fornece um relatório quantitativo de concessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = ConcessaoDTO.class),
		@ApiResponse(code = 204, message = "Nao existe concessao com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioQuantitativo(
			@ApiParam(value = "Nome do usuário") @QueryParam("usuario") String nmUsuario
			) {
		try {			
			ReportCriterios reportCriterios = new ReportCriterios();
			reportCriterios.addParametros("nmUsuario", nmUsuario);
			Report report = RelatorioConcessaoServices.findQuantitativo(reportCriterios);
			
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(new HashMap<String, Object>());
			
			return ResponseFactory.ok( ReportServices.getPdfReport("mob/relatorio_concessao_quantitativo", report.getParams(), rsm));
		} catch ( Exception e ) {
			e.printStackTrace();
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
	
	@GET
	@Path("/concessao")
	@ApiOperation(
		value = "Fornece um relatório de concessao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Concessao encontrada", response = Concessao.class),
		@ApiResponse(code = 204, message = "Nenhuma concessao encontrada", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces("application/pdf")
	public Response getRelatorioConcessoes(
			@ApiParam(value = "Situacao da vistoria") @QueryParam("stConcessao") @DefaultValue("-1") int stConcessao,
			@ApiParam(value = "Tipo da vistoria") @QueryParam("tpConcessao") @DefaultValue("-1") int tpConcessao,
			@ApiParam(value = "Nome do usuário") @QueryParam("nmUsuario") String nmUsuario
		) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.ST_CONCESSAO", stConcessao, stConcessao > -1);
			searchCriterios.addCriteriosEqualInteger("A.TP_CONCESSAO", tpConcessao, tpConcessao > -1);
			
			Report report = RelatorioConcessaoServices.findRelatorioConcessao(searchCriterios, nmUsuario);
			
			return ResponseFactory.ok(ReportServices.getPdfReport("mob/relatorio_concessao", report.getParams(), report.getRsm()));
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
	}
}
