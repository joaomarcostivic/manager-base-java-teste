package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
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
import sol.util.Result;

@Api(value = "ErrosRetorno", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/errosretorno")
@Produces(MediaType.APPLICATION_JSON)
public class ErroRetornoController {

	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um erro retorno"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Erro retorno encontrado", response = ErroRetorno.class),
			@ApiResponse(code = 204, message = "Erro retorno não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Número do erro") @PathParam("id") String nrErro) {
		try {

			ErroRetorno erroRetorno = ErroRetornoDAO.get(nrErro);
			if(erroRetorno == null) {
				return ResponseFactory.noContent("Nenhum erro retorno encontrado");
			}
			
			return ResponseFactory.ok(erroRetorno);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna os erros retorno"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Erros retorno encontrados", response = ErroRetorno[].class),
			@ApiResponse(code = 204, message = "Erros retorno não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll() {
		try {
			
			Orgao orgao = OrgaoServices.getOrgaoUnico();
			Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
			Estado estado = EstadoDAO.get(cidade.getCdEstado());
					
			Criterios criterios = new Criterios("uf", estado.getSgEstado(), ItemComparator.EQUAL, Types.VARCHAR);
			
			ResultSetMap rsm = ErroRetornoServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum erro retorno encontrado");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<ErroRetorno>(rsm, ErroRetorno.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/filtros")
	@ApiOperation(
			value = "Retorna os erros retorno"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Erros retorno encontrados", response = ErroRetorno[].class),
			@ApiResponse(code = 204, message = "Erros retorno não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response find(
			@ApiParam(value = "Descrição do Erro") @QueryParam("dsErro") String dsErro,
			@ApiParam(value = "Código do Erro") @QueryParam("nrErro") String nrErro
			) {
		try {
			
			Orgao orgao = OrgaoServices.getOrgaoUnico();
			Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
			Estado estado = EstadoDAO.get(cidade.getCdEstado());
					
			Criterios criterios = new Criterios("uf", estado.getSgEstado(), ItemComparator.EQUAL, Types.VARCHAR);
			
			if(dsErro != null)
				criterios.add("ds_erro", dsErro, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			if(nrErro != null)
				criterios.add("nr_erro", nrErro, ItemComparator.EQUAL, Types.CHAR);
			
			ResultSetMap rsm = ErroRetornoServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum erro retorno encontrado");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<ErroRetorno>(rsm, ErroRetorno.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{nrErro}")
	@ApiOperation(
			value = "Atualiza o retorno para os movimentos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retorno atualizado", response = ErroRetorno[].class),
			@ApiResponse(code = 204, message = "Erro retorno não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(@ApiParam(value = "Código do erro a ser atualizado") @PathParam("nrErro") String nrErro,
								  @ApiParam(value = "Erro Retorno a ser atualizado") ErroRetorno erroRetorno
			) {
		try {
			
			Orgao orgao = OrgaoServices.getOrgaoUnico();
			Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
			Estado estado = EstadoDAO.get(cidade.getCdEstado());
					
			erroRetorno.setUf(estado.getSgEstado());
			erroRetorno.setNrErro(nrErro);
			
			Result result = ErroRetornoServices.updateRetorno(erroRetorno);
			
			return ResponseFactory.ok(result);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
