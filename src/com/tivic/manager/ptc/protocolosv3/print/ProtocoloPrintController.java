package com.tivic.manager.ptc.protocolosv3.print;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos/impressoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloPrintController {
	
	private IProtocoloPrintService protocoloPrintService;
	
	public ProtocoloPrintController() throws Exception {
		this.protocoloPrintService = (IProtocoloPrintService) BeansFactory.get(IProtocoloPrintService.class);
	}

	@GET
	@Path("/{id}/documentos/{idDocumento}/impressao")
	@ApiOperation( value = "Impressão de protocolo lançado")
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Documento impresso", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response print(
			@ApiParam( value = "Id do AIT") @PathParam( "id" ) int cdAit, 
			@ApiParam( value = "Id do Documento" ) @PathParam( "idDocumento" ) int cdDocumento) {			
		try {
			byte[] protocoloPDF = this.protocoloPrintService.imprimirProtocolo(cdAit, cdDocumento);
			return ResponseFactory.ok(protocoloPDF);
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}		
	}
	
}
