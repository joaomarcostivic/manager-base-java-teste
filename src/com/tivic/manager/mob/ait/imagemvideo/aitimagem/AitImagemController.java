package com.tivic.manager.mob.ait.imagemvideo.aitimagem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.AitImagem;
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

@Api(value = "Captura imagens de AIT", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/mob/ait/{cdAit}/imagens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class AitImagemController {
	
	private ManagerLog managerLog;
	private IAitImagemService aitImagemService;
	
	public AitImagemController() throws Exception {
		this.aitImagemService = (IAitImagemService) BeansFactory.get(IAitImagemService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece uma lista de imagens de um AIT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Imagens encontradas", response = AitImagem[].class),
		@ApiResponse(code = 204, message = "Nao existe AIT com o cdAit indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response retrieveImagens(@ApiParam(value = "cd do AIT") @PathParam("cdAit") int cdAit) {
		try {	
			AitImagemSearch aitImagemSearch = new AitImagensSearchBuilder().setCdAit(cdAit).build();
			List<AitImagem> aitImagens = aitImagemService.buscarListaImagens(aitImagemSearch);
			return ResponseFactory.ok(aitImagens);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{cdImagem}")
	@Produces({ "image/jpg" })
	@ApiOperation(
			value = "Fornece imagem do AIT"
		)
	public Response streamImagem(
	        @ApiParam(value = "cd do AIT") @PathParam("cdAit") int cdAit,
	        @ApiParam(value = "cd da imagem") @PathParam("cdImagem") int cdImagem) throws IOException {
	    try {
	    	AitImagemSearch aitImagemSearch = new AitImagensSearchBuilder().setCdAit(cdAit).setCdImagem(cdImagem).build();
	        byte[] imagemBytes = aitImagemService.buscarImagem(aitImagemSearch).getBlbImagem();
	        return Response.ok(new ByteArrayInputStream(imagemBytes))
	                .header("Content-Type", "image/jpg")
	                .header("Content-Length", imagemBytes.length)
	                .build();
	    } catch (Exception e) {
	    	managerLog.showLog(e);
	    	return Response.serverError().build();
	    }
	}
}
