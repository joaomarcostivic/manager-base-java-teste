package com.tivic.manager.grl;

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

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
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

@Api( value = "TipoOcorrencia", tags = {"grl"}, authorizations = {
		@Authorization( value = "Bearer Auth", scopes = {
				@AuthorizationScope( scope = "Bearer", description = "JWT roken")
		})
})
@Path("/v2/grl/tipoOcorrencia")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TipoOcorrenciaController {
	
	@POST
	@Path("")
	@ApiOperation( 
				value = "Registra uma nova ocorrência"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Ocorrência registrada", response = TipoOcorrencia.class),
			@ApiResponse( code = 400, message = "Ocorrência inválida", response = ResponseBody.class ),
			@ApiResponse( code = 500, message = "Erro durante o provessamento da requisição", response = ResponseBody.class )
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response create( @ApiParam( value = "Ocorrência a ser registrada" ) TipoOcorrencia tipoOcorrencia ) {
		try {
			tipoOcorrencia.setCdTipoOcorrencia(0);
			
			Result result = TipoOcorrenciaServices.save( tipoOcorrencia );
			if( result.getCode() < 0 ) {
				return ResponseFactory.badRequest( result.getMessage() );
			}
			return ResponseFactory.ok( ( TipoOcorrencia )result.getObjects().get("TIPOOCORRENCIA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}	
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
				value = "Atualiza uma ocorrência"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Tipo de Ocorrência atualizada", response = TipoOcorrencia.class ),
			@ApiResponse( code = 400, message = "Tipo de Ocorrência inválida", response = ResponseBody.class ),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class )
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update( 
			@ApiParam( value = "Código do tipo de ocorrencia" ) @PathParam("id") int cdTipoOcorrencia,
			@ApiParam( value = "Tipo de Ocorrência a ser atualizada" ) TipoOcorrencia tipoOcorrencia ) 
	{
		try {
			tipoOcorrencia.setCdTipoOcorrencia(cdTipoOcorrencia);
			
			Result result = TipoOcorrenciaServices.save( tipoOcorrencia );
			if( result.getCode() < 0 ) {
				return ResponseFactory.badRequest( result.getMessage() );
			}
			
			return ResponseFactory.ok( ( TipoOcorrencia )result.getObjects().get("TIPOOCORRENCIA") );
		} catch( Exception e ) {
			return ResponseFactory.internalServerError( e.getMessage() );
		}
		
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
				value = "Retorna um tipo de ocorrência"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Tipo de Ocorrência encontrada", response = TipoOcorrencia.class ),
			@ApiResponse( code = 204, message = "Tipo de Ocorrência nãp localizada", response = ResponseBody.class ),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class )
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get( @ApiParam( value = "Código do Tipo de Ocorrência" ) @PathParam("id") int cdTipoOcorrencia ) {
		try {
			TipoOcorrencia tipoOcorrencia = TipoOcorrenciaDAO.get(cdTipoOcorrencia);
			if( tipoOcorrencia == null ) {
				return ResponseFactory.noContent("Nenhum Tipo de ocorrência encontrada");
			}
			return ResponseFactory.ok( tipoOcorrencia );
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	
	@GET
	@Path("")
	@ApiOperation(
				value = "Retorna uma lista de tipos de ocorrência"
			)
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Tipo de ocorrência encontrado", response = TipoOcorrencia[].class ),
			@ApiResponse( code = 400, message = "Tipo de ocorrência não encontrada", response = ResponseBody.class ),
			@ApiResponse( code= 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class )
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value  = "Nome do Tipo de Ocorrência") @QueryParam("nmTipoOcorrencia") String nmTipoOcorrencia,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		
		try {
			ArrayList< ItemComparator > criterios = new ArrayList< ItemComparator >();
			if( nmTipoOcorrencia != null ) {
				criterios.add( new ItemComparator( "nm_tipo_ocorrencia", nmTipoOcorrencia, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			ResultSetMap rsm = TipoOcorrenciaServices.find( criterios );
			
			if( rsm.getLines().size() <= 0 ) {
				return ResponseFactory.noContent( "Nenhum Registro" );
			}
			
			List< TipoOcorrencia > tipoOcorrencia = new ResultSetMapper< TipoOcorrencia>( rsm, TipoOcorrencia.class).toList();
			return ResponseFactory.ok( tipoOcorrencia );
		} catch( Exception e ) {
			return ResponseFactory.internalServerError( "Erro durante o processamento da requisição", e.getMessage() );
		}
	}
}
