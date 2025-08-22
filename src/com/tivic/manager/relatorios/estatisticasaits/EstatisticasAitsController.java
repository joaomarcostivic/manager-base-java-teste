package com.tivic.manager.relatorios.estatisticasaits;

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

import com.tivic.manager.relatorios.estatisticasaits.builders.EstatisticasAitsSearchBuilder;
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

@Api(value = "Estatísticas de AITs", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})

@Path("/v3/mob/ait/estatisticasaits")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EstatisticasAitsController {
	private ManagerLog managerLog;
	private IEstatisticasAitsService estatisticasAitsService;
	private IGeraRelatorioEstatisticasAits geraRelatorio;

	public EstatisticasAitsController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.estatisticasAitsService = (IEstatisticasAitsService) BeansFactory.get(IEstatisticasAitsService.class);
		this.geraRelatorio = (IGeraRelatorioEstatisticasAits) BeansFactory.get(IGeraRelatorioEstatisticasAits.class);
	}

	@GET
	@Path("")
	@ApiOperation(value = "Retorna os veículos com multas vencidas")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Veículos com multas vencidas encontrados", response = RelatorioEstatisticasAitsDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum veículo encontrado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findInfracao (
		@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInfracaoInicial") String dtInfracaoInicial,
		@ApiParam(value = "Data final da consulta") @QueryParam("dtInfracaoFinal") String dtInfracaoFinal,
		@ApiParam(value = "Tipo de relatório") @QueryParam("tpRelatorio") int tpRelatorio) {
		try {
			SearchCriterios searchCriterios = new EstatisticasAitsSearchBuilder()
					.setDtInfracaoInicial(dtInfracaoInicial)
					.setDtInfracaoFinal(dtInfracaoFinal)
					.setTpRelatorio(tpRelatorio)
				.build();
			PagedResponse<RelatorioEstatisticasAitsDTO> lista = this.estatisticasAitsService.findInfracoes(searchCriterios);
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
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Impressão de relatório")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorio (
		@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasAitsDTO> aitsList, 
		@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInfracaoInicial") String dtInfracaoInicial,
		@ApiParam(value = "Data final da consulta") @QueryParam("dtInfracaoFinal") String dtInfracaoFinal,
		@ApiParam(value = "Tipo de relatório") @QueryParam("tpRelatorio") int tpRelatorio) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new EstatisticasAitsSearchBuilder()
					.setDtInicialImpressao(dtInfracaoInicial)
					.setDtFinalImpressao(dtInfracaoFinal)
					.setTpRelatorio(tpRelatorio)
					.setTpGrafico(-1)
				.build();
			return ResponseFactory.ok(this.geraRelatorio.gerar(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/grafico")
	@ApiOperation(value = "Imprime o gráfico gerado na tela", notes = "Impressão de gráfico")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/pdf", "application/json"})
	public Response printGrafico(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasAitsDTO> aitsList,
			@ApiParam(value = "Data inicial") @QueryParam("dtInfracaoInicial") String dtInfracaoInicial,
			@ApiParam(value = "Data final") @QueryParam("dtInfracaoFinal") String dtInfracaoFinal,
			@ApiParam(value = "Tipo de gráfico") @QueryParam("tpGrafico") int tpGrafico,
			@ApiParam(value = "Tipo de relatório") @QueryParam("tpRelatorio") int tpRelatorio) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new EstatisticasAitsSearchBuilder()
					.setDtInicialImpressao(dtInfracaoInicial)
					.setDtFinalImpressao(dtInfracaoFinal)
					.setTpRelatorio(tpRelatorio)
					.setTpGrafico(tpGrafico)
				.build();
			return ResponseFactory.ok(this.geraRelatorio.gerarGrafico(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
