package com.tivic.manager.seg;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.seg.Acao;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Agrupamento Ações", tags = {"seg"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})

@Path("/v2/seg/acoes/grupo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AgrupamentoAcaoController {
	
	@GET
	@Path("")
	@ApiOperation( value = "Retorna lista de grupos de Ações")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupos de ações encontrados", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhum grupo de ação encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response getAll() {
		try {
			ResultSetMap rsm = AgrupamentoAcaoServices.getAllNmAgrupamento();
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.badRequest("Não foram encontrados grupos de ações.");
			
			return ResponseFactory.ok(new ResultSetMapper<AgrupamentoAcao>(rsm, AgrupamentoAcao.class));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/find")
	@ApiOperation( value = "Retorna lista de grupos de Ações filtrada")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupos de ações encontrados", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhum grupo de ação encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response find(@ApiParam( value = "Critérios") ArrayList<ItemComparator> criterios) {
		try {
			ResultSetMap rsm = AgrupamentoAcaoServices.find(criterios);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.badRequest("Não foram encontrados grupos de ações.");
			
			return ResponseFactory.ok(new ResultSetMapper<AgrupamentoAcao>(rsm, AgrupamentoAcao.class));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/findByModulo/{sistema}/{modulo}")
	@ApiOperation( value = "Retorna lista de grupos de Ações filtrada por módulo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupos de ações encontrados", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhum grupo de ação encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response findByModulo(@PathParam("sistema") int cdSistema, @PathParam("modulo") int cdModulo) {
		try {
			ResultSetMap rsm = AgrupamentoAcaoServices.getAllByModulo(cdSistema, cdModulo);
			
			if(rsm.getLines().size() <= 0)
				return ResponseFactory.badRequest("Não foram encontrados grupos de ações.");
			
			return ResponseFactory.ok(new ResultSetMapper<AgrupamentoAcao>(rsm, AgrupamentoAcao.class));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation( value = "Registra um novo grupo de Ações")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupos de ações registrado", response = Acao.class),
			@ApiResponse(code = 400, message = "Grupo de ações inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response create(
			@ApiParam( value = "Grupo de ação a ser registrado" ) AgrupamentoAcao grupoAcao) {
		try {
			Result result = AgrupamentoAcaoServices.save(grupoAcao);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest("Grupo de ações inválido");
			
			return ResponseFactory.ok(result.getObjects().get("AGRUPAMENTOACAO"));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
