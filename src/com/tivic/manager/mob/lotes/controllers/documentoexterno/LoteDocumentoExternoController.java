package com.tivic.manager.mob.lotes.controllers.documentoexterno;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.lotes.dto.documentoexterno.LoteDocumentoExternoDTO;
import com.tivic.manager.mob.lotes.service.documentoexterno.ILoteDocumentoExternoService;
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

@Api(value = "Lote Documento externo", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v3/mob/lote/documentoexterno")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LoteDocumentoExternoController {
	
	private ManagerLog managerLog;
	private ILoteDocumentoExternoService loteDocumentoExternoService;
	
	public LoteDocumentoExternoController() throws Exception{
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.loteDocumentoExternoService = (ILoteDocumentoExternoService) BeansFactory.get(ILoteDocumentoExternoService.class);
	}

	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de lotes de documento externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de documento externo encontrados", response = LoteDocumentoExternoDTO[].class),
			@ApiResponse(code = 204, message = "Nenhum lote de documento externo encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response findPaged(
			@ApiParam(value = "código do orgão externo") @QueryParam("cdOrgaoExterno") int cdOrgaoExterno,
			@ApiParam(value = "Data da criação do lote") @QueryParam("dtInicialCriacao") String dtInicialCriacao,
			@ApiParam(value = "Data do criação do lote") @QueryParam("dtFinalCriacao") String dtFinalCriacao,
			@QueryParam("limit") int nrLimite,
			@QueryParam("page") int nrPagina) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosGreaterDate("F.dt_criacao", dtInicialCriacao, dtInicialCriacao != null);
			searchCriterios.addCriteriosMinorDate("F.dt_criacao", dtFinalCriacao, dtFinalCriacao != null);
			searchCriterios.addCriteriosEqualInteger("D.cd_orgao_externo", cdOrgaoExterno, cdOrgaoExterno > 0);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			PagedResponse<LoteDocumentoExternoDTO> lotesDocumentoExterno = loteDocumentoExternoService.find(searchCriterios);
			return ResponseFactory.ok(lotesDocumentoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/download-arquivo")
	@ApiOperation(
			value = "Faz o download do ofício de documento externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo baixado.", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces({"application/pdf", "application/json"})
	public Response downloadDocumentoExterno(
			@ApiParam(value = "Código do documento") @QueryParam("cdLoteImpressao") int cdLoteImpressao
		) {
		try {	
			return ResponseFactory.ok(loteDocumentoExternoService.imprimirOficioExterno(cdLoteImpressao));
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
