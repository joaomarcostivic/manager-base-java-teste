package com.tivic.manager.ptc;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Formulario;
import com.tivic.manager.grl.FormularioDTO;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "TipoDocumento", tags = {"ptc"})
@Path("/v2/ptc/tiposdocumento")
@Produces(MediaType.APPLICATION_JSON)
public class TipoDocumentoController {

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo tipo de documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Tipo registrado", response = TipoDocumento.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@ApiParam(value = "Tipo a ser registrado", required = true) TipoDocumento tipoDocumento) {
		try {
			
			Result result = TipoDocumentoServices.save(tipoDocumento);
			if(result.getCode() <= 0)
				throw new IllegalArgumentException(result.getMessage());
			
			return ResponseFactory.created((TipoDocumento)result.getObjects().get("TIPODOCUMENTO"));
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Lista de Tipos de Documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo registrado", response = TipoDocumento[].class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAll() {
		try {
			
			ResultSetMap rsm = TipoDocumentoServices.getAll();
			if(!rsm.next())
				return ResponseFactory.noContent("Nenhum tipo encontrado.");
			
			ResultSetMapper<TipoDocumento> documentos = new ResultSetMapper<TipoDocumento>(rsm, TipoDocumento.class);
			
			return ResponseFactory.ok(documentos);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{nmTipoDocumento}")
	@ApiOperation(
			value = "Lista de Tipos de Documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo registrado", response = TipoDocumento[].class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTpDocumentoByNome(@ApiParam( value = "Nome do Tipo de Documento") @PathParam("nmTipoDocumento") String nmTipoDocumento) {
		try {
			
			TipoDocumento _tpDocumento= TipoDocumentoServices.getTpDocumentoByNome(nmTipoDocumento, null);
			if(_tpDocumento == null)
				return ResponseFactory.noContent("Nenhum tipo encontrado.");	
			
			return ResponseFactory.ok(_tpDocumento);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/formularios")
	@ApiOperation(
			value = "Lista de Tipos de Documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Formulários", response = FormularioDTO[].class),
			@ApiResponse(code = 204, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFormulario(@ApiParam(value = "id do tipo") @PathParam("id") int cdTipoDocumento) {
		try {
			List<FormularioDTO> forms = new ArrayList<FormularioDTO>();
			forms.add(TipoDocumentoServices.get(cdTipoDocumento, null));
			
			if(forms.isEmpty())
				return ResponseFactory.noContent("Nenhum formulário para o tipo indicado.");
			
			return ResponseFactory.ok(forms);
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
