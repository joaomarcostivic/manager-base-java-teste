package com.tivic.manager.grl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ResultSetMap;

@Api(value = "Grupo Parametro", tags = { "grl" })
@Path("/v2/grl/grupoparametro")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GrupoParametroController {
	private final GrupoParametroDAO _dao;
	
	public GrupoParametroController() {
		_dao = new GrupoParametroDAO();
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna os grupos de parâmetros cadastrados")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Grupo de Parâmetros encontrado", response = GrupoParametro.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) })
	public Response getAll() {
		try {
			ResultSetMap _rsm = _dao.getAll();
			
			if(_rsm != null)
				return ResponseFactory.ok(_rsm);
			
			return ResponseFactory.badRequest("Não foram encontrados grupos de parâmetros");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no processo de requisição");
		}
	}
}
