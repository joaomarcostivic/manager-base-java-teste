package com.tivic.manager.grl;

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

import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDTO;
import com.tivic.manager.grl.PessoaJuridicaServices;
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
@Api(value = "PessoaJuridica", tags = {"PessoaJuridica"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/grl/pessoajuridica")
@Produces(MediaType.APPLICATION_JSON)
public class PessoaJuridicaController {
	

	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova PessoaJuridica"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "PessoaJuridica registrada", response = PessoaJuridica.class),
			@ApiResponse(code = 400, message = "PessoaJuridica possui algum par�metro inv�lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "PESSOAJURIDICA a ser registrado", required = true) PessoaJuridica pessoaJuridica) {
		try {	
			pessoaJuridica.setCdPessoa(0);
			
			Result r = PessoaJuridicaServices.save(pessoaJuridica);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("PESSOAJURIDICA possui algum par�metro inv�lido", r.getMessage());
			}
			
			return ResponseFactory.ok((PessoaJuridica)r.getObjects().get("PESSOAJURIDICA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de uma PESSOAJURIDICA",
			notes = "Considere id = cdPessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "PessoaJuridica atualizado", response = PessoaJuridica.class),
			@ApiResponse(code = 400, message = "PessoaJuridica � nulo ou inv�lido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da PESSOAJURIDICA a ser atualizado", required = true) @PathParam("id") int cdPessoa, 
			@ApiParam(value = "PESSOAJURIDICA a ser atualizado", required = true) PessoaJuridica pessoaJuridica) {
		try {
			if(pessoaJuridica.getCdPessoa() == 0) 
				return ResponseFactory.badRequest("PESSOAJURIDICA � nulo ou inv�lido");
			
			pessoaJuridica.setCdPessoa(cdPessoa);
			Result r = PessoaJuridicaServices.save(pessoaJuridica);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("PESSOAJURIDICA � nulo ou inv�lido", r.getMessage());
			
			return ResponseFactory.ok((PessoaJuridica)r.getObjects().get("PESSOAJURIDICA"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna cartorios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "CARTORIO encontrado", response = Pessoa[].class),
			@ApiResponse(code = 204, message = "CARTORIO n�o encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(@ApiParam(value = "Id do logradouro") @QueryParam("id") String idServentia,
			@ApiParam(value = "Nome do cartorio") @QueryParam("cartorio") String nmPessoa,
			@ApiParam(value = "Nome da cidade") @QueryParam("cidade") String nmCidade,
			@ApiParam(value = "Quantidade m�xima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("qtLimite") int limit) {
		try {
			
			Criterios criterios = new Criterios("qtLimite", Integer.toString(limit), 0, 0);
			if(idServentia != null) {
				criterios.add("id_serventia", idServentia, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			if(nmPessoa != null) {
				criterios.add("A.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(nmCidade != null) {
				criterios.add("nm_cidade", nmCidade, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}	
			
			ResultSetMap rsm = PessoaServices.findAllCartorios(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum cartorio encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/cartorio")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "CARTORIO encontrado", response = PessoaJuridicaDTO.class),
		@ApiResponse(code = 204, message = "N�o existe CARTORIO com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response retrieveByCdCartorio(@ApiParam(value = "id de CARTORIO") @PathParam("id") int cdPessoa) {
		try {	
			
			Criterios crt = new Criterios();
			ResultSetMap rsm = PessoaServices.findAllCartorios(crt.add((Util.isStrBaseAntiga() ? "A.cd_pessoa" : "cd_pessoa"), Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe LogradouroBairro com o id indicado.");
			
			return ResponseFactory.ok(new PessoaJuridicaDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	
	
	@GET
	@Path("/{id}/cartoriocidade")
	@ApiOperation(
		value = "Fornece um LOGRADOURO dado o id indicado",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "CARTORIO encontrado", response = LogradouroDTO.class),
		@ApiResponse(code = 204, message = "N�o existe CARTORIO com o id indicado", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response cartorioEstado(@ApiParam(value = "id de CARTORIO") @PathParam("id") int cdPessoa) {
		try {	
			
			Criterios crt = new Criterios();
			ResultSetMap rsm = PessoaServices.findAllCartorios(crt.add((Util.isStrBaseAntiga() ? "A.cd_pessoa"  : "cd_pessoa"), Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.VARCHAR));
			
			if(!rsm.next())
				return ResponseFactory.noContent("N�o existe LogradouroBairro com o id indicado.");
			
			return ResponseFactory.ok(new LogradouroDTO.Builder(rsm.getRegister()).build());
		} catch(Exception e) {			
			return ResponseFactory.internalServerError("Erro durante o processamento da requisi��o.", e.getMessage());
		}	
	}
	
	
	
	
	


}
