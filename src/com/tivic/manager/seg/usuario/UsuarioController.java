package com.tivic.manager.seg.usuario;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
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

@Api(value = "Usuario", tags = {"seg"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/seg/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioController {
	private IUsuarioService usuarioService;
	private ManagerLog managerLog;
	
	public UsuarioController() throws Exception {
		usuarioService = (IUsuarioService) BeansFactory.get(IUsuarioService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@DELETE
	@Path("/delete/{cdUsuario}")
	@ApiOperation(value = "Exclusão de Usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Usuário excluido", response = Usuario.class),
			@ApiResponse(code = 400, message = "Erro ao excluir Usuário", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response delete(
			@ApiParam(value = "Código do Usuário") @PathParam("cdUsuario") int cdUsuario) {
		try {
			usuarioService.delete(cdUsuario);
			return ResponseFactory.noContent();
		} catch (ValidacaoException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/logins")
	@ApiOperation(value = "Logins de Usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Logins econtrados", response = Usuario.class),
			@ApiResponse(code = 400, message = "Nenhum login encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response findLogins(
			@ApiParam(value = "Usúario") @QueryParam("pessoa") String nmPessoa,
			@ApiParam(value = "Código da pessoa") @QueryParam("cdPessoa") int cdPessoa,
			@ApiParam(value = "Login Usúario") @QueryParam("nmLogin") String nmLogin,
			@ApiParam(value = "Situação do usuário") @QueryParam("stUsuario") @DefaultValue("-1") int stUsuario,
			@ApiParam(value = "Situação do login") @QueryParam("stLogin") @DefaultValue("-1") int stLogin,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new UsuarioSearchBuilder()
					.setCdPessoa(cdPessoa)
					.setNmPessoa(nmPessoa)
					.setNmLogin(nmLogin)
					.setStLogin(stLogin)
					.setStUsuario(stUsuario)
					.setQtDelocamento(limit, page)
					.setQtLimit(limit)
				.build();
			return ResponseFactory.ok(usuarioService.findLogins(searchCriterios));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/logout/{cdUsuario}")
	@ApiOperation(value = "Logout de Usuário")
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Usuário deslogado", response = Usuario.class),
			@ApiResponse(code = 400, message = "Erro ao fazer logout Usuário", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response logout(
			@ApiParam(value = "Código do Usuário") @PathParam("cdUsuario") int cdUsuario) {
		try {
			usuarioService.logoutUsuario(cdUsuario);
			return ResponseFactory.ok(cdUsuario);
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
