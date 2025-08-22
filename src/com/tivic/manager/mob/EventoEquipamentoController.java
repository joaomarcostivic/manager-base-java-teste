package com.tivic.manager.mob;

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

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.auth.jwt.JWTIgnore;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

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

@Api(value = "Tabela Horário", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/equipamentos/eventos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventoEquipamentoController {
	

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Evento de Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evento de Equipamento registrada", response = EventoEquipamento.class),
			@ApiResponse(code = 400, message = "Evento de Equipamento inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Evento de Equipamento a ser registrado") EventoEquipamento eventoEquipamentoDTO) {
		try {			
			eventoEquipamentoDTO.setCdEvento(0);
			Result result = EventoEquipamentoServices.save(eventoEquipamentoDTO);

			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((EventoEquipamento)result.getObjects().get("EVENTOEQUIPAMENTO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma Evento de Equipamento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evento de Equipamento atualizada", response = EventoEquipamento.class),
			@ApiResponse(code = 400, message = "Evento de Equipamento inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Evento de Equipamento") @PathParam("id") int cdEventoEquipamento,
			@ApiParam(value = "Evento de Equipamento a ser atualizada") EventoEquipamento eventoEquipamentoDTO) {
		try {			
			eventoEquipamentoDTO.setCdEvento(cdEventoEquipamento);

			Result result = EventoEquipamentoServices.save(eventoEquipamentoDTO);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((EventoEquipamento)result.getObjects().get("EVENTOEQUIPAMENTO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece todos os registros de Tabela Horário",
		notes = "Considere id = cdEventoEquipamento"
	)
	@JWTIgnore
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Eventos encontrados"),
		@ApiResponse(code = 204, message = "Nenhuma evento"),
		@ApiResponse(code = 204, message = "Não existe Eventos com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite,
		@DefaultValue("-1") @QueryParam("cdEquipamento") int cdEquipamento,
		@DefaultValue("-1") @QueryParam("cdTipoEvento") int cdTipoEvento,
		@QueryParam("stEvento") String stEvento,
		@QueryParam("nrPlaca") String nrPlaca,
		@QueryParam("idOrgao") String idOrgao,
		@QueryParam("dtInicial") String dtInicial,
		@QueryParam("dtFinal") String dtFinal
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(nrPagina > 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite > 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdEquipamento >= 0) {
				crt.add("A.cd_equipamento", Integer.toString(cdEquipamento), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdTipoEvento >= 0) {
				crt.add("A.cd_tipo_evento", Integer.toString(cdTipoEvento), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nrPlaca != null && !nrPlaca.trim().equals("")) {
				crt.add("A.nr_placa", nrPlaca, ItemComparator.LIKE_ANY, Types.INTEGER);				
			}
			
			if(stEvento != null && !stEvento.trim().equals("")) {
				crt.add("A.st_evento", stEvento, ItemComparator.IN, Types.INTEGER);				
			}
			
			if(stEvento != null && !stEvento.trim().equals("")) {
				crt.add("A.st_evento", stEvento, ItemComparator.IN, Types.INTEGER);				
			}

			if(dtInicial != null && !dtInicial.trim().equals("")) {
				crt.add("A.dt_conclusao", dtInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);				
			}

			if(dtFinal != null && !dtFinal.trim().equals("")) {
				crt.add("A.dt_conclusao", dtFinal, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);				
			}
						
			ResultSetMap rsm = EventoEquipamentoServices.find(crt);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Eventos com os filtros indicados.");

			return Response.ok(new PagedResponse<EventoEquipamento>(new ResultSetMapper<EventoEquipamento>(rsm, EventoEquipamento.class).toList(), rsm.getTotal())).build();
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	

	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece o registro de uma Evento de Equipamento dado seu id",
		notes = "Considere id = cdEventoEquipamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Tabelas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma tabela"),
		@ApiResponse(code = 204, message = "Não existe Tabelas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getEventoEquipamento(
			@ApiParam(value = "Id da Evento de Equipamento") @PathParam("id") int cdEventoEquipamento,
			@ApiParam(value = "Id da Linha") @PathParam("idLinha") int cdLinha
	) {
		try {
			Criterios crt = new Criterios();
			ResultSetMap rsm = EventoEquipamentoServices.find(
				crt.add("A.cd_evento", Integer.toString(cdEventoEquipamento), ItemComparator.EQUAL, Types.INTEGER)
			);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Evento com o id indicado.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
}
