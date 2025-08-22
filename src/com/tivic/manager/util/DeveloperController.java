package com.tivic.manager.util;

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
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Ferramentas de Desenvolvimento", tags = {"util"}, authorizations = {
			@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/developer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DeveloperController {
	
	private static final String CONNECTION_CLASS = "Conexao";
	private static final boolean ENABLE_COMPLIANCE = false;
	
	@GET
	@Path("/vo/{table}/{package}/{class}")
	@ApiOperation(
			value = "retorna a classe VO para a tabela com o nome do package e da classe"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Classe gerada", response = String.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public static Response getVO(
			@ApiParam(value = "nome da tabela") @PathParam("table") String nmTable,
			@ApiParam(value = "nome do pacote") @PathParam("package") String nmPackage,
			@ApiParam(value = "nome da classe") @PathParam("class") String nmClass
		) {
		try {
			String fileContent = DeveloperServices.getVOClass(nmTable, nmClass, "", "", nmPackage);
			return ResponseFactory.ok(fileContent.getBytes());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/dao/{table}/{package}/{class}")
	@ApiOperation(
			value = "retorna a classe DAO para a tabela com o nome do package e da classe"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Classe gerada", response = String.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public static Response getDAO(
			@ApiParam(value = "nome da tabela") @PathParam("table") String nmTable,
			@ApiParam(value = "nome do pacote") @PathParam("package") String nmPackage,
			@ApiParam(value = "nome da classe") @PathParam("class") String nmClass
		) {
		try {
			String fileContent = DeveloperServices.getDAOClass(nmTable, nmClass, "", "", nmPackage, CONNECTION_CLASS);
			return ResponseFactory.ok(fileContent.getBytes());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/services/{table}/{package}/{class}")
	@ApiOperation(
			value = "retorna a classe Services para a tabela com o nome do package e da classe"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Classe gerada", response = String.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public static Response getServices(
			@ApiParam(value = "nome da tabela") @PathParam("table") String nmTable,
			@ApiParam(value = "nome do pacote") @PathParam("package") String nmPackage,
			@ApiParam(value = "nome da classe") @PathParam("class") String nmClass
		) {
		try {
			String fileContent = DeveloperServices.getServicesClass(nmTable, nmClass, nmPackage, CONNECTION_CLASS, ENABLE_COMPLIANCE);
			return ResponseFactory.ok(fileContent.getBytes());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	

}
