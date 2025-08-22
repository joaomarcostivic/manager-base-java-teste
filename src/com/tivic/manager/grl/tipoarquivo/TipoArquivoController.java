package com.tivic.manager.grl.tipoarquivo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Tipo de Arquivo", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/grl/tipoarquivo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoArquivoController {
	
	private ManagerLog managerLog;
	private ITipoArquivoService tipoArquivoService;
	
	public TipoArquivoController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.tipoArquivoService = (ITipoArquivoService) BeansFactory.get(ITipoArquivoService.class);
	}
	
	@GET
	@Path("/listagem")
	@ApiOperation(value = "Busca a lista de tipos de aquivo")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Tipos arquivo buscados com sucesso", response = TipoArquivo[].class),
		@ApiResponse(code = 204, message = "Nenhum registro encontrado", response = TipoArquivo[].class),
		@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class) 
	})
	public Response buscarArquivosPublicados(
	) {
		try {	
			List<TipoArquivo> tipoArquivoList = this.tipoArquivoService.find();
			return ResponseFactory.ok(tipoArquivoList);
		}
		catch(Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
