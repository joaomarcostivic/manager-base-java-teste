package com.tivic.manager.mob.relatorioestatisticas;

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

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.mob.relatorioestatisticas.builders.RelatorioEstatisticasSearchBuilder;
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

@Api(value = "Equipamentos", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/relatorioestatisticas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioEstatisticasController {
	
	private IRelatorioEstatisticasService relatorioEstatisticasService;
	private IGerarRelatorioEstatistico gerarRelatorioEstatistico;
	private ManagerLog managerLog;

	public RelatorioEstatisticasController() throws Exception {
		relatorioEstatisticasService = (IRelatorioEstatisticasService) BeansFactory.get(IRelatorioEstatisticasService.class);
		gerarRelatorioEstatistico = (IGerarRelatorioEstatistico) BeansFactory.get(IGerarRelatorioEstatistico.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna as entradas de NAIs")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Entradas de nai encontradas", response = Equipamento[].class),
		@ApiResponse(code = 204, message = "Nenhuma NAI encontrada", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findNais(
		@ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
		@ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
		@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
		@ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade) {
		try {
			SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
					.setDtInicialMovimento(dtInicial)
					.setDtFinalMovimento(dtFinal)
					.setTpConsulta(tpConsulta)
					.setPeriodicidade(tpPeriodicidade)
				.build();
			PagedResponse<RelatorioEstatisticasDTO> lista = this.relatorioEstatisticasService.findNais(searchCriterios);
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
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorio(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasDTO> aitsList,
			@ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
					.setDtInicialFiltro(dtInicial)
					.setDtFinalFiltro(dtFinal)
					.setTpConsulta(tpConsulta)
					.setTpGrafico(-1)
				.build();
			return ResponseFactory.ok(this.gerarRelatorioEstatistico.gerar(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao/nip")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorioNips(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasNipDTO> aitsList,
			@ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
					.setDtInicialFiltro(dtInicial)
					.setDtFinalFiltro(dtFinal)
					.setTpConsulta(tpConsulta)
					.setTpGrafico(-1)
				.build();
			return ResponseFactory.ok(this.gerarRelatorioEstatistico.gerarRelatorioNips(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao/grafico/nai")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/pdf", "application/json"})
	public Response printGraficoNai(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasDTO> aitsList,
			@ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Tipo de gráfico") @QueryParam("tpGrafico") int tpGrafico,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
					.setDtInicialFiltro(dtInicial)
					.setDtFinalFiltro(dtFinal)
					.setTpConsulta(tpConsulta)
					.setTpGrafico(tpGrafico)
				.build();
			return ResponseFactory.ok(this.gerarRelatorioEstatistico.gerarGraficoNais(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao/grafico/nip")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/pdf", "application/json"})
	public Response printGraficoNip(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioEstatisticasNipDTO> aitsList,
			@ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Tipo de gráfico") @QueryParam("tpGrafico") int tpGrafico,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
					.setDtInicialFiltro(dtInicial)
					.setDtFinalFiltro(dtFinal)
					.setTpConsulta(tpConsulta)
					.setTpGrafico(tpGrafico)
				.build();
			return ResponseFactory.ok(this.gerarRelatorioEstatistico.gerarGraficoNips(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/entrada/nips")
	@ApiOperation(value = "Retorna as entradas de NIP diárias")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Entradas de NIP encontradas", response = RelatorioEstatisticasNipDTO[].class),
	    @ApiResponse(code = 204, message = "Nenhuma NIP encontrada", response = ResponseBody.class),
	    @ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findNips(
	    @ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
	    @ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
	    @ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
	    @ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade) {
	    try {
	        SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
	                .setDtInicialMovimento(dtInicial)
	                .setDtFinalMovimento(dtFinal)
	                .setPeriodicidade(tpPeriodicidade)
                .build();
	        PagedResponse<RelatorioEstatisticasNipDTO> lista = this.relatorioEstatisticasService.findNips(searchCriterios);
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
	@Path("/julgamento/jari")
	@ApiOperation(value = "Retorna julgamento de JARI diários")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Julgamentos JARI encontrados", response = RelatorioEstatisticasNipDTO[].class),
	    @ApiResponse(code = 204, message = "Nenhum julgamento JARI encontrado", response = ResponseBody.class),
	    @ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findJulgamentoJariDiaria(
	    @ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
	    @ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
	    @ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
	    @ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade) {
	    try {
	        SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
	                .setDtInicialMovimento(dtInicial)
	                .setDtFinalMovimento(dtFinal)
	                .setPeriodicidade(tpPeriodicidade)
                .build();
	        PagedResponse<RelatorioEstatisticasNipDTO> lista = this.relatorioEstatisticasService.findJulgamentoJari(searchCriterios);
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
	@Path("/pagamento/nip")
	@ApiOperation(value = "Retorna dados para relatórios de pagamento NIP.")
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Pagamento NIP encontrados", response = RelatorioEstatisticasNipDTO[].class),
	    @ApiResponse(code = 204, message = "Nenhum pagamento NIP encontrado", response = ResponseBody.class),
	    @ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response findPagamentoNipDiario(
	    @ApiParam(value = "Data inicial") @QueryParam("dtInicial") String dtInicial,
	    @ApiParam(value = "Data final") @QueryParam("dtFinal") String dtFinal,
	    @ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
	    @ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade) {
	    try {
	        SearchCriterios searchCriterios = new RelatorioEstatisticasSearchBuilder()
	                .setDtInicialVencimento(dtInicial)
	                .setDtFinalVencimento(dtFinal)
	                .setPeriodicidadePagamento(tpPeriodicidade)
                .build();
	        PagedResponse<RelatorioEstatisticasNipDTO> lista = this.relatorioEstatisticasService.findPagamentoNip(searchCriterios);
	        return ResponseFactory.ok(lista);
	    } catch (BadRequestException e) {
	        this.managerLog.showLog(e);
	        return ResponseFactory.noContent(e.getMessage());
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseFactory.internalServerError(e.getMessage());
	    }
	}
}
