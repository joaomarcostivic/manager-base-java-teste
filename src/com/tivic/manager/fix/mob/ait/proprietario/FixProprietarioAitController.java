package com.tivic.manager.fix.mob.ait.proprietario;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.zxing.Result;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "fix", tags = {"fix"})
@Path("/v3/sis/fix")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FixProprietarioAitController {

	private ManagerLog managerLog;
	private IFixProprietarioAitService fixProprietarioAitService;


	public FixProprietarioAitController() throws Exception {
		this.fixProprietarioAitService = (IFixProprietarioAitService) BeansFactory.get(IFixProprietarioAitService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@POST
	@Path("/proprietario-ait/executor")
	@ApiOperation(
			value = "Insere os dados de endereço do proprietário do AIT"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fix iniciado", response = Result.class),
			@ApiResponse(code = 204, message = "Nenhum AIT encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response iniciarCorrecao(
			@ApiParam(value = "AITs candidatos a lote de NIP (true/false)") @DefaultValue("false")
				@QueryParam("lgAitsCandidatosLoteNip") Boolean lgAitsCandidatosLoteNip, 
			@QueryParam("dtMovimento") String dtMovimento) {	
		try {
			this.fixProprietarioAitService.inserirDadosEnderecoAit(lgAitsCandidatosLoteNip, dtMovimento);
			return ResponseFactory.ok("Fixed");
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
