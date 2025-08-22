package com.tivic.manager.mob;

import java.sql.Types;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import sol.dao.ItemComparator;
import sol.util.Result;

@Api(value = "ConcessionarioPessoa", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/concessionariospessoas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConcessionarioPessoaController {
	
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna um concessionarioPessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessas encontradas", response = ConcessionarioPessoaDTO.class),
			@ApiResponse(code = 204, message = "Pessoa não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Codigo da Pessoa") @QueryParam("cdPessoa") int cdConcessionarioPessoa) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessionario_pessoa", Integer.toString(cdConcessionarioPessoa), ItemComparator.EQUAL, Types.INTEGER);
			
			List<ConcessionarioPessoaDTO> list = ConcessionarioPessoaServices.findDTO(crt);
			
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list.get(0));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um concessionarioPessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessas encontradas", response = ConcessionarioPessoaDTO.class),
			@ApiResponse(code = 204, message = "Pessoa não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Codigo da Pessoa") @PathParam("id") int cdConcessionarioPessoa) {
		try {
			Criterios crt = new Criterios();
			crt.add("A.cd_concessionario_pessoa", Integer.toString(cdConcessionarioPessoa), ItemComparator.EQUAL, Types.INTEGER);
			
			List<ConcessionarioPessoaDTO> list = ConcessionarioPessoaServices.findDTO(crt);
			
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list.get(0));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma pessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa atualizada", response = Pessoa.class),
			@ApiResponse(code = 400, message = "Pessoa invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da pessoa.") @PathParam("id") int cdPessoa,
			@ApiParam(value = "Pessoa a ser atualizada.") ConcessionarioPessoaDTO concessionarioPessoaDTO) {
		try {
			concessionarioPessoaDTO.getPessoa().setCdPessoa(cdPessoa);
			
			Result result = ConcessionarioPessoaServices.saveDTO(concessionarioPessoaDTO.getConcessionarioPessoa(),
																concessionarioPessoaDTO.getPessoa(), 
																concessionarioPessoaDTO.getPessoaFisica(),
																concessionarioPessoaDTO.getPessoaEndereco(),
																concessionarioPessoaDTO.getCdEmpresa(),
																concessionarioPessoaDTO.getCdVinculo());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((ConcessionarioPessoa)result.getObjects().get("CONCESSIONARIOPESSOA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova pessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa registrada", response = ConcessionarioPessoa.class),
			@ApiResponse(code = 400, message = "Pessoa inválida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Pessoa a ser registrada") ConcessionarioPessoaDTO concessionarioPessoaDTO) {
		try {
			if(concessionarioPessoaDTO.getPessoa().getCdPessoa() <0) {
		      concessionarioPessoaDTO.getPessoa().setCdPessoa(0);
			}
			if(concessionarioPessoaDTO.getConcessionarioPessoa().getCdConcessionarioPessoa() <0) {
				concessionarioPessoaDTO.getConcessionarioPessoa().setCdConcessionarioPessoa(0);
			}
			concessionarioPessoaDTO.getConcessionarioPessoa().setDtAtivacao(new GregorianCalendar());
			
			Result result = ConcessionarioPessoaServices.saveDTO(concessionarioPessoaDTO.getConcessionarioPessoa(),
					concessionarioPessoaDTO.getPessoa(), 
					concessionarioPessoaDTO.getPessoaFisica(),
					concessionarioPessoaDTO.getPessoaEndereco(),
					concessionarioPessoaDTO.getCdEmpresa(),
					concessionarioPessoaDTO.getCdVinculo());
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((ConcessionarioPessoa)result.getObjects().get("CONCESSIONARIOPESSOA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
}
