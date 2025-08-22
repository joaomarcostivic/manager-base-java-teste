package com.tivic.manager.mob.orgaoexterno;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Orgão externo", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v3/mob/orgaoexterno")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrgaoExternoController {

	IOrgaoExternoService orgaoExternoService;
	private ManagerLog managerLog;
	
	public OrgaoExternoController() throws Exception {
		orgaoExternoService = (IOrgaoExternoService) BeansFactory.get(IOrgaoExternoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo orgão externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Orgão externo registrado", response = OrgaoExterno.class),
			@ApiResponse(code = 400, message = "Orgão externo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response insert(@ApiParam(value = "") OrgaoExterno orgaoExterno) {
		try {
			orgaoExterno = orgaoExternoService.insert(orgaoExterno);
			return ResponseFactory.ok(orgaoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{cdOrgaoExterno}")
	@ApiOperation(
			value = "Atualiza um orgão externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "orgão externo atualizado", response = OrgaoExterno.class),
			@ApiResponse(code = 400, message = "orgão externo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(
			@ApiParam(value = "Código do orgão externo") @PathParam("cdOrgaoExterno") int cdOrgaoExterno,
			@ApiParam(value = "orgão externo a ser atualizado") OrgaoExterno orgaoExterno) {
		try {			
			orgaoExterno.setCdOrgaoExterno(cdOrgaoExterno);
			orgaoExterno = orgaoExternoService.update(orgaoExterno);
			return ResponseFactory.ok(orgaoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdOrgaoExterno}")
	@ApiOperation(
			value = "Retorna um orgão externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "orgão externo encontrado", response = OrgaoExterno.class),
			@ApiResponse(code = 204, message = "orgão externo não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response get(@ApiParam(value = "Código do Equipamento") @PathParam("cdOrgaoExterno") int cdOrgaoExterno) {
		try {
			OrgaoExterno orgaoExterno = orgaoExternoService.get(cdOrgaoExterno);
			return ResponseFactory.ok(orgaoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de orgão externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "orgão externo encontrados", response = OrgaoExterno[].class),
			@ApiResponse(code = 204, message = "Nenhum orgão externo", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response findPaged(
			@ApiParam(value = "código do orgão externo") @QueryParam("cdOrgaoExterno") int cdOrgaoExterno,
			@ApiParam(value = "nome do orgão externo") @QueryParam("nmOrgaoExterno") String nmOrgaoExterno,
			@ApiParam(value = "sigla do orgão externo") @QueryParam("sgOrgaoExterno") String sgOrgaoExterno,
			@ApiParam(value = "código do tipo de logradouro") @QueryParam("cdTipoLogradouro") int cdTipoLogradouro,
			@ApiParam(value = "nome do logradouro") @QueryParam("nmLogradouro") String nmLogradouro,
			@ApiParam(value = "código do logradouro") @QueryParam("cdCidade") int cdCidade,
			@ApiParam(value = "código do bairro") @QueryParam("cdBairro") int cdBairro,
			@ApiParam(value = "número do cep") @QueryParam("nrCep") String nrCep,
			@QueryParam("limit") int limit,
			@QueryParam("page") int page) {
		try {
			SearchCriterios searchCriterios = new OrgaoExternoSearchBuilder()
						.setCdOrgaoExterno(cdOrgaoExterno)
						.setNmOrgaoExterno(nmOrgaoExterno)
						.setSgOrgaoExterno(sgOrgaoExterno)
						.setCdTipoLogradouro(cdTipoLogradouro)
						.setNmLogradouro(nmLogradouro)
						.setCdCidade(cdCidade)
						.setCdBairro(cdBairro)
						.setNrCep(nrCep)
						.setLimit(limit, page)
					.build();
			PagedResponse<OrgaoExterno> listOrgaoExterno = orgaoExternoService.findPaged(searchCriterios);
			return ResponseFactory.ok(listOrgaoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
		
	@GET
	@Path("/listorgaoexterno")
	@ApiOperation(
			value = "Retorna uma lista de orgão externo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "orgão externo encontrados", response = OrgaoExterno[].class),
			@ApiResponse(code = 204, message = "Nenhum orgão externo", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response find(
			@ApiParam(value = "código do orgão externo") @QueryParam("cdOrgaoExterno") int cdOrgaoExterno,
			@ApiParam(value = "nome do orgão externo") @QueryParam("nmOrgaoExterno") String nmOrgaoExterno,
			@ApiParam(value = "sigla do orgão externo") @QueryParam("sgOrgaoExterno") String sgOrgaoExterno)
	{
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("cd_orgao_externo", cdOrgaoExterno, cdOrgaoExterno > 0);
			searchCriterios.addCriteriosEqualString("nm_orgao_externo", nmOrgaoExterno, nmOrgaoExterno != null);
			searchCriterios.addCriteriosEqualString("sg_orgao_externo", sgOrgaoExterno, sgOrgaoExterno != null);
			List<OrgaoExterno> listOrgaoExterno = orgaoExternoService.find(searchCriterios);
			return ResponseFactory.ok(listOrgaoExterno);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}