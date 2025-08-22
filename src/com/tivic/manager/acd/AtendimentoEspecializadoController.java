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
@Api(value = "AtendimentoEspecializado", tags = {"AtendimentoEspecializado"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/acd/atendimentoespecializado")
@Produces(MediaType.APPLICATION_JSON)
public class AtendimentoEspecializadoController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova AtendimentoEspecializado "
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtendimentoEspecializado registrada", response = AtendimentoEspecializado.class),
			@ApiResponse(code = 400, message = "AtendimentoEspecializado possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "TipoMantenedora a ser registrado", required = true) AtendimentoEspecializado atendimentoEspecializado) {
		try {	
			atendimentoEspecializado.setCdAtendimentoEspecializado(0);
			
			Result r = AtendimentoEspecializadoServices.save(atendimentoEspecializado, null);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("TipoMantenedora possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((AtendimentoEspecializado)r.getObjects().get("ATENDIMENTOESPECIALIZADO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma AtendimentoEspecializado",
			notes = "Considere id = idAtendimentoEspecializado"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtendimentoEspecializado atualizada", response = AtendimentoEspecializado.class),
			@ApiResponse(code = 400, message = "AtendimentoEspecializado � nula ou inv�lida"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da ETAPA a ser atualizado", required = true) @PathParam("id") int cdAtendimentoEspecializado, 
			@ApiParam(value = "ETAPA a ser atualizado", required = true) AtendimentoEspecializado atendimentoEspecializado) {
		try {
			if(atendimentoEspecializado.getCdAtendimentoEspecializado() == 0) 
				return ResponseFactory.badRequest("TipoMantenedora � nulo ou inv�lido");
			
			atendimentoEspecializado.setCdAtendimentoEspecializado(cdAtendimentoEspecializado);
			Result r = AtendimentoEspecializadoServices.save(atendimentoEspecializado);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("AtendimentoEspecializado � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((AtendimentoEspecializado)r.getObjects().get("ATENDIMENTOESPECIALIZADO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna AtendimentoEspecializado"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AtendimentoEspecializado encontrado", response = AtendimentoEspecializado[].class),
			@ApiResponse(code = 204, message = "AtendimentoEspecializado n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value = "Id do  atendimentoEspecializado") @QueryParam("id") String idAtendimentoEspecializado,
			@ApiParam(value = "Nome do atendimentoEspecializado") @QueryParam("atendimentoespecializado") String nmAtendimentoEspecializado
		) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			

			if(idAtendimentoEspecializado != null) {
				criterios.add("id_atendimento_especializado", idAtendimentoEspecializado, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			if(nmAtendimentoEspecializado != null) {
				criterios.add("nm_atendimento_especializado", nmAtendimentoEspecializado, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = AtendimentoEspecializadoServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum mantenedora encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/atendimentoespecializado")
	@ApiOperation(
		value = "Fornece um AtendimentoEspecializado dado o id indicado",
		notes = "Considere id = idEtapa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AtendimentoEspecializado encontrado", response = AtendimentoEspecializadoDTO.class),
		@ApiResponse(code = 204, message = "N�o existe AtendimentoEspecializado com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id da TipoMantenedora") @PathParam("id") int cdAtendimentoEspecializado) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = AtendimentoEspecializadoServices.find(crt.add((Util.isStrBaseAntiga() ? "identificacao_atendimento_especializado" : "cd_atendimento_especializado"), Integer.toString(cdAtendimentoEspecializado), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe AtendimentoEspecializado com o id indicado.");
			
			return ResponseFactory.ok(new AtendimentoEspecializadoDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	


}
