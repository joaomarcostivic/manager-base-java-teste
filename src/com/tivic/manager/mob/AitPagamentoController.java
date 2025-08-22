package com.tivic.manager.mob;

import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/aits/pagamento")
@Produces(MediaType.APPLICATION_JSON)
public class AitPagamentoController {
	@POST
	@Path("/upload")
	@ApiOperation(
			value = "Registra as baixas de arquivo bancário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Baixas registradas"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response upload(@ApiParam(value = "Arquivo do Banco") ArquivoBancoDTO arquivoBanco,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			
			
			if(arquivoBanco.getArquivo().equals("")) {
				return ResponseFactory.internalServerError("No Content");
			}
			List<RetornoBancoDTO> pagamentos = AitPagamentoServices.upload(arquivoBanco, cdUsuario);
			return ResponseFactory.ok(pagamentos);
			
		} 
		catch (ValidacaoException ve) 
		{
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/save")
	@ApiOperation(
			value = "Registra um pagamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagamento registrado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response save(@ApiParam(value = "Ait Pagamento") AitPagamento aitPagamento,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			Result result = AitPagamentoServices.savePagamento(aitPagamento, cdUsuario);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((AitPagamento) result.getObjects().get("AITPAGAMENTO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/update/{id}/")
	@ApiOperation(
			value = "Registra um pagamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pagamento atualizado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "Ait Pagamento") AitPagamento aitPagamento,
			@ApiParam(value = "id do Movimento") @PathParam("id") int cdMovimento,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			Result result = AitPagamentoServices.savePagamento(aitPagamento, cdUsuario);
			AitMovimento movimento = AitMovimentoDAO.get(cdMovimento, aitPagamento.getCdAit());
			System.out.println("getDtPagamento = "+aitPagamento.getDtPagamento().toString());
			System.out.println("getDtMovimento = "+movimento.getDtMovimento().toString());
			if(movimento != null && aitPagamento.getDtPagamento().compareTo(movimento.getDtMovimento()) != 0) {
				movimento.setDtMovimento(aitPagamento.getDtPagamento());
				movimento.setCdUsuario(cdUsuario);
				AitMovimentoDAO.update(movimento);
			}
			
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((AitPagamento) result.getObjects().get("AITPAGAMENTO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/")
	@ApiOperation(
		value = "Fornece um pagamento dado o id indicado",
		notes = "Considere id = cdAit"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Pagamento encontrado", response = AitDTO.class),
		@ApiResponse(code = 204, message = "Não existe Pagamento com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) {
		try {	
			AitPagamento aitPagamento = AitPagamentoDAO.get(cdAit);
			
			if(aitPagamento==null)
				return ResponseFactory.noContent("Não existe pagamento com o id indicado.");
			
			return ResponseFactory.ok(aitPagamento);
			
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response getAll() {
		try {
			ResultSetMap rsm =  AitPagamentoServices.get();
			return ResponseFactory.ok(new ResultSetMapper<AitPagamento>(rsm, AitPagamento.class));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/report/periodo/")
	@ApiOperation(
		value = "Fornece uma lista de pagamentos dado um range de datas"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportPeriodo(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportPeriodo(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal));
			return ResponseFactory.ok(new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class));
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/data/")
	@ApiOperation(
		value = "Fornece uma lista de pagamentos para um dia especifico"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de pagamentos do dia", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportData(@ApiParam(value = "Data de pagamento") @QueryParam("dtPagamento") String dtPagamento) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportData(LocalDate.parse(dtPagamento));
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/tramitacao/")
	@ApiOperation(
		value = "Fornece uma lista de pagamentos em tramitação no período informado"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportTramitacao(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportTramitacao(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal));
			return ResponseFactory.ok(rsm);
//			return ResponseFactory.ok(new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class));
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}

	@GET
	@Path("/report/estado/")
	@ApiOperation(
		value = "Fornece uma lista de AITs em tramitação no estado no período"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de AITs em tramitação no estado no período", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem AITs em tramitação neste estado no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportEstado(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal, 
			@ApiParam(value = "Sigla do estado consulta") @QueryParam("sgUf") String sgUf) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportEstado(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal), sgUf);
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/vencidas/")
	@ApiOperation(
		value = "Fornece uma lista de AITs vencidos no período informado"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos vencidos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos vencidos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportVencidas(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportVencidas(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal));
			return ResponseFactory.ok(rsm);
//			return ResponseFactory.ok(new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class));
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/vencidas/estado/")
	@ApiOperation(
		value = "Fornece uma lista de AITs vencidos no estado no período"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de AITs vencidos no estado no período", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem AITs em tramitação neste estado no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportVencidasEstado(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal, 
			@ApiParam(value = "Sigla do estado consulta") @QueryParam("sgUf") String sgUf) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportVencidasEstado(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal), sgUf);
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/bancos/")
	@ApiOperation(
		value = "Fornece uma lista de AITs vencidos no período informado"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos vencidos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos vencidos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportBancos(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportBancos(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal));
			return ResponseFactory.ok(rsm);
//			return ResponseFactory.ok(new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class));
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@GET
	@Path("/report/bancos/detalhamento/")
	@ApiOperation(
		value = "Fornece uma lista de AITs vencidos no período informado"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista de dias com pagamentos vencidos encontrado", response = AitPagamentoReportDataDTO.class),
		@ApiResponse(code = 204, message = "Não existem dias com pagamentos vencidos no período informado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response reportBancosDetalhamento(@ApiParam(value = "Data inicial da consulta") @QueryParam("dtInicial") String dtInicial,
			@ApiParam(value = "Data final da consulta") @QueryParam("dtFinal") String dtFinal,
			@ApiParam(value = "Número do Banco") @QueryParam("nrBanco") String nrBanco) {
		try {				
			ResultSetMap rsm = AitPagamentoReportServices.reportBancosDetalhamento(LocalDate.parse(dtInicial), LocalDate.parse(dtFinal), nrBanco);
			return ResponseFactory.ok(rsm);
//			return ResponseFactory.ok(new ResultSetMapper<AitPagamentoReportDataDTO>(rsm, AitPagamentoReportDataDTO.class));
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
}
