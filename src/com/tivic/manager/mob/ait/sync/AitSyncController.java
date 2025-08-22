package com.tivic.manager.mob.ait.sync;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.ait.sync.entities.AitSyncResponse;
import com.tivic.manager.mob.ait.sync.entities.SyncResponse;
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

@Api(value = "Sincronização Ait", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/sync/ait")
@Produces(MediaType.APPLICATION_JSON)
public class AitSyncController {
	
	private ManagerLog managerLog;
	private IAitSyncService aitSyncService;
	
	public AitSyncController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		aitSyncService = (IAitSyncService) BeansFactory.get(IAitSyncService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Inserção Geral de sincronização de AITs")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros salvos", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response SyncReceiveAit(@ApiParam(value = "Ait") List<Ait> aitsList){
		try {	
			managerLog.showLog(new InfoLogBuilder("[POST] /sync", "Iniciando sincronização de AITs...").build());
			List<AitSyncResponse> list = aitSyncService.syncReceive(aitsList);
			managerLog.showLog(new InfoLogBuilder("[POST] /sync", "Sincronização concluída...").build());
			return ResponseFactory.created(list);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Sincronização Geral")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Registros encontrados", response = ResponseBody.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ResponseBody.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response SyncGetAll(@ApiParam(value = "Ait") @QueryParam("cdAgente") int cdAgente){
		try {
			managerLog.showLog(new InfoLogBuilder("[GET] /sync", "Requisição de solicitação de sincronização...").build());
			List<SyncResponse<?>> syncResponseList = aitSyncService.sync(cdAgente);
			managerLog.showLog(new InfoLogBuilder("[GET] /sync", "Sincronização concluida com sucesso...").build());
			return ResponseFactory.ok(syncResponseList);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}	
}
