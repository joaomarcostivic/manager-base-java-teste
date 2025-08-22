package com.tivic.manager.mob.colaborador;

import javax.ws.rs.BadRequestException;
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

import com.tivic.manager.mob.colaborador.builders.ColaboradorSearchBuilder;
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

@Api(value = "Colaborador", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/colaboradores")
@Produces(MediaType.APPLICATION_JSON)
public class ColaboradorController {
	
	private IColaboradorService service;
	private ManagerLog managerLog;
	
	public ColaboradorController() throws Exception {
		service = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@POST
	@Path("")
	@ApiOperation(value = "Cadastra um colaborador")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Cadastro realizado com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "O colaborador possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response create(@ApiParam(value = "Colaborador a ser registrado", required = true) ColaboradorDTO colaborador) {
		try {
			return ResponseFactory.ok(service.create(colaborador));
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna colaboradores")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Colaboradores encontrados", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhum colaborador encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response findColaboradores(
			@ApiParam(value = "Nome do colaborador") @QueryParam("nmColaborador") String nmColaborador,
			@ApiParam(value = "Vinculo") @QueryParam("cdVinculo") @DefaultValue("-1") int cdVinculo,
			@ApiParam(value = "Situação") @QueryParam("stVinculo") @DefaultValue("-1") int stVinculo,
			@ApiParam(value = "CPF do colaborador") @QueryParam("cpfColaborador") String cpfColaborador,
			@ApiParam(value = "Data Inicial") @QueryParam("dtVinculoInical") String dtVinculoInicial,
			@ApiParam(value = "Data Final") @QueryParam("dtVinculoFinal") String dtVinculoFinal,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new ColaboradorSearchBuilder()
					.setNmPessoa(nmColaborador)
					.setNrCpf(cpfColaborador)
					.setDtVinculoInicial(dtVinculoInicial)
					.setDtVinculoFinal(dtVinculoFinal)
					.setCdVinculo(cdVinculo)
					.setStVinculo(stVinculo)
					.setQtDelocamento(limit, page)
					.setQtLimit(limit)
				.build();
			return ResponseFactory.ok(service.findColaboradores(searchCriterios));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(value = "Edita um colaborador com base no id e dados passados")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Edição realizada com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "O colaborador possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(@ApiParam(value = "Colaborador a ser atualizado", required = true) ColaboradorDTO colaborador) {
		try {
			return ResponseFactory.ok(service.update(colaborador));
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdPessoa}")
	@ApiOperation(value = "Retorna um colaborador com base no cdPessoa")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Colaborador encontrado", response = Result.class),
			@ApiResponse(code = 204, message = "Colaborador Não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response get(@ApiParam(value = "id do colaborador") @PathParam("cdPessoa") int cdPessoa) {
		try {
			return  ResponseFactory.ok(service.get(cdPessoa));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdPessoa}/inativar")
	@ApiOperation(value = "Inativa um colaborador")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Edição realizada com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "O colaborador possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response inativar(@ApiParam(value = "Colaborador a ser atualizado", required = true) ColaboradorDTO colaboradorDTO) {
		try {
			return ResponseFactory.ok(service.inativar(colaboradorDTO));
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{cdPessoa}/ativar")
	@ApiOperation(value = "Inativa um colaborador")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Edição realizada com sucesso", response = Result.class),
			@ApiResponse(code = 400, message = "O colaborador possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response ativar(@ApiParam(value = "Colaborador a ser atualizado", required = true) ColaboradorDTO colaboradorDTO) {
		try {
			return ResponseFactory.ok(service.ativar(colaboradorDTO));
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}