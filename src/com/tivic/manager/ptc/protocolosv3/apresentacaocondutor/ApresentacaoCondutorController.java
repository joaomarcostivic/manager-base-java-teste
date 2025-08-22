package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
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
@Path("/v3/ptc/protocolos/fici")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApresentacaoCondutorController {
	
	private ApresentacaoCondutorService apresentacaoCondutorService;
	private ManagerLog managerLog;
	
	public ApresentacaoCondutorController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.apresentacaoCondutorService = (ApresentacaoCondutorService) BeansFactory.get(ApresentacaoCondutorService.class);
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo FICI.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo FICI Criado", response = ApresentacaoCondutorDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ApresentacaoCondutorInsertDTO protocolo) {
		try {
			ProtocoloDTO protocoloInserido = apresentacaoCondutorService.insert(protocolo);
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
	@ApiOperation(value = "Atualização da Apresentação de Condutor.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Apresentação de Condutor atualizada", response = ApresentacaoCondutorDTO.class),
            @ApiResponse(code = 400, message = "Erro na atualização da Apresentação de Condutor", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response update(ApresentacaoCondutorDTO protocolo) {
		try {
			protocolo = apresentacaoCondutorService.update(protocolo);
			return ResponseFactory.ok(protocolo);
		} catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/documento/")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolo buscado com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getDocumento(
		@ApiParam(value = "Código de Documento") @QueryParam("cdDocumento") int cdDocumento,
		@ApiParam(value = "Código da Apresentação de Condutor") @QueryParam("cdApresentacaoCondutor") int cdApresentacaoCondutor
	) {
	   try {
		    ApresentacaoCondutorDTO protocolo = apresentacaoCondutorService.get(cdDocumento, cdApresentacaoCondutor);
			return ResponseFactory.ok(protocolo);
		}
		catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/cancelamento")
	@ApiOperation(value = "Cancelamento de Apresentação de Condutor")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Apresentação de Condutor cancelada", response = ApresentacaoCondutorDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response cancelamento(
			@ApiParam(value = "Apresentação Condutor DTO") ApresentacaoCondutorDTO documento) {
		try {
			ProtocoloDTO protocolo = apresentacaoCondutorService.cancelar(documento);
			return ResponseFactory.ok(protocolo);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
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
			protocolo = apresentacaoCondutorService.deferir(protocolo);
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
			protocolo = apresentacaoCondutorService.indeferir(protocolo);
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
