package com.tivic.manager.prc;

import java.sql.Types;
import java.util.ArrayList;

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
import javax.ws.rs.core.Response.Status;

import com.tivic.manager.util.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Path("/prc/processoandamento")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessoAndamentoController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Grava um andamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Andamento salvo com sucesso"),
		@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Andamento a ser gravado", required = true) ProcessoAndamento ProcessoAndamento) {
		return Response
				.status(Status.NOT_IMPLEMENTED)
				.entity(new com.tivic.sol.response.ResponseBody(501, "Not Implemented", "Não é possível salvar um andamento."))
				.build();
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
		value = "Atualiza dados de um andamento",
		notes = "Considere id = cdAndamento"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Andamento atualizado"),
		@ApiResponse(code = 400, message = "Andamento nulo ou inválido"),
		@ApiResponse(code = 204, message = "Andamento não encontrado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(){		
		try {
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", "Funcionou."))
					.build();
		} catch(Exception e) {
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece lista de andamentos"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista fornecida"),
		@ApiResponse(code = 204, message = "Sem resultado"),
		@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieve(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit, 
			@ApiParam(value = "Codigo do andamento") @QueryParam("cdAndamento") int cdAndamento,
			@ApiParam(value = "Codigo do processo") @QueryParam("cdProcesso") int cdProcesso) {
		
		try {
			
			// validar limit
			if(limit < 0 || limit > 100) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Limite máximo de 100 registros."))
						.build();
			}

			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("qtLimite", Integer.toString(limit), ItemComparator.EQUAL, Types.INTEGER));
						
			// código do andamento
			if(cdAndamento != 0) {				
				crt.add(new ItemComparator("A.cd_andamento", String.valueOf(cdAndamento), ItemComparator.EQUAL, Types.VARCHAR));
			}
						
			// código do processo
			if(cdProcesso != 0) {				
				crt.add(new ItemComparator("A.cd_processo", String.valueOf(cdProcesso), ItemComparator.EQUAL, Types.VARCHAR));
			}
			
			ResultSetMap rsm = com.tivic.manager.prc.ProcessoAndamentoServices.getAndamentos(cdProcesso, cdAndamento, null, null);
			
			if(rsm.getLines().size() <= 0) {
				return Response
						.status(Status.NO_CONTENT)
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(Util.rsm2Json(rsm).toString())
					.build();
		} catch(Exception e) {			
			return Response
					.status(Status.INTERNAL_SERVER_ERROR)
					.entity(new com.tivic.sol.response.ResponseBody(500, "Internal Server Error", e.getMessage()))
					.build();
		}	
	}

}
