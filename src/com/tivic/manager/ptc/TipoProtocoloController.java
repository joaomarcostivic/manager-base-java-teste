package com.tivic.manager.ptc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.mob.AitDAO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "TipoProtocolo", tags = { "ptc" })
@Path("/v2/ptc/tiposprotocolo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoProtocoloController {
	
	private final TipoProtocoloServices tipoProtocoloServices;
	
	public TipoProtocoloController() {
		tipoProtocoloServices = new TipoProtocoloServices();
	}
	
	@GET
	@Path("/{cdAit}")
	@ApiOperation(value = "Retorna os possíveis tipos de Protocolo para um determinado AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "AIT Encontrado", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "AIT Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getTiposProtocoloDisponiveis(@ApiParam(value = "Código do AIT") @PathParam("cdAit") int cdAit) {
		try {
			Ait ait = AitDAO.get(cdAit);
			List<TipoProtocolo> protocolosDisponiveis = tipoProtocoloServices.getTiposDisponiveis(ait);
			
			if(protocolosDisponiveis.size() > 0)
				return ResponseFactory.ok(protocolosDisponiveis);
			
			return ResponseFactory.badRequest("Nenhum tipo de protocolo disponível para lançamento");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.internalServerError(ex.getMessage());
		}
	}
	
}
