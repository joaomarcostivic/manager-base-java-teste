package com.tivic.manager.mob;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.validation.Validators;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "ATA (Documento)", tags = {"mob"})
@Path("/v2/mob/ata")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AtaController {
	
	private final AtaServices ataServices;
	
	public AtaController() {
		this.ataServices = new AtaServices();
	}
	
	@POST
	@Path("/boletim/publicar")
	@ApiOperation(value = "Publica um Boletim de ATA de Reunião")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Documento registrado", response = AitMovimentoDocumentoDTO.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response publicar(@ApiParam(value = "Documento a ser publicado") DocumentoOcorrencia _documentoOcorrencia) {
		try {
			DocumentoOcorrencia _resultado = this.ataServices.publicarDocumento(_documentoOcorrencia, getValidators(_documentoOcorrencia), null);
			
			if(_resultado != null)
				return ResponseFactory.ok(_resultado);
			
			return ResponseFactory.badRequest("");
		}  catch (ValidationException e) {
			e.printStackTrace(System.out);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	private Validators<DocumentoOcorrencia> getValidators(DocumentoOcorrencia _documentoOcorrencia) {
		return new Validators<DocumentoOcorrencia>(_documentoOcorrencia).put(new AtaValidator());
	}

}
