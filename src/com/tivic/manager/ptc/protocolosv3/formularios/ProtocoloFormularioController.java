package com.tivic.manager.ptc.protocolosv3.formularios;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Protocolo", tags = { "ptc" })
@Path("/v3/ptc/protocolos/formulario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtocoloFormularioController {

	@GET
	@Path("/formulario")
	@ApiOperation(value = "Gera formulário de Apresentação Condutor", notes = "geração de formulário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha formulário"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getFormularioCondutor() throws ValidacaoException, Exception {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", new GeradorFormularioCondutorProtocolo().gerar());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/formulario/defesa")
	@ApiOperation(value = "Gera formulário de Defesa", notes = "geração de formulário")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha formulário"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor") })
	@Produces({ "application/pdf", "application/json" })
	public Response getFormularioDefesa() throws ValidacaoException, Exception {
		try {
			JSONObject response = new JSONObject();
			response.put("code", 1);
			response.put("blob", new GeradorFormularioDefesaProtocolo().gerar());
			return ResponseFactory.ok(response);
		} catch (ValidacaoException ve) {
			ve.printStackTrace();
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
