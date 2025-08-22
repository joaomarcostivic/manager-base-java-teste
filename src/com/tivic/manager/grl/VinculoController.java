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
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Vínculo", tags = {"grl"})
@Path("/v2/grl/vinculo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON) 
public class VinculoController {
		
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo tipo de vínculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo de Vínculo registrado", response = Vinculo.class),
			@ApiResponse(code = 400, message = "O tipo de Vínculo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response create( @ApiParam( value = "Vínculo a ser registrado", required = true ) Vinculo vinculo ) {
		try {
			vinculo.setCdVinculo(0);
			Result result = VinculoServices.save( vinculo );
			if( result.getCode() < 0 ) {
				return ResponseFactory.badRequest("O Vínculo possui algum parâmetro inválido", result.getMessage());
			}
			return ResponseFactory.ok( ( Vinculo )result.getObjects().get("VINCULO") );
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um vínculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vínculo atualizado", response = Vinculo.class),
			@ApiResponse(code = 400, message = "O vínculo possui algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(@ApiParam(value = "Vínculo a ser registrado") Vinculo vinculo,
			@ApiParam(value = "Código do Tipo de Veículo") @PathParam("id") int cdVinculo) {
		try {
			vinculo.setCdVinculo(cdVinculo);
			
			Result result = VinculoServices.save(vinculo);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest("O Vínculo possui algum parâmetro inválido", result.getMessage());
			}
			
			return ResponseFactory.ok((Vinculo)result.getObjects().get("VINCULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um vínculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vínculo encontrado", response = Vinculo.class),
			@ApiResponse(code = 204, message = "Vínculo não localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código do vínculo") @PathParam("id") int cdVinculo) {
		try {
			Vinculo vinculo = VinculoDAO.get(cdVinculo);
			if ( vinculo == null )
				return ResponseFactory.noContent("Nenhum vínculo encontrado");
			return ResponseFactory.ok( vinculo );
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna vínculos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vínculos encontrados", response = Vinculo[].class),
			@ApiResponse(code = 204, message = "Nenhum Vínculo localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value  = "Nome do Vínculo") @QueryParam("nmVinculo") String nmVinculo,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			ArrayList< ItemComparator > criterios = new ArrayList< ItemComparator >();
			if(nmVinculo != null) {
				criterios.add(new ItemComparator( "nm_vinculo", nmVinculo, ItemComparator.LIKE_ANY, Types.VARCHAR ));
			}
			ResultSetMap rsm = VinculoServices.find(criterios);
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List< Vinculo > vinculo = new ResultSetMapper< Vinculo >( rsm, Vinculo.class ).toList();
			return ResponseFactory.ok( vinculo );
		} catch ( Exception e ) {
			return ResponseFactory.internalServerError( "Erro durante o processamento da requisição.", e.getMessage() );
		}
	}
}
