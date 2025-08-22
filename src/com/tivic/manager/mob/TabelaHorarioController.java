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

import com.tivic.manager.mob.tabelashorarios.TabelaHorarioService;
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

@Api(value = "Tabela Horário", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/tabelas-horario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TabelaHorarioController {

	TabelaHorarioService tabelaHorarioService;
	
	public TabelaHorarioController() {
		tabelaHorarioService = new TabelaHorarioService();
	}
	
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Tabela de Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tabela de Horário registrada", response = TabelaHorario.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response create(@ApiParam(value = "Tabela de Horário a ser registrado") TabelaHorarioDTO tabelaHorarioDTO) {
		try {			
			tabelaHorarioDTO.setCdTabelaHorario(0);
			tabelaHorarioService.saveComRotas(tabelaHorarioDTO);
			return ResponseFactory.ok(tabelaHorarioDTO);
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
			@ApiResponse(code = 200, message = "Tabela de Horário atualizada", response = TabelaHorario.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(
			@ApiParam(value = "Código da Tabela de Horário") @PathParam("id") int cdTabelaHorario,
			@ApiParam(value = "Tabela de Horário a ser atualizada") TabelaHorarioDTO tabelaHorarioDTO) {
		try {			
			tabelaHorarioDTO.setCdTabelaHorario(cdTabelaHorario);
			tabelaHorarioService.saveComRotas(tabelaHorarioDTO);
			return ResponseFactory.ok(tabelaHorarioDTO);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
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
		@QueryParam("cdLinha") int cdLinha,
		@QueryParam("cdRota") int cdRota
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(nrPagina > 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite > 0) {
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
			
			if(cdLinha > 0) {
				crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdRota > 0) {
				crt.add("A.cd_rota", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			ResultSetMap rsm = TabelaHorarioServices.find(crt);
			

			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Tabela com o id indicado.");

			return ResponseFactory.ok(new TabelaHorarioDTO.ListBuilder(rsm, rsm.getTotal()).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	

	@GET
	@Path("/{id}/{idLinha}")
	@ApiOperation(
		value = "Fornece o registro de uma Tabela de Horário dado seu id",
		notes = "Considere id = cdTabelaHorario"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Tabelas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma tabela"),
		@ApiResponse(code = 204, message = "Não existe Tabelas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getTabelaHorario(
			@ApiParam(value = "Id da Tabela de Horário") @PathParam("id") int cdTabelaHorario,
			@ApiParam(value = "Id da Linha") @PathParam("idLinha") int cdLinha
	) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER)
		   .add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);
			ResultSetMap rsm = TabelaHorarioServices.find(crt);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Tabela com o id indicado.");
			
			return ResponseFactory.ok(new TabelaHorarioDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@PUT
	@Path("/inactivate")
	@ApiOperation(
			value = "Inativar uma Tabela de Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tabela de Horário inativar", response = TabelaHorario.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response inactivate(
			@ApiParam(value = "Tabela de Horário a ser atualizada") TabelaHorario tabelaHorario) {
		try {			
			tabelaHorario = tabelaHorarioService.inactivate(tabelaHorario);
			return ResponseFactory.ok(tabelaHorario);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/activate")
	@ApiOperation(
			value = "Ativar uma Tabela de Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tabela de Horário ativar", response = TabelaHorario.class),
			@ApiResponse(code = 400, message = "Tabela de Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response activate(
			@ApiParam(value = "Tabela de Horário a ser atualizada") TabelaHorario tabelaHorario) {
		try {			
			tabelaHorario = tabelaHorarioService.activate(tabelaHorario);
			return ResponseFactory.ok(tabelaHorario);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
