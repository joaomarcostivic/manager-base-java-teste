package com.tivic.manager.mob.correios;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.CorreiosEtiquetaDTO;
import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.correios.builder.CorreiosEtiquetaSearchBuilder;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.ArquivoRetornoCorreiosDTO;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
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

@Api(value = "Etiquetas", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/correiosetiqueta")
@Produces(MediaType.APPLICATION_JSON)
public class CorreiosEtiquetaController {
	
	private ICorreiosEtiquetaService correiosEtiquetaService;
	private ManagerLog managerLog;
	
	public CorreiosEtiquetaController() throws Exception {
		correiosEtiquetaService = (ICorreiosEtiquetaService) BeansFactory.get(ICorreiosEtiquetaService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra uma nova etiqueta."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etiqueta Registrada", response = CorreiosEtiqueta.class),
			@ApiResponse(code = 400, message = "Etiqueta Inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response create(
			@ApiParam(value = "Etiqueta a ser criada", required = true) CorreiosEtiqueta correiosEtiqueta) {
		try {
			return ResponseFactory.ok(correiosEtiquetaService.insert(correiosEtiqueta));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro na criação de novo lote: " + e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma etiqueta"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote atualizado", response = CorreiosLote.class),
			@ApiResponse(code = 400, message = "Lote Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(
			@ApiParam(value = "Lote a ser atualizado", required = true) CorreiosEtiqueta correiosEtiqueta){
		try {
			return ResponseFactory.ok(correiosEtiquetaService.update(correiosEtiqueta));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro ao atualizar lote: " + e.getMessage());
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(value = "Busca de etiquetas")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiqueta[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Codigo da etiqueta") @QueryParam("cdEtiqueta") Integer cdEtiqueta,
			@ApiParam(value = "Codigo da lote") @QueryParam("cdLote") Integer cdLote,
			@ApiParam(value = "Número da etiqueta") @QueryParam("nrEtiqueta") Integer nrEtiqueta,
			@ApiParam(value = "Data de envio da etiqueta") @QueryParam("dtEnvio") String dtEnvio,
			@ApiParam(value = "Sigla de servico") @QueryParam("sgServico") String sgServico,
			@ApiParam(value = "Codigo do ait") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Tipo de status") @QueryParam("tpStatus") Integer tpStatus,
			@ApiParam(value = "Numero do movimento") @QueryParam("nrMovimento") Integer nrMovimento,
			@ApiParam(value = "Tipo de lote") @QueryParam("tpLote") Integer tpLote,
			@ApiParam(value = "Digito verificador") @QueryParam("nrDigitoVerificador") Integer nrDigitoVerificador,
			@QueryParam("limit") int limit) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_etiqueta", cdEtiqueta, cdEtiqueta != null);
			searchCriterios.addCriteriosEqualInteger("cd_lote", cdLote, cdLote != null);
			searchCriterios.addCriteriosEqualInteger("nr_etiqueta", nrEtiqueta, nrEtiqueta != null);
			searchCriterios.addCriteriosGreaterDate("dt_lote", dtEnvio, dtEnvio != null);
			searchCriterios.addCriteriosEqualString("sg_servico", sgServico, sgServico != null);
			searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
			searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus, tpStatus != null);
			searchCriterios.addCriteriosEqualInteger("nr_movimento", nrMovimento, nrMovimento != null);
			searchCriterios.addCriteriosEqualInteger("tp_lote", tpLote, tpLote != null);
			searchCriterios.addCriteriosEqualInteger("nr_digito_verificador", nrDigitoVerificador, nrDigitoVerificador != null);
			searchCriterios.setQtLimite(limit);
			
			return ResponseFactory.ok(correiosEtiquetaService.find(searchCriterios));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro ao buscar etiquetas:" + e.getMessage());
		}
	}
	
	@GET
	@Path("/findetiquetadto")
	@ApiOperation(value = "Busca de etiquetas")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiquetaDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findEtiquetaDTO(
			@ApiParam(value = "Codigo da etiqueta") @QueryParam("cdEtiqueta") Integer cdEtiqueta,
			@ApiParam(value = "Codigo da lote") @QueryParam("cdLote") Integer cdLote,
			@ApiParam(value = "Número da etiqueta") @QueryParam("nrEtiqueta") String nrEtiqueta,
			@ApiParam(value = "Data de envio da etiqueta") @QueryParam("dtEnvio") String dtEnvio,
			@ApiParam(value = "Sigla de servico") @QueryParam("sgServico") String sgServico,
			@ApiParam(value = "Codigo do ait") @QueryParam("cdAit") int cdAit,
			@ApiParam(value = "Tipo de status") @QueryParam("tpStatus") Integer tpStatus,
			@ApiParam(value = "Numero do movimento") @QueryParam("nrMovimento") Integer nrMovimento,
			@ApiParam(value = "Tipo de lote") @QueryParam("tpLote") Integer tpLote,
			@ApiParam(value = "Digito verificador") @QueryParam("nrDigitoVerificador") Integer nrDigitoVerificador,
			@QueryParam("limit") int limit) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_etiqueta", cdEtiqueta, cdEtiqueta != null);
			searchCriterios.addCriteriosEqualInteger("cd_lote", cdLote, cdLote != null);
			searchCriterios.addCriteriosGreaterDate("dt_lote", dtEnvio, dtEnvio != null);
			searchCriterios.addCriteriosEqualString("sg_servico", sgServico, sgServico != null);
			searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
			searchCriterios.addCriteriosEqualInteger("tp_status", tpStatus, tpStatus != null);
			searchCriterios.addCriteriosEqualInteger("nr_movimento", nrMovimento, nrMovimento != null);
			searchCriterios.addCriteriosEqualInteger("tp_lote", tpLote, tpLote != null);
			searchCriterios.addCriteriosEqualInteger("nr_digito_verificador", nrDigitoVerificador, nrDigitoVerificador != null);
			searchCriterios.setQtLimite(limit);
			
			return ResponseFactory.ok(correiosEtiquetaService.findEtiquetaDTO(searchCriterios, nrEtiqueta));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro ao buscar etiquetas:" + e.getMessage());
		}
	}
	
	@POST
	@Path("/upload/arquivo-retorno")
	@ApiOperation(
			value = "Registra o retorno das entragas do correio"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornos registrados"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response upload(
			@ApiParam(value = "Arquivo dos Correios") ArquivoRetornoCorreiosDTO arquivoRetornoCorreios,
			@ApiParam(value = "Usuário que fez upload do retorno") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			Boolean isTask = false;
			List<DadosRetornoCorreioDto> dadosRetornoCorreioDtosList = correiosEtiquetaService.uploadRetornoCorreiosWeb(arquivoRetornoCorreios, cdUsuario, isTask);
			return ResponseFactory.ok(dadosRetornoCorreioDtosList);
		} 
		catch(Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/arquivos")
	@ApiOperation(value = "Busca de arquivos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos encontrados", response = Arquivo[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findArquivo(
			@ApiParam(value = "Nome do arquivo") @QueryParam("nmArquivo") String nmArquivo,
			@ApiParam(value = "Código do tipo de arquivo") @QueryParam("cdTipoArquivo") int cdTipoArquivo,
			@ApiParam(value = "Data da criação inicial") @QueryParam("dtCriacaoInicial") String dtCriacaoInicial,
			@ApiParam(value = "Data da criação final") @QueryParam("dtCriacaoFinal") String dtCriacaoFinal,
			@ApiParam(value = "Nome do arquivo") @QueryParam("nrRegistro") int nrRegistro,
			@QueryParam("limit") int limit,
			@QueryParam("page") int page) {
		try {
			CorreiosEtiquetaSearch correiosEtiquetaSearch = new CorreiosEtiquetaSearchBuilder()
					.setNmArquivo(nmArquivo)
					.setCdTipoArquivo(cdTipoArquivo)
					.setDtCriacaoInicial(dtCriacaoInicial)
					.setDtCriacaoFinal(dtCriacaoFinal)
					.setNrRegistro(nrRegistro)
					.setLimit(limit)
					.setPage(page)
					.build();
			
			PagedResponse<Arquivo> pagedResponse = correiosEtiquetaService.findArquivo(correiosEtiquetaSearch);
			return ResponseFactory.ok(pagedResponse);
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
