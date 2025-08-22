package com.tivic.manager.ptc.protocolosv3.parecer;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.ptc.protocolosv3.parecer.relatorioparecer.IRelatorioParecerService;
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

@Api(value = "Parecer", tags = { "protocolosv3" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/ptc/protocolos/parecer")
@Produces(MediaType.APPLICATION_JSON)
public class ParecerController {

	private final IParecerService parecerService;
	private final IRelatorioParecerService relatorioParecerService;
	private final ManagerLog managerLog;

	public ParecerController() throws Exception {
		this.relatorioParecerService = (IRelatorioParecerService) BeansFactory.get(IRelatorioParecerService.class);
		this.parecerService = (IParecerService) BeansFactory.get(IParecerService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@GET
	@Path("/{cdParecer}")
	@ApiOperation(value = "Buscar Parecer por ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Parecer encontrado", response = Parecer.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response get(@ApiParam(value = "ID do Parecer", required = true) @PathParam("cdParecer") int cdParecer) {
		try {
			Parecer parecer = parecerService.get(cdParecer);
			return ResponseFactory.ok(parecer);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("")
	@ApiOperation(value = "Buscar Parecer por critérios")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Parecer encontrado", response = Parecer.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response search(
			@ApiParam(value = "Nome do Parecer", required = false) @QueryParam("nmParecer") String nmParecer,
			@ApiParam(value = "Tipo de Parecer", required = false) @QueryParam("cdSituacaoDocumento") int cdSituacaoDocumento,
			@ApiParam(value = "Tipo de Documeto", required = false) @QueryParam("tpDocumento") int tpDocumento) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosLikeAnyString("nm_parecer", nmParecer, nmParecer != null);
			searchCriterios.addCriteriosEqualInteger("cd_situacao_documento", cdSituacaoDocumento, cdSituacaoDocumento != 0);
			searchCriterios.addCriteriosEqualInteger("tp_documento", tpDocumento, tpDocumento != 0);
			List<Parecer> parecerList = parecerService.find(searchCriterios);
			return ResponseFactory.ok(parecerList);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@POST
	@Path("")
	@ApiOperation(value = "Inserir Parecer")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parecer inserido com sucesso", response = Parecer.class),
			@ApiResponse(code = 400, message = "Parecer Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response create(@ApiParam(value = "Parecer a ser inserido", required = true) Parecer parecer) {
		try {
			parecerService.insert(parecer);
			return ResponseFactory.ok(parecer);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{cdParecer}")
	@ApiOperation(value = "Atualizar Parecer por ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Parecer atualizado com sucesso", response = Parecer.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response update(@ApiParam(value = "ID do Parecer", required = true) @PathParam("cdParecer") int cdParecer,
			@ApiParam(value = "Parecer a ser atualizado", required = true) Parecer parecer) {
		try {
			parecerService.update(parecer);
			return ResponseFactory.ok(parecer);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@DELETE
	@Path("/{cdParecer}")
	@ApiOperation(value = "Excluir Parecer por ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Parecer excluído com sucesso", response = Long.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response delete(
			@ApiParam(value = "ID do Parecer a ser excluído", required = true) @PathParam("cdParecer") int cdParecer) {
		try {
			parecerService.delete(cdParecer);
			return ResponseFactory.noContent();
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdAit}/{cdTipoDocumento}/imprimir")
	@ApiOperation(value = "Imprimi o parecer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Parecer excluído com sucesso", response = Long.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição") })
	public Response gerarParecer(@ApiParam(value = "Código do ait") @PathParam("cdAit") int cdAit,
			@ApiParam(value = "Código do tipo de documento") @PathParam("cdTipoDocumento") int cdTipoDocumento) {
		try {
			byte[] relatorioParecer = this.relatorioParecerService.gerarParecer(cdAit, cdTipoDocumento);
			return ResponseFactory.ok(relatorioParecer);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}