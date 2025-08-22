package com.tivic.manager.grl.equipamento.radar.v3;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Listagem de radares", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/grl/radar")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RadarController {
	
	IRadarService radarService;
	ManagerLog managerLog;
	
	public RadarController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.radarService = (IRadarService) BeansFactory.get(IRadarService.class);
	}
	
	@GET
	@Path("/radarlocalizacao")
	@ApiOperation(
			value = "Retorna radares e suas localizações"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Radares encontrada.", response = RadarLocalizacaoDTO[].class),
			@ApiResponse(code = 204, message = "Radares não encontrada.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	}) 
	public Response buscarRadarLocalizacao(){
		try {
			return ResponseFactory.ok(radarService.findRadar());	
		} catch (NoContentException e) {
			managerLog.showLog(e);
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
