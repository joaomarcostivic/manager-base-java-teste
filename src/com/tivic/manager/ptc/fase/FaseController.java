package com.tivic.manager.ptc.fase;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.ptc.Fase;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Fase", tags = {"fase"})
@Path("/v3/ptc/fases")
@Produces(MediaType.APPLICATION_JSON)
public class FaseController {
	private IFaseService faseService;
	
	public FaseController() throws Exception{
		this.faseService = (IFaseService) BeansFactory.get(IFaseService.class);
	}
	
	@GET
    @Path("/{cdFase}")
    @ApiOperation(
            value = "Retorna um tipo de documento"
        )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tipo de Fase encontrada", response = Fase.class),
            @ApiResponse(code = 204, message = "Há algum parâmetro inválido", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
        })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFase(@ApiParam(value = "Código da Fase") @PathParam("cdFase") int cdFase) {
        try {
            Fase fase = faseService.get(cdFase);
            return ResponseFactory.ok(fase);
        } catch(IllegalArgumentException e) {
            return ResponseFactory.badRequest(e.getMessage());
        } catch (NoContentException e) {
        	return ResponseFactory.noContent(e.getMessage());
        } catch(Exception e) {
            return ResponseFactory.internalServerError(e.getMessage());
        } 
    }
	
	@GET
    @Path("")
    @ApiOperation(
            value = "Retorna um tipo de fase"
        )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tipo de Fase encontrada", response = Fase[].class),
            @ApiResponse(code = 204, message = "Há algum parâmetro inválido", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
        })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFase() {
        try {
            List<Fase> fases = faseService.getAll();
            return ResponseFactory.ok(fases);
        } catch(Exception e) {
            return ResponseFactory.internalServerError(e.getMessage());
        } 
    }
}
