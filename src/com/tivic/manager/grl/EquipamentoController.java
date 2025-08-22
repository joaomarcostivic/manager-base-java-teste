package com.tivic.manager.grl;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.rest.request.filter.Criterios;
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

@Api(value = "Equipamentos", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/equipamentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EquipamentoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento registrado", response = Equipamento.class),
			@ApiResponse(code = 400, message = "Equipamento inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Equipamento a ser registrado") Equipamento equipamento) {
		try {			
			equipamento.setCdEquipamento(0);
			
			if(EquipamentoServices.validEquipamentoInsert(equipamento).getCode() == -1)
				return ResponseFactory.badRequest("Equipamento já existe");				
			
			Result result = EquipamentoServices.save(equipamento);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Equipamento)result.getObjects().get("EQUIPAMENTO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento atualizado", response = Equipamento.class),
			@ApiResponse(code = 400, message = "Equipamento inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento,
			@ApiParam(value = "Equipamento a ser atualizado") Equipamento equipamento) {
		try {			
			equipamento.setCdEquipamento(cdEquipamento);
			
			Result result = EquipamentoServices.save(equipamento);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Equipamento)result.getObjects().get("EQUIPAMENTO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamento encontrado", response = Equipamento.class),
			@ApiResponse(code = 204, message = "Equipamento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento) {
		try {
			Equipamento equipamento = EquipamentoDAO.get(cdEquipamento);
			if(equipamento == null) {
				return ResponseFactory.noContent("Nenhum equipamento encontrado");
			}
			
			return ResponseFactory.ok(equipamento);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna uma lista de Equipamentos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Equipamentos encontrados", response = Equipamento[].class),
			@ApiResponse(code = 204, message = "Nenhum equipamento", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros")
				@QueryParam("limit") int limit,
			@ApiParam(value = "Id do equipamento") 
				@QueryParam("id") String idEquipamento,
			@ApiParam(name = "situacao", value = "\t0: Inativo\n\t1: Ativo", allowableValues = "0, 1") 
				@DefaultValue("-1") 
				@QueryParam("situacao") int stEquipamento,
			@ApiParam(name = "tipo", value = "\n"
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
					+ "\t10: Radar estático\n", allowableValues = "0,1,2,3,4,5,6,7,8,9,10") 
				@DefaultValue("-1") 
				@QueryParam("tipo") int tpEquipamento,
			@ApiParam(value = "Código do órgão") @QueryParam("orgao") int cdOrgao) {
		try {
			Criterios criterios = new Criterios();
			
			if(idEquipamento != null)
				criterios.add("id_equipamento", idEquipamento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			if(stEquipamento >= 0)
				criterios.add("st_equipamento", Integer.toString(stEquipamento), ItemComparator.EQUAL, Types.INTEGER);
			if(tpEquipamento >= 0)
				criterios.add("tp_equipamento", Integer.toString(tpEquipamento), ItemComparator.EQUAL, Types.INTEGER);
			if(cdOrgao > 0)
				criterios.add("cd_orgao", Integer.toString(cdOrgao), ItemComparator.EQUAL, Types.INTEGER);
			
			ResultSetMap rsm = EquipamentoServices.find(criterios);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum equipamento encontrado");
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
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
	public static Response getInfoRadar(@ApiParam(name = "tipo", value = "\n"
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
			
			ResultSetMap rsm = EquipamentoServices.getInfoRadar();
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum resultado encontrado");
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
