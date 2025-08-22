package com.tivic.manager.mob.ait.validacao;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
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

@Api(value = "Relatório de AITs", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/ait/pendente")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class ValidacaoAitPendenteController {
	
	IValidacaoAitPendente validacaoAitPendente;
	private ManagerLog managerLog;
	
	public ValidacaoAitPendenteController() throws Exception {	
		this.validacaoAitPendente = (IValidacaoAitPendente) BeansFactory.get(IValidacaoAitPendente.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/lista")
	@ApiOperation(
			value = "Retorna AITs a serem validados para listagem"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Listagem de AITs a serem validados encontrados.", response = RelatorioAitDTO[].class),
			@ApiResponse(code = 204, message = "Listagem de AITs a serem validados não encontrados.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response gerarRelatorioAits(
			@ApiParam(value = "Cd. AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Data da infração") @QueryParam("dtInfracao") String dtInfracao,
			@ApiParam(value = "Código do agente autuador") @QueryParam("cdAgente") @DefaultValue("-1") Integer cdAgente,
			@ApiParam(value = "Código do motivo cancelamento") @QueryParam("cdOcorrencia") @DefaultValue("-1") Integer cdOcorrencia,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page
			) {
		try {
			ValidacaoAitPendenteSearch validacaoAitPendenteSearch = new ValidacaoAitPendenteSearchBuilder()
					.setIdAit(idAit)
					.setDtInfracao(dtInfracao)
					.setCdAgente(cdAgente)
					.setCdOcorrencia(cdOcorrencia)
					.setPage(page)
					.setLimit(limit)
					.build();
			return ResponseFactory.ok(validacaoAitPendente.buscarAitsPendentes(validacaoAitPendenteSearch));	
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/valida")
	@ApiOperation(
		value = "Valida um AIT pendente"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response validarAit(@ApiParam(value = "aitPendenteDto") AitPendenteDTO aitPendenteDTO) {
		try {
			this.validacaoAitPendente.validarAit(aitPendenteDTO);
			return ResponseFactory.ok(aitPendenteDTO);
		} catch (BadRequestException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/invalida")
	@ApiOperation(
		value = "Invalida um AIT pendente"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = AitMovimento.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response invalidarAit(@ApiParam(value = "aitPendenteDto") AitPendenteDTO aitPendenteDTO) {
		try {
			this.validacaoAitPendente.invalidarAit(aitPendenteDTO);
			return ResponseFactory.ok(aitPendenteDTO);
		} catch (BadRequestException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
