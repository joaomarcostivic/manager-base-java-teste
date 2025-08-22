package com.tivic.manager.mob.funset;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.funset.file.FunsetFile;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.cdi.BeansFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "FUNSET", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/funset")
@Produces(MediaType.APPLICATION_JSON)
public class FunsetController {

	IFunsetService funsetService;
	
	public FunsetController() throws Exception {
		funsetService = (IFunsetService)BeansFactory.get(IFunsetService.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Fornece lista de AITs para o lançamento do FUNSET"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getAits(
			@ApiParam(value = "Mês do periodo") @QueryParam("nrMes") String nrMes,
			@ApiParam(value = "Ano do periodo") @QueryParam("nrAno") String nrAno) {
		try {	
			if(nrMes==null || nrMes.trim().equals("")) {
				return ResponseFactory.badRequest("Faltando parametro do mês (nrMes)");
			}
			if(nrAno==null || nrAno.trim().equals("")) {
				return ResponseFactory.badRequest("Faltando parametro do ano (nrAno)");
			}
			
			FunsetParametrosEntrada funsetParametrosEntrada = new FunsetParametrosEntrada(nrAno, nrMes);
			List<FunsetAitDTO> funsetAits = funsetService.findAits(funsetParametrosEntrada);
			
			if(funsetAits.isEmpty()) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			return ResponseFactory.ok(funsetAits);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
	

	@GET
	@Path("arquivo")
	@ApiOperation(
		value = "Fornece lista de AITs para o lançamento do FUNSET"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = Ait[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@JWTIgnore
	public Response gerarArquivoFunset(
			@ApiParam(value = "Mês do periodo") @QueryParam("nrMes") String nrMes,
			@ApiParam(value = "Ano do periodo") @QueryParam("nrAno") String nrAno,
			@ApiParam(value = "Código do banco arrecadador") @QueryParam("nrCodigoBancoArrecadador") String nrCodigoBancoArrecadador) {
		try {	

			if(nrMes==null || nrMes.trim().equals("")) {
				return ResponseFactory.badRequest("Faltando parametro do mês (nrMes)");
			}
			if(nrAno==null || nrAno.trim().equals("")) {
				return ResponseFactory.badRequest("Faltando parametro do ano (nrAno)");
			}
			if(nrCodigoBancoArrecadador==null || nrCodigoBancoArrecadador.trim().equals("")) {
				return ResponseFactory.badRequest("Faltando parametro do código do banco arrecadador (nrCodigoBancoArrecadador)");
			}
			
			FunsetParametrosEntrada funsetParametrosEntrada = new FunsetParametrosEntrada(nrAno, nrMes);
			funsetParametrosEntrada.setNrCodigoBancoArrecadador(nrCodigoBancoArrecadador);
			FunsetFile funsetFile = funsetService.generateArquivo(funsetParametrosEntrada);
			
			return ResponseFactory.ok(funsetFile);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}	
	}
}
