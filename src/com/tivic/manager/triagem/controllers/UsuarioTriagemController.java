package com.tivic.manager.triagem.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import com.tivic.manager.seg.UsuarioDTO;
import com.tivic.manager.triagem.dtos.UsuarioTriagemDTO;
import com.tivic.manager.triagem.services.IUsuarioTriagem;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "triagem", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/triagem/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioTriagemController {

	private IUsuarioTriagem usuarioTriagemService;
	private ManagerLog managerLog;
	
	public UsuarioTriagemController() throws Exception {
		this.usuarioTriagemService = (IUsuarioTriagem) BeansFactory.get(IUsuarioTriagem.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/by-email")
	@ApiOperation(
			value = "Retorna o usuário com base no email"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Usuários encontrados", response = UsuarioDTO[].class),
			@ApiResponse(code = 400, message = "Requisição inválida", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Nenhum Usuário encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getAll(
			@ApiParam(value = "Email do usuario") @QueryParam("nmEmail") String nmEmail
			) {
		try {
			UsuarioTriagemDTO usuario = usuarioTriagemService.getByEmail(nmEmail);			
			return ResponseFactory.ok(usuario);
		} 
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
