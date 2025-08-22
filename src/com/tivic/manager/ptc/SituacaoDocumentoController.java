package com.tivic.manager.ptc;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "SituacaoDocumento", tags = { "ptc" })
@Path("/v2/ptc/situacoesdocumento")
@Produces(MediaType.APPLICATION_JSON)
public class SituacaoDocumentoController {

	@POST
	@Path("")
	@ApiOperation(
			value = "Registra situação de documento"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Item registrado", response = SituacaoDocumento.class),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(@ApiParam(value = "Situação a ser registrada", required = true) SituacaoDocumento situacao) {
		try {
			
			Result result = SituacaoDocumentoServices.save(situacao);
			if(result.getCode() <= 0)
				throw new IllegalArgumentException(result.getMessage());
			
			return ResponseFactory.created((SituacaoDocumento)result.getObjects().get("SITUACAODOCUMENTO"));
			
		} catch(IllegalArgumentException e) {
			return ResponseFactory.badRequest(e.getMessage());
		} catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}

	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna a lista de situações")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Situação encontrada", response= SituacaoDocumento[].class),
			@ApiResponse(code = 204, message = "Situação não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public static Response getAll(@ApiParam(value = "Id da situação") @QueryParam("id") String cdSituacaoDocumento,
			@ApiParam(value = "Nome da situação do documento") @QueryParam("nmSituacaoDocumento") String nmSituacaoDocumento) {
		try {
			Criterios crt = new Criterios();
			
			if(cdSituacaoDocumento != null) {
				crt.add("cd_situacao_documento", cdSituacaoDocumento, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmSituacaoDocumento != null) {
				crt.add("nm_situacao_documento", nmSituacaoDocumento, ItemComparator.LIKE, Types.VARCHAR);
			}			
			
			ResultSetMap rsm = SituacaoDocumentoServices.find(crt);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum situação encontrado.");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<SituacaoDocumento>(rsm, SituacaoDocumento.class).toList());
			
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
