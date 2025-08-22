package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;

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


@Api(value = "Horário", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/horarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HorarioController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma novo Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Horário registrada", response = Horario.class),
			@ApiResponse(code = 400, message = "Horário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Tabela de Horário a ser registrado") Horario horario) {
		try {			
			horario.setCdHorario(0);
			
			Result result = HorarioServices.save(horario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Horario)result.getObjects().get("HORARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/loteHorarios")
	@ApiOperation(
			value = "Registra um lote de hor�rios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Horário registrada", response = Horario.class),
			@ApiResponse(code = 400, message = "Horário inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response saveHorarios(@ApiParam(value = "LoteHorariosDTO com as informa��es dos hor�rios a ser gerado") LoteHorariosDTO loteHorariosDTO) {
		try {			
			Result result = HorarioServices.saveHorarios(loteHorariosDTO);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Horario)result.getObjects().get("HORARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/grupoHorarios")
	@ApiOperation(
			value = "Registra um grupo de 3 hor�rios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Horários registrada", response = Horario.class),
			@ApiResponse(code = 400, message = "Horários inválidos", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response saveHorarios(@ApiParam(value = "Array de 3 hor�rios para salvar em sequencia") ArrayList<Horario> horario) {
		try {			
			Result result = HorarioServices.saveGrupoHorarios(horario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((ArrayList<Horario>)result.getObjects().get("HORARIOS"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um Horário"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Horário atualizada", response = Horario.class),
			@ApiResponse(code = 400, message = "Horário inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Tabela de Horário") @PathParam("id") int cdHorario,
			@ApiParam(value = "Tabela de Horário a ser atualizada") Horario horario) {
		try {			
			horario.setCdHorario(cdHorario);

			Result result = HorarioServices.save(horario);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((Horario)result.getObjects().get("HORARIO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	@GET
	@Path("/{cdTabelaHorario}/{cdLinha}/{cdRotaIda}/{cdRotaVolta}")
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
	public static Response getTabelaHorario(
			@ApiParam(value = "Id da Tabela de Horário") @PathParam("idTabela") int idTabela,
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Linha") @PathParam("cdRotaIda") int cdRotaIda,
			@ApiParam(value = "Id da Linha") @PathParam("cdRotaVolta") int cdRotaVolta
	) {
		try {
			Result result = HorarioServices.getHorarios(idTabela, cdLinha, cdRotaIda, cdRotaVolta);
			
			if(result.getCode() < 0)
				return ResponseFactory.noContent("Nao existe horários nessa rota.");
			
			return ResponseFactory.ok((Horario)result.getObjects().get("HORARIO"));
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{cdTabelaHorario}/{cdLinha}/{cdRotaIda}/ida")
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
	public static Response getHorariosIda(
			@ApiParam(value = "Id da Tabela de Horário") @PathParam("idTabela") int idTabela,
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Linha") @PathParam("cdRotaIda") int cdRotaIda
	) {
		try {
			ResultSetMap horariosIda = HorarioServices.getHorariosIda(idTabela, cdLinha, cdRotaIda);
			
			if(!horariosIda.next())
				return ResponseFactory.noContent("Nao existe horários nessa rota.");
			
			return ResponseFactory.ok(horariosIda);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLinha}/{cdTabelaHorario}/{cdRota}/horariorota")
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
	public static Response getAllByTabelaHorarioRota(
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Linha") @PathParam("cdTabelaHorario") int cdTabelaHorario,
			@ApiParam(value = "Id da Linha") @PathParam("cdRota") int cdRota
	) {
		try {
			ResultSetMap rsm = HorarioServices.getAllByTabelaHorarioRota(cdLinha, cdTabelaHorario, cdRota);
			
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
		@ApiResponse(code = 200, message = "TabelasHorarios encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma tabela"),
		@ApiResponse(code = 204, message = "Não existe Tabelas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("cdRota") int cdRota,
		@QueryParam("cdTabelaHorario") int cdTabelaHorario,
		@QueryParam("cdLinha") int cdLinha,
		@QueryParam("tpLinha") int tpLinha
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(cdRota > 0) {
				crt.add("A.cd_rota", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdTabelaHorario > 0) {
				crt.add("A.cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdLinha > 0) {
				crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(tpLinha > 0) {
				crt.add("tp_linha", Integer.toString(tpLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			ResultSetMap rsm = HorarioServices.find(crt);

			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Tabela com o id indicado.");

			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	

}
