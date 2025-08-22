package com.tivic.manager.fta;

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

import com.tivic.manager.mob.Equipamento;
import com.tivic.manager.rest.request.filter.Criterios;
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

@Api(value = "Marca/Modelo", tags = {"fta"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/fta/marcas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarcaModeloController {
	
	@POST
	@Path("/sync")
	@ApiOperation(
			value = "Atualiza registros de Marca/Modelo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros atualizados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response sync() {
		try {
			MarcaModeloSync syncService = MarcaModeloSyncFactory.gerarServico();
			if(syncService == null)
				return ResponseFactory.badRequest("Nenhum serviço de importação encontrado para este órgão.");
			
			Result result = syncService.sync();
			
			if(result.getCode() <= 0)
				throw new Exception(result.getMessage());
			
			return ResponseFactory.ok(result.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/sync/str")
	@ApiOperation(
			value = "Atualiza registros de Marca/Modelo em tabelas STR e FTA"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Registros atualizados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response syncFtaStr() {
		try {
			MarcaModeloSync syncService = MarcaModeloSyncFactory.gerarServico(true);
			if(syncService == null)
				return ResponseFactory.badRequest("Nenhum serviço de importação encontrado para este órgão.");
			
			Result result = syncService.sync();
			
			if(result.getCode() <= 0)
				throw new Exception(result.getMessage());
			
			return ResponseFactory.ok(result.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna lista de Marca/Modelo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = MarcaModelo[].class),
			@ApiResponse(code = 204, message = "Nenhuma marca encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Offset") @QueryParam("offset") int offset,
			@ApiParam(value = "Marca") @QueryParam("marca") String nmMarca,
			@ApiParam(value = "Modelo") @QueryParam("modelo") String nmModelo,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword) {
		try {
			
			Criterios crt = new Criterios("limit", Integer.toString(limit), 0, 0);
			
			if(offset > 0) {
				crt.add("offset", Integer.toString(offset), 0, 0);
			}
			
			if(nmMarca!=null && !nmMarca.equals("")) {
				crt.add("A.nm_marca", nmMarca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nmModelo!=null && !nmModelo.equals("")) {
				crt.add("A.nm_modelo", nmModelo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}

			if(keyword != null) {
				crt.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			ResultSetMap rsm = MarcaModeloServices.find(crt);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro encontrado");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<MarcaModelo>(rsm, MarcaModelo.class));			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna Marca/Modelo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Itens encontrados", response = MarcaModelo.class),
			@ApiResponse(code = 404, message = "Nenhuma marca encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código da marca") @PathParam("id") int cdMarca) {
		try {
			MarcaModelo marca = MarcaModeloDAO.get(cdMarca);
			
			if(marca == null)
				return ResponseFactory.notFound("Nenhum registro encontrado");
			
			return ResponseFactory.ok(marca);			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza Marca/Modelo"
		)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Itens atualizados", response = MarcaModelo.class),
		@ApiResponse(code = 404, message = "Marca ou Modelo invalídos", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Código da Marca") @PathParam("id") int cdMarca,
			@ApiParam(value = "Marca a ser atualizada") MarcaModelo marca) {
		try {
			marca.setCdMarca(cdMarca);
			
			Result result = MarcaModeloServices.save(marca);
			if(result.getCode() < 0) 
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((MarcaModelo)result.getObjects().get("MARCAMODELO"));
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
			
	
 
}
