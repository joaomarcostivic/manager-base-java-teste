package com.tivic.manager.mob.orgao;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Orgao;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Orgão", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})

@Path("/v3/mob/orgaos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrgaoController {
	
	private ManagerLog managerLog;
	private IOrgaoService orgaoService;
	
	public OrgaoController() throws Exception {
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Fornece o orgão."
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Orgão encontrado."),
		@ApiResponse(code = 204, message = "Nenhum orgão."),
		@ApiResponse(code = 500, message = "Erro no servidor.")
	})
	public Response find() {
		try {
			managerLog.showLog(new InfoLogBuilder("[GET]", "Buscando o orgão.").build());
			Orgao orgao = orgaoService.getOrgaoUnico();
			managerLog.showLog(new InfoLogBuilder("[GET]", "A busca foi realizada com sucesso.").build());
			return ResponseFactory.ok(orgao);	
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

}