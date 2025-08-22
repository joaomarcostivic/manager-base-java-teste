package com.tivic.manager.mob.edat.controller;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.edat.TermosECondicoes;
import com.tivic.manager.mob.edat.repositories.ITermosECondicoesService;
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

@Api(value = "Termos e Condições", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/edat/termos-condicoes")
@Produces(MediaType.APPLICATION_JSON)
public class TermosECondicoesController {

    private ITermosECondicoesService termosECondicoesService;
    private ManagerLog managerLog;

    public TermosECondicoesController() throws Exception {
        this.termosECondicoesService = (ITermosECondicoesService) BeansFactory.get(ITermosECondicoesService.class);
        this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
    }

    @GET
    @Path("/{cdTermo}")
    @ApiOperation(value = "Buscar Termos e Condições por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições encontrado", response = TermosECondicoes.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response get(
            @ApiParam(value = "ID do Termo e Condições", required = true) @PathParam("cdTermo") int cdTermo) {
        try {
            TermosECondicoes termo = termosECondicoesService.get(cdTermo);
            return ResponseFactory.ok(termo);
        } catch (Exception e) {
        	this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }

    @GET
    @Path("")
    @ApiOperation(value = "Buscar Termos e Condições por critérios")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições encontrados", response = TermosECondicoes.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response search(
		    @ApiParam(value = "Texto dos Termos e Condições", required = false) @QueryParam("dsTermo") String dsTermo)
    {
        try {
            SearchCriterios searchCriterios = new SearchCriterios();
            searchCriterios.addCriteriosLikeAnyString("ds_Termo", dsTermo, dsTermo != null);
            List<TermosECondicoes> termosList = termosECondicoesService.find(searchCriterios);
            return ResponseFactory.ok(termosList);
        } catch (Exception e) {
        	this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }

    @POST
    @Path("")
    @ApiOperation(value = "Inserir Termos e Condições")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições inseridos com sucesso", response = TermosECondicoes.class),
            @ApiResponse(code = 400, message = "Termos e Condições Inválidos", response = ResponseBody.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response create(
            @ApiParam(value = "Termos e Condições a serem inseridos", required = true) TermosECondicoes termos) {
        try {
            termosECondicoesService.insert(termos);
            return ResponseFactory.ok(termos);
        } catch (Exception e) {
            this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }

    @PUT
    @Path("/{cdTermo}")
    @ApiOperation(value = "Atualizar Termos e Condições por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições atualizados com sucesso", response = TermosECondicoes.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response update(
            @ApiParam(value = "ID do Termo e Condições", required = true) @PathParam("cdTermo") int cdTermo,
            @ApiParam(value = "Termos e Condições a serem atualizados", required = true) TermosECondicoes termos) {
        try {
            termosECondicoesService.update(termos);
            return ResponseFactory.ok(termos);
        } catch (Exception e) {
            this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }
    
    @PUT
    @Path("")
    @ApiOperation(value = "Atualiza as posições dos termos e condições")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições atualizados com sucesso", response = List.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response updateAll(
            @ApiParam(value = "Termos e Condições a serem atualizados", required = true) List<TermosECondicoes> termosList) {
        try {
            List<TermosECondicoes> updatedTermosList = termosECondicoesService.updateAll(termosList);
            return ResponseFactory.ok(updatedTermosList);
        } catch (Exception e) {
            this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }

    @DELETE
    @Path("/{cdTermo}")
    @ApiOperation(value = "Excluir Termos e Condições por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Termos e Condições excluídos com sucesso", response = Long.class),
            @ApiResponse(code = 500, message = "Erro durante o processamento da requisição")
    })
    public Response delete(
            @ApiParam(value = "ID do Termo e Condições a ser excluído", required = true) @PathParam("cdTermo") int cdTermo) {
        try {
            termosECondicoesService.delete(cdTermo);
            return ResponseFactory.noContent();
        } catch (Exception e) {
            this.managerLog.showLog(e);
            return ResponseFactory.internalServerError(e.getMessage());
        }
    }
}

