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

import com.tivic.manager.acd.AtendimentoEspecializado;
import com.tivic.manager.acd.AtendimentoEspecializadoDTO;
import com.tivic.manager.acd.AtendimentoEspecializadoServices;
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
@Api(value = "AtividadeComplementar", tags = {"AtividadeComplementar"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/atividadecomplementar")
@Produces(MediaType.APPLICATION_JSON)
public class AtividadeComplementarController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova AtividadeComplementar "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtividadeComplementar registrada", response = AtividadeComplementar.class),
			@ApiResponse(code = 400, message = "AtividadeComplementar possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TipoMantenedora a ser registrado", required = true) AtividadeComplementar atividadeComplementar) {
		try {	
			atividadeComplementar.setCdAtividadeComplementar(0);
			
			Result r = AtividadeComplementarServices.save(atividadeComplementar, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("AtividadeComplementar possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((AtividadeComplementar)r.getObjects().get("ATIVIDADECOMPLEMENTAR"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma AtividadeComplementar",
			notes = "Considere id = idAtividadeComplementar"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtividadeComplementar atualizada", response = AtividadeComplementar.class),
			@ApiResponse(code = 400, message = "AtividadeComplementar � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da AtividadeComplementar a ser atualizado", required = true) @PathParam("id") int cdAtividadeComplementar, 
			@ApiParam(value = "AtividadeComplementar a ser atualizado", required = true) AtividadeComplementar atividadeComplementar) {
		try {
			if(atividadeComplementar.getCdAtividadeComplementar() == 0) 
				return ResponseFactory.badRequest("AtividadeComplementar � nulo ou inv�lido");
			
			atividadeComplementar.setCdAtividadeComplementar(cdAtividadeComplementar);
			Result r = AtividadeComplementarServices.save(atividadeComplementar);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("AtividadeComplementar � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((AtividadeComplementar)r.getObjects().get("ATIVIDADECOMPLEMENTAR"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna AtividadeComplementar"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtividadeComplementar encontrado", response = AtividadeComplementar[].class),
			@ApiResponse(code = 204, message = "AtividadeComplementar n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id do  atendimentoEspecializado") @QueryParam("id") String idAtividadeComplementar,
			@ApiParam(value = "Nome do atendimentoEspecializado") @QueryParam("atividadecomplementar") String nmAtividadeComplementar
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idAtividadeComplementar != null) {
				criterios.add("id_atividade_complementar", idAtividadeComplementar, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmAtividadeComplementar != null) {
				criterios.add("nm_atividade_complementar", nmAtividadeComplementar, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = AtividadeComplementarServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum mantenedora encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/atividadecomplementar")
	@ApiOperation(
		value = "Fornece um AtividadeComplementar dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AtividadeComplementar encontrado", response = AtividadeComplementarDTO.class),
		@ApiResponse(code = 204, message = "N�o existe AtividadeComplementar com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da AtividadeComplementar") @PathParam("id") int cdAtividadeComplementar) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = AtividadeComplementarServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_atividade_complementar" : "cd_atividade_complementar"), Integer.toString(cdAtividadeComplementar), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe AtendimentoEspecializado com o id indicado.");
			
			return ResponseFactory.ok(new AtividadeComplementarDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
