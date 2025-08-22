package com.tivic.manager.mob.correios;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.mob.CorreiosLoteDTO;
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


@Api(value = "Lote de Etiquetas", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/correioslote")
@Produces(MediaType.APPLICATION_JSON)
public class CorreiosLoteController {
	private ICorreiosLoteService correiosLoteService;
	private ManagerLog managerLog;
	
	public CorreiosLoteController() throws Exception {
		correiosLoteService = (ICorreiosLoteService) BeansFactory.get(ICorreiosLoteService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra um novo lote de etiquetas."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote Registrado", response = CorreiosLote.class),
			@ApiResponse(code = 400, message = "Lote Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response create(
			@ApiParam(value = "Lote de etiqueta a ser criado", required = true) CorreiosLote correiosLote) {
		try {
			return ResponseFactory.ok(correiosLoteService.create(correiosLote));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro na criação de novo lote: " + e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um lote dos Correios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote atualizado", response = CorreiosLote.class),
			@ApiResponse(code = 400, message = "Lote Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(
			@ApiParam(value = "Lote a ser atualizado", required = true) CorreiosLote correiosLote){
		try {
			return ResponseFactory.ok(correiosLoteService.update(correiosLote));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro ao atualizar lote: " + e.getMessage());
		}
	}
		
	@GET
	@Path("/")
	@ApiOperation(value = "Busca de lotes do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lotes encontrados", response = CorreiosLoteDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response find(
			@ApiParam(value = "Codigo do lote") @QueryParam("cdLote") Integer cdLote,
			@ApiParam(value = "Data de criação do lote") @QueryParam("dtLote") String dtLote,
			@ApiParam(value = "Numero inicial do lote") @QueryParam("nrInicial") int nrInicial,
			@ApiParam(value = "Numero final do lote") @QueryParam("nrFinal") int nrFinal,
			@ApiParam(value = "Numero da etiqueta") @QueryParam("nrEtiqueta") String nrEtiqueta,
			@ApiParam(value = "Sitiacao do lote") @QueryParam("stLote") Integer stLote,
			@ApiParam(value = "Tipo de lote") @QueryParam("tpLote") Integer tpLote,
			@ApiParam(value = "Sigla do lote") @QueryParam("sgLote") String sgLote,
			@ApiParam(value = "Data de vencimento do lote") @QueryParam("dtVencimento") String dtVencimento,
			@QueryParam("page") int page,
			@QueryParam("limit") int limit) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_lote", cdLote, cdLote != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_lote", dtLote, dtLote != null);
			searchCriterios.addCriteriosMinorDate("A.dt_lote", dtLote, dtLote != null);
			searchCriterios.addCriteriosEqualInteger("A.nr_inicial", nrInicial, nrInicial > 0);
			searchCriterios.addCriteriosEqualInteger("A.nr_final", nrFinal, nrFinal > nrInicial);
			searchCriterios.addCriteriosEqualString("b.nr_etiqueta", nrEtiqueta, nrEtiqueta != null);
			searchCriterios.addCriteriosEqualInteger("A.st_lote", stLote, stLote != null);
			searchCriterios.addCriteriosEqualInteger("A.tp_lote", tpLote, tpLote != null);
			searchCriterios.addCriteriosLikeAnyString("A.sg_lote", sgLote, sgLote != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_vencimento", dtVencimento, dtVencimento != null);
			searchCriterios.addCriteriosMinorDate("A.dt_vencimento", dtVencimento, dtVencimento != null);
			searchCriterios.setQtLimite(limit);
			searchCriterios.setQtDeslocamento((limit*page)-limit);
			PagedResponse<CorreiosLoteDTO> correiosLoteDTO = this.correiosLoteService.findTable(searchCriterios);
			return ResponseFactory.ok(correiosLoteDTO);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLote}")
	@ApiOperation(value = "Busca de lotes do Correios por cd")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lote encontrado", response = CorreiosLoteDTO.class),
		@ApiResponse(code = 204, message = "Sem resultado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response get(@ApiParam(value = "Codigo do lote") @PathParam("cdLote") int cdLote) {
		try {
			return ResponseFactory.ok(correiosLoteService.get(cdLote));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro ao buscar lote:" + e.getMessage());
		}
	}
	
}
