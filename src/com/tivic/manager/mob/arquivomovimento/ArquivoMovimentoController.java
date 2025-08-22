package com.tivic.manager.mob.arquivomovimento;

import java.sql.Types;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
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

@Api(value = "AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/arquivomovimento")
@Produces(MediaType.APPLICATION_JSON)
public class ArquivoMovimentoController {
	private IArquivoMovimentoService arquivoMovimentoService;
	private ManagerLog managerLog;	
	
	public ArquivoMovimentoController() throws Exception {
		arquivoMovimentoService = (IArquivoMovimentoService) BeansFactory.get(IArquivoMovimentoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/pendentes")
	@ApiOperation(value = "Retorna Arquivo Movimento")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Movimento pendente encontrado"),
		@ApiResponse(code = 204, message = "Nenhum movimento pendente"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response getAllMovimentosPendentes(
		@ApiParam(value = "ID do Ait") @QueryParam("idAit") String idAit,
		@ApiParam(value = "Status do Movimento") @QueryParam("tpStatus") Integer tpStatus
	) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("C.id_ait","%"  + idAit + "%", ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosEqualInteger("B.tp_status", tpStatus, tpStatus != null);
			return ResponseFactory.ok(arquivoMovimentoService.getMovimentoPendente(searchCriterios));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
