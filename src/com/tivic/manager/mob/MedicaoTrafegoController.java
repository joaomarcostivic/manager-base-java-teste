package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Medição de Tráfego", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/medicaotrafego")
@Produces(MediaType.APPLICATION_JSON)
public class MedicaoTrafegoController {


	@GET
	@Path("/estatisticas")
	@ApiOperation(value = "Estatisticas de Tráfego por hora durante um período")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response getEstatistica(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Agrupamento", allowableValues = "velocidade, faixa, especie") @QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Codigo do Orgao", defaultValue = "0") @DefaultValue("0") @QueryParam("orgao") int cdOrgao,
			@ApiParam(value = "Codigo do Equipamento", defaultValue = "0") @DefaultValue("0") @QueryParam("equipamento") int cdEquipamento) {
		try {
			
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim + " 23:59:59");
			
			switch (agrupamento) {
				case "pista-hora":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoPistaHora(cdOrgao, dtInicial, dtFinal));
				case "via":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoVia(cdOrgao, dtInicial, dtFinal));
				case "via-hora":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoViaHora(cdOrgao, cdEquipamento, dtInicial, dtFinal));
				case "total-semana":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoSemana(cdOrgao, dtInicial, dtFinal));
				case "total-hora":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoHora(cdOrgao, dtInicial, dtFinal));
				case "composicao":
					return ResponseFactory.ok(MedicaoTrafegoBIServices.statsTrafegoTipoVeiculo(cdOrgao, dtInicial, dtFinal));
				default:
					return ResponseFactory.noContent("Nao ha estatistica para os parametros fornecidos.");
			}
			
			
			
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getLocalizedMessage());
		}
	}

}
