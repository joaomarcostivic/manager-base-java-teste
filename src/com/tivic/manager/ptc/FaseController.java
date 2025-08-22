package com.tivic.manager.ptc;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Fase", tags = {"ptc"})
@Path("/v2/ptc/fases")
@Produces(MediaType.APPLICATION_JSON)
public class FaseController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra fase de documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Item registrado", response = Fase.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@ApiParam(value = "Fase a ser registrada", required = true) Fase fase) {
		try {
			
			Result result = FaseServices.save(fase);
			if(result.getCode() <= 0)
				throw new IllegalArgumentException(result.getMessage());
			
			return ResponseFactory.created((Fase)result.getObjects().get("FASE"));
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Lista de Fase"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo registrado", response = Fase[].class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAll() {
		try {
			
			ResultSetMap rsm = FaseServices.getAll();
			if(!rsm.next())
				return ResponseFactory.noContent("Nenhuma fase encontrada.");
			
			ResultSetMapper<Fase> fases = new ResultSetMapper<Fase>(rsm, Fase.class);
			
			return ResponseFactory.ok(fases);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{nmFase}")
	@ApiOperation(
			value = "Retorna Fase"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo registrado", response = Fase[].class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFaseByNome(@ApiParam( value = "Nome da Fase" ) @PathParam("nmFase") String nmFase) {
		try {
			
			Fase _fase = FaseServices.getFaseByNome(nmFase, null);
			if(_fase == null)
				return ResponseFactory.noContent("Nenhuma fase encontrada.");			
			
			return ResponseFactory.ok(_fase);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/documento/{tpDocumento}")
	@ApiOperation(
			value = "Retorna Fase"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fases encontradas", response = Fase[].class),
			@ApiResponse(code = 204, message = "Nenhum registro", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getFasesByTpDocumento(@ApiParam( value = "ID do Tipo de Documento" ) @PathParam("tpDocumento") int cdTipoDocumento) {
		try {			
			List<Fase> fases = FaseServices.getFaseByTpDocumento(cdTipoDocumento);
			
			if(fases == null)
				return ResponseFactory.noContent("Nenhuma fase encontrada.");			
			
			return ResponseFactory.ok(fases);
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
