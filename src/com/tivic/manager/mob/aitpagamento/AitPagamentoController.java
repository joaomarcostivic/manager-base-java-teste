package com.tivic.manager.mob.aitpagamento;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.AitPagamentoReportDataDTO;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.aitpagamento.builders.PagamentosSearchBuilder;
import com.tivic.manager.mob.restituicao.IGerarRelatorioRestituicao;
import com.tivic.manager.mob.restituicao.RestituicaoDTO;
import com.tivic.manager.mob.restituicao.builders.RestituicaoSearchBuilder;
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

@Api(value = "Ait Pagamento ", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")}) })
@Path("/v3/mob/aitpagamento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AitPagamentoController {
	private IAitPagamentoService aitPagamentoService;
	private IGerarRelatorioRestituicao gerarRelatorioRestituicao;
	private ManagerLog managerLog;
	
	public AitPagamentoController() throws Exception {
		this.aitPagamentoService = (IAitPagamentoService) BeansFactory.get(IAitPagamentoService.class);
		this.gerarRelatorioRestituicao = (IGerarRelatorioRestituicao) BeansFactory.get(IGerarRelatorioRestituicao.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Busca de pagamentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagamentos encontrados", response = AitPagamento[].class),
			@ApiResponse(code = 404, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(@ApiParam(value = "Código do registro") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
			searchCriterios.setQtLimite(limit);
			searchCriterios.setQtDeslocamento((limit*page)-limit);
			PagedResponse<AitPagamento> aitPagamentoList = this.aitPagamentoService.find(searchCriterios);
			return ResponseFactory.ok(aitPagamentoList);	
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdPagemento}/{cdUsuario}")
	@ApiOperation(value = "Altera a situação do pagamento para cancelado.")
	@ApiResponses(value= {
			@ApiResponse(code = 200, message = "Situação atualizada", response = AitPagamento.class),
			@ApiResponse(code = 204, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	}) 
	public Response cancelarPagamento(@ApiParam(value = "Código do pagamento") @PathParam("cdPagamento") int cdPagamento,
			@ApiParam(value = "Código do usuário") @PathParam("cdUsuario") int cdUsuario,
			@ApiParam(value = "Pagamento a ser atualizado") AitPagamento aitPagamento) {
		try {
			this.aitPagamentoService.cancelarPagamento(aitPagamento, cdUsuario);
			return ResponseFactory.ok(aitPagamento);
		} catch(NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/recebidos")
	@ApiOperation(value = "Busca para relatorio de pagamentos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagamentos encontrados", response = AitPagamento[].class),
			@ApiResponse(code = 404, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findRelatorio(@ApiParam(value = "Data de pagamento") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data de pagamento") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosGreaterDate("A.dt_pagamento", dtInicial, dtInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_pagamento", dtFinal, dtFinal != null);
			searchCriterios.setQtLimite(limit);
			searchCriterios.setQtDeslocamento((limit*page)-limit);
			PagedResponse<AitPagamentoRecebidoDTO> aitPagamentoList = this.aitPagamentoService.findValoresRecebidos(searchCriterios);
			return ResponseFactory.ok(aitPagamentoList);	
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/report/restituicao")
	@ApiOperation(
		value = "Fornece uma lista de AITs em que há restituição."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response reportDuplicadosPeriodo(
			@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new RestituicaoSearchBuilder()
					.setDtInicialPagamento(dtInicial)
					.setDtFinalPagamento(dtFinal)
					.setDtInicialMovimento(dtInicial)
					.setDtFinalMovimento(dtFinal)
					.setTpConsulta(tpConsulta)
					.build();
			PagedResponse<RestituicaoDTO> restituicaoList = this.aitPagamentoService.findRestituicao(searchCriterios);
			return ResponseFactory.ok(restituicaoList);	
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/restituicao/impressao")
	@ApiOperation(value = "Imprime o relatório de AITs filtrados na tela", notes = "Reimpressão de AIT")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response printRelatorio(
			@ApiParam(value = "Lista de AITs buscados na tela") List<RestituicaoDTO> aitsList) throws ValidacaoException {
		try {
			return ResponseFactory.ok(this.gerarRelatorioRestituicao.gerar(aitsList));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/report/periodo")
	@ApiOperation(
		value = "Fornece uma lista de pagamentos dada uma faixa de datas"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de períodos com pagamentos encontrado", response = AitPagamentoRecebidoDTO.class),
		@ApiResponse(code = 204, message = "Não existem pagamentos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response reportPeriodo(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws Exception {
		try {
			SearchCriterios searchCriterios = new PagamentosSearchBuilder()
					.setDtInicialPagamento(dtInicial)
					.setDtFinalPagamento(dtFinal)
					.setPeriodicidade(tpPeriodicidade)
					.setDtInicialInfracao(dtInicial)
					.setDtFinalInfracao(dtFinal)
					.setTpConsulta(tpConsulta)
				.build();
			PagedResponse<AitPagamentoRecebidoDTO> pagamentosList = this.aitPagamentoService.reportPeriodo(searchCriterios);
			return ResponseFactory.ok(pagamentosList);	
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/detalhamento")
	@ApiOperation(
			value = "Fornece uma lista de pagamentos dada uma faixa de datas"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de períodos com pagamentos encontrado", response = RelatorioAitDTO.class),
			@ApiResponse(code = 204, message = "Não existem pagamentos no período informado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response detalhamento(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Sigla da UF do veículo") @QueryParam("sgUfVeiculo") String sgUfVeiculo,
			@ApiParam(value = "Banco arrecadador") @QueryParam("nrBanco") String nrBanco,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws Exception {
		try {
			SearchCriterios searchCriterios = new PagamentosSearchBuilder()
					.setTpConsulta(tpConsulta)
					.setDtInicialInfracao(dtInicial)
					.setDtFinalInfracao(dtFinal)
					.setSgUfVeiculo(sgUfVeiculo)
					.setNrBanco(nrBanco, tpConsulta)
				.build();
			PagedResponse<RelatorioAitDTO> pagamentosList = this.aitPagamentoService.detalhamento(searchCriterios);
			return ResponseFactory.ok(pagamentosList);	
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/detalhamento/periodo")
	@ApiOperation(
			value = "Fornece uma lista de AITs dado um período"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista de AITs encontrada", response = RelatorioAitDTO.class),
			@ApiResponse(code = 204, message = "Não existem AITs no período informado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response detalhamentoPeriodo(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Data do pagamento selecionado") @QueryParam("dtPagamento") String dtPagamento,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta,
			@ApiParam(value = "Tipo de periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade) throws Exception {
		try {
			SearchCriterios searchCriterios = new PagamentosSearchBuilder()
					.setTpConsulta(tpConsulta)
					.setDtPagamentoDetalhamento(tpPeriodicidade, dtPagamento)
					.setDtInicialPagamento(dtInicial)
					.setDtFinalPagamento(dtFinal)
				.build();
			PagedResponse<RelatorioAitDTO> pagamentosList = this.aitPagamentoService.detalhamento(searchCriterios);
			return ResponseFactory.ok(pagamentosList);	
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/report/impressao")
	@ApiOperation(value = "Imprime o relatório de items filtrados na tela", notes = "Impressão de relatório")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não ha relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados inválidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Produces({"application/pdf", "application/json"})
	public Response imprimirRelatorio(
			@ApiParam(value = "Lista de AITs buscados na tela") List<AitPagamentoRecebidoDTO> aitsList,
			@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Periodicidade") @QueryParam("tpPeriodicidade") int tpPeriodicidade,
			@ApiParam(value = "Tipo de consulta") @QueryParam("tpConsulta") int tpConsulta) throws ValidacaoException {
		try {
			SearchCriterios searchCriterios = new PagamentosSearchBuilder()
					.setTpConsulta(tpConsulta)
					.setTpPeriodicidade(tpPeriodicidade)
					.setDtInicialInfracao(dtInicial)
					.setDtFinalInfracao(dtFinal)
				.build();
			return ResponseFactory.ok(this.aitPagamentoService.gerarRelatorio(aitsList, searchCriterios));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
