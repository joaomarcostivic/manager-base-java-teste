package com.tivic.manager.mob.ait.imagemvideo.aitvideo;

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

@Api(value = "Captura vídeos de AIT", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		})
})

@Path("/v3/mob/ait/{cdAit}/videos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class AitVideoController {
	
	private ManagerLog managerLog;
	private IAitVideoService aitVideoService;
	
	public AitVideoController() throws Exception {
		this.aitVideoService = (IAitVideoService) BeansFactory.get(IAitVideoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece uma lista de videos de um AIT"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Videos encontrados", response = AitImagem[].class),
		@ApiResponse(code = 204, message = "Nao existe AIT com o cdAit indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response retrieveVideos(@ApiParam(value = "cd do AIT") @PathParam("cdAit") int cdAit) {
		try {	
			AitVideoSearch aitVideoSearch = new AitVideoSearchBuilder().setCdAit(cdAit).build();
			List<AitImagem> aitVideos = aitVideoService.buscarListaVideos(aitVideoSearch);
			
			return ResponseFactory.ok(aitVideos);
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@GET
	@Path("/{cdImagem}")
	@Produces("video/mp4")
	@ApiOperation(
			value = "Fornece video do AIT"
		)
	public Response streamVideo(
	        @ApiParam(value = "cd do AIT") @PathParam("cdAit") int cdAit,
	        @ApiParam(value = "cd do video") @PathParam("cdImagem") int cdImagem) throws IOException {
	    try {
	        AitVideoSearch aitVideoSearch = new AitVideoSearchBuilder().setCdAit(cdAit).setCdImagem(cdImagem).build();
	        byte[] videoBytes = aitVideoService.buscarVideo(aitVideoSearch).getBlbImagem();
	        return Response.ok(new ByteArrayInputStream(videoBytes))
	                .header("Content-Type", "video/mp4")
	                .header("Content-Length", videoBytes.length)
	                .build();
	    } catch (Exception e) {
	        managerLog.showLog(e);
	        return Response.serverError().build();
	    }
	}

}
