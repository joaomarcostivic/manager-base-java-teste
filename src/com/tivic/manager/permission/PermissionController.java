package com.tivic.manager.permission;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.permission.Permission;
import com.tivic.sol.permission.usuariomodulo.UsuarioModulo;
import com.tivic.sol.permission.usuariopermissaoacao.UsuarioPermissaoAcao;
import com.tivic.sol.response.ResponseFactory;

@Path("/v3/permission")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionController {
	
	private Permission permission;
	
	public PermissionController() throws Exception{
		this.permission = (Permission) BeansFactory.get(Permission.class);
	}
	

	@POST
	@Path("/acao/{cdUsuario}/{idAcao}")
	public Response verifyAcao(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idAcao") String idAcao
	) {
		try {
			return ResponseFactory.ok(this.permission.hasAction(cdUsuario, idAcao));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@POST
	@Path("/usuarioacao/{cdUsuario}/{idAcao}")
	@IdentifierPermission(identifier = "SEG.INS_USUARIO_ACAO")
	public Response addAcao(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idAcao") String idAcao
	) {
		try {
			UsuarioPermissaoAcao usuarioPermissaoAcao = this.permission.addAction(cdUsuario, idAcao);
			return ResponseFactory.ok(usuarioPermissaoAcao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@DELETE
	@Path("/usuarioacao/{cdUsuario}/{idAcao}")
	@IdentifierPermission(identifier = "SEG.DEL_USUARIO_ACAO")
	public Response removeAcao(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idAcao") String idAcao
	) {
		try {
			UsuarioPermissaoAcao usuarioPermissaoAcao = this.permission.removeAction(cdUsuario, idAcao);
			return ResponseFactory.ok(usuarioPermissaoAcao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@POST
	@Path("/modulo/{cdUsuario}/{idModulo}")
	public Response verifyModulo(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idModulo") String idModulo
	) {
		try {
			return ResponseFactory.ok(this.permission.hasModule(cdUsuario, idModulo));
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@POST
	@Path("/usuariomodulo/{cdUsuario}/{idModulo}")
	@IdentifierPermission(identifier = "SEG.INS_USUARIO_MODULO")
	public Response addModulo(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idModulo") String idModulo
	) {
		try {
			UsuarioModulo usuarioModulo = this.permission.addModule(cdUsuario, idModulo);
			return ResponseFactory.ok(usuarioModulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@DELETE
	@Path("/usuariomodulo/{cdUsuario}/{idModulo}")
	@IdentifierPermission(identifier = "SEG.DEL_USUARIO_MODULO")
	public Response removeModulo(
		@PathParam("cdUsuario") int cdUsuario,
		@PathParam("idModulo") String idModulo
	) {
		try {
			UsuarioModulo usuarioModulo = this.permission.removeModule(cdUsuario, idModulo);
			return ResponseFactory.ok(usuarioModulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	
}
