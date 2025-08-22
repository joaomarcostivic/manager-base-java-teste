package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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

@Api(value = "Agentes", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/agentes")
@Produces(MediaType.APPLICATION_JSON)
public class AgenteController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo Agente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Agente registrado", response = Agente.class),
			@ApiResponse(code = 400, message = "Agente inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Agente a ser registrado") Agente agente) {
		try {
			agente.setCdAgente(0);
			
			ResultSetMap rsm = AgenteServices.getByMatricula(agente.getNrMatricula());
			
			if(rsm.next())
				return ResponseFactory.badRequest("Já existe agente cadastrado com essa matrícula");
			
			Result result = AgenteServices.save(agente);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((Agente) result.getObjects().get("AGENTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um Agente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Agente atualizado", response = Agente.class),
			@ApiResponse(code = 400, message = "Agente inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(@ApiParam(value = "Código do agente") @PathParam("id") int cdAgente,
			@ApiParam(value = "Agente a ser registrado") Agente agente) {
		try {
			agente.setCdAgente(cdAgente);
			
			ResultSetMap rsm = AgenteServices.getByMatricula(agente.getNrMatricula());
			
			if(rsm.next() && rsm.getInt("CD_AGENTE") != agente.getCdAgente())				
				return ResponseFactory.badRequest("Já existe agente cadastrado com essa matrícula");
			
			Result result = AgenteServices.save(agente);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((Agente) result.getObjects().get("AGENTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Agente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Agente encontrado", response = Agente.class),
			@ApiResponse(code = 204, message = "Agente não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Agente") @PathParam("id") int cdAgente) {
		try {
			Agente _agente = AgenteDAO.get(cdAgente);
			if(_agente == null) {
				return ResponseFactory.noContent("Nenhum agente encontrado");
			}
			
			return ResponseFactory.ok(_agente);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Agentes"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Agente encontrado", response = Agente[].class),
			@ApiResponse(code = 204, message = "Agente não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Nome do Agente") @QueryParam("agente") String nmAgente,
			@ApiParam(value = "Matricula do Agente") @QueryParam("matricula") int nrMatricula,
			@ApiParam(value = "Status do Agente") @QueryParam("stAgente") String stAgente,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "200") @DefaultValue("200") 
			@QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			
			if(nmAgente != null) {
				criterios.add("A.nm_agente", nmAgente, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nrMatricula != 0) {
				criterios.add("A.nr_matricula", String.valueOf(nrMatricula), ItemComparator.LIKE, Types.VARCHAR);
			}	
			
			if(stAgente != null) {
				criterios.add("A.st_agente", stAgente, ItemComparator.EQUAL, Types.INTEGER);				
			}

			if(keyword != null) {
				criterios.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			ResultSetMap _rsm = AgenteServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum agente encontrado");
			}
			
			List<Agente> agentes = new ResultSetMapper<Agente>(_rsm, Agente.class).toList();
			
			return ResponseFactory.ok(agentes);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	/** ***********************************************************************
	 * TALONARIOS
	 */
	
	@GET
	@Path("/{id}/talonarios")
	@ApiOperation(
			value = "Retorna os talões de um Agente"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Talões encontrados", response = Talonario[].class),
			@ApiResponse(code = 204, message = "Talões não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getTalonarios(@ApiParam(value = "Código do Agente") @PathParam("id") int cdAgente,
			@ApiParam(value = "Apenas talões de monitoramento", defaultValue = "false", allowableValues = "true, false") @DefaultValue("false") @QueryParam("monitoramento") boolean lgMonitoramento) {
		try {
			List<Talonario> talonarios = new ArrayList<Talonario>();
			if(lgMonitoramento) {
				Talonario talonario = TalonarioServices.getTalaoMonitoramento(cdAgente, null);
				if(talonario == null)
					return ResponseFactory.noContent("Nenhum talão encontrado");
				talonarios.add(talonario);
			} else {
				Result result = TalonarioServices.getTalonariosByAgente(cdAgente);
				if(result.getCode() <= 0)
					return ResponseFactory.badRequest(result.getMessage());
				
				talonarios = (ArrayList<Talonario>) result.getObjects().get("TALOES");			
			}
			return ResponseFactory.ok(talonarios);	
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
