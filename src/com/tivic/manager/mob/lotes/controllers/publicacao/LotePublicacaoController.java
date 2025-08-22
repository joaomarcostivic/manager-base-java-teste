package com.tivic.manager.mob.lotes.controllers.publicacao;

import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DefaultValue;
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
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoSearch;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoSearchBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.NotificacaoPublicacaoPendenteSearchBuilder;
import com.tivic.manager.mob.lotes.dto.publicacao.LotePublicacaoNotificacaoDTO;
import com.tivic.manager.mob.lotes.dto.publicacao.NotificacaoPublicacaoPendenteDTO;
import com.tivic.manager.mob.lotes.service.publicacao.ILotePublicacaoService;
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

@Api(value = "LotePublicacao", tags = { "mob" }, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/lote/publicacao")
@Produces(MediaType.APPLICATION_JSON)
public class LotePublicacaoController {
	
	private ManagerLog managerLog;
	private ILotePublicacaoService lotePublicacaoService;
	
	public LotePublicacaoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.lotePublicacaoService = (ILotePublicacaoService) BeansFactory.get(ILotePublicacaoService.class);
	}
	
	@GET
	@Path("/listagem")
	@ApiOperation(value = "Busca os lotes de publicação de Notificação.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Notificações buscadas com sucesso", response = LotePublicacaoNotificacaoDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = LotePublicacaoNotificacaoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarArquivosPublicados(
		@ApiParam(value = "Tipo de publicação") @QueryParam("tpPublicacao") int tpPublicacao,
		@ApiParam(value = "Data Criação Inicial") @QueryParam("dtCriacaoInicial") String dtCriacaoInicial,
		@ApiParam(value = "Data Criação Final") @QueryParam("dtCriacaoFinal") String dtCriacaoFinal,
		@ApiParam(value = "Data Publicação Inicial") @QueryParam("dtPublicacaoInicial") String dtPublicacaoInicial,
		@ApiParam(value = "Data Publicação Final") @QueryParam("dtPublicacaoFinal") String dtPublicacaoFinal,
		@ApiParam(value = "ID do AIT") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Limite") @QueryParam("limit") int limit,
		@ApiParam(value = "Páginas") @QueryParam("page") int page
	) {
		try {	
			LotePublicacaoSearch lotePublicacaoSearch = new LotePublicacaoSearchBuilder()
					.setTpPublicacao(tpPublicacao)
					.setDtCriacaoInicial(dtCriacaoInicial)
					.setDtCriacaoFinal(dtCriacaoFinal)
					.setDtPublicacaoInicial(dtPublicacaoInicial)
					.setDtPublicacaoFinal(dtPublicacaoFinal)
					.setIdAit(idAit)
					.setPage(page)
					.setLimit(limit)
					.build();
			return ResponseFactory.ok(this.lotePublicacaoService.buscarArquivosPublicados(lotePublicacaoSearch));
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/notificacao-pendente")
	@ApiOperation(value = "Busca as Notificações Pendentes de Publicação.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Notificações buscados com sucesso", response = NotificacaoPublicacaoPendenteDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = NotificacaoPublicacaoPendenteDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarNotificacoesPendentes(
		@ApiParam(value = "Tipo de publiacação") @QueryParam("tpPublicacao") int tpPublicacao,
		@ApiParam(value = "Data do Movimento Inicial") @QueryParam("dtMovimentoInicial") String dtMovimentoInicial,
		@ApiParam(value = "Data do Movimento Final") @QueryParam("dtMovimentoFinal") String dtMovimentoFinal,
		@ApiParam(value = "Apenas não Entregues (true/false)") @DefaultValue("false") @QueryParam("lgNaoEntregues") boolean lgNaoEntregues,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new NotificacaoPublicacaoPendenteSearchBuilder()
					.setTpPublicacao(tpPublicacao)
					.setDtMovimentoMaiorQue(dtMovimentoInicial)
					.setDtMovimentoMenorQue(dtMovimentoFinal)
					.setParametrosPaginator(nrPagina, nrLimite)
					.build();
			PagedResponse<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtoList = 
					this.lotePublicacaoService.buscarPendentesPublicacao(searchCriterios, lgNaoEntregues, tpPublicacao);
			return ResponseFactory.ok(notificacaoPublicacaoPendenteDtoList);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/publica-notificacao")
	@ApiOperation(value = "Faz a publicação de notificações pendentes")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notificações Publicadas com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "As Notificações possuem algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response publicarNotificacao(
			@ApiParam(value = "Usuario que solicitou a publicação") @QueryParam("cdUsuario") int cdUsuario,
			@ApiParam(value = "Tipo de Documento a ser Publicado") @QueryParam("tpPublicacao") int tpPublicacao,
			@ApiParam(value = "Notificações a serem publicados") List<NotificacaoPublicacaoPendenteDTO> notificacaoPublicacaoPendenteDtos) {
		try {
			Arquivo arquivoPublicacao = this.lotePublicacaoService.publicar(cdUsuario, tpPublicacao, notificacaoPublicacaoPendenteDtos);
			return ResponseFactory.ok(arquivoPublicacao);
		} catch (Exception e) {
			e.printStackTrace(System.out);
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
			@ApiParam(value = "Data de Confirmação Publicacao") GregorianCalendar dtConfirmacaoPublicado,
			@ApiParam(value = "Usuario que solicitou a publicação") @QueryParam("cdUsuario") int cdUsuario
		) {
		try {
			LotePublicacaoNotificacaoDTO lotePublicacaoDto = this.lotePublicacaoService.confirmar(cdLotePublicacao, dtConfirmacaoPublicado, cdUsuario);
			return ResponseFactory.ok(lotePublicacaoDto);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
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
			Arquivo arquivo = this.lotePublicacaoService.getArquivoLote(cdLotePublicacao, tpArquivo);
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
