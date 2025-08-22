package com.tivic.manager.grl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import sol.dao.ResultSetMap;

@Api(value = "Grupo Parâmetro", tags = { "grl" })
@Path("/v2/grl/parametrogrupo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParametroGrupoController {
	
	private final ParametroGrupoServices _services;
	
	public ParametroGrupoController() {
		_services = new ParametroGrupoServices();
	}
	
	@GET
	@Path("/{cdGrupoParametro}")
	@ApiOperation(value = "Retorna os parâmetros de um grupo cadastrados")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Grupo de Parâmetros encontrado", response = GrupoParametro.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getAll(@ApiParam(value = "Código do Grupo de Parâmetros") @PathParam("cdGrupoParametro") int cdGrupoParametro) {
		try {
			List<Parametro> parametros = this._services.getParametrosFromGrupo(cdGrupoParametro);
			
			if(parametros != null)
				return ResponseFactory.ok(parametros);
			
			return ResponseFactory.badRequest("Não foram encontrados grupos de parâmetros");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no processo de requisição");
		}
	}

}
