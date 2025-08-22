package com.tivic.manager.grl.equipamento.radar;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.cdi.BeansFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ResultSetMap;

@Api(value = "Radares", tags = { "grl" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/grl/equipamentos/radares")
@Consumes(MediaType.APPLICATION_JSON)
public class RadarController {

	RadarService radarService;
	
	public RadarController() throws Exception {
		radarService = (RadarService) BeansFactory.get(RadarService.class);
	}
	
	@GET
	@Path("/estaticos")
	@ApiOperation(
		value = "Retorna os radares estáticos"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Radares encontrados"),
		@ApiResponse(code = 204, message = "Nenhum radar encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response getAllRadarEstatico() {
		try {
			return ResponseFactory.ok(radarService.getAllRadarEstatico());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/fixos")
	@ApiOperation(
		value = "Retorna os radares fixos"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Radares encontrados"),
		@ApiResponse(code = 204, message = "Nenhum radar encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response getAllRadarFixo() {
		try {
			return ResponseFactory.ok(radarService.getAllRadarFixo());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/fixos/ftp")
	@ApiOperation(
		value = "Retorna os radares fixos sincronizados com ftp"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Radares encontrados"),
		@ApiResponse(code = 204, message = "Nenhum radar encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public Response getAllRadarFtp() {
		try {
			return ResponseFactory.ok(radarService.getAllRadarFtp());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	
	@GET
	@Path("/informacoes")
	@ApiOperation(
			value = "Retorna informações de operação do equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Informações encontradas", response = Object[].class),
			@ApiResponse(code = 204, message = "Nenhum resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getInfoRadar(@ApiParam(name = "tipo", value = "\n"
			+ "\t 0: Talonário eletrônico\n"
			+ "\t 1: Semáforo\n"
			+ "\t 2: Radar fixo\n"
			+ "\t 3: Radar móvel\n"
			+ "\t 4: GPS\n"
			+ "\t 5: Taxímetro\n"
			+ "\t 6: Impressora\n"
			+ "\t 7: Fiscalizador\n"
			+ "\t 8: Tacógrafo\n"
			+ "\t 9: Câmera\n"
			+ "\t10: Radar estático\n", allowableValues = "0,1,2,3,4,5,6,7,8,9,10", required = true) 
		@DefaultValue("2") 
		@QueryParam("tipo") int tpEquipamento) {
		try {
			
			if(tpEquipamento != 2)
				return ResponseFactory.noContent("Nenhum resultado encontrado");
			
			List<InfoRadar> infoRadares = radarService.getInfoRadar(tpEquipamento);
			if(infoRadares.isEmpty())
				return ResponseFactory.noContent("Nenhum resultado encontrado");
			return ResponseFactory.ok(infoRadares);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
