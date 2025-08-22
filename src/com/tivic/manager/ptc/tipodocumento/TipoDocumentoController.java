package com.tivic.manager.ptc.tipodocumento;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "TipoDocumento", tags = {"ptc"})
@Path("/v3/ptc/tipodocumento")
@Produces(MediaType.APPLICATION_JSON)
public class TipoDocumentoController {
	private ITipoDocumentoService tipoDocumentoService;
	
	public TipoDocumentoController() throws Exception{
		this.tipoDocumentoService = (ITipoDocumentoService) BeansFactory.get(ITipoDocumentoService.class);
	}
	
	@GET
    @Path("/{id}")
    @ApiOperation(
            value = "Retorna um tipo de documento"
        )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tipo de Documento encontrado", response = TipoDocumento.class),
            @ApiResponse(code = 204, message = "Há algum parâmetro inválido", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
        })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTpDocumento(@ApiParam(value = "Código do Tipo de documento") @PathParam("id") int cdTipoDocumento) {
        try {
            TipoDocumento tipoDocumento = tipoDocumentoService.get(cdTipoDocumento);
            
            if(tipoDocumento == null)
                return ResponseFactory.noContent("Nenhum tipo de documento encontrado");
            
            return ResponseFactory.ok(tipoDocumento);
        } catch(IllegalArgumentException e) {
            return ResponseFactory.badRequest(e.getMessage());
        } catch(Exception e) {
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }
	
	@GET
	@Path("/disponiveis/{cdAit}")
	@ApiOperation(value = "Retorna os possíveis tipos de Protocolo para um determinado AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "AIT Encontrado", response = TipoProtocolo.class),
			@ApiResponse(code = 400, message = "AIT Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getTiposProtocoloDisponiveis(@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit) {
		try {
			List<TipoProtocolo> protocolosDisponiveis = tipoDocumentoService.getTiposDisponiveis(cdAit);

			return ResponseFactory.ok(protocolosDisponiveis);
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.internalServerError(ex.getMessage());
		}
	}
}
