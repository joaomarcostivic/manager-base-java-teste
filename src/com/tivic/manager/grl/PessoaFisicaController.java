package com.tivic.manager.grl;

import java.sql.Types;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.ptc.SituacaoDocumento;
import com.tivic.manager.ptc.SituacaoDocumentoServices;
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

@Api(value = "Pessoa Fisica", tags = { "grl" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v2/grl/pessoafisica")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class PessoaFisicaController {
	@GET
	@Path("")
	@ApiOperation(value = "Retorna a lista de pessoas fisicas")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa encontrada", response= PessoaFisica[].class),
			@ApiResponse(code = 204, message = "Pessoa não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	
	public static Response getAll(@ApiParam(value = "Id da pessoa") @QueryParam("id") int cdPessoa,
			@ApiParam(value = "Nome da pessoa") @QueryParam("nmPessoa") String nmPessoa,
	        @ApiParam(value = "Quantidade de registros") @QueryParam("limit") @DefaultValue("50") int nrLimite) {
		try {
			Criterios crt = new Criterios();
			
			if (nrLimite != 0) {
				crt.add("LIMIT", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(cdPessoa != 0) {
				crt.add("A.cd_pessoa", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nmPessoa != null) {
				crt.add("B.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap rsm = PessoaFisicaServices.findPessoaFisica(crt);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum pessoa encontrada.");
			}
			
			return ResponseFactory.ok(new ResultSetMapper<PessoaFisica>(rsm, PessoaFisica.class).toList());
			
		}catch(Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
