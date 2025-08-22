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
import com.tivic.sol.permission.IdentifierPermission;
import com.tivic.sol.permission.acao.Acao;
import com.tivic.sol.permission.acao.IAcaoService;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

@Path("/v3/acao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AcaoController {

	IAcaoService acaoService;
	
	public AcaoController() throws Exception {
		this.acaoService = (IAcaoService) BeansFactory.get(IAcaoService.class);
	}
	
	@POST
	@Path("")
	@IdentifierPermission(identifier = "SEG.INS_ACAO")
	public Response insert(Acao acao) {
		try {
			this.acaoService.insert(acao);
			return ResponseFactory.ok(acao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	@PUT
	@Path("/{cdAcao}/{cdModulo}/{cdSistema}")
	@IdentifierPermission(identifier = "SEG.UPD_ACAO")
	public Response update(
		@PathParam("cdAcao") int cdAcao,
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema,
 	    Acao acao
   ) {
		try {
			acao.setCdAcao(cdAcao);
			acao.setCdModulo(cdModulo);
			acao.setCdSistema(cdSistema);
			this.acaoService.update(acao);
			return ResponseFactory.ok(acao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@GET
	@Path("/{cdAcao}/{cdModulo}/{cdSistema}")
	public Response get(
		@PathParam("cdAcao") int cdAcao,
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema
	) {
		try {
			Acao acao = this.acaoService.get(cdAcao, cdModulo, cdSistema);
			return ResponseFactory.ok(acao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
	@GET
	@Path("")
	public Response find(@QueryParam("idAcao") String idAcao) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("id_acao", idAcao, idAcao != null);
			List<Acao> acaos = this.acaoService.find(searchCriterios);
            return ResponseFactory.ok(acaos);
		} catch(NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());	
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}

	@DELETE
	@Path("/{cdAcao}/{cdModulo}/{cdSistema}")
	@IdentifierPermission(identifier = "SEG.DEL_ACAO")
	public Response delete(
		@PathParam("cdAcao") int cdAcao,
		@PathParam("cdModulo") int cdModulo,
		@PathParam("cdSistema") int cdSistema
	) {
		try {
			Acao acao = this.acaoService.delete(cdAcao, cdModulo, cdSistema);
			return ResponseFactory.ok(acao);
		} catch (BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro no sistema. Contate o suporte");
		}
	}
	
}
