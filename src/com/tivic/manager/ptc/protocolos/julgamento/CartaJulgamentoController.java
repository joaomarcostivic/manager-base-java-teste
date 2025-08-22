package com.tivic.manager.ptc.protocolos.julgamento;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoStatusBuilder;
import com.tivic.manager.mob.lotes.dto.impressao.CreateLoteImpressaoDTO;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
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
import sol.util.Result;

@Api(value = "Carta de Julgamento", tags = {"ptc"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/ptc/protocolos/julgamento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)


public class CartaJulgamentoController {

	 ICartaJulgamentoService cartaJulgamentoService;
	
	public CartaJulgamentoController() throws Exception {	
		cartaJulgamentoService = (ICartaJulgamentoService) BeansFactory.get(ICartaJulgamentoService.class);
	}
	
	@POST
	@Path("/lote/impressao")
	@ApiOperation(
			value = "Gera Lote de Impressão para Carta de Julgamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de Lote iniciada.", response = CreateLoteImpressaoDTO.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT com Resultado de Julgamento encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarNovoLoteJulgamento(
			@ApiParam(value = "Lista de AITs selecionadas pelo usuario que farão parte do lote") CreateLoteImpressaoDTO createLoteImpressao
			) {	
		try {
			return ResponseFactory.ok(cartaJulgamentoService.gerarLoteCartaJulgamento(createLoteImpressao));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	
	}
	
	@GET
	@Path("/lote/quantidade/{tipoLote}")
	@ApiOperation(
			value = "Busca quantidade de AITs com movimento de Resultado de Julgamento para Lote de Impressão"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso.", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response buscarQuantidadeAitsParaLoteImpressao(
			@ApiParam(value = "Quantiade de Autos no lote") @DefaultValue("10000") @QueryParam("quantidadeAit") int quantidadeAit,
			@ApiParam(value = "Tipo do lote a ser gerado") @PathParam("tipoLote") int tipoLote
			) {	
		try {
			JSONObject json = new JSONObject();
			json.put("quantidade", cartaJulgamentoService.buscarQuantidadeAitsParaLoteImpressao(quantidadeAit, tipoLote).size());
			return ResponseFactory.ok(json);
		} 
		catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/aits")
	@ApiOperation(
			value = "Busca os AITs com Resultado de Julgamento que podem fazer parte de um lote"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso.", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT encontrada para gerar lote.", response = ResponseBody.class),
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
			searchCriterios.addCriteriosEqualInteger("K.tp_status", tipoLote, tipoLote > -1);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(cartaJulgamentoService.buscarAitsParaLoteImpressao( tipoLote, searchCriterios));
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote")
	@ApiOperation(
			value = "Retorna Lotes de Impressão de Resultados de Julgamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão encontrado.", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAll(
			@ApiParam(value = "Id. Do AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Dt. criação") @QueryParam("data") String dtCriacao,
			@ApiParam(value = "Tipo do lote") @QueryParam("tpImpressao") @DefaultValue("-1") Integer tpLoteImpressao,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") Integer limit) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("E.id_ait", "%"  + idAit + "%" , ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosGreaterDate("G.dt_criacao", dtCriacao, dtCriacao != null);
			searchCriterios.addCriteriosMinorDate("G.dt_criacao", dtCriacao, dtCriacao != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_impressao", tpLoteImpressao, tpLoteImpressao > -1);
			searchCriterios.setQtLimite(10);
			return ResponseFactory.ok(cartaJulgamentoService.buscarLotes(searchCriterios).getList(CartaJulgamentoDTO.class));
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/status")
	@ApiOperation(
			value = "Retorna o status de geração de documentos do Lote de Resultados de Julgamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Status de geração de documentos do Lote de Resultados de Julgamento", response = LoteImpressaoStatusBuilder.class),
			@ApiResponse(code = 204, message = "Lote de Resultados de Julgamento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response retrieveStatus(
			@ApiParam(value = "Código do lote") @QueryParam("cdLoteImpressao") int cdLoteImpressao) {
		try {
			return ResponseFactory.ok(cartaJulgamentoService.getStatusGeracaoDocumentos(cdLoteImpressao));
		} catch (Exception e) {
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
			@ApiResponse(code = 200, message = "Documentos do lote gerado.", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhuma AIT com Resultado de Julgamento encontrada para gerar lote.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response gerarDocumentosLote(
			@ApiParam(value = "Código do lote a ser gerado") @QueryParam("cdLoteImpressao") int cdLoteImpressao,
			@ApiParam(value = "Usuário que gerou o lote") @QueryParam("cdUsuario") int cdUsuario
			) {	
		try {
			return ResponseFactory.ok(cartaJulgamentoService.iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario));
		}
		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/imprimir")
	@ApiOperation(
			value = "Inicia o processo de impressão do lote de Resultados de Julgamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão do documento iniciada.", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response imprimirLoteNotificacao(
			@ApiParam(value = "Código do Lote de Impressão") @QueryParam("cdLoteImpressao") int cdLoteImpressao){
		try {
			return ResponseFactory.ok(cartaJulgamentoService.imprimirLoteJulgamento(cdLoteImpressao));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
