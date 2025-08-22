package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

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

@Api(value = "Órgãos", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/orgaos")
@Produces(MediaType.APPLICATION_JSON)
public class OrgaoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo Órgão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Órgão registrado", response = Orgao.class),
			@ApiResponse(code = 400, message = "Órgão inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Órgão a ser registrado") Orgao orgao) {
		try {			
			orgao.setCdOrgao(0);
			
			Result result = OrgaoServices.save(orgao);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Orgao)result.getObjects().get("ORGAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Registra um novo Órgão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Órgão atualizado", response = Orgao.class),
			@ApiResponse(code = 400, message = "Órgão inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do Órgão") @PathParam("id") int cdOrgao,
			@ApiParam(value = "Órgão a ser atualizado") Orgao orgao) {
		try {			
			Result result = OrgaoServices.save(orgao);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Orgao) result.getObjects().get("ORGAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Órgão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Órgão encontrado", response = Orgao.class),
			@ApiResponse(code = 204, message = "Órgão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Órgão") @PathParam("id") int cdOrgao) {
		try {
			System.out.println(cdOrgao);
			OrgaoDTO orgaoDto = new OrgaoDTO.Builder(cdOrgao, true).build();
			if(orgaoDto == null) {
				return ResponseFactory.noContent("Nenhum órgão encontrado");

			}
			
			return ResponseFactory.ok(orgaoDto);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Órgãos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Órgãos encontrado", response = Orgao[].class),
			@ApiResponse(code = 204, message = "Órgãos não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Nome do Órgão") @QueryParam("orgao") String nmOrgao,
			@ApiParam(value = "Id do Órgão") @QueryParam("id") String idOrgao,
			@ApiParam(value = "Código da cidade") @QueryParam("cidade") int cdCidade,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			if(nmOrgao != null) {
				criterios.add("A.nm_orgao", nmOrgao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(idOrgao != null) {
				criterios.add("A.id_orgao", idOrgao, ItemComparator.LIKE, Types.VARCHAR);
			}
			if(cdCidade > 0) {
				criterios.add("A.cd_cidade", Integer.toString(cdCidade), ItemComparator.EQUAL, Types.INTEGER);
			}		
			
			ResultSetMap rsm = OrgaoServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum órgão encontrado");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	/** ***********************************************************************
	 * 
	 */
	@GET
	@Path("/{id}/equipamentos")
	@ApiOperation(
			value = "Retorna equipamentos de um Órgão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamentos encontrados", response = Equipamento[].class),
			@ApiResponse(code = 204, message = "Nenhum equipamento encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getEquipamentos(@ApiParam(value = "Código do Órgão") @PathParam("id") int cdOrgao) {
		try {			
			ResultSetMap rsm = EquipamentoServices.find(new Criterios("cd_orgao", Integer.toString(cdOrgao), ItemComparator.EQUAL, Types.INTEGER));
			if(rsm == null || rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Nenhum equipamento encontrado");
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/agentes")
	@ApiOperation(
			value = "Retorna agetes de um Órgão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Agentes encontrados", response = Agente[].class),
			@ApiResponse(code = 204, message = "Nenhum agente encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAgentes(@ApiParam(value = "Código do Órgão") @PathParam("id") int cdOrgao) {
		try {
			Orgao orgao = OrgaoDAO.get(cdOrgao);
			if(orgao == null)
				return ResponseFactory.noContent("Nenhum órgão encontrado");
			
			ResultSetMap rsm = AgenteServices.getAllByIdOrgao(orgao.getIdOrgao());
			
			if(rsm == null || rsm.getLines().size() <= 0)
				return ResponseFactory.noContent("Nenhum agente encontrado");
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	

}
