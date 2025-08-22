package com.tivic.manager.mob;

import java.sql.Types;

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

@Api( value = "GrupoParada", tags = { "GrupoParada" }, authorizations = {
		@Authorization( value = "Bearer Auth", scopes = {
			@AuthorizationScope( scope = "Bearer", description = "JWT token")
		})
})
@Path( "/v2/mob/gruposparadas" )
@Produces( MediaType.APPLICATION_JSON )
public class GrupoParadaController {
	
	@POST
	@Path("")
	@ApiOperation(
				value = "Cria um ponto de taxi"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Registro criado", response = GrupoParada.class ),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class )
	})
	public static Response create(@ApiParam(value = "Ponto a ser registrado") GrupoParada grupoParada) {
		try {
			grupoParada.setCdGrupoParada(0);
			
			Result result = GrupoParadaServices.save(grupoParada);
			
			if(result.getCode()< 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok(result.getObjects().get("GRUPOPARADA"));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/pontostaxi")
	@ApiOperation(
				value = "Retorna os Pontos de Taxi (Número de Ponto)"
			)
	@ApiResponses( value = {
				@ApiResponse( code = 200, message = "Grupo de Parada encontrada", response = GrupoParada[].class ),
				@ApiResponse( code = 204, message = "Grupo de Parada não encontrada", response = ResponseBody.class ),
				@ApiResponse( code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class )
			})
	public static Response getAllPracaTaxi(@ApiParam(value = "Numero do praça") @QueryParam("nrPraca") String nrPraca) {
		try {
			Criterios crt = new Criterios();
			crt.add("tp_grupo_parada", Integer.toString(GrupoParadaServices.TP_GRUPO_PRACA_TAXI), ItemComparator.EQUAL, Types.INTEGER);
			
			if(nrPraca != null) {
				crt.add("nm_grupo_parada", nrPraca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			ResultSetMap rsm = GrupoParadaServices.findPracaTaxi(crt);

			if(rsm == null)
				return ResponseFactory.noContent( "Nenhuma parada encontrada" );
			
			return ResponseFactory.ok(rsm);
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage() );
		}
	}
	
	@PUT
	@Path("/pontostaxi/{id}")
	@ApiOperation(
				value = "Atualiza um ponto de taxi"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ponto de taxi atualizados", response = GrupoParada.class),
			@ApiResponse(code = 404, message = "Ponto de taxi invalídos", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response updateTaxi(
			@ApiParam(value = "Código do ponto") @PathParam("id") int cdGrupoParada,
			@ApiParam(value = "Ponto a ser atualizada") GrupoParada grupoParada) {
		try {
			grupoParada.setCdGrupoParada(cdGrupoParada);
			
			Result result = GrupoParadaServices.save(grupoParada);
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((GrupoParada)result.getObjects().get("GRUPOPARADA"));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/pontostaxi/{id}")
	@ApiOperation(
			value = "Retorna Ponto de Taxi"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ponto encontrado", response = GrupoParada.class),
			@ApiResponse(code = 404, message = "Nenhum ponto encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getTaxi(@ApiParam(value = "Código do ponto") @PathParam("id") int cdGrupoParada) {
		try {
			GrupoParada grupoParada = GrupoParadaDAO.get(cdGrupoParada);

			if(grupoParada == null)
				return ResponseFactory.notFound("Nenhum registro encontrado");

			return ResponseFactory.ok(grupoParada);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um grupo parada"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupo Parada atualizada", response = GrupoParada.class),
			@ApiResponse(code = 400, message = "Grupo Parada inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Código do grupo parada") @PathParam("id") int cdGrupoParada,
			@ApiParam(value = "Grupo parada a ser atualizado") GrupoParada grupoParada) {
		try {			
			grupoParada.setCdGrupoParada(cdGrupoParada);

			Result result = GrupoParadaServices.save(grupoParada);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((GrupoParada)result.getObjects().get("GRUPOPARADA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece todos os registros de grupo parada",
		notes = ""
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Grupo Parada encontrados"),
		@ApiResponse(code = 204, message = "Nenhum grupo parada"),
		@ApiResponse(code = 204, message = "Não existe grupo parada com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response getAll(
		@QueryParam("parada") String dsNmGrupoParada,
		@QueryParam("dsNmGrupoParadaSuperior") String dsNmGrupoParadaSuperior,
		@QueryParam("tpGrupoParada") @DefaultValue("-1") int tpGrupoParada

	) {
		try {
			Criterios crt = new Criterios();
			
			if(dsNmGrupoParada != null) {
				crt.add("B.nm_grupo_parada", dsNmGrupoParada, ItemComparator.LIKE_ANY, Types.VARCHAR);				
			}
			
			if(dsNmGrupoParadaSuperior != null) {
				crt.add("D.nm_grupo_parada", dsNmGrupoParadaSuperior, ItemComparator.LIKE_ANY, Types.VARCHAR);				
			}
			
			if(tpGrupoParada > 0) {
				crt.add("B.tp_grupo_parada", Integer.toString(tpGrupoParada), ItemComparator.EQUAL, Types.INTEGER);				
			}
				
			ResultSetMap rsm = GrupoParadaServices.find(crt);
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe grupo parada com os filtros indicados.");

			
			return ResponseFactory.ok(new GrupoParadaDTO.ListBuilder(rsm).build());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{cdParada}")
	@ApiOperation(
		value = "Busca um registro de grupo parada",
		notes = "Considere id = cdParada"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Grupo Parada encontrados"),
			@ApiResponse(code = 204, message = "Nenhum grupo parada"),
			@ApiResponse(code = 204, message = "Não existe grupo parada com o id indicado"),
			@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response get(
			@ApiParam(value = "Id do Grupo Parada") @PathParam("cdGrupoParada") int cdGrupoParada
	) {
		try {
			Criterios    crt = new Criterios();
			
			ResultSetMap rsm = ParadaServices.find(crt.add("A.cd_grupo_parada",  Integer.toString(cdGrupoParada), ItemComparator.EQUAL, Types.INTEGER));
			
			if(!rsm.next())
				return ResponseFactory.noContent("Nao existe Grupo Parada com o id indicado.");

			return ResponseFactory.ok(rsm.getRegister());
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
}
