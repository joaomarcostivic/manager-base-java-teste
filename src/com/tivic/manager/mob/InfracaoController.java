package com.tivic.manager.mob;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
import com.tivic.sol.auth.jwt.JWTIgnore;
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
import sol.util.Result;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Api(value = "Infracao", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/infracoes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfracaoController {
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Registra uma nova Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infração registrada", response = Infracao.class),
			@ApiResponse(code = 400, message = "A infração possui algum parÃ¢metro invÃ¡lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response create(@ApiParam(value = "Infração a ser registrado", required = true) Infracao infracao) {
		try {	
			infracao.setCdInfracao(0);
			
			ResultSetMap rsm = InfracaoServices.validVigente(infracao.getNrCodDetran(), null);
			
			if(rsm.next())
				return ResponseFactory.badRequest("Já existe infração ativa com esse código na base do DETRAN");

			Result r = InfracaoServices.save(infracao);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("A infração não pode ser inserida com campos em branco", r.getMessage());
			
			return ResponseFactory.ok((Infracao)r.getObjects().get("INFRACAO"));
			
		} catch(Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisição.", e.getMessage());
		}
	}
	
	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza uma Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infração atualizada", response = Infracao.class),
			@ApiResponse(code = 400, message = "A infração possui algum parÃ¢metro invÃ¡lido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response update(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao,
			@ApiParam(value = "Infração a ser atualizada") Infracao infracao) {
		try {
			infracao.setCdInfracao(cdInfracao);
			
			ResultSetMap rsm = InfracaoServices.validVigente(infracao.getNrCodDetran(), null);
			
			if(rsm.next() && infracao.getCdInfracao() != rsm.getInt("CD_INFRACAO"))
				return ResponseFactory.badRequest("Já existe infração ativa com esse código na base do DETRAN");
			
			Result result = InfracaoServices.save(infracao);
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok((Infracao)result.getObjects().get("INFRACAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna uma Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infração encontrada", response = Infracao.class),
			@ApiResponse(code = 204, message = "Infração nÃ£o encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao) {
		try {
			Infracao _infracao = InfracaoDAO.get(cdInfracao);
			if(_infracao == null) {
				return ResponseFactory.notFound("Nenhuma infração encontrada");
			}
			
			return ResponseFactory.ok(_infracao);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Infrações"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infrações encontradas", response = Infracao[].class),
			@ApiResponse(code = 204, message = "Nenhuma infração encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getAll(
			@ApiParam(value  = "Codigo DETRAN") @QueryParam("codDetran") int nrCodDetran,
			@ApiParam(value  = "Natureza da Infracao") @QueryParam("nmNatureza") String nmNatureza,
//			@ApiParam(value  = "Tipo Competencia") @QueryParam("tpCompetencia") int tpCompetencia,
			@ApiParam(value  = "Descricao da Infracao") @QueryParam("dsInfracao") String dsInfracao,
			@ApiParam(value  = "Infracao ATIVO/INATIVO") @QueryParam("ativo") @DefaultValue("true") Boolean isActive,
			@ApiParam(value  = "Busca em multiplos campos por esta palavra chave") @QueryParam("keyword") String keyword,
			@ApiParam(value = "Quantidade maxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
		try {
			
			Criterios criterios = new Criterios("limit", Integer.toString(limit), 0, 0);
			
			if(nrCodDetran > 0) {
				criterios.add("nr_cod_detran", Integer.toString(nrCodDetran), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nmNatureza != null) {
				criterios.add("nm_natureza", nmNatureza, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
//			if(tpCompetencia >= 0) {
//				criterios.add("tp_competencia", Integer.toString(tpCompetencia), ItemComparator.EQUAL, Types.INTEGER);
//			}
			
			if(dsInfracao != null) {
				criterios.add("ds_infracao", dsInfracao, ItemComparator.LIKE_ANY, Types.VARCHAR);
			}
			
			if(isActive) {
				criterios.add("dt_fim_vigencia", null, ItemComparator.ISNULL, Types.INTEGER);
			}
			
			if(keyword != null) {
				criterios.add("keyword", keyword, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			ResultSetMap rsm = InfracaoServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
						
			List<Infracao> infracoes = new ResultSetMapper<Infracao>(rsm, Infracao.class).toList();
			
			
			
			return ResponseFactory.ok(infracoes);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	/**
	 * OBSERVACOES INFRACAO
	 */
	@GET
	@Path("/{id}/observacoes")
	@ApiOperation(
			value = "Retorna as ObservacÃ´es da Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Observações encontradas", response = InfracaoObservacao[].class),
			@ApiResponse(code = 204, message = "Nenhuma observação encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response getObservacoes(@ApiParam(value  = "Código da Infração") @PathParam("id") int cdInfracao) {
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_infracao", Integer.toString(cdInfracao), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = InfracaoObservacaoServices.find(criterios);
			
			if(rsm.getLines().size() <= 0) {
				return ResponseFactory.noContent("Nenhum registro");
			}
			
			List<InfracaoObservacao> observacoes = new ResultSetMapper<InfracaoObservacao>(rsm, InfracaoObservacao.class).toList();
			return ResponseFactory.ok(observacoes);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/{id}/observacoes")
	@ApiOperation(
			value = "Registra a observação da Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Observações registrada", response = InfracaoObservacao.class),
			@ApiResponse(code = 400, message = "Erro ao salvar", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response addObservacao(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao,
			@ApiParam(value = "Observação a ser registrada", required = true) InfracaoObservacao observacao) {
		try {
			observacao.setCdInfracao(cdInfracao);
			observacao.setCdObservacao(0);
			
			Result result = InfracaoObservacaoServices.save(observacao);
			
			if(result.getCode() <= 0)
				return ResponseFactory.badRequest(result.getMessage());
			
			return ResponseFactory.ok("Observação inserida com sucesso");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}		
	}
	
	@PUT
	@Path("/{id}/observacoes/{idObservacao}")
	@ApiOperation(
			value = "Registra a observação da Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Observações registrada", response = InfracaoObservacao.class),
			@ApiResponse(code = 400, message = "Erro ao salvar", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response updateObservacao(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao,
			@ApiParam(value = "Código da Observação") @PathParam("idObservacao") int cdObservacao,
			@ApiParam(value = "Observação a ser registrada", required = true) InfracaoObservacao observacao) {
		try {
			observacao.setCdInfracao(cdInfracao);
			observacao.setCdObservacao(cdObservacao);
			
			Result result = InfracaoObservacaoServices.save(observacao);
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok((InfracaoObservacao) result.getObjects().get("INFRACAOOBSERVACAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}		
	}
	
	@DELETE
	@Path("/{id}/observacoes/{idObservacao}")
	@ApiOperation(
			value = "Remove a observação da Infração"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Observação removida", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Erro ao remover", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response removeObservacao(@ApiParam(value = "Código da Infração") @PathParam("id") int cdInfracao,
			@ApiParam(value = "Código da Observação") @PathParam("idObservacao") int cdObservacao) {
		try {
			Result result = InfracaoObservacaoServices.remove(cdInfracao, cdObservacao);
			
			if(result.getCode() <= 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.ok("Removido com sucesso");
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/sync")
	@ApiOperation(
			value = "Sincroniza tabela de infrações utilizando base de dados da PRODEMGE"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Infrações sincronizadas", response = ResponseBody.class),
			@ApiResponse(code = 400, message = "Erro ao sincronizar as infrações", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	@JWTIgnore
	public static Response sync() {
		try {
			InfracaoSync infracaoSync = new InfracaoSyncMg();
			Result result = infracaoSync.sync();
			return ResponseFactory.ok(result.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro no processo de requisição");
		}
	}
}
