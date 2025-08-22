package com.tivic.manager.mob.lote.impressao;

import java.util.List;

import javax.ws.rs.DefaultValue;
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

import org.json.JSONObject;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearch;
import com.tivic.manager.mob.lote.impressao.loteimpressaosearch.LoteImpressaoSearchBuilder;
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
import sol.util.Result;

@Api(value = "LoteMovimentos", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/loteimpressao")
@Produces(MediaType.APPLICATION_JSON)
public class LoteNotificacaoController {
	ILoteNotificacaoService loteNotificacaoService;
	private ManagerLog managerLog;

	public LoteNotificacaoController() throws Exception {
		loteNotificacaoService = (LoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@GET
	@Path("/nai")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response retrieveAllAitsNAI() {	
		try {
			
			List<Ait> aitList = loteNotificacaoService.getAllAitsEmitirNAI();
			if(aitList.isEmpty()) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			return ResponseFactory.ok(aitList);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/nai")
	@ApiOperation(
			value = "Gera os movimentos de NAI"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de NAIs iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "AITs canditadas não encontrdas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarMovimentosNais(List<Ait> aitList, @ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {	
		try {
			return ResponseFactory.ok(loteNotificacaoService.gerarMovimentoNotificacaoNaiLote(aitList, cdUsuario));
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/nip")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NIPS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response retrieveAllAitsNIP() {	
		try {
			List<Ait> aitList = loteNotificacaoService.getAllAitsEmitirNIP();
			if(aitList.isEmpty()) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			return ResponseFactory.ok(aitList);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/nip")
	@ApiOperation(
			value = "Gera os movimentos de NIP"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de NAIs iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "AITs canditadas não encontrdas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarMovimentosNips(List<Ait> aitList, @ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {	
		try {
			return ResponseFactory.ok(loteNotificacaoService.gerarMovimentoNotificacaoNipLote(aitList, cdUsuario));
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote")
	@ApiOperation(
			value = "Busca os AITs que podem fazer parte de um lote"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response buscarAitsParaLoteImpressao(
			@ApiParam(value = "Quantiade de Autos no lote") @QueryParam("quantidadeAit") int quantidadeAit,
			@ApiParam(value = "Tipo do lote a ser gerado") @QueryParam("tipoLote") int tipoLote,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(loteNotificacaoService.buscarAitsParaLoteImpressao( tipoLote, searchCriterios));
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/quantidade/{tipoLote}")
	@ApiOperation(
			value = "Busca quantidade de AITs para Lote de Impressão"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response buscarQuantidadeAitsParaLoteImpressao(
			@ApiParam(value = "Quantiade de Autos no lote") @DefaultValue("10000") @QueryParam("quantidadeAit") int quantidadeAit,
			@ApiParam(value = "Tipo do lote a ser gerado") @PathParam("tipoLote") int tipoLote
			) {	
		try {
			JSONObject json = new JSONObject();
			json.put("quantidade", loteNotificacaoService.buscarQuantidadeAitsParaLoteImpressao(quantidadeAit, tipoLote).size());
			return ResponseFactory.ok(json);
		} 
		catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} 
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/lote")
	@ApiOperation(
			value = "Gera lote de impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de Lote iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarNovoLoteNotificacao(
			@ApiParam(value = "Lista de AITs selecionadas pelo usuario que farão parte do lote") LoteImpressao loteImpressao
			) {	
		try {
			
			return ResponseFactory.ok(loteNotificacaoService.gerarLoteNotificacao(loteImpressao));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	
	}
	
	@POST
	@Path("/lote/documentos")
	@ApiOperation(
			value = "Gera os documentos do lote de impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documentos do lote gerado", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarDocumentosLote(
			@ApiParam(value = "Código do lote a ser gerado") @QueryParam("cdLoteImpressao") int cdLoteImpressao,
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdUsuario") int cdUsuario
			) {	
		try {
			loteNotificacaoService.iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario);
			return ResponseFactory.ok("Geração de lote iniciada");
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/lote/documentos/reinicia")
	@ApiOperation(
			value = "Reiniciar a geração dos documentos do lote de impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documentos do lote gerado", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response reiniciarGeracaoDocumentosLote(
			@ApiParam(value = "Código do lote a ser gerado") @QueryParam("cdLoteImpressao") int cdLoteImpressao,
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdUsuario") int cdUsuario
			) {	
		try {
			return ResponseFactory.ok(loteNotificacaoService.reiniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario));
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/status")
	@ApiOperation(
			value = "Retorna o status de geração de documentos do lote de notificação"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Status de geração de documentos do lote de notificação", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Lotes de Notificação não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void retrieveStatus(
			@Context SseEventSink sseEventSink,
			@Context Sse sse,
			@ApiParam(value = "Código do lote") @QueryParam("cdLoteImpressao") int cdLoteImpressao) {
		try {
			loteNotificacaoService.getStatusGeracaoDocumentos(sseEventSink, sse, cdLoteImpressao);		
		} catch (Exception e) {
			managerLog.showLog(e);
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão encontrado", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAll(
			@ApiParam(value = "Id. Lote Impressao") @QueryParam("idLoteImpressao") String idLoteImpressao,
			@ApiParam(value = "Dt. criação") @QueryParam("data") String dtCriacao,
			@ApiParam(value = "Id. Do AIT") @QueryParam("nrAit") String nrAit,
			@ApiParam(value = "Situação do lote") @QueryParam("stLoteImpressao") @DefaultValue("-1") Integer stLoteImpressao,
			@ApiParam(value = "Número de etiqueta do AIT no lote") @QueryParam("nrEtiqueta") @DefaultValue("-1") Integer nrEtiqueta,
			@ApiParam(value = "Tipo do lote") @QueryParam("tipo") @DefaultValue("-1") Integer tpLoteImpressao,
			@ApiParam(value = "Tipo do documento contido no lote") @QueryParam("documento") @DefaultValue("-1") Integer tpDocumento,
			@ApiParam(value = "Tipo de destino do lote após impresso") @QueryParam("destino") @DefaultValue("-1") Integer tpDestino,
			@ApiParam(value = "Tipo do transporte para entrega do lote") @QueryParam("transporte") @DefaultValue("-1") Integer tpTransporte,
			@ApiParam(value = "Usuario que gerou o lote") @QueryParam("usuario") @DefaultValue("-1") Integer cdUsuario,
			@QueryParam("page") Integer nrPagina,
			@QueryParam("limit") Integer nrLimite
	) {
		try {
			
			LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
					.setIdLoteImpressao(idLoteImpressao)
					.setIdAit(nrAit)
					.setDtCriacao(dtCriacao)
					.setStLoteImpressao(stLoteImpressao)
					.setNrEtiqueta(nrEtiqueta)
					.setTpLoteImpressao(tpLoteImpressao)
					.setTpDocumento(tpDocumento)
					.setTpDestino(tpDestino)
					.setTpTransporte(tpTransporte)
					.setCdUsuario(cdUsuario)
					.setPage(nrPagina)
					.setLimit(nrLimite)
					.build();
			
			return ResponseFactory.ok(loteNotificacaoService.buscarLotes(loteImpressaoSearch));
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/aits")
	@ApiOperation(
			value = "Retorna AITs do Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs do Lote encontrados.", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "AITs do Lote não encontrados.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAllAits(
			@ApiParam(value = "Cd. Lote de Impressao") @QueryParam("cdLoteImpressaoAit") int cdLoteImpressaoAit,
			@ApiParam(value = "Nr. AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Data da Autuação") @QueryParam("data") String dtInfracao,
			@ApiParam(value = "Placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Renavan") @QueryParam("nrRenavan") String nrRenavan,
			@ApiParam(value = "Etiqueta") @QueryParam("nrEtiqueta") @DefaultValue("-1") int nrEtiqueta,
	        @QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite)
	{
		
		try {
			LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
					.setCdLoteImpressaoAit(cdLoteImpressaoAit)
					.setIdAit(idAit)
					.setNrEtiqueta(nrEtiqueta)
					.setDtInfracao(dtInfracao)
					.setNrRenavan(nrRenavan)
					.setNrPlaca(nrPlaca)
					.setPage(nrPagina)
					.setLimit(nrLimite)
					.build();
			
			return ResponseFactory.ok(loteNotificacaoService.buscarLotesAits(loteImpressaoSearch));	
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/imprimir")
	@ApiOperation(
			value = "Inicia o processo de impressão do lote"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response imprimirLoteNotificacao(
			@ApiParam(value = "Código do Lote de Impressão") @QueryParam("cdLoteImpressao") int cdLoteImpressao) {
		try {
			byte[] loteNotificacao = loteNotificacaoService.imprimirLoteNotificacao(cdLoteImpressao);
			return ResponseFactory.ok(loteNotificacao);
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/loteviaunica/nai")
	@ApiOperation(
			value = "Inicia o processo de impressão de um lote de via única para NAI"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarLoteNotificacaoNaiViaUnica(
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			return ResponseFactory.ok(loteNotificacaoService.gerarLoteNotificacaoNaiViaUnica(cdAit, cdUsuario));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/loteviaunica/nip")
	@ApiOperation(
			value = "Inicia o processo de impressão de um lote de via única para NIP"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarLoteNotificacaoNipViaUnica(
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			return ResponseFactory.ok(loteNotificacaoService.gerarLoteNotificacaoNipViaUnica(cdAit, cdUsuario));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLoteImpressao}")
	@ApiOperation(
			value = "Retorna um Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de Impressão encontrado", response = LoteImpressao.class),
			@ApiResponse(code = 204, message = "Órgão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response get(@ApiParam(value = "Código do Lote de Impressão") @PathParam("cdLoteImpressao") @DefaultValue("-1") int cdLoteImpressao) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao, cdLoteImpressao > -1);
			
			
			LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
					.setCdLoteImpressao(cdLoteImpressao)
					.build();
			
			return ResponseFactory.ok(loteNotificacaoService.buscarLote(loteImpressaoSearch));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/nip/comjuros")
	@ApiOperation(
			value = "Calcula e gera a NIP com juros"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response gerarNotificacaoNipComJuros(
			@ApiParam(value = "Código do AIT") @QueryParam("cdAit") int cdAit) {
		try {
			Boolean printPortal = false;
			return ResponseFactory.ok(loteNotificacaoService.gerarNotificacaoNipComJuros(cdAit, printPortal));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/impressoes/nai/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNai(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteNotificacaoService.gerarLoteNotificacaoNaiViaUnica(cdAit, cdUsuario));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (AitReportErrorException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/impressoes/nip/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NIP", notes = "impressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNip(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) throws ValidacaoException {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteNotificacaoService.gerarLoteNotificacaoNipViaUnica(cdAit, cdUsuario).getArquivo());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/impressoes/nip/nic")
	@ApiOperation(value = "Gera arquivos de impressao de NIP-NIC", notes = "impressão de nip-nic")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNipNic(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) throws ValidacaoException {
		try {
			return ResponseFactory.ok(loteNotificacaoService.gerarLoteNotificacaoNipViaUnica(cdAit, cdUsuario));
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/impressoes/nip")
	@ApiOperation(value = "Gera arquivos de impressao de NIP", notes = "impressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha relatorio com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNipComJuros(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException {
		try {
			Boolean printPortal = false;
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteNotificacaoService.gerarNotificacaoNipComJuros(cdAit, printPortal));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/impressoes/nic-nip")
	@ApiOperation(value = "Gera arquivos de impressao de NIP", notes = "impressão de nip")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNicNipComJuros(@ApiParam(value = "id do AIT") @PathParam("id") int cdAit) throws ValidacaoException {
		try {
			Boolean printPortal = false;
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteNotificacaoService.gerarNotificacaoNicNipComJuros(cdAit, printPortal));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
	
	@GET
	@Path("/{cdLoteNotificacao}/download-arquivo/{tpArquivo}")
	@ApiOperation(
			value = "Faz o download do arquivo de previsão postagem"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo baixado"),
			@ApiResponse(code = 204, message = "Nao ha arquivo para o lote indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response downloadDocumentoPublicacao(
			@ApiParam(value = "Código do documento") @PathParam("cdLoteNotificacao") int cdLoteNotificacao,
			@ApiParam(value = "Código do documento") @PathParam("tpArquivo") int tpArquivo
		) {
		try {	
			Arquivo arquivo = this.loteNotificacaoService.pegarArquivoLote(cdLoteNotificacao, tpArquivo);
			return ResponseFactory.ok(arquivo);
		}
		catch (NoContentException e) {
				return ResponseFactory.noContent(e.getMessage());
		} 
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
 
}
