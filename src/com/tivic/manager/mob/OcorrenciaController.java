package com.tivic.manager.mob;

import java.sql.Types;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Cidade;
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

@Api(value = "Ocorrencias", tags = {"mob"}, authorizations = {
			@Authorization(value = "Bearer Auth", scopes = {
			@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/ocorrencias")
@Produces(MediaType.APPLICATION_JSON)
public class OcorrenciaController {
	
	
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Ocorrencia"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ocorrencia encontrado", response = Orgao.class),
			@ApiResponse(code = 204, message = "Ocorrencia não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Ocorrencia") @PathParam("id") int cdOcorrencia) {
		try {

			Ocorrencia ocorrencia = OcorrenciaDAO.get(cdOcorrencia);
			if(ocorrencia == null) {
				return ResponseFactory.noContent("Nenhuma ocorrencia encontrada");
			}
			
			return ResponseFactory.ok(ocorrencia);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Ocorrencias"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ocorrencias encontrado", response = Orgao[].class),
			@ApiResponse(code = 204, message = "Ocorrencias não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Descricao da Ocorrencia") @QueryParam("descricao") String dsOcorrencia) {
		try {
			
			Criterios criterios = new Criterios();
			//crt.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);
			
			if(dsOcorrencia != null) 
				criterios.add("ds_ocorrencia", dsOcorrencia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			ResultSetMap rsm = OcorrenciaServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Ocorrencia encontrado");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<Ocorrencia>(rsm, Ocorrencia.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/ordered")
	@ApiOperation(
			value = "Retorna Ocorrencias"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ocorrencias encontrado", response = Orgao[].class),
			@ApiResponse(code = 204, message = "Ocorrencias não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAllOrdered(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Descricao da Ocorrencia") @QueryParam("descricao") String dsOcorrencia) {
		try {
			
			Criterios criterios = new Criterios();
			//crt.add("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER);
			
			if(dsOcorrencia != null) 
				criterios.add("ds_ocorrencia", dsOcorrencia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			
			ResultSetMap rsm = OcorrenciaServices.findOrdered(criterios);
			if(rsm==null || rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum Ocorrencia encontrado");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<Ocorrencia>(rsm, Ocorrencia.class));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

}
