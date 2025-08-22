package com.tivic.manager.grl;

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
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Arquivo", tags = {"grl"})
@Path("/v2/grl/arquivos/tipos")
@Produces(MediaType.APPLICATION_JSON)
public class TipoArquivoController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Registra um novo tipo de arquivo"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipo registrado", response = TipoArquivo.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response addTipoArquivo(@ApiParam(value = "Tipo a ser registrado", required = true) TipoArquivo tipo) {
		try {
			
			Result result = TipoArquivoServices.save(tipo);
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok((TipoArquivo) result.getObjects().get("TIPOARQUIVO"));
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
//	@PUT
//	@Path("/{id}")
//	@ApiOperation(
//		value = "Edita tipo de arquivo"
//	)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Tipo registrado"),
//			@ApiResponse(code = 500, message = "Erro no servidor")
//	})
//	public static Response editTipoArquivo(@ApiParam(value = "Id do Tipo") @PathParam("id") int cdTipo, @ApiParam(value = "Tipo a ser registrado", required = true) TipoArquivo tipo) {
//		try {
//			
//			tipo.setCdTipoArquivo(cdTipo);
//			
//			Result result = TipoArquivoServices.save(tipo);
//			if(result.getCode() <= 0) {
//				return Response
//						.status(Status.BAD_REQUEST)
//						.entity(new com.tivic.manager.rest.Response(400, "Bad Request", result.getMessage()))
//						.build();
//			}
//			
//			return Response
//					.status(Status.OK)
//					.entity(tipo)
//					.build();
//		} catch(Exception e) {
//			return Response
//					.status(Status.INTERNAL_SERVER_ERROR)
//					.entity(new com.tivic.manager.rest.Response(500, "Internal Server Error", e.getMessage()))
//					.build();
//		}
//	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece lista de tipos de arquivo"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista fornecida", response = TipoArquivo[].class),
			@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
	@ApiParam(value = "Nome do arquivo") @QueryParam("nmTipoArquivo") String nmTipoArquivo) {
		try {
			Criterios criterios = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			if(nmTipoArquivo != null)
				criterios.add("nm_tipo_arquivo", nmTipoArquivo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			ResultSetMap rsm = TipoArquivoServices.find(criterios);
			
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma arquivo encontrado");
			
			return ResponseFactory.ok(new ResultSetMapper<TipoArquivo>(rsm, TipoArquivo.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}


@GET
@Path("/{id}")
@ApiOperation(
		value = "Retorna um arquivo"
		)
@ApiResponses( value = {
		@ApiResponse(code = 200, message = "Arquivo encontrado", response = TipoArquivo.class),
		@ApiResponse(code = 204, message = "Arquivo não encontrado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
})

@Produces(MediaType.APPLICATION_JSON)

public static Response get(@ApiParam(value = "Código do arquivo")@PathParam("id") int cdTipoArquivo) {
	try {
		TipoArquivo tipoArquivo = TipoArquivoServices.get(cdTipoArquivo);
		
		if(tipoArquivo == null)
			return ResponseFactory.noContent("Nenhum arquivo encontrado!");
		
		return ResponseFactory.ok(tipoArquivo);
	} catch(Exception e) {
		return ResponseFactory.internalServerError(e.getMessage());
	}
  }


@PUT
@Path("/{id}")
@ApiOperation(
		value = "Atualiza um arquivo"
	)
@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivo atualizado", response = TipoArquivo.class),
		@ApiResponse(code = 400, message = "Arquivo inválido", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
})
@Produces(MediaType.APPLICATION_JSON)
public static Response update(
		@ApiParam(value = "Código do Tipo de Arquivo") @PathParam("id") int cdTipoArquivo,
		@ApiParam(value = "Arquivo a ser atualizado") TipoArquivo tipoArquivo) {
	try {			
		tipoArquivo.setCdTipoArquivo(cdTipoArquivo);
		
		Result result = TipoArquivoServices.save(tipoArquivo);
		if(result.getCode() < 0)
			return ResponseFactory.badRequest(result.getMessage());
			
		return ResponseFactory.ok((TipoArquivo)result.getObjects().get("TIPOARQUIVO"));
	} catch (Exception e) {
		return ResponseFactory.internalServerError(e.getMessage());
	}
}

}

