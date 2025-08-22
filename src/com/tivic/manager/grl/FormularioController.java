package com.tivic.manager.grl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.ptc.TipoDocumento;
import com.tivic.manager.ptc.TipoDocumentoServices;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;

@Api(value = "Formulario", tags = {"grl"})
@Path("/v2/grl/formulario")
@Produces(MediaType.APPLICATION_JSON)
public class FormularioController {

	@GET
	@Path("/{cdFormulario}")
	@ApiOperation(
			value = "Retorna um formulário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "FormularioEncontrado", response = Formulario.class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro na requisição ao servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFormulario(@ApiParam(value = "Código do Formulário") @PathParam("cdFormulario") int cdFormulario) {
		try {			
			ResultSetMap rsm = FormularioServices.getAllAtributos(cdFormulario);
			if(!rsm.next())
				return ResponseFactory.noContent("Nenhum tipo encontrado.");
			
			ResultSetMapper<FormularioAtributo> atributos = new ResultSetMapper<FormularioAtributo>(rsm, FormularioAtributo.class);
			
			return ResponseFactory.ok(atributos);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
