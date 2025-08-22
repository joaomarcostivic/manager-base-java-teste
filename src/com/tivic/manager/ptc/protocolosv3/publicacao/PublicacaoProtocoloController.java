package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.util.pagination.PagedResponse;
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

@Api(value = "Protocolo", tags = {"ptc"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/ptc/protocolos/publicacao")
@Produces(MediaType.APPLICATION_JSON)
public class PublicacaoProtocoloController {
	
	private IPublicacaoProtocoloService publicacaoProtocoloService;
	private ManagerLog managerLog;
	
	public PublicacaoProtocoloController() throws Exception {
		this.publicacaoProtocoloService = (IPublicacaoProtocoloService) BeansFactory.get(IPublicacaoProtocoloService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/lote-publicacao")
	@ApiOperation(value = "Busca os lotes de publicação de protocolos.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos buscados com sucesso", response = LotePublicacaoDto[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = LotePublicacaoDto[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarArquivosPublicados(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpPublicacao") int tpPublicacao,
		@ApiParam(value = "Data Criação Inicial") @QueryParam("dtCriacaoInicial") String dtCriacaoInicial,
		@ApiParam(value = "Data Criação Final") @QueryParam("dtCriacaoFinal") String dtCriacaoFinal,
		@ApiParam(value = "Data Publicação Inicial") @QueryParam("dtPublicacaoInicial") String dtPublicacaoInicial,
		@ApiParam(value = "Data Publicação Final") @QueryParam("dtPublicacaoFinal") String dtPublicacaoFinal,
		@ApiParam(value = "Limite") @QueryParam("limit") int limit,
		@ApiParam(value = "Páginas") @QueryParam("page") int page
	) {
		try {	
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_publicacao", tpPublicacao, tpPublicacao > 0);
			searchCriterios.addCriteriosGreaterDate("G.dt_criacao", dtCriacaoInicial, dtCriacaoInicial != null);
			searchCriterios.addCriteriosMinorDate("G.dt_criacao", dtCriacaoFinal, dtCriacaoFinal != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_publicacao", dtPublicacaoInicial, dtPublicacaoInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_publicacao", dtPublicacaoFinal, dtPublicacaoFinal != null);
			searchCriterios.setQtLimite(limit);
			searchCriterios.setQtDeslocamento((limit*page)-limit);
			PagedResponse<LotePublicacaoDto> lotePublicacaoDtoList = this.publicacaoProtocoloService.buscarArquivosPublicados(searchCriterios);
			return ResponseFactory.ok(lotePublicacaoDtoList);
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{cdLotePublicacao}/confirma-publicacao")
	@ApiOperation(
			value = "Confirma o envio do arquivo de publicação."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Confirmação Realizada com sucesso"),
			@ApiResponse(code = 204, message = "Nao ha dados para o lote indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response confirmarPublicacao(
			@ApiParam(value = "Tipo do lote a ser gerado") @PathParam("cdLotePublicacao") int cdLotePublicacao,
			@ApiParam(value = "Data de confirmação Publicação") GregorianCalendar dtConfirmacaoPublicado,
			@ApiParam(value = "Usuario que solicitou a publicação") @QueryParam("cdUsuario") int cdUsuario
		) {
		try {
			LotePublicacaoDto lotePublicacaoDto = this.publicacaoProtocoloService.confirmarPublicacao(cdLotePublicacao, dtConfirmacaoPublicado, cdUsuario);
			return ResponseFactory.ok(lotePublicacaoDto);
		}
		catch (Exception e) {
			managerLog.error("Erro ao confirmar publicação: ", e.getMessage());
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/protocolo-pendente")
	@ApiOperation(value = "Busca os Protocolos Pendentes de Publicação.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos buscados com sucesso", response = ProtocoloPublicacaoPendenteDto[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ProtocoloPublicacaoPendenteDto[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarProtocolosPendentes(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpPublicacao") String tpPublicacao,
		@ApiParam(value = "Data do Julgamento Inicial") @QueryParam("dtJulgamentoInicial") String dtJulgamentoInicial,
		@ApiParam(value = "Data do Julgamento Final") @QueryParam("dtJulgamentoFinal") String dtJulgamentoFinal,
		@ApiParam(value = "Situação do Documento (Deferido/Indeferido)") @QueryParam("stJulgamento") int stJulgamento,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {	
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("E.id_tipo_documento", tpPublicacao, tpPublicacao != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_ocorrencia", dtJulgamentoInicial, dtJulgamentoInicial != null);
			searchCriterios.addCriteriosMinorDate("B.dt_ocorrencia", dtJulgamentoFinal, dtJulgamentoFinal != null);
			searchCriterios.addCriteriosEqualInteger("A.cd_situacao_documento", stJulgamento, stJulgamento > 0);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			PagedResponse<ProtocoloPublicacaoPendenteDto> protocoloPublicacaoPendenteDtoList = this.publicacaoProtocoloService.buscarPendentesPublicacao(searchCriterios);
			return ResponseFactory.ok(protocoloPublicacaoPendenteDtoList);
		}
		catch(Exception e) {
			managerLog.error("Erro ao realizar busca: ", e.getMessage());
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/publica-protocolo")
	@ApiOperation(value = "Faz a publicação de protocolos pendentes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Protocolos Publicados com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "Os Protocolos possuem algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response publicarProtocolo(
			@ApiParam(value = "Usuario que solicitou a publicação") @QueryParam("cdUsuario") int cdUsuario,
			@ApiParam(value = "Tipo de Documento a ser Publicado") @QueryParam("idTipoDocumento") String idTipoDocumento,
			@ApiParam(value = "Situação do julgamento") @QueryParam("stJulgamento") int stJulgamento,
			@ApiParam(value = "Protocolos a serem publicados") List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtos) {
		try {
			Arquivo arquivoPublicacao = this.publicacaoProtocoloService.publicarProtocolo(cdUsuario, idTipoDocumento, stJulgamento, publicaProtocoloDtos);
			return ResponseFactory.ok(arquivoPublicacao);
		} catch (Exception e) {
			managerLog.error("Erro ao gerar publicação: ", e.getMessage());
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelamento/publicacao/{cdAit}")
	@ApiOperation(
		value = "Cancela a publicação de resultado de JARI de um AIT"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AIT encontrado", response = Result.class),
			@ApiResponse(code = 400, message = "A solicitação possue algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	public Response cancelarPublicacaoJari(
			@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "Código do usuario que realizou o cancelamento") @QueryParam("cdUsuario") int cdUsuario,
			@ApiParam(value = "Codigo do movimento") @QueryParam("cdMovimento") int cdMovimento){
		try {
			this.publicacaoProtocoloService.cancelarPublicacao(cdAit, cdMovimento, cdUsuario);
			return ResponseFactory.noContent();
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLotePublicacao}/download-arquivo/{tpArquivo}")
	@ApiOperation(
			value = "Faz o download do arquivo do lote gerado"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo baixado"),
			@ApiResponse(code = 204, message = "Nao ha arquivo para o lote indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public Response downloadDocumentoPublicacao(
			@ApiParam(value = "Código do documento") @PathParam("cdLotePublicacao") int cdLotePublicacao,
			@ApiParam(value = "Código do documento") @PathParam("tpArquivo") int tpArquivo
		) {
		try {	
			Arquivo arquivo = this.publicacaoProtocoloService.getArquivoLote(cdLotePublicacao, tpArquivo);
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
