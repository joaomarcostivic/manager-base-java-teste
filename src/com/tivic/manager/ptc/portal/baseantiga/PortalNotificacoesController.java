package com.tivic.manager.ptc.portal.baseantiga;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.pessoa.dto.AitPortalDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "PortalNotificacoes", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/portal/notificacoes")
@Produces(MediaType.APPLICATION_JSON)
public class PortalNotificacoesController {

	private ManagerLog managerLog;
	private IPortalNotificacoesService portalNotificacoesService;

	public PortalNotificacoesController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.portalNotificacoesService = (IPortalNotificacoesService) BeansFactory.get(IPortalNotificacoesService.class);
	}

	@GET
	@Path("/")
	@ApiOperation(value = "Consulta de AITs", notes = "Consulta de AITs.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Consulta realizada com sucesso."),
			@ApiResponse(code = 400, message = "Dados invalidos."),
			@ApiResponse(code = 500, message = "Erro no servidor.") })
	public Response getAits(
			@ApiParam(value = "Placa do veículo autuado", required = true) @QueryParam("placa") String nrPlaca,
			@ApiParam(value = "No. do RENAVAM", required = true) @QueryParam("renavan") String nrRenavam,
			@ApiParam(value = "No. do AIT") @QueryParam("ait") String nrAit) {
		try {
			List<AitPortalDTO> printNotificacao = new PortalNotificacoesService().listarNotificacoes(nrPlaca,
					nrRenavam, nrAit);
			return ResponseFactory.ok(printNotificacao);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{cdAit}/segundavia/{tpStatus}")
	@ApiOperation(value = "Gera arquivos de impressao de NAI/NIP", notes = "impressão de nai/nip segunda via.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado."),
			@ApiResponse(code = 204, message = "Não ha notificação com o tipo indicado."),
			@ApiResponse(code = 400, message = "Dados invalidos."),
			@ApiResponse(code = 500, message = "Erro no servidor.") })
	@Produces({ "application/pdf", "application/json" })
	public Response getPrintNai(@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "Tipo de Notificação") @PathParam("tpStatus") int tpStatus) {
		try {
			byte[] printNotificacao = new PortalNotificacoesService().gerarSegundaViaNotificacao(cdAit, tpStatus);
			return ResponseFactory.ok(printNotificacao);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/cidade")
	@ApiOperation(value = "Busca todas as cidades", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Cidades encontradas."),
			@ApiResponse(code = 204, message = "Erro ao buscar cidades"),
			@ApiResponse(code = 400, message = "Dados invalidos."),
			@ApiResponse(code = 500, message = "Erro no servidor.") })
	public Response getCidades() {
		try {
			List<CidadeDTO> printNotificacao = new PortalNotificacoesService().findCidade();
			return ResponseFactory.ok(printNotificacao);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/carta-julgamento/jari/{cdAit}/{tpStatus}")
	@ApiOperation(value = "Gera carta de julgamento", notes = "Carta de Julgamento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Carta de julgamento gerada com sucesso."),
			@ApiResponse(code = 400, message = "Dados invalidos."),
			@ApiResponse(code = 500, message = "Erro no servidor.") })
	@Produces({ "application/pdf", "application/json" })
	public Response getCartaJulgamentoJari(
			@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "Status do resultado") @PathParam("tpStatus") int tpStatus) {
		try {
			byte[] cartaJulgamento = this.portalNotificacoesService.getCartaJulgamento(cdAit, tpStatus);
			return ResponseFactory.ok(cartaJulgamento);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
