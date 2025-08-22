package com.tivic.manager.mob;

import java.sql.Types;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

@Api(value = "InfracaoTransporte", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/infracoes-transporte")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfracaoTransporteController {
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infração encontrada", response = InfracaoTransporte.class),
			@ApiResponse(code = 204, message = "Infração nÃ£o encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao) {
		try {
			InfracaoTransporte _infracao = InfracaoTransporteDAO.get(cdInfracao);
			if(_infracao == null) {
				return ResponseFactory.notFound("Nenhuma infração encontrada");
			}
			
			return ResponseFactory.ok(_infracao);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Infrações"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infrações encontradas", response = InfracaoTransporte[].class),
			@ApiResponse(code = 204, message = "Nenhuma infração encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value  = "Natureza da Infracao") @QueryParam("nmNatureza") String nmNatureza,
			@ApiParam(value  = "Descricao da Infracao") @QueryParam("dsInfracao") String dsInfracao,
			@ApiParam(value  = "Infracao ATIVO/INATIVO") @QueryParam("ativo") @DefaultValue("true") Boolean isActive,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword,
			@ApiParam(value  = "Busca núemro da Infracao") @QueryParam("nrInfracao") String nrInfracao,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
	
			if(nmNatureza != null) {
				criterios.add("nm_natureza", nmNatureza, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(dsInfracao != null) {
				criterios.add("ds_infracao", dsInfracao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nrInfracao != null) {
				criterios.add("nr_infracao", nrInfracao, ItemComparator.LIKE_ANY,  Types.VARCHAR);
			}
			
			if(keyword != null) {
				criterios.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			ResultSetMap rsm = InfracaoTransporteServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
						
			List<InfracaoTransporte> infracoes = new ResultSetMapper<InfracaoTransporte>(rsm, InfracaoTransporte.class).toList();
			
			
			
			return ResponseFactory.ok(infracoes);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
