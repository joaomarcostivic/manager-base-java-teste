package com.tivic.manager.fix.mob.ait.infracao;

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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.util.Result;

@Api(value = "fix", tags = {"fix"})
@Path("/v3/sis/fix")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FixInfracaoAitController {
	private ManagerLog managerLog;
	private IFixInfracaoAitService fixInfracaoAitService;

	
	public FixInfracaoAitController() throws Exception {
		this.fixInfracaoAitService = (IFixInfracaoAitService) BeansFactory.get(IFixInfracaoAitService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@POST
	@Path("/infracao-ait/executor")
	@ApiOperation(
			value = "Corrige o cd_infracao do AIT para infracao vigente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fix iniciado", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response iniciarCorrecao(
			@QueryParam(value = "dtInfracao") String dtInfracao,
			@QueryParam(value = "cdInfracaoAntigo") int cdInfracaoAntigo,
			@QueryParam(value = "cdInfracaoNovo") int cdInfracaoNovo
			) {	
		try {
			this.fixInfracaoAitService.corrigirInfracaoAit(cdInfracaoAntigo, cdInfracaoNovo, dtInfracao);
			return ResponseFactory.ok("Fixed");
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	
	}

	@POST
	@Path("/evento-ait/executor")
	@ApiOperation(
			value = "Corrige o cd_equipamento do AIT a partir de um lote de impressao para o equipamento correto"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fix iniciado", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public Response iniciarCorrecao(
			@QueryParam(value = "cdLoteImpressao") int cdLoteImpressao
			) {	
		try {
			this.fixInfracaoAitService.corrigirEventoAit(cdLoteImpressao);
			return ResponseFactory.ok("Fixed");
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}

}
