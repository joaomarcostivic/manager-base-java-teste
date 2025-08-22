package com.tivic.manager.permission;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.permission.modulo.Modulo;
import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.permission.modulo.IModuloService;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

@Path("/v3/modulo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ModuloController {

	IModuloService moduloService;
	
	public ModuloController() throws Exception {
		this.moduloService = (IModuloService) BeansFactory.get(IModuloService.class);
	}
	
	@POST
	@Path("")
	@IdentifierPermission(identifier = "SEG.INS_MODULO")
	public Response insert(Modulo modulo) {
		try {
			this.moduloService.insert(modulo);
			return ResponseFactory.ok(modulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	@PUT
	@Path("/{cdModulo}/{cdSistema}")
	@IdentifierPermission(identifier = "SEG.UPD_MODULO")
	public Response update(
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema,
	    Modulo modulo
   ) {
		try {
			modulo.setCdModulo(cdModulo);
			modulo.setCdSistema(cdSistema);
			this.moduloService.update(modulo);
			return ResponseFactory.ok(modulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@GET
	@Path("/{cdModulo}/{cdSistema}")
	public Response get(
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema
	) {
		try {
			Modulo modulo = this.moduloService.get(cdModulo, cdSistema);
			return ResponseFactory.ok(modulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	@GET
	@Path("")
	public Response find(@QueryParam("idModulo") String idModulo) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("id_modulo", idModulo, idModulo!=null);
			List<Modulo> modulos = this.moduloService.find(searchCriterios);
            return ResponseFactory.ok(modulos);
		} catch(NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());	
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@DELETE
	@Path("/{cdModulo}/{cdSistema}")
	@IdentifierPermission(identifier = "SEG.DEL_MODULO")
	public Response delete(
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema
	) {
		try {
			Modulo modulo = this.moduloService.delete(cdModulo, cdSistema);
			return ResponseFactory.ok(modulo);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
}
