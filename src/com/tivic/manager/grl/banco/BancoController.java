package com.tivic.manager.grl.banco;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Banco;
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

@Api(value = "Bancos", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/grl/bancos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BancoController {
	
	IBancoService bancoService;
	private ManagerLog managerLog;
	
	public BancoController() throws Exception {
		bancoService = (IBancoService) BeansFactory.get(IBancoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo banco"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Banco registrado", response = Banco.class),
			@ApiResponse(code = 400, message = "Banco inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response create(@ApiParam(value = "Banco a ser registrado") Banco banco) {
		try {		
			banco = bancoService.insert(banco);
			return ResponseFactory.created(banco);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um banco"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Banco atualizado", response = Banco.class),
			@ApiResponse(code = 400, message = "Banco inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(
			@ApiParam(value = "Código do banco") @PathParam("id") int cdBanco,
			@ApiParam(value = "Banco a ser atualizado") Banco banco) {
		try {			
			banco.setCdBanco(cdBanco);
			banco = bancoService.update(banco);
			return ResponseFactory.ok(banco);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um banco"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Banco encontrado", response = Banco.class),
			@ApiResponse(code = 204, message = "Banck não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response get(@ApiParam(value = "Código do banco") @PathParam("id") int cdBanco) {
		try {
			Banco banco = bancoService.get(cdBanco);
			return ResponseFactory.ok(banco);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de bancos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Bancos encontrados", response = Banco[].class),
			@ApiResponse(code = 204, message = "Nenhum banco encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response find(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int nrLimite,
			@ApiParam(value = "Número da página") @QueryParam("page") int nrPagina,
			@ApiParam(value = "ID do banco") @QueryParam("idBanco") String idBanco,
			@ApiParam(value = "Número do banco") @QueryParam("nrBanco") String nrBanco,
			@ApiParam(value = "Nome do banco") @QueryParam("nmBanco") String nmBanco,
			@ApiParam(value = "Procura por bancos conveniados") @QueryParam("bancoConveniado") boolean bancoConveniado
				) {
		try {
			SearchCriterios searchCriterios = new BancoSearch()
					.setIdBanco(idBanco)
					.setNrBanco(nrBanco)
					.setNmBanco(nmBanco)
					.setLgBancoConveniado(bancoConveniado)
					.setLimite(nrLimite)
					.setDeslocamento(nrLimite, nrPagina)
					.build();
			
			PagedResponse<Banco> bancos = bancoService.find(searchCriterios);
			return ResponseFactory.ok(bancos);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
