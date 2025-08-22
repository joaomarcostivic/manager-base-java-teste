package com.tivic.manager.mob.grafica;

import java.sql.Types;

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
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.sol.search.SearchCriterios;

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

@Api(value = "AIT", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/loteimpressao")
@Produces(MediaType.APPLICATION_JSON)
public class LoteImpressaoController {


	@POST
	@Path("")
	@ApiOperation(
			value = "Registra um novo lote de impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de impressão registrado"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response create(
			@ApiParam(value = "Lote de impressão a ser registrado", required = true) LoteImpressao lote) {
		try {
			Result result = LoteImpressaoServices.save(lote);
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((LoteImpressao) result.getObjects().get("LOTEIMPRESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}

	@PUT
	@Path("/{id}")
	@ApiOperation(
			value = "Atualiza dados de um lote de impressão",
			notes = "Considere id = cdLoteImpressao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de impressão atualizado", response = Ait.class),
			@ApiResponse(code = 400, message = "Lote de impressão é nulo ou invalido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response update(
			@ApiParam(value = "Id do Lote de impressão a ser atualizado", required = true) @PathParam("id") int cdLoteImpressao, 
			@ApiParam(value = "Lote de impressao a ser atualizado", required = true) LoteImpressao lote) {
		try {
			if(lote.getCdLoteImpressao() == 0) 
				return ResponseFactory.badRequest("Lote de impressão é nulo ou invalido");
			
			lote.setCdLoteImpressao(cdLoteImpressao);
			Result r = LoteImpressaoServices.save(lote);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("Lote de impressão é nulo ou invalido", r.getMessage());
			
			return ResponseFactory.ok((LoteImpressao)r.getObjects().get("LOTEIMPRESSAO"));
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
	}

	@DELETE
	@Path("/{id}")
	@ApiOperation(
		value = "Apaga um Lote de Impressão",
		notes = "Considere id = cdLoteImpressao"
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lote de Impressão a ser apagado"),
		@ApiResponse(code = 400, message = "Lote de impressão é nulo ou invalido"),
		@ApiResponse(code = 500, message = "Erro no servidor")
	})
	public static Response delete(@ApiParam(value = "Id do Lote de Impressão a ser apagado", required = true) @PathParam("id") int cdLoteImpressao) {
		try {
			
			if(cdLoteImpressao == 0) 
				return ResponseFactory.badRequest("Lote de impressão é nulo ou invalido");
			
			Result r = LoteImpressaoServices.remove(cdLoteImpressao);
			if(r.getCode() < 0) 
				return ResponseFactory.badRequest("Lote de impressão é nulo ou invalido", r.getMessage());

			return ResponseFactory.ok(r);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
		}
		
	}
	


	@GET
	@Path("/{id}")
	@ApiOperation(
			value = "Retorna um Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de Impressão encontrado", response = LoteImpressao.class),
			@ApiResponse(code = 204, message = "Órgão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response get(@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
		try {
			LoteImpressao lote = LoteImpressaoDAO.get(cdLoteImpressao);
			if(lote == null) {
				return ResponseFactory.noContent("Nenhum lote de impressão encontrado");
			}
			
			return ResponseFactory.ok(lote);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("")
	@ApiOperation(
			value = "Retorna Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão encontrado", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAll(
			@ApiParam(value = "Id. Lote Impressao") @QueryParam("id") String idLoteImpressao,
			@ApiParam(value = "Dt. criação") @QueryParam("data") String dtCriacao,
			@ApiParam(value = "Situação do lote") @QueryParam("situacao") Integer stLoteImpressao,
			@ApiParam(value = "Tipo do lote") @QueryParam("tipo") Integer tpLoteImpressao,
			@ApiParam(value = "Tipo do documento contido no lote") @QueryParam("documento") Integer tpDocumento,
			@ApiParam(value = "Tipo de destino do lote após impresso") @QueryParam("destino") Integer tpDestino,
			@ApiParam(value = "Tipo do transporte para entrega do lote") @QueryParam("transporte") Integer tpTransporte,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") Integer limit) {
			
		try {
			
			Criterios criterios = new Criterios();
			
			if(idLoteImpressao != null) {
				criterios.add("A.id_lote_impressao", idLoteImpressao, ItemComparator.LIKE, Types.VARCHAR);
			}
			
			if(stLoteImpressao != null && stLoteImpressao >= 0) {
				criterios.add("A.st_lote_impressao", Integer.toString(stLoteImpressao), ItemComparator.EQUAL, Types.INTEGER);
			}		
			
			if(tpLoteImpressao != null && tpLoteImpressao >= 0) {
				criterios.add("A.tp_lote_impressao", Integer.toString(tpLoteImpressao), ItemComparator.EQUAL, Types.INTEGER);
			}			
			
			if(tpDocumento != null && tpDocumento >= 0) {
				criterios.add("A.tp_documento", Integer.toString(tpDocumento), ItemComparator.EQUAL, Types.INTEGER);
			}			
			
			if(tpDestino != null && tpDestino >= 0) {
				criterios.add("A.tp_destino", Integer.toString(tpDestino), ItemComparator.EQUAL, Types.INTEGER);
			}	

			if(tpTransporte != null && tpTransporte >= 0) {
				criterios.add("A.tp_transporte", Integer.toString(tpTransporte), ItemComparator.EQUAL, Types.INTEGER);
			}	
			
			ResultSetMap rsm = LoteImpressaoServices.find(criterios);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum lote de impressão encontrado");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/inconsistentes")
	@ApiOperation(
			value = "Retorna Lotes de Impressão Inconsistentes"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão Inconsistentes encontrado", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão Inconsistentes não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response lotesInconsistentes(
			@ApiParam(value = "Id. Lote Impressao") @QueryParam("id") String idLoteImpressao,
			@ApiParam(value = "Dt. criação") @QueryParam("data") String dtCriacao,
			@ApiParam(value = "Situação do lote") @QueryParam("situacao") int stLoteImpressao,
			@ApiParam(value = "Tipo do lote") @QueryParam("tipo") int tpLoteImpressao,
			@ApiParam(value = "Tipo do documento contido no lote") @QueryParam("documento") int tpDocumento,
			@ApiParam(value = "Tipo de destino do lote após impresso") @QueryParam("destino") int tpDestino,
			@ApiParam(value = "Tipo do transporte para entrega do lote") @QueryParam("transporte") int tpTransporte,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "50") @DefaultValue("50") @QueryParam("limit") int limit) {
			
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.id_lote_impressao", idLoteImpressao, idLoteImpressao != null);
			searchCriterios.addCriteriosGreaterDate("A.dt_criacao", dtCriacao, dtCriacao != null);
			searchCriterios.addCriteriosMinorDate("A.dt_criacao", dtCriacao, dtCriacao != null);
			searchCriterios.addCriteriosEqualInteger("A.st_lote_impressao", stLoteImpressao, stLoteImpressao > 0);
			searchCriterios.addCriteriosEqualInteger("A.tp_lote_impressao", tpLoteImpressao, tpLoteImpressao > 0);
			searchCriterios.addCriteriosEqualInteger("A.tp_documento", tpDocumento, tpDocumento > 0);
			searchCriterios.addCriteriosEqualInteger("A.tp_destino", tpDestino, tpDestino > 0);
			searchCriterios.addCriteriosEqualInteger("A.tp_transporte", tpTransporte, tpTransporte >= 0);
			ResultSetMap rsm = LoteImpressaoServices.findLoteErro(searchCriterios);
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/aits")
	@ApiOperation(
			value = "Retorna AITs do Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs do Lote encontrados.", response = LoteImpressao[].class),
			@ApiResponse(code = 204, message = "AITs do Lote não encontrados.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		}) 
	public Response retrieveAllAits(
			@ApiParam(value = "Cd. Lote de Impressao") @QueryParam("cdLoteImpressaoAit") int cdLoteImpressaoAit,
			@ApiParam(value = "Nr. AIT") @QueryParam("idAit") String idAit,
			@ApiParam(value = "Data da Autuação") @QueryParam("data") String dtInfracao,
			@ApiParam(value = "Placa") @QueryParam("nrPlaca") String nrPlaca,
			@ApiParam(value = "Renavan") @QueryParam("nrRenavan") String nrRenavan,
			@ApiParam(value = "Etiqueta") @QueryParam("nrEtiqueta") @DefaultValue("-1") int nrEtiqueta,
	        @QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite)
	{
		
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressaoAit, cdLoteImpressaoAit > -1);
			searchCriterios.addCriterios("B.nr_ait", idAit, ItemComparator.LIKE, Types.VARCHAR, idAit != null);
			searchCriterios.addCriteriosGreaterDate("B.dt_infracao", dtInfracao, dtInfracao != null);
			searchCriterios.addCriteriosMinorDate("B.dt_infracao", dtInfracao, dtInfracao != null);
			searchCriterios.addCriterios("B.nr_placa", nrPlaca, ItemComparator.LIKE, Types.VARCHAR, nrPlaca != null);
			searchCriterios.addCriterios("B.cd_renavan", nrRenavan, ItemComparator.LIKE, Types.VARCHAR, nrRenavan != null);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			return ResponseFactory.ok(LoteImpressaoServices.buscarLotesAitsErro(searchCriterios));	
		} catch (ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return ResponseFactory.badRequest(ve.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/lote/{cdLoteImpressao}")
	@ApiOperation(
			value = "Retorna um Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote de Impressão encontrado", response = LoteImpressao.class),
			@ApiResponse(code = 204, message = "Órgão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getLoteErro(@ApiParam(value = "Código do Lote de Impressão") @PathParam("cdLoteImpressao") @DefaultValue("-1") int cdLoteImpressao) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualInteger("A.cd_lote_impressao", cdLoteImpressao, cdLoteImpressao > -1);
			return ResponseFactory.ok(LoteImpressaoServices.buscarLoteErro(searchCriterios));
		} catch (NoContentException e) {
			return ResponseFactory.noContent(e.getMessage());
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/wizard/nai")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNAI(@ApiParam(value = "Quantidade de NAIs", required = false) int qtdLote) {
			
		try {
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNAI(qtdLote);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/wizardcorrecoes/nai")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNAICorrecoes() {
			
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNAICorrecoes();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/wizardcorrecoes/nip")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNIPCorrecoes() {
			System.out.println("Cheguei ao retiver NIP");
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNIPCorrecoes();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/search/nai")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão no modal caso apos gerar ainda existam mais"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response searchAllAitsNAI() {
		int qtdAits = 0;
			
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNAI();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.ok(String.valueOf(qtdAits));
			}
			
			qtdAits = rsm.getLines().size();
			
			return ResponseFactory.ok(String.valueOf(qtdAits));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/wizard/nip")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NIPS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNIP(@ApiParam(value = "Quantidade de NIPs", required = false) int qtdLote) {	
		try {
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNIP(qtdLote);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			return ResponseFactory.ok(rsm);
		} 
		catch (Exception e) 
		{
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/search/nip")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NIPS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response searchAllAitsNIP() {
		int qtdAits = 0;
			
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNIP();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.ok(String.valueOf(qtdAits));
			}
			
			qtdAits = rsm.getLines().size();
			
			return ResponseFactory.ok(String.valueOf(qtdAits));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/aits")
	@ApiOperation(
			value = "Retorna aits vinculadas ao Lote de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lotes de Impressão encontrado", response = LoteImpressaoAit[].class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAits(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
			
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAits(cdLoteImpressao);
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhum lote de impressão encontrado");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/gerar")
	@ApiOperation(
			value = "Inicia o processo de geração de documentos do lote de impressao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de documentos iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response gerarDocumentos(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario
			) {
			
		try {
			Result result = LoteImpressaoServices.iniciarGeracaoDocumentos(cdLoteImpressao, cdUsuario);
			return ResponseFactory.ok(result);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	//***Processo de geração do lote novo uso para verificar as remessas
	@GET
	@Path("/{id}/gerarlote")
	@ApiOperation(
			value = "Inicia o processo de geração de documentos do lote de impressao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de documentos iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response gerarLote(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao,
			@ApiParam(value = "Usuário que cadastrou a penalidade") @QueryParam("cdUsuario") int cdUsuario
			) {
			
		try {
			
			Result result = LoteImpressaoServices.iniciarGeracaoLote(cdLoteImpressao, cdUsuario);
			
			return ResponseFactory.ok(result);
			
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	//Imprimir pdf do lote
	@GET
	@Path("/{id}/imprimir")
	@ApiOperation(
			value = "Inicia o processo de impressão de documentos do lote"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Impressão de documentos iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response imprimirDocumentos(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
			
		try {
			
			return ResponseFactory.ok(LoteImpressaoServices.iniciarImpressaoDocumentos(cdLoteImpressao));
			
			//return ResponseFactory.ok(result);
		} catch (Exception e) {
			System.out.println("Caiu no erro dentro do controle");
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	//====================
	

	@GET
	@Path("/{id}/ecarta")
	@ApiOperation(
			value = "Inicia o processo de envio para eCartas"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Geração de documentos iniciada", response = Result.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response enviarECarta
	(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao
	)
	{
		try 
		{
			Result result = LoteImpressaoServices.enviarECarta(cdLoteImpressao);
			
			return ResponseFactory.ok(result);
		} 
		catch (Exception e) 
		{
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{id}/status")
	@ApiOperation(
			value = "Retorna o status de geração de documentos do lote de impressao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Status de geração de documentos do lote de impressão", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveStatus(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
			
		try {
			
			LoteImpressaoStatus status = LoteImpressaoServices.getStatusGeracaoDocumentos(cdLoteImpressao);
			
			return ResponseFactory.ok(status);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{id}/statusdocumento")
	@ApiOperation(
			value = "Retorna o status de geração de documentos do lote de impressao"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Status de geração de documentos do lote de impressão", response = LoteImpressaoStatus.class),
			@ApiResponse(code = 204, message = "Lotes de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveStatusDocumetnos(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
			
		try {
			LoteImpressaoStatus status = LoteImpressaoServices.getStatusGeracaoLote(cdLoteImpressao);
			
			return ResponseFactory.ok(status);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}


//	@POST
//	@Path("/{id}/aits")
//	@ApiOperation(
//			value = "Vincula aits ao lote de impressão"
//		)
//	@ApiResponses(value = {
//			@ApiResponse(code = 200, message = "Aits vinculados"),
//			@ApiResponse(code = 400, message = "Aits não puderam ser vinculados"),
//			@ApiResponse(code = 500, message = "Erro durante o processamento da requisicao.")
//		})
//	@Consumes(MediaType.APPLICATION_JSON)
//	public static Response atatchAits(
//			@ApiParam(value = "Id do Lote de Impressão", required = true) @PathParam("id") int cdLoteImpressao,
//			@ApiParam(value = "Aits a serem vinculadas", required = true) List<LoteImpressaoAit> aits) {
//		try {
//			
//			Result r = LoteImpressaoServices.vincularAits(aits);
//			if(r.getCode() < 0) {
//				return ResponseFactory.badRequest("Aits não puderam ser vinculadas", r.getMessage());	
//			}
//			
//			return ResponseFactory.ok(r);
//		} catch(Exception e) {
//			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getMessage());
//		}
//	}
}
