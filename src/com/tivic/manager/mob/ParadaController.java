package com.tivic.manager.mob;

import java.sql.Types;
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

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.grl.TipoLogradouro;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;

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

@Api( value = "Parada", tags = { "Parada" }, authorizations = {
		@Authorization( value = "Bearer Auth", scopes = {
			@AuthorizationScope( scope = "Bearer", description = "JWT token")
		})
})
@Path( "/v2/mob/paradas" )
@Produces( MediaType.APPLICATION_JSON )
public class ParadaController {

	
	@POST
	@Path("")
	@ApiOperation(
				value = "Registra uma nova Parada"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Parada registrada", response = Parada.class),
			@ApiResponse( code = 400, message = "Parada possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro duranto o processamento da requisição", response = ResponseBody.class )
	})
	@Consumes( MediaType.APPLICATION_JSON )
	public static Response create( @ApiParam( value = "Parada a ser registrada", required= true ) ParadaDTO paradaDTO) {
		try {

			paradaDTO.getParada().setCdParada(0);
			
			Result r = ParadaServices.save(paradaDTO.getParada(), paradaDTO.getGrupoParada());
			
			if( r.getCode() < 0 ) {
				return ResponseFactory.badRequest("Parada possui algum parâmetro inválido", r.getMessage() );
			}

			return ResponseFactory.ok(r.getObjects().get( "PARADA" ) );

		} catch( Exception e ) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage() );
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma Parada"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Parada atualizada", response = Parada.class ),
			@ApiResponse( code = 400, message = "Parada é nula ou inválida" ),
			@ApiResponse( code = 500, message = "Erro no servidor" )
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam( value = "Id da Parada a ser Atualizada", required = true) @PathParam("id") int cdParada,
			@ApiParam( value = "Parada a ser atualizada", required = true ) Parada parada ) {
		
		try {
			if( parada.getCdParada() == 0 ) {
				return ResponseFactory.badRequest("Parada nula ou inválida");
			}
			parada.setCdParada( cdParada );
			Result r = ParadaServices.save( parada );
			
			return ResponseFactory.ok( ( Parada )r.getObjects().get( "PARADA" ) );
			
		} catch( Exception e ) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage() );
		}
	}
	
	@GET
	@Path("/taxis/{cdGrupoParada}")
	@ApiOperation(
				value = "Retorna Paradas de Taxi (Número de Ordem)"
			)
	@ApiResponses( value = {
				@ApiResponse( code = 200, message = "Parada encontrada", response = Parada[].class ),
				@ApiResponse( code = 204, message = "Parada não encontrada", response = ResponseBody.class ),
				@ApiResponse( code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class )
			})
	public static Response getByGrupo(@ApiParam( value = "Id do grupo parada (Nr Ponto)") @PathParam("cdGrupoParada") int cdGrupoParada) {
		try {
			ResultSetMap rsm = ParadaServices.getAllByGrupoParada(cdGrupoParada);
			if( !rsm.next() )
				return ResponseFactory.noContent( "Nenhuma parada encontrada" );
			
			List<Parada> paradas = new ResultSetMapper<Parada>(rsm, Parada.class).toList();
			return ResponseFactory.ok(paradas);
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição", e.getMessage() );
		}
	}
}
