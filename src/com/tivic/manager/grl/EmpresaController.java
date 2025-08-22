package com.tivic.manager.grl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Empresa", authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/grl/empresa/")
@Produces(MediaType.APPLICATION_JSON)
public class EmpresaController {



	@GET
	@Path("/{cdEmpresa}")
	@ApiOperation(
			value = "Busca uma empresa"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Empresa buscada"),
		@ApiResponse(code = 400, message = "A busca teve algum parâmetro inválido"),
		@ApiResponse(code = 204, message = "Nenhum resultado"),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response get(@PathParam("cdEmpresa") int cdEmpresa, @QueryParam("cascade") boolean cascade) {
		try {
			if(cdEmpresa <= 0) 
				return ResponseFactory.badRequest("Código da instituição é nulo ou inválido");
			
			EmpresaDTO empresaDto = new EmpresaDTO.Builder(cdEmpresa, cascade).build();
			
			if(empresaDto == null){
				return ResponseFactory.noContent("Nenhuma empresa encontrada");
			}
			
			return ResponseFactory.ok(empresaDto);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	
}
