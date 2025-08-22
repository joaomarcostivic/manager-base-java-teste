package com.tivic.manager.ptc.fici;

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
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Apresentação de Condutor", tags = {"ptc"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/ptc/apresentacaocondutor")
@Produces(MediaType.APPLICATION_JSON)
public class ApresentacaoCondutorController {

	private IApresentacaoCondutorService ficiService;
	
	public ApresentacaoCondutorController() throws Exception{
		this.ficiService = (IApresentacaoCondutorService) BeansFactory.get(IApresentacaoCondutorService.class);
	}
	
	@GET
    @Path("/{idFici}")
    @ApiOperation(
            value = "Retorna uma Apresentação de Condutor"
        )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Apresentação de Condutor encontrada", response = Fase.class),
            @ApiResponse(code = 204, message = "Há algum parâmetro inválido", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
        })
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getApresentacaoCondutor(@ApiParam(value = "Código da Apresentação de Condutor") @PathParam("idFici") int idFici) {
        try {
            ApresentacaoCondutor fici = ficiService.get(idFici);
            return ResponseFactory.ok(fici);
        } catch(IllegalArgumentException e) {
            return ResponseFactory.badRequest(e.getMessage());
        } catch (NoContentException e) {
        	return ResponseFactory.noContent(e.getMessage());
        } catch(Exception e) {
            return ResponseFactory.internalServerError(e.getMessage());
        } 
    }
}
