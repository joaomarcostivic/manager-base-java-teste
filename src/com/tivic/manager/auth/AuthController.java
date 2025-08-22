package com.tivic.manager.auth;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.auth.Auth;
import com.tivic.sol.auth.AuthService;
import com.tivic.sol.auth.UserManager;
import com.tivic.sol.auth.UserManagerDTO;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.auth.usuario.Usuario;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v3/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

	private AuthService authService;
	
	public AuthController() throws Exception {
		this.authService = (AuthService) BeansFactory.get(AuthService.class);
	}
	
	@POST
	@Path("/login")
	@ApiOperation(
		value = "Login Geral"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Ok", response = Auth.class),
		@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
		@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@JWTIgnore
	public Response login(Auth auth) {
		try {
			UserManager userManager = this.authService.login(auth);
			return ResponseFactory.ok(userManager.getUsuario());
		} catch(NotAuthorizedException e) {
			e.printStackTrace();
			return ResponseFactory.unauthorized(e.getMessage());
		} catch(ForbiddenException e) {
			e.printStackTrace();
			return ResponseFactory.forbidden(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

	@POST
	@Path("/logout")
	@ApiOperation(
		value = "Deslogar usuário"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Ok", response = Auth.class),
		@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
		@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(Usuario usuario) {
		try {
			this.authService.logout(usuario);
			return ResponseFactory.ok("Logout realizado com sucesso");
		} catch(NotAuthorizedException e) {
			return ResponseFactory.unauthorized(e.getMessage());
		} catch(ForbiddenException e) {
			return ResponseFactory.forbidden(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}


	@GET
	@Path("/")
	@ApiOperation(
		value = "Busca todos os usuários logados"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Ok", response = Auth.class),
		@ApiResponse(code = 400, message = "Erro na requisição", response = ResponseBody.class),
		@ApiResponse(code = 401, message = "Usuário não autorizado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Consumes(MediaType.APPLICATION_JSON)
	@IdentifierPermission(identifier = "SEG.BUSCA_USUARIOS_LOGADOS")
	public Response getLogados() {
		try {
			List<UserManagerDTO> listUserManagerDTOs = this.authService.getUsuariosLogados();
			return ResponseFactory.ok(listUserManagerDTOs);
		} catch(NotAuthorizedException e) {
			return ResponseFactory.unauthorized(e.getMessage());
		} catch(ForbiddenException e) {
			return ResponseFactory.forbidden(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

}
