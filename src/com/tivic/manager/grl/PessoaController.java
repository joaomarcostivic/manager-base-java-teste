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
import javax.ws.rs.core.Response.Status;

import com.tivic.manager.mob.ConcessaoColetivoRuralDTO;
import com.tivic.manager.mob.ConcessaoColetivoUrbanoDTO;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Path("/v2/grl/pessoas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PessoaController {
	
	@POST
	@Path("/")
	@ApiOperation(
		value = "Grava uma nova pessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Pessoa salva com sucesso"),
		@ApiResponse(code = 501, message = "Recurso ainda não disponível")
	})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Pessoa a ser gravada", required = true) Pessoa pessoa) {
		return Response
				.status(Status.NOT_IMPLEMENTED)
				.entity(new com.tivic.sol.response.ResponseBody(501, "Not Implemented", "Não é possível salvar uma pessoa."))
				.build();
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
		value = "Atualiza dados de uma pessoa",
		notes = "Considere id = cdPessoa"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Pessoa atualizada"),
		@ApiResponse(code = 400, message = "Pessoa nula ou inválida"),
		@ApiResponse(code = 204, message = "Pessoa não encontrada"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id da pessoa a ser atualizada", required = true) @PathParam("id") int cdPessoa, 
			@ApiParam(value = "Pessoa a ser atualizada", required = true) Pessoa pessoa) {
		
		try {
			if(pessoa == null) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Pessoa não existente na base de dados."))
						.build();
			}
			
			if(PessoaServices.get(cdPessoa, null) == null) {
				return Response
						.status(Status.NOT_FOUND)
						.entity(new com.tivic.sol.response.ResponseBody(404, "Not Found", "Pessoa não existente na base de dados."))
						.build();
			}
			
			pessoa.setCdPessoa(cdPessoa);
			
			int r = PessoaDAO.update(pessoa);
			if(r < 0) {
				return Response
						.status(Status.BAD_REQUEST)
						.entity(new com.tivic.sol.response.ResponseBody(400, "Bad Request", "Erro ao atualizar pessoa."))
						.build();
			}
			
			return Response
					.status(Status.OK)
					.entity(new com.tivic.sol.response.ResponseBody(200, "Ok", "Pessoa atualizada."))
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
		value = "Fornece lista de pessoas"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lista fornecida"),
		@ApiResponse(code = 204, message = "Sem resultado"),
		@ApiResponse(code = 400, message = "Parâmetro nulo ou inválido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response retrieve(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "100") @DefaultValue("100") @QueryParam("limit") int limit, 
			@ApiParam(value = "Nome da pessoa", required = true) @QueryParam("nome") String nmPessoa, 
			@ApiParam(value = "Codigo da pessoa") @QueryParam("cdPessoa") int cdPessoa) {
		
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
			
			// validar nome
			if(nmPessoa != null) {				
				crt.add(new ItemComparator("A.nm_pessoa", nmPessoa, ItemComparator.EQUAL, Types.VARCHAR));
			} 
						
			// código da pessoa
			if(cdPessoa != 0) {				
				crt.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdPessoa), ItemComparator.EQUAL, Types.VARCHAR));
			}

			ResultSetMap rsm = com.tivic.manager.grl.PessoaServices.find(crt);
			
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
	
	@GET
	@Path("/find")
	@ApiOperation(
			value = "Retorna Pessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa encontrado", response = Pessoa[].class),
			@ApiResponse(code = 204, message = "Pessoa não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisi��o.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("qtLimite") int qtLimite,
			@ApiParam(value = "Nome da Pessoa") @QueryParam("pessoa") String nmPessoa
		) {
		try {
			
			Criterios criterios = new Criterios("qtLimite", Integer.toString(qtLimite), 0, 0);
			

			if(nmPessoa != null) {
				criterios.add("A.nm_pessoa", nmPessoa, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}			
			
			ResultSetMap _rsm = PessoaServices.find(criterios);
			if(_rsm==null || _rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum Pessoa encontrada");
			}
			
			return ResponseFactory.ok(_rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/findByCpfCnpj")
	@ApiOperation(
			value = "Retorna uma Pessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa encontrada", response = Pessoa.class),
			@ApiResponse(code = 204, message = "Pessoa não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Numero do cpf/cnpj") @QueryParam("nrCpfCnpj") String nrCpfCnpj) {
		try {
			
			ResultSetMap _rsm = PessoaServices.findByCpfCnpjCompleto(nrCpfCnpj);
			
			if(_rsm.next()) 
				return ResponseFactory.ok(_rsm);
			
			return ResponseFactory.noContent("Nenhuma pessoa encontrada");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma Pessoa"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Pessoa encontrada", response = Pessoa.class),
			@ApiResponse(code = 204, message = "Pessoa não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Pessoa") @PathParam("id") int cdPessoa) {
		try {
			Pessoa pessoa = PessoaServices.get(cdPessoa, null);
			if(pessoa == null) 
				return ResponseFactory.noContent("Nenhuma pessoa encontrada");			
			
			return ResponseFactory.ok(pessoa);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/{id}/arquivos")
	@ApiOperation(value = "Busca a lista de arquivos ligadas a pessoa.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Arquivos encontrados", response = Arquivo[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getPessoas (@ApiParam(value = "id da pessoa") @PathParam("id") int cdPessoa) {
		try {	
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			crt.add(new ItemComparator("A.cd_pessoa", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = PessoaArquivoServices.find(crt);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum arquivo encontrado.");
			
			return ResponseFactory.ok(new ResultSetMapper<Arquivo>(rsm, Arquivo.class));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

}
