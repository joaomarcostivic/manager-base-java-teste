package com.tivic.manager.ptc.protocolosv3.ocorrencia;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Protocolo Ocorrencia", tags = { "ptc" })
@Path("/v3/ptc/protocolos/ocorrencia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloOcorrenciaController {

	private IProtocoloOcorrenciaService protocoloOcorrenciaService;
	private ManagerLog managerLog;
	
	public ProtocoloOcorrenciaController() throws Exception {
		this.protocoloOcorrenciaService = (IProtocoloOcorrenciaService) BeansFactory.get(IProtocoloOcorrenciaService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Fornece lista de Ocorrencias de Protocolo")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Ocorrencias de Protocolos buscadas com sucesso", response = ProtocoloOcorrenciaDTO[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = ProtocoloOcorrenciaDTO[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response find(
		@ApiParam(value = "Código do Documento") @QueryParam("cdDocumento") int cdDocumento
	) {
		try {		
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_documento", cdDocumento, cdDocumento > 0);
			List<ProtocoloOcorrenciaDTO> ocorrencias = protocoloOcorrenciaService.find(cdDocumento);
			return ResponseFactory.ok(ocorrencias);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
