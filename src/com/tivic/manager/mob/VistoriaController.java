package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;

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

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.ResultSetMapper;
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
import sol.dao.ResultSetMap;
import sol.util.Result;

@Api(value = "Vistoria", tags = {"mob"}, authorizations = {
	@Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token")
	})
})
@Path("/v2/mob/vistorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VistoriaController {
	
	@GET
	@Path("")
	@ApiOperation(
		value = "Retorna a lista de vistorias"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Vistorias encontrados"),
		@ApiResponse(code = 204, message = "Nenhuma vistoria"),
		@ApiResponse(code = 204, message = "Não existe vistorias com o id indicado"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response find (
		@ApiParam(value = "Inicio do periodo de vistoria (dd/mm/yyyy)") @QueryParam("dtVistoriaInicial") String dtVistoriaInicial,
		@ApiParam(value = "Fim do periodo de vistoria (dd/mm/yyyy)") @QueryParam("dtVistoriaFinal") String dtVistoriaFinal,
		@ApiParam(value = "Codigo da concessao") @QueryParam("cdConcessao") int cdConcessao,
		@ApiParam(value = "Tipo da concessao") @QueryParam("tpConcessao") @DefaultValue("-1") int tpConcessao,
		@ApiParam(value = "Codigo da concessaoVeiculo") @QueryParam("cdConcessaoVeiculo") int cdConcessaoVeiculo,
		@ApiParam(value = "Situacao da vistoria") @QueryParam("stVistoria") @DefaultValue("-1") int stVistoria,
		@ApiParam(value = "Quantidade de registros") @QueryParam("limit") int nrLimite
	) {
		try {
			Criterios crt = new Criterios();
			
			if(nrLimite != 0) {
				crt.add("LIMIT", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(dtVistoriaInicial != null) {
				crt.add("A.DT_VISTORIA", dtVistoriaInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtVistoriaFinal != null) {
				crt.add("A.DT_VISTORIA", dtVistoriaFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(cdConcessao != 0) {
				crt.add("D.CD_CONCESSAO", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(tpConcessao >= 0) {
				crt.add("C.TP_CONCESSAO", Integer.toString(tpConcessao), ItemComparator.EQUAL, Types.INTEGER);			
			}
			
			if(cdConcessaoVeiculo != 0) {
				crt.add("D.CD_CONCESSAO_VEICULO", Integer.toString(cdConcessaoVeiculo), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(stVistoria >= 0) {
				crt.add("A.ST_VISTORIA", Integer.toString(stVistoria), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			ResultSetMap rsm = VistoriaServices.findSimples(crt);

			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma vistoria encontrada.");
			
			return ResponseFactory.ok(new ResultSetMapper<Vistoria>(rsm, Vistoria.class));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/find")
	@ApiOperation(
		value = "Retorna a lista de vistorias"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Vistorias encontrados"),
		@ApiResponse(code = 204, message = "Nenhuma vistoria"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response findVistorias (
		@ApiParam(value = "Inicio do periodo de vistoria (dd/mm/yyyy)") @QueryParam("dtVistoriaInicial") String dtVistoriaInicial,
		@ApiParam(value = "Fim do periodo de vistoria (dd/mm/yyyy)") @QueryParam("dtVistoriaFinal") String dtVistoriaFinal,
		@ApiParam(value = "Inicio do periodo de aplicação da vistoria (dd/mm/yyyy)") @QueryParam("dtAplicacaoInicial") String dtAplicacaoInicial,
		@ApiParam(value = "Fim do periodo de aplicação da vistoria (dd/mm/yyyy)") @QueryParam("dtAplicacaoFinal") String dtAplicacaoFinal,
		@ApiParam(value = "Inicio do periodo de retorno da vistoria (dd/mm/yyyy)") @QueryParam("dtRetornoInicial") String dtRetornoInicial,
		@ApiParam(value = "Fim do periodo de retorno da vistoria (dd/mm/yyyy)") @QueryParam("dtRetornoFinal") String dtRetornoFinal,
		@ApiParam(value = "Situacao da vistoria") @QueryParam("stVistoria") @DefaultValue("-1") int stVistoria,
		@ApiParam(value = "Tipo da vistoria") @QueryParam("tpVistoria") @DefaultValue("-1") int tpVistoria,
		@ApiParam(value = "Número de Prefixo") @QueryParam("nrPrefixo") @DefaultValue("-1") int nrPrefixo,
		@ApiParam(value = "Número de Ponto") @QueryParam("nrPonto") int nrPonto,
		@ApiParam(value = "Número de Ordem") @QueryParam("nrOrdem") int nrOrdem,
		@ApiParam(value = "Número de Placa") @QueryParam("nrPlaca") String nrPlaca,
		@ApiParam(value = "Número de Placa") @QueryParam("cdConcessionario") int cdConcessionario,
		@ApiParam(value = "Número do selo") @QueryParam("idSelo") String idSelo,
		@ApiParam(value = "Nome do vistoriado") @QueryParam("cdPessoa") int cdPessoa,
		@ApiParam(value = "Nome do vistoriador") @QueryParam("nmVistoriado") String nmVistoriado,
		@ApiParam(value = "Nome do vistoriador") @QueryParam("nmVistoriador") String nmVistoriador,
		@ApiParam(value = "Texto de observações") @QueryParam("dsObservacao") String dsObservacao,
		@ApiParam(value = "Ordenação da tabela") @QueryParam("ordenacao") String ordenacao,
		@ApiParam(value = "Quantidade de registros") @QueryParam("limit") @DefaultValue("50")  int nrLimite
	) {
		try {
			Criterios crt = new Criterios();

			if(nrLimite != 0) {
				crt.add("LIMIT", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nmVistoriado != null) {
				crt.add("G.NM_PESSOA", nmVistoriado, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(idSelo != null) {
				crt.add("A.ID_SELO", idSelo, ItemComparator.LIKE, Types.VARCHAR);				
			}

			if(ordenacao != null) {
				crt.add("ORDERBY", ordenacao, ItemComparator.LIKE, Types.VARCHAR);				
			}
						
			if(nmVistoriador != null) {
				crt.add("I.NM_PESSOA", nmVistoriador, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(dsObservacao != null) {
				crt.add("A.DS_OBSERVACAO", dsObservacao, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(nrPrefixo != -1) {
				crt.add("D.NR_PREFIXO", Integer.toString(nrPrefixo), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdPessoa != 0) {
				crt.add("C.CD_PESSOA", Integer.toString(cdPessoa), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(cdConcessionario != 0) {
				crt.add("C.CD_CONCESSIONARIO", Integer.toString(cdConcessionario), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nrPonto != 0) {
				crt.add("M.NR_PONTO", Integer.toString(nrPonto), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(nrOrdem != 0) {
				crt.add("L.NR_ORDEM", Integer.toString(nrOrdem), ItemComparator.EQUAL, Types.INTEGER);				
			}
						
			if(nrPlaca != null) {
				crt.add("B.NR_PLACA", nrPlaca, ItemComparator.LIKE, Types.VARCHAR);				
			}
			
			if(dtVistoriaInicial != null) {
				crt.add("A.DT_VISTORIA", dtVistoriaInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtVistoriaFinal != null) {
				crt.add("A.DT_VISTORIA", dtVistoriaFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}

			if(dtAplicacaoInicial != null) {
				crt.add("A.DT_APLICACAO", dtAplicacaoInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtAplicacaoFinal != null) {
				crt.add("A.DT_APLICACAO", dtAplicacaoFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}

			if(dtRetornoInicial != null) {
				crt.add("J.DT_VISTORIA", dtRetornoInicial, ItemComparator.GREATER_EQUAL, Types.TIMESTAMP);
			}
			
			if(dtRetornoFinal != null) {
				crt.add("J.DT_VISTORIA", dtRetornoFinal, ItemComparator.MINOR_EQUAL, Types.TIMESTAMP);
			}
			
			if(tpVistoria != -1) {
				crt.add("A.TP_VISTORIA", Integer.toString(tpVistoria), ItemComparator.EQUAL, Types.INTEGER);			
			}
			
			if(stVistoria != -1) {
				crt.add("A.ST_VISTORIA", Integer.toString(stVistoria), ItemComparator.EQUAL, Types.INTEGER);				
			}

			ResultSetMap rsm = VistoriaServices.findVistoriador(crt);
			
			if(rsm == null || rsm.getLines().size() == 0)
				return ResponseFactory.noContent("Nenhuma vistoria encontrada.");
			
			return ResponseFactory.ok(new ResultSetMapper<Vistoria>(rsm, Vistoria.class));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma vistoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vistoria encontrada", response = Vistoria.class),
			@ApiResponse(code = 204, message = "Vistoria não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Codigo da Vistoria") @PathParam("id") int cdVistoria) {
		try {
			if(cdVistoria == 0) 
				return ResponseFactory.badRequest("Vistoria é nula ou invalida");
			
//			Criterios crt = new Criterios();
//			ResultSetMap rsm = VistoriaServices.findSimples(crt.add("A.cd_vistoria", Integer.toString(cdVistoria), ItemComparator.EQUAL, Types.INTEGER));
			
			Vistoria vistoria = VistoriaDAO.get(cdVistoria);
			
			if(vistoria==null) 
				return ResponseFactory.noContent("Nenhuma vistoria encontrado");
			
			return ResponseFactory.ok(vistoria);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova vistoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vistoria registrada", response = Vistoria.class),
			@ApiResponse(code = 400, message = "Vistoria possui algum parametro invalido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Vistoria a ser registrada", required = true) Vistoria vistoria) {
		try {	
			vistoria.setCdVistoria(0);
			vistoria.setStVistoria(VistoriaServices.ST_PENDENTE);
			
			Result r = VistoriaServices.save(vistoria);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("Vistoria possui algum parametro invalido", r.getMessage());
			}
			
			return ResponseFactory.ok((Vistoria)r.getObjects().get("VISTORIA"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@POST
	@Path("/lote")
	@ApiOperation(
			value = "Registra vistoria em lote"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vistorias registradas.", response = Vistoria.class),
			@ApiResponse(code = 400, message = "Vistoria possui algum parametro invalido.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.", response = ResponseBody.class)
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(@ApiParam(value = "Lista de vistoria a ser registradas.", required = true) ArrayList<Vistoria> vistorias) {
		try {	
			
			Result r = VistoriaServices.saveLote(vistorias);
			if(r.getCode() < 0) {
				return ResponseFactory.badRequest("Vistoria possui algum parametro invalido.", r.getMessage());
			}
			
			return ResponseFactory.ok(r);
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma vistoria"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vistoria atualizada", response = Vistoria.class),
			@ApiResponse(code = 400, message = "Vistoria invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(
			@ApiParam(value = "Codigo da vistoria") @PathParam("id") int cdVistoria,
			@ApiParam(value = "Vistoria a ser atualizada") Vistoria vistoria) {
		try {			
			vistoria.setCdVistoria(cdVistoria);
			
			Result result = VistoriaServices.save(vistoria);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
				
			return ResponseFactory.ok((Vistoria)result.getObjects().get("VISTORIA"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga uma vistoria",
		notes = "Considere id = cdVistoria"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Vistoria apagada"),
			@ApiResponse(code = 400, message = "Vistoria possui algum parametro invalido"),
			@ApiResponse(code = 204, message = "Vistoria nao encontrada"),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
	})
	public static Response delete(@ApiParam(value = "Id da vistoria a ser apagada", required = true) @PathParam("id") int cdVistoria) {
		try {
			Result r = VistoriaServices.remove(cdVistoria);
			if(r.getCode() < 0) {
				return ResponseFactory.internalServerError(r.getMessage());
			}
			
			return ResponseFactory.ok("Vistoria apagada");
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}
}
