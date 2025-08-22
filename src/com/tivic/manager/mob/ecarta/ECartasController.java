package com.tivic.manager.mob.ecarta;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.ecarta.dtos.ListaArquivoRetornoCorreioDTO;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
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
import sol.util.Result;

@Api(value = "eCartas", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/mob/ecartas")
@Produces(MediaType.APPLICATION_JSON)
public class ECartasController {
	private IEcartasService ecartasService;
	private ManagerLog managerLog;

	public ECartasController() throws Exception {
		this.ecartasService = (IEcartasService) BeansFactory.get(IEcartasService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@POST
	@Path("/{id}/gerar")
	@ApiOperation(value = "Inicia o processo de geração de documentos do lote")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de documentos iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lote de impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response gerarDocumentosLote(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			return ResponseFactory.ok(ecartasService.iniciarGeracaoArquivoServicoEDI(cdLoteImpressao, cdUsuario));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/lote/status")
	@ApiOperation(value = "Retorna o status de geração de documentos do lote de notificação")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Status de geração de documentos do lote de notificação", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Lotes de Notificação não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void retrieveStatus(@Context SseEventSink sseEventSink, @Context Sse sse,
			@ApiParam(value = "Código do lote") @QueryParam("cdLoteImpressao") int cdLoteImpressao) {
		try {
			ecartasService.getStatusGeracaoDocumentos(sseEventSink, sse, cdLoteImpressao);
		} catch (Exception e) {
			managerLog.showLog(e);
		}
	}

	@GET
	@Path("/{id}/download-arquivo")
	@ApiOperation(value = "Faz o download do arquivo de serviço EDI")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo baixado"),
			@ApiResponse(code = 204, message = "Nao ha arquivo para o lote indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	public Response downloadDocumentoPublicacao(
			@ApiParam(value = "Código do documento") @PathParam("id") int cdLoteNotificacao) {
		try {
			return ResponseFactory.ok(ecartasService.pegarArquivoLote(cdLoteNotificacao));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/processar-arquivo-retorno/{id}")
	@ApiOperation(value = "Recebe arquivo de retorno gerado pelos correios")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo de retorno processado com sucesso", response = Result.class),
			@ApiResponse(code = 204, message = "Arquivo invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response receberArquivoCorreios(@ApiParam(value = "Código do lote") @PathParam("id") int cdLoteImpressao,
			@ApiParam(value = "Lista de arquivos de retorno") ListaArquivoRetornoCorreioDTO listaArquivoRetornoCorreio,
			@ApiParam(value = "Usuário que solicitou o processamento dos arquivos") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			return ResponseFactory.ok(ecartasService.processarArquivo(listaArquivoRetornoCorreio, cdLoteImpressao, cdUsuario));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/lote/{id}/confimar-producao")
	@ApiOperation(value = "Confirmação da produção do lote")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Confirmação da produção do lote realizada com sucesso", response = Result.class),
			@ApiResponse(code = 204, message = "Lote invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response confirmarProducao(@ApiParam(value = "Código do lote") @PathParam("id") int cdLoteImpressao,
			@ApiParam(value = "Usuário que solicitou a confirmação de produção do lote") @QueryParam("cdUsuario") int cdUsuario){
		try {
			return ResponseFactory.ok(ecartasService.confirmarProducao(cdLoteImpressao, cdUsuario));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/lote/{id}/rejeitar-producao")
	@ApiOperation(value = "Rejeição da produção do lote")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Rejeição da produção do lote realizada com sucesso", response = Result.class),
			@ApiResponse(code = 204, message = "Lote invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response rejeitarProducao(@ApiParam(value = "Código do lote") @PathParam("id") int cdLoteImpressao) {
		try {
			return ResponseFactory.ok(ecartasService.rejeitarProducao(cdLoteImpressao));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
