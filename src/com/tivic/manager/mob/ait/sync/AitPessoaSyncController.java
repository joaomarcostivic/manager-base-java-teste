package com.tivic.manager.mob.ait.sync;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.ait.aitpessoa.AitPessoa;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.log.builders.InfoLogBuilder;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Sincronização AitPessoa", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/sync/aitpessoa")
@Produces(MediaType.APPLICATION_JSON)
public class AitPessoaSyncController {

	private ManagerLog managerLog;
	private IAitPessoaSyncService aitPessoaSyncService;
	
	public AitPessoaSyncController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		aitPessoaSyncService = (IAitPessoaSyncService) BeansFactory.get(IAitPessoaSyncService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Inserção Geral de sincronização de AITPessoa")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros salvos", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response SyncReceiveAitPessoa(@ApiParam(value = "AitPessoa") List<AitPessoa> aitsList){
		try {	
			managerLog.showLog(new InfoLogBuilder("[POST] /sync", "Iniciando sincronização de AITPessoa...").build());
			List<AitSyncResponse> list = aitPessoaSyncService.syncReceive(aitsList);
			managerLog.showLog(new InfoLogBuilder("[POST] /sync", "Sincronização concluída...").build());
			return ResponseFactory.created(list);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
