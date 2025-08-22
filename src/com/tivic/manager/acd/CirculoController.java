package com.tivic.manager.acd;
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

import com.tivic.manager.acd.Circulo;
import com.tivic.manager.acd.CirculoDTO;
import com.tivic.manager.acd.CirculoServices;
import com.tivic.manager.grl.PessoaJuridicaDTO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

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

@Api(value = "Circulo", tags = {"Circulo"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/circulos")
@Produces(MediaType.APPLICATION_JSON)
public class CirculoController {
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Circulos"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Circulo encontrado", response = Circulo[].class),
			@ApiResponse(code = 204, message = "Circulo n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Nome do circulo") @QueryParam("circulo") String nmCirculo
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			
			if(nmCirculo != null) {
				criterios.add("nm_circulo", nmCirculo, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = CirculoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum circulo encontrado");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/instituicaocirculo")
	@ApiOperation(
		value = "Fornece instituicoes referente a um circulo indicado",
		notes = "Considere id = cdCirculo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Circulo encontrado", response = Circulo[].class),
		@ApiResponse(code = 204, message = "N�o existe Circulo com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id do circulo") @PathParam("id") int cdCirculo) {
		try {	
		
			ResultSetMap rsm = InstituicaoCirculoServices.getAllByCirculo(cdCirculo);
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe InstituicaoCirculo com o id indicado.");
			
			return ResponseFactory.ok(rsm);
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	

	@GET
	@Path("/{id}/circulo")
	@ApiOperation(
		value = "Fornece um Circulo dado o id indicado",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Circulo encontrado", response = CirculoDTO.class),
		@ApiResponse(code = 204, message = "N�o existe Circulo com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveByCdCirculo(@ApiParam(value = "id de Circulo") @PathParam("id") int cdCirculo) {
		try {	
			
			Criterios crt = new Criterios();
			ResultSetMap rsm = CirculoServices.find(crt.add((Util.isStrBaseAntiga() ? "cd_circulo" : "A.cd_circulo"), Integer.toString(cdCirculo), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe LogradouroBairro com o id indicado.");
			
			return ResponseFactory.ok(new CirculoDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}

}
