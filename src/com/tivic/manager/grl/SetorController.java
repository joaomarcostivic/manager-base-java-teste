package com.tivic.manager.grl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.util.Result;

@Api(value = "Setor", tags = {"grl"})
@Path("/v2/grl/setores")
@Produces(MediaType.APPLICATION_JSON)
public class SetorController {

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra setor"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Item registrado", response = Setor.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@ApiParam(value = "Setor a ser registrada", required = true) Setor setor) {
		try {
			
			Result result = SetorServices.save(setor);
			if(result.getCode() <= 0)
				throw new IllegalArgumentException(result.getMessage());
			
			return ResponseFactory.created((Setor)result.getObjects().get("SETOR"));
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
