package com.tivic.manager.seg;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.seg.Modulo;
import com.tivic.manager.util.ResultSetMapper;
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

@Api(value = "Módulos", tags = {"seg"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT Token")
		})
})

@Path("/v2/seg/modulos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModuloController {
	
	@GET
	@Path("")
	@ApiOperation( value = "Retorna lista de módulos")	
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Módulos encontrados", response = Modulo.class),
			@ApiResponse( code = 400, message = "Nenhum módulo encontrado", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response getAll() {
		try { 
			ResultSetMap rsm = ModuloServices.getAllOrdered();
			ResultSetMapper<Modulo> rsmModulo = new ResultSetMapper<Modulo>(rsm, Modulo.class);
			
			if( rsm == null || rsm.getLines().size() <= 0 )
				return ResponseFactory.noContent("Nenhum módulo encontrado");
			
			return ResponseFactory.ok(rsmModulo);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation( value = "Registra novo módulo")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Módulo registrado", response = Modulo.class ),
			@ApiResponse( code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response create(Modulo modulo) {
		
		try {
			Result result = ModuloServices.save(modulo);
			
			if (result.getCode() < 0)
				return ResponseFactory.badRequest("Módulo Inválido");
			
			return ResponseFactory.ok(result.getObjects().get("MODULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation( value = "Exclui registro de módulo")
	@ApiResponses( value = {
			@ApiResponse( code = 200, message = "Módulo excluído", response = Modulo.class ),
			@ApiResponse( code = 400, message = "Nenhuma ação encontrada", response = ResponseBody.class),
			@ApiResponse( code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response delete(Modulo modulo, @PathParam("id") int cdModulo) {
		
		try {
			Result result = ModuloServices.remove(cdModulo, modulo.getCdSistema());
			
			if (result.getCode() < 0)
				return ResponseFactory.badRequest("Módulo Inválido");
			
			return ResponseFactory.ok(result.getObjects().get("MODULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/ativos")
	public static Response getAllAtivos() {
		try {
			ResultSetMap rsm = ModuloServices.getAllAtivos();			
			ResultSetMapper<Modulo> rsmModulos = new ResultSetMapper<Modulo>(rsm, Modulo.class);
			
			if( rsm == null || rsm.getLines().size() <= 0 )
				return ResponseFactory.noContent("Nenhum módulo encontrado");
			
			return ResponseFactory.ok(rsmModulos);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/sistema")
	public static Response getAllBySistema(Sistema sistema) {
		try {
			ResultSetMap rsm = ModuloServices.getAllBySistema(sistema.getCdSistema());			
			ResultSetMapper<Modulo> rsmModulos = new ResultSetMapper<Modulo>(rsm, Modulo.class);
			
			if( rsm == null || rsm.getLines().size() <= 0 )
				return ResponseFactory.noContent("Nenhum módulo encontrado");
			
			return ResponseFactory.ok(rsmModulos);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	
}

























