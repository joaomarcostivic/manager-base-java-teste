package com.tivic.manager.fix.mob.ait.cidade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

@Api(value = "Fix Cidade/UF", tags = {"fix"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
	})
@Path("/v3/sis/fix/ait/cidade")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FixCidadeController {
	private ManagerLog managerLog;
	private IFixCidadeService fixCidadeService;

	public FixCidadeController() throws Exception {
		this.fixCidadeService = (IFixCidadeService) BeansFactory.get(IFixCidadeService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("/executor")
	@ApiOperation(
			value = "Corrige o cd_cidade e sg_uf_cidade do AIT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fix iniciado", response = Response.class),
			@ApiResponse(code = 204, message = "Nenhum AIT para correção foi encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response corrigeCidadeUf(
			@ApiParam(value = "Data da infração") @QueryParam(value = "dtInfracao") String dtInfracao,
			@ApiParam(value = "Códigos dos lotes de impressão")@QueryParam(value = "cdsLoteImpressao") List<Integer> cdsLoteImpressao,
			@ApiParam(value = "Códigos dos AITs")@QueryParam(value = "cdsAit") List<Integer> cdsAit) {
		try {
			this.fixCidadeService.corrigirCidadeUf(dtInfracao, cdsLoteImpressao, cdsAit);
			return ResponseFactory.ok("Fixed");
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
