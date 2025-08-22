package com.tivic.manager.mob.listagempagamentos;

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

import com.tivic.manager.mob.listagempagamentos.builders.ListagemPagamentosSearchBuilder;
import com.tivic.manager.util.pagination.PagedResponse;
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

@Api(value = "Listagem de pagamento de AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")}) })
@Path("/v3/mob/relatorios/pagamentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListagemPagamentosController {
	private ManagerLog managerLog;
	private IListagemPagamentosService listagemPagamentosService;
	private IGeraRelatorioListagemPagamentos geraRelatorioListagemPagamentos;
	
	public ListagemPagamentosController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.listagemPagamentosService = (IListagemPagamentosService) BeansFactory.get(IListagemPagamentosService.class);
		this.geraRelatorioListagemPagamentos = (IGeraRelatorioListagemPagamentos) BeansFactory.get(IGeraRelatorioListagemPagamentos.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Busca de pagamentos para relatório")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagamentos encontrados", response = RelatorioPagamentoDTO[].class),
			@ApiResponse(code = 404, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(@ApiParam(value = "Situação do pagamento") @QueryParam("stPagamento") Integer stPagamento,
			@ApiParam(value = "Tipo de arrecadação") @QueryParam("tpArrecadacao") Integer tpArrecadacao,
			@ApiParam(value = "Data inicial de pagamento") @QueryParam("dtInicialPagamento") String dtInicialPagamento,
			@ApiParam(value = "Data final de pagamento") @QueryParam("dtFinalPagamento") String dtFinalPagamento,
			@ApiParam(value = "Data inicial de crédito") @QueryParam("dtInicialCredito") String dtInicialCredito,
			@ApiParam(value = "Data final de crédito") @QueryParam("dtFinalCredito") String dtFinalCredito,
			@ApiParam(value = "Infração") @QueryParam("cdInfracao") Integer cdInfracao,
			@ApiParam(value = "Banco") @QueryParam("cdBanco") Integer cdBanco,
			@ApiParam(value = "Forma de pagamento") @QueryParam("tpPagamento") Integer tpPagamento,
			@ApiParam(value = "Sigla da UF de pagamento") @QueryParam("sgUfPagamento") String sgUfPagamento,
			@ApiParam(value = "Tipo de forma de pagamento") @QueryParam("tpFormaPagamento") Integer tpFormaPagamento,
			@ApiParam(value = "Possui desconto?") @QueryParam("tpCondicionalidade") Integer tpCondicionalidade) throws Exception {
		try {
			SearchCriterios searchCriterios = new ListagemPagamentosSearchBuilder()
					.setStPagamento(stPagamento)
					.setTpArrecadacao(tpArrecadacao)
					.setDtInicialPagamento(dtInicialPagamento)
					.setDtFinalPagamento(dtFinalPagamento)
					.setDtInicialCredito(dtInicialCredito)
					.setDtFinalCredito(dtFinalCredito)
					.setCdInfracao(cdInfracao)
					.setCdBanco(cdBanco)
					.setTpPagamento(tpPagamento)
					.setSgUfPagamento(sgUfPagamento)
					.setTpCondicionalidade(tpCondicionalidade)
					.setTpFormaPagamento(tpFormaPagamento)
				.build();
			PagedResponse<RelatorioPagamentoDTO> aitPagamentoList = this.listagemPagamentosService.find(searchCriterios);
			return ResponseFactory.ok(aitPagamentoList);	
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao")
	@ApiOperation(value = "Imprime o relatório de pagamentos filtrados na tela")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há relatório com o ID indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorio(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioPagamentoDTO> list) throws ValidacaoException {
		try {
			return ResponseFactory.ok(this.geraRelatorioListagemPagamentos.gerar(list));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/impressao/grafico")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	@Produces({"application/pdf", "application/json"})
	public Response printGrafico(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RelatorioPagamentoDTO> aitsList,
			@ApiParam(value = "Situação do pagamento") @QueryParam("stPagamento") Integer stPagamento,
			@ApiParam(value = "Tipo de arrecadação") @QueryParam("tpArrecadacao") Integer tpArrecadacao,
			@ApiParam(value = "Data inicial de pagamento") @QueryParam("dtInicialPagamento") String dtInicialPagamento,
			@ApiParam(value = "Data final de pagamento") @QueryParam("dtFinalPagamento") String dtFinalPagamento,
			@ApiParam(value = "Data inicial de crédito") @QueryParam("dtInicialCredito") String dtInicialCredito,
			@ApiParam(value = "Data final de crédito") @QueryParam("dtFinalCredito") String dtFinalCredito,
			@ApiParam(value = "Infração") @QueryParam("cdInfracao") Integer cdInfracao,
			@ApiParam(value = "Banco") @QueryParam("cdBanco") Integer cdBanco,
			@ApiParam(value = "Forma de pagamento") @QueryParam("tpPagamento") Integer tpPagamento,
			@ApiParam(value = "Sigla da UF de pagamento") @QueryParam("sgUfPagamento") String sgUfPagamento,
			@ApiParam(value = "Tipo de forma de pagamento") @QueryParam("tpFormaPagamento") Integer tpFormaPagamento,
			@ApiParam(value = "Possui desconto?") @QueryParam("tpCondicionalidade") Integer tpCondicionalidade,
			@ApiParam(value = "Tipo de gráfico gerado") @QueryParam("tpGrafico") Integer tpGrafico,
			@ApiParam(value = "Campo analisado ao gerar o gráfico") @QueryParam("campoAnalisado") String campoAnalisado) throws Exception {
		try {
			SearchCriterios searchCriterios = new ListagemPagamentosSearchBuilder()
					.setStPagamento(stPagamento)
					.setTpArrecadacao(tpArrecadacao)
					.setDtInicialPagamento(dtInicialPagamento)
					.setDtFinalPagamento(dtFinalPagamento)
					.setDtInicialCredito(dtInicialCredito)
					.setDtFinalCredito(dtFinalCredito)
					.setCdInfracao(cdInfracao)
					.setCdBanco(cdBanco)
					.setTpPagamento(tpPagamento)
					.setSgUfPagamento(sgUfPagamento)
					.setTpCondicionalidade(tpCondicionalidade)
					.setTpFormaPagamento(tpFormaPagamento)
					.setTpGrafico(tpGrafico)
					.setCampoAnalisado(campoAnalisado)
				.build();
			return ResponseFactory.ok(this.geraRelatorioListagemPagamentos.gerarGrafico(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
