package com.tivic.manager.mob.ait.relatorios;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
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
import sol.dao.ItemComparator;

@Api(value = "Relatório de AITs por Tipo de Adesão a SNE", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/ait/sne/relatorios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class RelatorioAitSneController {
	
	private IGeraRelatorioAitSne gerarRelatorioAitSne;
	private ManagerLog managerLog;
	
	public RelatorioAitSneController() throws Exception {	
		this.gerarRelatorioAitSne = (IGeraRelatorioAitSne) BeansFactory.get(IGeraRelatorioAitSne.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/print/relatorio/aits/sne")
	@ApiOperation(
			value = "Retorna AIT's por tipo de adesão a SNE para geração de relatório"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Opção a SNE dos AIT's encontrada.", response = RelatorioAitSneDTO[].class),
			@ApiResponse(code = 204, message = "Opção a SNE dos AIT's não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	@Produces({ "application/pdf", "application/json" })
	public Response gerarRelatorioAits(
			@ApiParam(value = "Situação atual do documento") @QueryParam("stAtual") Integer stAtual,
			@ApiParam(value = "movimento") @QueryParam("tpStatus") @DefaultValue("-1") Integer tpStatus,
			@ApiParam(value = "Documento com movimento") @QueryParam("ctMovimento") @DefaultValue("-100") Integer ctMovimento,
			@ApiParam(value = "Data Incial de movimento") @QueryParam("dtInicialMovimento") @DefaultValue("2000-01-01") String dtInicialMovimento,
			@ApiParam(value = "Data Final de movimento") @QueryParam("dtFinalMovimento") @DefaultValue("-1") String dtFinalMovimento,
			@ApiParam(value = "Data inicial de infração") @QueryParam("dtInicialInfracao") String dtInicialInfracao,
			@ApiParam(value = "Data final de infração") @QueryParam("dtFinalInfracao") String dtFinalInfracao,
			@ApiParam(value = "Placa do veículo") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Opção de SNE") @QueryParam("stSne") int stSne
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			dtFinalMovimento = dtFinalMovimento.equals("-1") ? Util.formatDate(Util.getDataAtual(), "yyyy-MM-dd") : dtFinalMovimento;
			searchCriterios.addCriteriosEqualInteger("A.tp_status", stAtual, stAtual != null);
			searchCriterios.addCriteriosEqualInteger("B.tp_status", ctMovimento, ctMovimento != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_movimento_inicial", dtInicialMovimento, dtInicialMovimento != null);
			searchCriterios.addCriteriosMinorDate("B.dt_movimento_final", dtFinalMovimento, dtFinalMovimento != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInicialInfracao, dtInicialInfracao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtFinalInfracao, dtFinalInfracao != null);
			searchCriterios.addCriterios("A.nr_placa", nrPlaca, ItemComparator.LIKE, Types.VARCHAR, nrPlaca != null);
			searchCriterios.addCriteriosEqualInteger("B.st_adesao_sne", stSne, stSne > -1);
			return ResponseFactory.ok(this.gerarRelatorioAitSne.gerarSne(searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

}
