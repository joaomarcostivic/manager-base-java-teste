package com.tivic.manager.ptc.protocolosv3.cetran;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
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

@Api(value = "Protocolo Cetran", tags = {"ptc"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/ptc/protocolos/cetran")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class CetranController {
	private CetranService cetranService;
	private ManagerLog managerLog;
	
	public CetranController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.cetranService = (CetranService) BeansFactory.get(CetranService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo Cetran.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Cetran Criado", response = ProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ProtocoloInsertDTO protocolo) {
		try {
			ProtocoloDTO protocoloInserido = cetranService.insert(protocolo);
			return ResponseFactory.ok(protocoloInserido);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}	

	@PUT
	@Path("")
	@ApiOperation(value = "Atualização do Protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo atualizado", response = DadosProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(ProtocoloDTO protocolo) {
		try {
			protocolo = cetranService.update(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
