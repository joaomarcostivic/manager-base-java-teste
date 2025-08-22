package com.tivic.manager.mob.ocorrencia;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ocorrencia;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Ocorrencias", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v3/mob/ocorrencias")
@Produces(MediaType.APPLICATION_JSON)
public class OcorrenciaController {
	
	IOcorrenciaService ocorrenciaService;
	
	public OcorrenciaController() throws Exception {
		ocorrenciaService = (IOcorrenciaService) BeansFactory.get(IOcorrenciaService.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Ocorrencias"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ocorrencias encontrado", response = Ocorrencia[].class),
			@ApiResponse(code = 204, message = "Ocorrencias não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Descricao da Ocorrencia") @QueryParam("descricao") String dsOcorrencia, @ApiParam(value = "Tipo da Ocorrencia") @QueryParam("tipoOcorrencia") @DefaultValue("-1") int tpOcorrencia) {
		try {	
			SearchCriterios search = new SearchCriterios();
			search.addCriteriosEqualString("ds_ocorrencia", dsOcorrencia, dsOcorrencia != null);
			search.addCriteriosEqualInteger("tp_ocorrencia", tpOcorrencia, tpOcorrencia > -1);
			search.setQtLimite(limit);
			return ResponseFactory.ok(ocorrenciaService.find(search));
		} catch(Exception e) {		
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
}
