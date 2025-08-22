package com.tivic.manager.grl.vinculo;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Vinculo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Vinculo", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/grl/vinculo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VinculoController {
	private IVinculoService vinculoService;
	private ManagerLog managerLog;
	
	public VinculoController() throws Exception {
		vinculoService = (IVinculoService) BeansFactory.get(IVinculoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/{cdVinculo}")
	@ApiOperation(value = "Busca vinculos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Vinculo encontrado com sucesso", response = Vinculo.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Vinculo.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response get(@ApiParam(value = "Código do vinculo") @PathParam("cdVinculo") int cdVinculo) {
		try {
			return ResponseFactory.ok(this.vinculoService.get(cdVinculo));
		}catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Busca vinculos")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Vinculo encontrado com sucesso", response = Vinculo.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = Vinculo.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find() {
		try {
			return ResponseFactory.ok(this.vinculoService.find());
		}catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
