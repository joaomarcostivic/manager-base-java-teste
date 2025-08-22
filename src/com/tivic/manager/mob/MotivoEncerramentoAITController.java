package com.tivic.manager.mob;

import java.sql.Types;

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

import com.tivic.manager.grl.TipoArquivo;
import com.tivic.manager.grl.TipoArquivoServices;
import com.tivic.manager.mob.MotivoEncerramentoAIT;
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
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;


@Api(value="Motivo", tags = {"mob"})
@Path("/v2/mob/motivo/encerramentos")
@Produces(MediaType.APPLICATION_JSON)
public class MotivoEncerramentoAITController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Registra um novo motivo de encerramento AIT"	
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Motivo de encerramento registrado", response = MotivoEncerramentoAIT.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response create(@ApiParam(value = "Motivo de encerramento a ser registrado", required = true) MotivoEncerramentoAIT motivo) {
		try {
			Result result= MotivoEncerramentoAITServices.save(motivo);
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok((MotivoEncerramentoAIT) result.getObjects().get("MOTIVOENCERRAMENTOAIT"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/")
	@ApiOperation(
		value = "Fornece lista de motivos de encerramento AIT"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lista fornecida", response = MotivoEncerramentoAIT[].class),
			@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Sem resultado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response getAll(@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit,
			@ApiParam(value="Nome do motivo") @QueryParam("nmMotivoEncerramento") String nmMotivoEncerramento) {
		try {
			Criterios criterios = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			if(nmMotivoEncerramento != null)
				criterios.add("nm_motivo_encerramento", nmMotivoEncerramento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			ResultSetMap rsm = MotivoEncerramentoAITServices.find(criterios);
			
			if(rsm == null  || rsm.getLines().size()==0)
				return ResponseFactory.noContent("Nenhum motivo encontrado");
			
			return ResponseFactory.ok(new ResultSetMapper<MotivoEncerramentoAIT>(rsm, MotivoEncerramentoAIT.class));
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Motivo de Encerramento "
			)
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Motivo de encerramento encontrado", response = MotivoEncerramentoAIT.class),
			@ApiResponse(code = 204, message = "Motivo de encerramento  não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response get(@ApiParam(value = "Código do arquivo") @PathParam("id") int cdMotivoEncerramento) {
		try {
			MotivoEncerramentoAIT motivoEncerramento = MotivoEncerramentoAITServices.get(cdMotivoEncerramento);
			if(motivoEncerramento == null)
				return ResponseFactory.noContent("Nenhum Motivo de encerramento encontrado");
			
			
			return ResponseFactory.ok(motivoEncerramento);		
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza um motivo de encerramento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Motivo atualizado", response = MotivoEncerramentoAIT.class),
			@ApiResponse(code = 400, message = "Motivo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	@Produces(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Código do Motivo de Encerramento") @PathParam("id") int cdMotivoEncerramento,
			@ApiParam(value = "Arquivo a ser atualizado") MotivoEncerramentoAIT motivoEncerramento) {
		try {			
			motivoEncerramento.setCdMotivoEncerramento(cdMotivoEncerramento);
			
			Result result = MotivoEncerramentoAITServices.save(motivoEncerramento);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((MotivoEncerramentoAIT)result.getObjects().get("MOTIVOENCERRAMENTOAIT"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
		
}
