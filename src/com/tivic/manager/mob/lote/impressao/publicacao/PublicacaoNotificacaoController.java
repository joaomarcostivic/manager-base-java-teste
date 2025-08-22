package com.tivic.manager.mob.lote.impressao.publicacao;

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
import javax.ws.rs.core.Response;
import java.sql.Types;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.lote.impressao.publicacao.builder.NotificacaoPublicacaoPendenteBuilder;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.LotePublicacaoNotificacaoDto;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.NotificacaoPublicacaoPendenteDto;
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
import sol.dao.ItemComparator;
import sol.util.Result;

@Api(value = "Notificação", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/notificacao/publicacao")
@Produces(MediaType.APPLICATION_JSON)
public class PublicacaoNotificacaoController {

	private ManagerLog managerLog;
	private IPublicacaoNotificacaoService publicacaoNotificacaoService;
	
	public PublicacaoNotificacaoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.publicacaoNotificacaoService = (IPublicacaoNotificacaoService) BeansFactory.get(IPublicacaoNotificacaoService.class);
	}
	
	@GET
	@Path("/lote-publicacao")
	@ApiOperation(value = "Busca os lotes de publicação de Notificação.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Notificações buscadas com sucesso", response = LotePublicacaoNotificacaoDto[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = LotePublicacaoNotificacaoDto[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarArquivosPublicados(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpDocumento") int tpDocumento,
		@ApiParam(value = "Data Criação Inicial") @QueryParam("dtCriacaoInicial") String dtCriacaoInicial,
		@ApiParam(value = "Data Criação Final") @QueryParam("dtCriacaoFinal") String dtCriacaoFinal,
		@ApiParam(value = "Data Publicação Inicial") @QueryParam("dtPublicacaoInicial") String dtPublicacaoInicial,
		@ApiParam(value = "Data Publicação Final") @QueryParam("dtPublicacaoFinal") String dtPublicacaoFinal,
		@ApiParam(value = "ID do AIT") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Limite") @QueryParam("limit") int limit,
		@ApiParam(value = "Páginas") @QueryParam("page") int page
	) {
		try {	
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.tp_documento", tpDocumento, tpDocumento > 0);
			searchCriterios.addCriteriosGreaterDate("A.dt_criacao", dtCriacaoInicial, dtCriacaoInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_criacao", dtCriacaoFinal, dtCriacaoFinal != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_envio", dtPublicacaoInicial, dtPublicacaoInicial != null);
			searchCriterios.addCriteriosMinorDate("A.dt_envio", dtPublicacaoFinal, dtPublicacaoFinal != null);
			searchCriterios.addCriterios("E.id_ait", "%"  + idAit + "%" , ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.setQtLimite(limit);
			searchCriterios.setQtDeslocamento((limit*page)-limit);
			PagedResponse<LotePublicacaoNotificacaoDto> lotePublicacoes = this.publicacaoNotificacaoService.buscarArquivosPublicados(searchCriterios);
			return ResponseFactory.ok(lotePublicacoes);
		} catch (BadRequestException e) {
			this.managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{cdLoteImpressao}/confirma-publicacao")
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
			@ApiParam(value = "Tipo do lote a ser gerado") @PathParam("cdLoteImpressao") int cdLoteImpressao,
			@ApiParam(value = "Data de Confirmação Publicacao") GregorianCalendar dtConfirmacaoPublicado,
			@ApiParam(value = "Usuario que solicitou a publicação") @QueryParam("cdUsuario") int cdUsuario
		) {
		try {
			LotePublicacaoNotificacaoDto lotePublicacaoDto = this.publicacaoNotificacaoService.confirmar(cdLoteImpressao, dtConfirmacaoPublicado, cdUsuario);
			return ResponseFactory.ok(lotePublicacaoDto);
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/notificacao-pendente")
	@ApiOperation(value = "Busca as Notificações Pendentes de Publicação.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Notificações buscados com sucesso", response = NotificacaoPublicacaoPendenteDto [].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = NotificacaoPublicacaoPendenteDto[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarNotificacoesPendentes(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpDocumento") int tpDocumento,
		@ApiParam(value = "Data do Movimento Inicial") @QueryParam("dtMovimentoInicial") String dtMovimentoInicial,
		@ApiParam(value = "Data do Movimento Final") @QueryParam("dtMovimentoFinal") String dtMovimentoFinal,
		@ApiParam(value = "Apenas não Entregues (true/false)") @DefaultValue("false") @QueryParam("lgNaoEntregues") boolean lgNaoEntregues,
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite
	) {
		try {
			SearchCriterios searchCriterios = new NotificacaoPublicacaoPendenteBuilder()
					.setTpDocumento(tpDocumento)
					.setDtMovimentoMaiorQue(dtMovimentoInicial)
					.setDtMovimentoMenorQue(dtMovimentoFinal)
					.setParametrosPaginator(nrPagina, nrLimite)
					.build();
			PagedResponse<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtoList = this.publicacaoNotificacaoService.buscarPendentesPublicacao(searchCriterios, lgNaoEntregues, tpDocumento);
			return ResponseFactory.ok(notificacaoPublicacaoPendenteDtoList);
		}
		catch(Exception e) {
			managerLog.error("Erro ao realizar busca: ", e.getMessage());
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
			@ApiParam(value = "Tipo de Documento a ser Publicado") @QueryParam("tpDocumento") int tpDocumento,
			@ApiParam(value = "Notificações a serem publicados") List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) {
		try {
			Arquivo arquivoPublicacao = this.publicacaoNotificacaoService
					.publicar(cdUsuario, tpDocumento, notificacaoPublicacaoPendenteDtos);
			return ResponseFactory.ok(arquivoPublicacao);
		} catch (Exception e) {
			managerLog.error("Erro ao gerar publicação: ", e.getMessage());
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
