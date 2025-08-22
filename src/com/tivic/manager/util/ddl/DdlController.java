package com.tivic.manager.util.ddl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Ferramentas de Desenvolvimento", tags = {"util/v2"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v3/util")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DdlController {

	@POST
	@Path("/ddl/init")
	@ApiOperation( value = "Inicialização do DDL para atualização do Banco")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Classe gerada", response = String.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@IdentifierPermission(identifier = "SEG.PERMISSAO_DDL")
	public Response initDDL(){
		try {
			DDLManager ddlManager = DDLManagerFactory.generate();
			ddlManager.executar();
			return ResponseFactory.ok("DDL inicializado com sucesso !");		
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.badRequest("Erro na inicialização do DDL");
		}
	}

}
