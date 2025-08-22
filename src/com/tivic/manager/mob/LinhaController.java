package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

import com.tivic.manager.fta.VeiculoDTO;
import com.tivic.manager.mob.tabelashorarios.TabelaHorarioRotaBuilder;
import com.tivic.manager.mob.tabelashorarios.TabelaHorarioRotaDTO;

@Api(value = "Linha", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/linhas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LinhaController {
	
	

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Linha"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linha a registrada", response = Linha.class),
			@ApiResponse(code = 400, message = "Linha inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Linha a ser registrado") Linha linha) {
		try {			
			linha.setCdLinha(0);
			
			Result result = LinhaServices.save(linha);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Linha)result.getObjects().get("LINHA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma nova Linha"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linha a registrada", response = Linha.class),
			@ApiResponse(code = 400, message = "Linha inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código da Linha") @PathParam("id") int cdLinha,
			@ApiParam(value = "Linha a ser registrado") Linha linha) {
		try {			
			linha.setCdLinha(cdLinha);
			
			Result result = LinhaServices.save(linha);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Linha)result.getObjects().get("LINHA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece todos os registros de Linha",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Linhas encontradas"),
		@ApiResponse(code = 204, message = "Nenhuma linha"),
		@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("page") int nrPagina,
		@QueryParam("limit") int nrLimite,
		@QueryParam("nrLinha") String nrLinha,
		@QueryParam("cdLinha") int cdLinha,
		@DefaultValue("-1") @QueryParam("stLinha") int stLinha,
		@DefaultValue("-1") @QueryParam("tpLinha") int tpLinha,
		@QueryParam("cdConcessao") int cdConcessao
	) {
		try {
			Criterios    crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nrLinha != null) {
				crt.add("nr_linha", nrLinha, ItemComparator.LIKE_ANY, Types.VARCHAR);				
			}
			
			if(cdLinha != 0) {
				crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(stLinha >= 0) {
				crt.add("A.st_linha", Integer.toString(stLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(tpLinha >= 0) {
				crt.add("A.tp_Linha", Integer.toString(tpLinha), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdConcessao != 0) {
				crt.add("B.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			ResultSetMap rsm = LinhaServices.find(crt);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Linhas com os filtros indicados.");

			
			return ResponseFactory.ok(new LinhaDTO.ListBuilder(rsm, rsm.getTotal()).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	

	@GET
	@Path("/{idLinha}")
	@ApiOperation(
		value = "Fornece o registro de uma Linha dado seu id",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linhas encontradas"),
			@ApiResponse(code = 204, message = "Nenhuma linha"),
			@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getLinha(
			@ApiParam(value = "Id da Linha") @PathParam("idLinha") int idLinha
	) {
		try {
			Criterios crt = new Criterios();
			ResultSetMap rsm = LinhaServices.find(
				   crt.add("A.cd_linha", Integer.toString(idLinha), ItemComparator.EQUAL, Types.INTEGER)
			);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Tabela com o id indicado.");
			
			return ResponseFactory.ok(new LinhaDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdLinha}/tabelas/{cdTabelaHorario}")
	@ApiOperation(
		value = "Fornece o registro de horarios das rotas de uma Linha dado seu código",
		notes = "Considere id = cdLinha"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Linhas encontradas"),
			@ApiResponse(code = 204, message = "Nenhuma linha"),
			@ApiResponse(code = 204, message = "Não existe linhas com o id indicado"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public  Response getTabelas(
			@ApiParam(value = "Id da Linha") @PathParam("cdLinha") int cdLinha,
			@ApiParam(value = "Id da Tabela Horario") @PathParam("cdTabelaHorario") int cdTabelaHorario
	) {
		try {
			Criterios crt = new Criterios();
			
			ResultSetMap rsmTabela = TabelaHorarioServices.find(
					crt.add("A.cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER)
					   .add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER)
			);
			rsmTabela.next();
			
			TabelaHorarioDTO tabelahorarioDTO = new TabelaHorarioDTO.Builder(rsmTabela.getRegister()).build();

			ResultSetMap rsm = LinhaServices.getTabelas(cdLinha, cdTabelaHorario);

			tabelahorarioDTO.setHorariosParadas(new TabelaHorarioRotaBuilder().builder(rsm));
			
			return ResponseFactory.ok(tabelahorarioDTO);
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
		
}
