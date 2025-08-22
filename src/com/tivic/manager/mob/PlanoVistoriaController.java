package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Api(value = "PlanoVistoria", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/planosvistoria")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlanoVistoriaController {
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Retorna uma lista de planos de vistoria"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Planos encontrados"),
		@ApiResponse(code = 204, message = "Nenhum plano"),
		@ApiResponse(code = 204, message = "Não existe plano com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response find (
		@ApiParam(value = "Quantidade de paginas") @QueryParam("page") int nrPagina,
		@ApiParam(value = "Quantidade de registros") @QueryParam("limit") int nrLimite,
		@ApiParam(value = "Codigo do plano") @QueryParam("cdPlanoVistoria") int cdPlanoVistoria,
		@ApiParam(value = "Nome do plano") @QueryParam("nmPlanoVistoria") String nmPlanoVistoria,
		@ApiParam(value = "Tipo de concessao") @QueryParam("tpConcessao") int tpConcessao
	) {
		try {
			Criterios crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdPlanoVistoria != 0) {
				crt.add("A.cd_plano_vistoria", Integer.toString(cdPlanoVistoria), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nmPlanoVistoria != null) {
				crt.add("A.nm_plano_vistoria", nmPlanoVistoria, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(tpConcessao >= 0) {
				crt.add("A.tp_concessao", Integer.toString(tpConcessao), ItemComparator.EQUAL, Types.INTEGER);				
			}

			ResultSetMap rsm = PlanoVistoriaServices.find(crt);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum plano encontrado");
			
			return ResponseFactory.ok(new ResultSetMapper<PlanoVistoria>(rsm, PlanoVistoria.class));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um plano de vistoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Plano encontrado", response = PlanoVistoria.class),
			@ApiResponse(code = 204, message = "Plano não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Codigo do Plano de Vistoria") @PathParam("id") int cdPlanoVistoria) {
		try {
			if(cdPlanoVistoria == 0) 
				return ResponseFactory.badRequest("Plano é nulo ou invalido");
			
			PlanoVistoria planoVistoria = PlanoVistoriaDAO.get(cdPlanoVistoria);
			
			if(planoVistoria==null) 
				return ResponseFactory.noContent("Nenhum plano encontrado");
			
			return ResponseFactory.ok(planoVistoria);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

}
