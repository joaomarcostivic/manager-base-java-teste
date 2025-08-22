package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.TipoAcidente;
import com.tivic.manager.mob.TipoAcidenteServices;
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

@Api(value = "TiposAcidentes", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/tipos-acidente")
public class TipoAcidenteController {
	
//	@POST
//	@Path("")
//	@ApiOperation(
//			value = "Registra um novo tipo de veiculo"
//		)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Tipo de Acidente registrado", response = TipoAcidente.class),
//			@ApiResponse(code = 400, message = "O tipo de Acidente possui algum parâmetro inválido", response = ResponseBody.class),
//			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
//		})
//	public static Response create(@ApiParam(value = "Tipo de Acidente a ser registrado", required = true) TipoAcidente tipoAcidente) {
//		try {
//			tipoAcidente.setCdTipoAcidente(0);
//			Result result = TipoAcidenteServices.save(tipoAcidente);
//			if(result.getCode() < 0) {
//				return ResponseFactory.badRequest("O tipo de Acidente possui algum parâmetro inválido", result.getMessage());
//			}
//			
//			return ResponseFactory.ok((TipoAcidente)result.getObjects().get("TIPOVEICULO"));
//		} catch (Exception e) {
//			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
//		}
//	}
	
//	@PUT
//	@Path("/{id}")
//	@ApiOperation(
//			value = "Atualiza um tipo de veiculo"
//		)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Tipo de Acidente atualizado", response = TipoAcidente.class),
//			@ApiResponse(code = 400, message = "O tipo de Acidente possui algum parâmetro inválido", response = ResponseBody.class),
//			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
//		})
//	public static Response update(@ApiParam(value = "Tipo de Acidente a ser registrado") TipoAcidente tipoAcidente,
//			@ApiParam(value = "Código do Tipo de Acidente") @PathParam("id") int cdTipoAcidente) {
//		try {
//			tipoAcidente.setCdTipoAcidente(cdTipoAcidente);
//			
//			Result result = TipoAcidenteServices.save(tipoAcidente);
//			if(result.getCode() < 0) {
//				return ResponseFactory.badRequest("O tipo de Acidente possui algum parâmetro inválido", result.getMessage());
//			}
//			
//			return ResponseFactory.ok((TipoAcidente)result.getObjects().get("TIPOVEICULO"));
//		} catch (Exception e) {
//			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
//		}
//	}
	
//	@GET
//	@Path("/{id}")
//	@ApiOperation(
//			value = "Retorna um tipo de veiculo"
//		)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Tipo de Acidente encontrado", response = TipoAcidente.class),
//			@ApiResponse(code = 204, message = "Tipo de Acidente não localizado", response = ResponseBody.class),
//			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
//		})
//	public static Response get(@ApiParam(value = "Código do Tipo de Acidente") @PathParam("id") int cdTipoAcidente) {
//		try {
//			TipoAcidente tipoAcidente = TipoAcidenteDAO.get(cdTipoAcidente);
//			if (tipoAcidente == null)
//				return ResponseFactory.noContent("Nenhum tipo de veiculo encontrado");
//
//			return ResponseFactory.ok(tipoAcidente);
//		} catch (Exception e) {
//			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
//		}
//	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna tipos de acidente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tipos de Acidente encontrados", response = TipoAcidente[].class),
			@ApiResponse(code = 204, message = "Nenhum tipo de Acidente localizado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value  = "Nome do Tipo de Acidente") @QueryParam("nmTipoAcidente") String nmTipoAcidente,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(nmTipoAcidente != null) {
				criterios.add(new ItemComparator("nm_tipo_veiculo", nmTipoAcidente, ItemComparator.LIKE_ANY, Types.VARCHAR));
			}
			
			ResultSetMap rsm = TipoAcidenteServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<TipoAcidente> tiposAcidente = new ResultSetMapper<TipoAcidente>(rsm, TipoAcidente.class).toList();
			return ResponseFactory.ok(tiposAcidente);
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
}
