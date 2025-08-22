package com.tivic.manager.ptc.protocolosv3.advertencia;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.resultado.ProtocoloResultadoService;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
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

@Api(value = "Protocolo Defesa com Adventencia", tags = {"ptc"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/ptc/protocolos/defesa-advertencia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DefesaAdvertenciaController {
	private DefesaAdvertenciaService defesaAdvertenciaService;
	private ManagerLog managerLog;
	
	public DefesaAdvertenciaController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.defesaAdvertenciaService = (DefesaAdvertenciaService) BeansFactory.get(DefesaAdvertenciaService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo de Defesa com Advertencia.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Defesa com Advertencia criado", response = ProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ProtocoloInsertDTO protocolo) {
		try {
			ProtocoloDTO protocoloInserido = defesaAdvertenciaService.insert(protocolo);
			return ResponseFactory.ok(protocoloInserido);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
	
	@POST
	@Path("/deferido")
	@ApiOperation(value = "Julgamento deferido do Protocolo de Defesa com Advertencia.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Julgamento deferido Protocolo Defesa com Advertencia criado", response = ResultadoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response deferido(ResultadoDTO resultadoDto) {
		try {
			ResultadoDTO protocoloInserido = new ProtocoloResultadoService().deferir(resultadoDto);
			return ResponseFactory.ok(protocoloInserido);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
	
	@POST
	@Path("/indeferido")
	@ApiOperation(value = "Julgamento indeferido do Protocolo de Defesa com Advertencia.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Julgamento indeferido Defesa com Advertencia criado", response = ResultadoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response indeferido(ResultadoDTO resultadoDto) {
		try {
			ResultadoDTO protocoloInserido = new ProtocoloResultadoService().indeferir(resultadoDto);
			return ResponseFactory.ok(protocoloInserido);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	
}
