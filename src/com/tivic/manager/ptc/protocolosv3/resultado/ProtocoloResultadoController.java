package com.tivic.manager.ptc.protocolosv3.resultado;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos/resultado")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloResultadoController {

	private IProtocoloResultadoService resultadoService;
	private ManagerLog managerLog;
	
	public ProtocoloResultadoController() throws Exception {
		this.resultadoService = (IProtocoloResultadoService) BeansFactory.get(IProtocoloResultadoService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	

	@POST
	@Path("/deferir")
	@ApiOperation(value = "Deferir Resultado do Protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Resultado Criado", response = ResultadoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do resultado", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response deferir(ResultadoDTO protocolo) {
		try {
			protocolo = resultadoService.deferir(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/indeferir")
	@ApiOperation(value = "Indeferir Resultado do Protocolo.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Resultado Criado", response = ResultadoDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do resultado", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response indeferir(ResultadoDTO protocolo) {
		try {
			protocolo = resultadoService.indeferir(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelamento/julgamento")
	@ApiOperation(value = "Cancelamento de julgamento")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Cancelamento de julgamento", response = ProtocoloDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response cancelarJulgamento( @ApiParam(value = "Documento DTO") ProtocoloDTO protocoloDTO) {
		try {
			ProtocoloDTO protocolo = resultadoService.cancelarJulgamento(protocoloDTO);
			return ResponseFactory.ok(protocolo);
		} catch(ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
}
	
