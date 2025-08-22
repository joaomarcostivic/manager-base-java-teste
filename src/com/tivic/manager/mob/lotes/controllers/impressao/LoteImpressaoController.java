package com.tivic.manager.mob.lotes.controllers.impressao;

import java.util.List;

import javax.ws.rs.DELETE;
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

import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearch;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoSearchBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.AitDTO;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.model.arquivo.Arquivo;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressaoAit;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
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

@Api(value = "LoteImpressao", tags = { "mob" }, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/lote/impressao")
@Produces(MediaType.APPLICATION_JSON)
public class LoteImpressaoController {
	private ManagerLog managerLog;
	private ILoteImpressaoService loteImpressaoService;

	public LoteImpressaoController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
	}

	@GET
	@Path("/aits")
	@ApiOperation(value = "Busca os AITs que podem fazer parte de um lote")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso.", response = AitDTO.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response buscarAitsParaLoteImpressao(
			@ApiParam(value = "Tipo do lote a ser gerado") @QueryParam("tipoLote") int tipoLote) {
		try {
			return ResponseFactory.ok(loteImpressaoService.buscarAitsParaLoteImpressao(tipoLote));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/lote")
	@ApiOperation(value = "Gera lote de impressão")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lote criado com sucesso.", response = CreateLoteImpressaoDTO.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response gerarNovoLoteNotificacao(
			@ApiParam(value = "Lista de AITs selecionadas pelo usuario que farão parte do lote") CreateLoteImpressaoDTO loteImpressao) {
		try {
			return ResponseFactory.ok(loteImpressaoService.insert(loteImpressao));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@DELETE
	@Path("/delete/{cdLoteImpressao}")
	@ApiOperation(value = "Exclusão de lote de impressão por completo.")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Lote excluído", response = LoteImpressao.class),
			@ApiResponse(code = 400, message = "Erro co excluir lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response delete(
			@ApiParam(value = "Código do lote de impressão") @PathParam("cdLote") int cdLoteImpressao) {
		try {
			this.loteImpressaoService.delete(cdLoteImpressao);
			return ResponseFactory.noContent();
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("/viaunica")
	@ApiOperation(value = "Gera lote de impressão de via única")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de Lote iniciada.", response = CreateLoteImpressaoDTO.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response gerarNovoLoteNotificacaoViaUnica(
			@ApiParam(value = "DTO que contem o AIT que fará parte do lote") CreateLoteImpressaoDTO loteImpressao) {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteImpressaoService.insertViaUnica(loteImpressao));
			return ResponseFactory.ok(response);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/exclui/aits")
	@ApiOperation(
			value = "Excluir AITs e reiniciar geração de documentos do lote"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Documentos do lote gerado", response = LoteImpressaoAit.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrado para gerar lote", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response excluirAitsLote(
			@ApiParam(value = "Lista de AITs para excluir") List<LoteImpressaoAit> loteImpressaoAitList,
			@ApiParam(value = "Usuário que fez o procedimento") @QueryParam("cdUsuario") int cdUsuario
			) {	
		try {
			return ResponseFactory.ok(loteImpressaoService.excluirAitsLote(loteImpressaoAitList, cdUsuario));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/regera/documentos")
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
			return ResponseFactory.ok(loteImpressaoService.reiniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
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
			loteImpressaoService.iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario);
			return ResponseFactory.ok("Geração de lote iniciada");
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
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
			loteImpressaoService.getStatusGeracaoDocumentos(sseEventSink, sse, cdLoteImpressao);		
		} catch (Exception e) {
			managerLog.showLog(e);
		}
	}
	
	@GET
	@Path("/listagem")
	@ApiOperation(
			value = "Retorna Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão encontrados", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAll(
			@ApiParam(value = "Id. Lote Impressao") @QueryParam("idLoteImpressao") String idLote,
			@ApiParam(value = "Dt. criação") @QueryParam("data") String dtCriacao,
			@ApiParam(value = "Id. Do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Situação do lote") @QueryParam("stLote") @DefaultValue("-1") Integer stLote,
			@ApiParam(value = "Tipo do lote") @QueryParam("tpImpressao") @DefaultValue("-1") Integer tpImpressao,
			@ApiParam(value = "Usuario que gerou o lote") @QueryParam("usuario") @DefaultValue("-1") Integer cdUsuario,
			@QueryParam("page") Integer nrPagina,
			@QueryParam("limit") Integer nrLimite
	) {
		try {
			
			LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
					.setIdLote(idLote)
					.setDtCriacao(dtCriacao)
					.setIdAit(idAit)
					.setStLote(stLote)
					.setTpImpressao(tpImpressao)
					.setCdUsuario(cdUsuario)
					.setPage(nrPagina)
					.setLimit(nrLimite)
					.build();
			
			return ResponseFactory.ok(loteImpressaoService.buscarLotes(loteImpressaoSearch));
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
	public Response imprimirLoteImpressao(
			@ApiParam(value = "Código do Lote de Impressão") @QueryParam("cdLoteImpressao") int cdLoteImpressao) {
		try {
			byte[] loteNotificacao = loteImpressaoService.imprimirLoteImpressao(cdLoteImpressao);
			return ResponseFactory.ok(loteNotificacao);
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/aits/listagem")
	@ApiOperation(
			value = "Retorna AITs do Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs do Lote encontrados.", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "AITs do Lote não encontrados.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAllAits(
			@ApiParam(value = "Cd. Lote de Impressao") @QueryParam("cdLoteImpressaoAit") int cdLoteImpressao,
			@ApiParam(value = "Nr. AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Data da Autuação") @QueryParam("data") String dtInfracao,
			@ApiParam(value = "Placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Renavan") @QueryParam("nrRenavan") String nrRenavan,
	        @QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite)
	{
		
		try {
			LoteImpressaoSearch loteImpressaoSearch = new LoteImpressaoSearchBuilder()
					.setCdLoteImpressao(cdLoteImpressao)
					.setIdAit(idAit)
					.setDtInfracao(dtInfracao)
					.setNrRenavan(nrRenavan)
					.setNrPlaca(nrPlaca)
					.setPage(nrPagina)
					.setLimit(nrLimite)
					.build();
			
			return ResponseFactory.ok(loteImpressaoService.buscarLotesAits(loteImpressaoSearch));	
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLoteImpressao}")
	@ApiOperation(
			value = "Retorna um Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de Impressão encontrado", response = CreateLoteImpressaoDTO.class),
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
			
			return ResponseFactory.ok(loteImpressaoService.buscarLote(loteImpressaoSearch));
		} catch (NoContentException ne) {
			managerLog.showLog(ne);
			return ResponseFactory.noContent(ne.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdAit}/impressoes/nai/{idRelatorio}")
	@ApiOperation(value = "Gera arquivos de impressao de NAI", notes = "impressão de nai")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Não há relatório com o id indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNai(@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "id do relatorio") @PathParam("idRelatorio") String idReport,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", loteImpressaoService.gerarLoteNotificacaoNaiViaUnica(cdAit, cdUsuario));
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			managerLog.showLog(ve);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
			response.put("blob", loteImpressaoService.gerarLoteNotificacaoNipViaUnica(cdAit, cdUsuario).getArquivo());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
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
			response.put("blob", loteImpressaoService.gerarNotificacaoNipComJuros(cdAit, printPortal));
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
			response.put("blob", loteImpressaoService.gerarNotificacaoNicNipComJuros(cdAit, printPortal));
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
			Arquivo arquivo = this.loteImpressaoService.pegarArquivoLote(cdLoteNotificacao, tpArquivo);
			return ResponseFactory.ok(arquivo);
		}
		catch (NoContentException e) {
				return ResponseFactory.noContent(e.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
