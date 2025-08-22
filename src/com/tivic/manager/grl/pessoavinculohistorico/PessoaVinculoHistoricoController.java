package com.tivic.manager.grl.pessoavinculohistorico;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.pessoavinculohistorico.builders.PessoaHistoricoSearchBuilder;
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
import sol.util.Result;

@Api(value = "PessoaVinculoHistorico", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/grl/pessoavinculohistorico")
@Produces(MediaType.APPLICATION_JSON)
public class PessoaVinculoHistoricoController {
	private IPessoaVinculoHistoricoService pessoaVinculoHistoricoService;
	private ManagerLog managerLog;
	
	
	public PessoaVinculoHistoricoController() throws Exception {
		pessoaVinculoHistoricoService = (IPessoaVinculoHistoricoService) BeansFactory.get(IPessoaVinculoHistoricoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna historico de relatores")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Historico encontrados", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhum historico encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response findHistorico(
			@ApiParam(value = "codigo da pessoa") @QueryParam("cdPessoa") int cdPessoa,
			@ApiParam(value = "Limite") @QueryParam("limit") int limit,
			@ApiParam(value = "Páginas") @QueryParam("page") int page) throws Exception {
		try {
			SearchCriterios searchCriterios = new PessoaHistoricoSearchBuilder()
					.setCdPessoa(cdPessoa)
					.setQtDeslocamento(limit, page)
					.setQtLimite(limit)
				.build();
			return ResponseFactory.ok(pessoaVinculoHistoricoService.findPessoaVinculo(searchCriterios));
		} catch (BadRequestException e) {
			managerLog.showLog(e);
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
