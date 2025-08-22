package com.tivic.manager.ptc.protocolosv3.documento.situacao;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.protocolosv3.situacaodocumento.SituacaoDocumentoDTO;
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

@Api(value = "Situacao Documento", tags = {"ptc"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/ptc/protocolos/situacao-documento")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SituacaoDocumentoController {
	private ISituacaoDocumentoService situacaoDocumentoService;
	private ManagerLog managerLog;
	
	public SituacaoDocumentoController() throws Exception {
		situacaoDocumentoService = (ISituacaoDocumentoService) BeansFactory.get(ISituacaoDocumentoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Situações buscadas com sucesso", response = SituacaoDocumento[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = SituacaoDocumento[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getAll() {
		try {		
			List<SituacaoDocumento> situacaoDocumentoList = situacaoDocumentoService.getAll();
			return ResponseFactory.ok(situacaoDocumentoList);
		}
		catch(Exception e) {
			managerLog.error("Erro ao buscar dados das Situações:", e.getMessage());
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@GET
	@Path("/situacoes")
	@ApiOperation(value = "Busca situações de documento", notes ="Endpoint para busca situações de documento com conexão personalidada de DB.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Situações buscadas com sucesso", response = SituacaoDocumento[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = SituacaoDocumento[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getAllSituacao() {
		try {		
			List<SituacaoDocumento> situacaoDocumentoList = situacaoDocumentoService.findAllSituacaoDocumento();
			return ResponseFactory.ok(situacaoDocumentoList);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("{cdDocumento}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Situacao do Protocolo buscado com sucesso", response = SituacaoDocumentoDTO.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = SituacaoDocumentoDTO.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getSituacaoDocumento(
			@ApiParam( value = "Nome do Tipo de Documento") @PathParam("cdDocumento") Integer cdDocumento
	) {
	   try {
		   SituacaoDocumentoDTO situacaoDocumento = this.situacaoDocumentoService.getDocumentoByOcorrencia(cdDocumento);
			return ResponseFactory.ok(situacaoDocumento);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/credencial/{cdDocumento}")
	@ApiOperation(value = "")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Situacao do Protocolo credencial buscado com sucesso", response = SituacaoDocumentoDTO.class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = SituacaoDocumentoDTO.class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response getSituacaoDocumentoCredencial(
			@ApiParam( value = "Nome do Tipo de Documento") @PathParam("cdDocumento") Integer cdDocumento
	) {
	   try {
		   SituacaoDocumentoDTO situacaoDocumento = this.situacaoDocumentoService.getSituacaoDocumento(cdDocumento);
			return ResponseFactory.ok(situacaoDocumento);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
