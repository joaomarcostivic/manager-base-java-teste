package com.tivic.manager.grl.equipamento;

import java.util.List;

import javax.ws.rs.BadRequestException;
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
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;

@Api(value = "Equipamentos", tags = {"grl"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v3/grl/equipamentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EquipamentoController {
	
	IEquipamentoService equipamentoService;
	private ManagerLog managerLog;
	
	public EquipamentoController() throws Exception {
		equipamentoService = (IEquipamentoService) BeansFactory.get(IEquipamentoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
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
	public Response create(@ApiParam(value = "Equipamento a ser registrado") Equipamento equipamento) {
		try {			
			equipamento = equipamentoService.insert(equipamento);
			return ResponseFactory.ok(equipamento);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch (Exception e) {
			managerLog.showLog(e);
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
	public Response update(
			@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento,
			@ApiParam(value = "Equipamento a ser atualizado") Equipamento equipamento) {
		try {			
			equipamento.setCdEquipamento(cdEquipamento);
			equipamento = equipamentoService.update(equipamento);
			return ResponseFactory.ok(equipamento);
		} catch(BadRequestException e) {
			return ResponseFactory.badRequest(e.getMessage());
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
	public Response get(@ApiParam(value = "Código do Equipamento") @PathParam("id") int cdEquipamento) {
		try {
			Equipamento equipamento = equipamentoService.get(cdEquipamento);
			return ResponseFactory.ok(equipamento);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
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
	public Response find(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") 
				@DefaultValue("50")
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
			
			List<Equipamento> equipamentos = equipamentoService.find(new EquipamentoSearch(limit, idEquipamento, stEquipamento, tpEquipamento, cdOrgao));
			return ResponseFactory.ok(equipamentos);
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{idSerial}/bloqueado")
	@ApiOperation(
			value = "Retorna se o equipamento está bloqueado"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Situação do equipamento", response = Equipamento.class),
			@ApiResponse(code = 204, message = "Equipamento não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response get(@ApiParam(value = "Serial do equipamento") @PathParam("idSerial") String idSerial) {
		try {
			return ResponseFactory.ok(equipamentoService.verificarBloqueio(idSerial));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
