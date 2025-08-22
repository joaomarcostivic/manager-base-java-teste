package com.tivic.manager.ptc.protocolosv3.jari;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.search.DadosProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.search.ProtocoloSearchDTO;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Protocolo Jari", tags = {"ptc"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/ptc/protocolos/jari")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class JariController {
	private JariService jariService;
	private ManagerLog managerLog;
	
	public JariController() throws Exception {
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.jariService = (JariService) BeansFactory.get(JariService.class);
	}
	
	@GET
	@Path("/grafico")
	@ApiOperation(value = "Retorna AITs com JARI")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Protocolos buscados com sucesso", response = ProtocoloSearchDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = AitMovimentoDocumentoDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Tipo de Documento") @QueryParam("tpStatus") int tpStatus,
		@ApiParam(value = "Data Inicial") @QueryParam("dataInicial") String dtInicial,
		@ApiParam(value = "Data Final") @QueryParam("dataFinal") String dtFinal,
		@ApiParam(value = "Situação do Documento") @QueryParam("cdSituacaoDocumento") int situacaoDocumento
	) {
		try {		
			SearchCriterios searchCriterios = new JariSearchBuilder()
					.setTpStatus()
					.setDtProtocoloInicial(dtInicial)
					.setDtProtocoloFinal(dtFinal)
					.setCdSituacaoDocumento(situacaoDocumento)
					.build();
			
			PagedResponse<ProtocoloSearchDTO> protocolos = jariService.findJari(searchCriterios);
			return ResponseFactory.ok(protocolos);
		} catch (NoContentException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(value = "Criação do Protocolo Jari.")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Protocolo Jari Criado", response = ProtocoloDTO.class),
            @ApiResponse(code = 400, message = "Erro na inserção do protocolo", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response create(ProtocoloInsertDTO protocolo) {
		try {
			ProtocoloDTO protocoloInserido = jariService.insert(protocolo);
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
			protocolo = jariService.update(protocolo);
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
