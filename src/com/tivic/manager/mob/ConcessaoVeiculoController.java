package com.tivic.manager.mob;

import java.sql.Types;
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

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.equipamento.Equipamento;
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

@Api(value = "ConcessaoVeiculo", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/concessoesveiculos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConcessaoVeiculoController {
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Retorna uma lista de concessao veiculo"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ConcessoesVeiculos encontrados"),
		@ApiResponse(code = 204, message = "Nenhuma concessaoVeiculo"),
		@ApiResponse(code = 204, message = "Não existe concessoesVeiculos com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response find (
		@ApiParam(value = "Tipo de Concessao") @QueryParam("tpConcessao") @DefaultValue("-1") int tpConcessao,
		@ApiParam(value = "Codigo da concessao") @QueryParam("cdConcessao") int cdConcessao,
		@ApiParam(value = "Codigo do veiculo") @QueryParam("cdVeiculo") int cdVeiculo,
		@ApiParam(value = "Numero de prefixo") @QueryParam("nrPrefixo") String nrPrefixo,
		@ApiParam(value = "Numero da placa") @QueryParam("nrPlaca") String nrPlaca,
		@ApiParam(value = "Numero da concessao Veiculo") @QueryParam("cdConcessaoVeiculo") int cdConcessaoVeiculo

	) {
		try {
			Criterios crt = new Criterios();
			
			if(tpConcessao>-1)
				crt.add("C.tp_concessao", Integer.toString(tpConcessao), ItemComparator.EQUAL, Types.INTEGER);
			
			if(cdConcessao != 0)
				crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);				
			
			if(cdVeiculo != 0)
				crt.add("A.cd_veiculo", Integer.toString(cdVeiculo), ItemComparator.EQUAL, Types.INTEGER);				

			if(nrPrefixo != null)
				crt.add("A.nr_prefixo", nrPrefixo, ItemComparator.EQUAL, Types.VARCHAR);
			
			if(nrPlaca != null && !nrPlaca.equals("")) {
				nrPlaca = nrPlaca.toUpperCase();
				crt.add("nr_placa", nrPlaca, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
				
			if(cdConcessaoVeiculo > 0)
				crt.add("A.cd_concessao_veiculo", Integer.toString(cdConcessaoVeiculo), ItemComparator.EQUAL, Types.VARCHAR);	
			
			List<ConcessaoVeiculoDTO> list = ConcessaoVeiculoServices.findDTO(crt);
			
			if(list.isEmpty())
				return ResponseFactory.noContent(list);

			return ResponseFactory.ok(list);
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Busca uma concessao veiculo com seus dados.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "ConcessaoVeiculo encontrados", response = ConcessaoVeiculoDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getVeiculoConcessao (@ApiParam(value = "id da concessao veiculo") @PathParam("id") int cdConcessaoVeiculo) {
		
		try {
			Criterios criterios = new Criterios();
			criterios.add("A.cd_concessao_veiculo", Integer.toString(cdConcessaoVeiculo), ItemComparator.EQUAL, Types.INTEGER);
			
			List<ConcessaoVeiculoDTO> list = ConcessaoVeiculoServices.findDTO(criterios);
			
			if(list.isEmpty())
				return ResponseFactory.noContent(list);
			
			ConcessaoVeiculoDTO dto = list.get(0);

			return ResponseFactory.ok(dto);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/equipamentos")
	@ApiOperation(value = "Busca a lista de equipamentos ligadas a concessaoveiculo.")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Equipamentos encontrads", response = Equipamento[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public Response getEquipamentos (@ApiParam(value = "id da concessao veiculo") @PathParam("id") int cdConcessaoVeiculo) {
		try {	
			Criterios criterios = new Criterios();
			criterios.add("D.cd_concessao_veiculo", Integer.toString(cdConcessaoVeiculo), ItemComparator.EQUAL, Types.INTEGER);
			
			ResultSetMap rsm = VeiculoEquipamentoServices.find(criterios);
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhum equipamento encontrado.");
			
			return ResponseFactory.ok(new ResultSetMapper<Equipamento>(rsm, Equipamento.class));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma concessao veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Veiculo atualizado", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 400, message = "Veiculo invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da concessao veiculo a ser atualizada") @PathParam("id") int cdConcessaoVeiculo,
			@ApiParam(value = "ConcessaoVeiculo a ser atualizada.") ConcessaoVeiculoDTO dto) {
		try {
			dto.getConcessaoVeiculo().setCdConcessaoVeiculo(cdConcessaoVeiculo);

			Result result = ConcessaoVeiculoServices.save(dto, null);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((ConcessaoVeiculo)result.getObjects().get("CONCESSAOVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova concessao veiculo"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Veiculo registrado", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 400, message = "Veiculo inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "ConcessaoVeiculo a ser registrado") ConcessaoVeiculoDTO dto) {
		try {
			dto.getConcessaoVeiculo().setCdConcessaoVeiculo(0);
			dto.getPessoa().setCdPessoa(0);
			dto.getPessoaEndereco().setCdPessoa(0);
			
			Result result = ConcessaoVeiculoServices.save(dto, null);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());

			return ResponseFactory.ok((ConcessaoVeiculo)result.getObjects().get("CONCESSAOVEICULO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/concessionario/{nrPrefixo}")
	@ApiOperation(
			value="Retornar o nome do concessionário pelo o numero do prefixo"
		)
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Concessionario encontrado", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 204, message = "Concessionario não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	
	
	public static Response getConcessionario(@ApiParam(value = "Nome do concessionario") @PathParam("nrPrefixo") int nrPrefixo) {
		try {
			if(nrPrefixo == 0) 
				return ResponseFactory.badRequest("Nome do concessionario nulo ou inválido");
				
				Pessoa result = ConcessaoVeiculoServices.getConcessionarioByNumeroPrefixo(nrPrefixo);
				
				if(result == null)
					return ResponseFactory.noContent("Nenhum concessionario encontrado");
				
				return ResponseFactory.ok(result);
			} catch(Exception e) {
				return ResponseFactory.internalServerError(e.getMessage());
			}
		}
	
	
	@GET
	@Path("/concessao/{nrPrefixo}")
	@ApiOperation(
			value="Retornar a concessão do veiculo pelo o numero do prefixo"
		)
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Concessão encontrada", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 204, message = "Concessão não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	
	
	public static Response getConcessao(@ApiParam(value = "Concessão do veiculo") @PathParam("nrPrefixo") int nrPrefixo) {
		try {
			if(nrPrefixo == 0) 
				return ResponseFactory.badRequest("Concessão do veiculo nulo ou inválido");
				
				ConcessaoVeiculo result = ConcessaoVeiculoServices.getByNrPrefixo(nrPrefixo);
				
				if(result == null)
					return ResponseFactory.noContent("Nenhum concessionario encontrado");
				
				return ResponseFactory.ok(result);
			} catch(Exception e) {
				return ResponseFactory.internalServerError(e.getMessage());
			}
		}
	
	@GET
	@Path("/veiculo/{nrPlaca}")
	@ApiOperation(
			value="Retornar veiculo pelo o numero da placa"
		)
	@ApiResponses( value = {
			@ApiResponse(code = 200, message = "Veiculo encontrada", response = ConcessaoVeiculo.class),
			@ApiResponse(code = 204, message = "Veiculo não encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	
	
	public static Response getVeiculoByPlaca(@ApiParam(value = "Informações de veiculo e seu proprietario") @PathParam("nrPlaca") String nrPlaca) {
		try {
			if(nrPlaca == null) 
				return ResponseFactory.badRequest("Informações de veiculo e seu proprietario");
				
				List<ConcessaoVeiculoDTO> list = ConcessaoVeiculoServices.getVeiculoByPlaca(nrPlaca);
				
				if(list.isEmpty())
					return ResponseFactory.noContent(list);
				
				ConcessaoVeiculoDTO dto = list.get(0);

				return ResponseFactory.ok(dto);
			} catch(Exception e) {
				return ResponseFactory.internalServerError(e.getMessage());
			}
		}
}