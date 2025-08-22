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

@Api(value = "Tabela Horário Rota", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
	})
@Path("/v2/mob/tabelas-horario-rota")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TabelaHorarioRotaController {
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Tabela de Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tabela de Horário registrada", response = TabelaHorarioRota.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Tabela de Horário a ser registrado") TabelaHorarioRota tabelaHorarioRota) {
		try {			
			tabelaHorarioRota.setCdTabelaHorario(0);
			
			Result result = TabelaHorarioRotaServices.save(tabelaHorarioRota);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((TabelaHorarioRota)result.getObjects().get("TABELAHORARIOROTA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma Tabela de Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tabela de Horário atualizada", response = TabelaHorarioRota.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Tabela de Horário") @PathParam("id") int cdTabelaHorario,
			@ApiParam(value = "Tabela de Horário a ser atualizada") TabelaHorarioRota tabelaHorarioRota) {
		try {			
			tabelaHorarioRota.setCdTabelaHorario(cdTabelaHorario);

			Result result = TabelaHorarioRotaServices.save(tabelaHorarioRota);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((TabelaHorario)result.getObjects().get("TABELAHORARIOROTA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLinha}/{cdTabelaHorario}")
	@ApiOperation(
		value = "Fornece o registro de horarios de uma determinada tabela horario rota",
		notes = "Considere id = cdTabelaHorario"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Tabelas Horarios Rotas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma tabela"),
		@ApiResponse(code = 204, message = "Não existe Tabelas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAllByTabelaLinha(
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Tabela de Horário") @PathParam("cdTabelaHorario") int cdTabelaHorario

	) {
		try {
			ResultSetMap rsm = TabelaHorarioRotaServices.getAllByTabelaLinha(cdLinha, cdTabelaHorario);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe horários nessa rota.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece todos os registros de Tabela Horário",
		notes = "Considere id = cdTabelaHorario"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Tabelas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma tabela"),
		@ApiResponse(code = 204, message = "Não existe Tabelas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite,
		@DefaultValue("-1") @QueryParam("tpTabelaHorario") int tpTabelaHorario,
		@DefaultValue("-1") @QueryParam("stTabelaHorario") int stTabelaHorario,
		@QueryParam("nmTabelaHorario") String nmTabelaHorario,
		@QueryParam("idLinha") int idLinha
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(tpTabelaHorario >= 0) {
				crt.add("A.tp_tabela_horario", Integer.toString(tpTabelaHorario), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(stTabelaHorario >= 0) {
				crt.add("A.st_tabela_horario", Integer.toString(stTabelaHorario), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nmTabelaHorario != null && !nmTabelaHorario.trim().equals("")) {
				crt.add("A.nm_tabela_horario", nmTabelaHorario, ItemComparator.LIKE_ANY, Types.INTEGER);				
			}
			
			if(idLinha != 0) {
				crt.add("A.cd_linha", Integer.toString(idLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			ResultSetMap rsm = TabelaHorarioRotaServices.find(crt);

			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Tabela com o id indicado.");

			
			return ResponseFactory.ok(new TabelaHorarioDTO.ListBuilder(rsm, rsm.getTotal()).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	


}
