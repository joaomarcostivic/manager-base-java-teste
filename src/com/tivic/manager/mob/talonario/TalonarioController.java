package com.tivic.manager.mob.talonario;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.pagination.PagedResponse;
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

@Api(value = "Talonários", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/mob/talonario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TalonarioController {
	
	private ITalonarioService talonarioService;
	private ManagerLog managerLog;
	public TalonarioController() throws Exception {
		this.talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("/lote")
	@ApiOperation(
			value = "Registra um novo lote de talonários"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote registrado", response = TalonarioLoteDTO.class),
			@ApiResponse(code = 400, message = "Lote inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response saveLote(@ApiParam(value = "Lote de talonários a ser registrado") TalonarioLoteDTO talonarioLote) {
		try {
			return ResponseFactory.ok(this.talonarioService.saveLote(talonarioLote));
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro ao cadastrar lote de talonários: " + e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdTalao}")
	@ApiOperation(
				value = "Busca uma lista de talões"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso", response = TalonarioLoteDTO.class),
			@ApiResponse(code = 400, message = "Busca inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)	
	})
	public Response findTaloesUsados(@ApiParam(value = "Código do talão") @PathParam("cdTalao") int cdTalao) {
		try {
			List<Talonario> talonarioList  = this.talonarioService.findTaloesUsados(cdTalao);
			return ResponseFactory.ok(talonarioList);
		} catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError("Erro ao buscar talonários: " + e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
				value = "Busca uma lista de talões"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Busca realizada com sucesso", response = TalonarioLoteDTO.class),
			@ApiResponse(code = 400, message = "Busca inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)	
	})
	public Response find(
			@ApiParam(name = "tpConvenio",value = "Tipo do talonario") @QueryParam("tipo") @DefaultValue("-1") int tipo,
			@ApiParam(name = "stTalao",value = "Situação do talão") @QueryParam("situacao") @DefaultValue("-1")  int situacao,
			@ApiParam(name = "stTalao",value = "Situação do talão") @QueryParam("agente") @DefaultValue("-1")  int cdAgente,
			@ApiParam(name = "nrLimite",value = "Limite") @QueryParam("limit")  int limit,
			@ApiParam(name = "nrPagina",value = "Numero da pagina") @QueryParam("page")  int page) {
		try {
			SearchCriterios searchCriterios = new TalonarioSearchBuilder()
					.setSituacao(situacao)
					.setTipo(tipo)
					.setCdAgente(cdAgente)
					.setLimit(limit, page)
					.build();
			
			PagedResponse<TalonarioDTO> talonarioList  = this.talonarioService.findTaloes(searchCriterios);
			return ResponseFactory.ok(talonarioList);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro ao buscar talonários: " + e.getMessage());
		}
	}
}
