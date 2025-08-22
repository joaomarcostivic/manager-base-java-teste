package com.tivic.manager.mob.pericia;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Gerencia de Resultado de Perícia", tags = { "mob" },
	authorizations = { @Authorization(value = "Bearer Auth", 
	scopes = {@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/mob/pericias")
public class PericiaController {

	@GET
	@Path("/")
	@ApiOperation(
			value = "Retorna lista solicitações de perícia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Solicitações encontrados", response = ResultadoPericiaDTO[].class),
			@ApiResponse(code = 204, message = "Nenhuma solicitação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response find(
			@ApiParam(value = "Data da realização da perícia") @QueryParam("dtSolicitacao") String dtSolicitacao,
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			
			searchCriterios.addCriteriosGreaterDate("B.dt_protocolo", dtSolicitacao, dtSolicitacao != null);
			searchCriterios.addCriteriosMinorDate("B.dt_protocolo", dtSolicitacao, dtSolicitacao != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			searchCriterios.setOrderBy("A.CD_AGENDA_ITEM DESC");

			return ResponseFactory.ok(PericiaServices.find(searchCriterios));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}		
	}
}
