package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.str.Ait;
import com.tivic.manager.str.AitDAO;
import com.tivic.manager.str.AitServices;
import com.tivic.manager.util.Util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import sol.dao.Conexao;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

//@Api(value = "AIT", authorizations = {
//		@Authorization(value = "Bearer Auth", scopes = {
//				@AuthorizationScope(scope = "Bearer", description = "JWT token")
//		})
//})
@Path("/v2/prc/processo")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessoController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Grava um novo processo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Processo salvo com sucesso"),
		@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Processo a ser gravado", required = true) Processo processo) {
		return Response
				.status(Status.NOT_IMPLEMENTED)
				.entity(new com.tivic.sol.response.ResponseBody(501, "Not Implemented", "Não é possível salvar um processo."))
				.build();
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
		value = "Fornece um Processo dado o id indicado",
		notes = "Considere id = cdProcesso"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "AIT encontrado", response = Object.class),
		@ApiResponse(code = 204, message = "Não existe Processo com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveById(@ApiParam(value = "id do Processo") @PathParam("id") int cdProcesso) {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap rsm = ProcessoServices.find(crt.add(("cd_processo"), Integer.toString(cdProcesso), ItemComparator.EQUAL, Types.INTEGER));
			
			if(!rsm.next())
				return ResponseFactory.noContent("Não existe Processo com o id indicado.");
			
			return ResponseFactory.ok(rsm.getRegister());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}	
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
		value = "Atualiza dados de um Processo",
		notes = "Considere id = cdProcesso"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Processo atualizado"),
		@ApiResponse(code = 400, message = "Processo nulo ou inválido"),
		@ApiResponse(code = 204, message = "Processo não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do processo a ser atualizado", required = true) @PathParam("id") int cdProcesso, 
			@ApiParam(value = "Processo a ser atualizado", required = true) Processo processo) {
		
		try {
			if(processo == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Processo não existente na base de dados."))
						.build();
			}
			
			if(ProcessoDAO.get(cdProcesso, null) == null) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(404, "Not Found", "Processo não existe"))
						.build();
			}
			
			processo.setCdProcesso(cdProcesso);
			
			int r = ProcessoDAO.update(processo);
			if(r < 0) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Erro ao atualizar Processo."))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", "Processo atualizado."))
					.build();
		} catch(Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@GET
	@Path("/{cdProcesso}/andamentos")
	@ApiOperation(value = "Andamentos de Processo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Não há andamentos para os parâmetros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response getAndamentos(@PathParam("cdProcesso") int cdProcesso, 
			@QueryParam("cdUsuario") int cdUsuario, @DefaultValue("30") @QueryParam("limit") int limit) {
		 try {
			 ResultSetMap rsm = ProcessoServices.getProcessosMovimentados(cdUsuario, limit);
			    if(rsm==null || rsm.getLimit()<=0)
			        return ResponseFactory.noContent("Nenhum registro");
			    return ResponseFactory.ok(rsm);
		 }
		 catch(Exception e) {
			 return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getLocalizedMessage());
		 }
	}
	
	@GET
	@Path("/estatisticas")
	@ApiOperation(value = "Estatísticas de Processo")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Não há estatística para os parâmetros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response getEstatistica(
			@ApiParam(value = "Início do período (dd/mm/yyyy)", required = true) @QueryParam("inicio") String inicio,
			@ApiParam(value = "Fim do período (dd/mm/yyyy)", required = true) @QueryParam("fim") String fim,
			@ApiParam(value = "Relação", allowableValues = "cadastro, ativo, repasse, encerramento, baseAtiva", required = true)
				@QueryParam("relacao") String relacao,
			@ApiParam(value = "Agrupamento", allowableValues = "grupo, fase, cidade, estado, mensal, agrupado, tipo") 
				@QueryParam("agrupamento") String agrupamento,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int limite) {
		try {
			
			GregorianCalendar dtInicial = Util.convStringToCalendar(inicio + " 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(fim + " 23:59:59");
			int mes = 0;
			int ano = 0;
			
			switch (relacao) {
			case "cadastro":
				return ResponseFactory.ok(ProcessoServices.getCadastrosMensais(mes, ano));
			case "ativo":
				if(agrupamento.equals("grupo"))
					return ResponseFactory.ok(ProcessoServices.getAtivosPorGrupo());
				else if(agrupamento.equals("fase"))
					return ResponseFactory.ok(ProcessoServices.getAtivosPorFase());
				else if(agrupamento.equals("cidade"))
					return  ResponseFactory.ok(ProcessoServices.getAtivosPorCidade(limite));
				else if(agrupamento.equals("estado"))
					return ResponseFactory.ok(ProcessoServices.getAtivosPorEstado());
				else
					return ResponseFactory.noContent("Não há estatística para os parâmetros fornecidos.");
			case "repasse":
				return ResponseFactory.ok(ProcessoServices.getRepasseMensal());
			case "encerramento":
				if(agrupamento.equals("mensal"))
					return ResponseFactory.ok(ProcessoServices.getEncerramentoMensal());
				else if(agrupamento.equals("agrupado"))
					return ResponseFactory.ok(ProcessoServices.getEncerramentoAgrupado());
				else if(agrupamento.equals("tipo"))
					return ResponseFactory.ok(ProcessoServices.getEncerramentosTipo());
				else
					return ResponseFactory.noContent("Não há estatística para os parâmetros fornecidos.");
			case "baseAtiva":
				return ResponseFactory.ok(ProcessoServices.getBaseAtiva());
			default:
				return ResponseFactory.noContent("Não há estatística para os parâmetros fornecidos.");
			}
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getLocalizedMessage());
		}
	}
}