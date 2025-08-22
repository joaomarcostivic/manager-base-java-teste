package com.tivic.manager.mob;

import java.sql.Types;
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

import org.json.JSONObject;

//import org.codehaus.jettison.json.JSONObject;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoDAO;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.mob.grafica.LoteImpressaoStatus;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.Util;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/mob/loteimpressao/")
public class LoteImpressaoRest {
	
	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
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

			
			return ResponseFactory.ok(rsm);
			//return Util.rsmToJSON(rsm);
		} catch (Exception e) {
			return null;
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
	public static byte[] imprimirDocumentos(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
		try {
			byte[] testebyte = LoteImpressaoServices.iniciarImpressaoDocumentos(cdLoteImpressao);
			return testebyte;
		} catch (Exception e) {
			return null;
		}
	}
	//====================
	
	
	@GET
	@Path("/{id}/gerar")
	@Produces(MediaType.APPLICATION_JSON)
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
	
	@GET
	@Path("/{id}/status")
	@Produces(MediaType.APPLICATION_JSON)
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
	@Path("/search/nai")
	@Produces(MediaType.APPLICATION_JSON)
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
	
	@GET
	@Path("/search/nip")
	@Produces(MediaType.APPLICATION_JSON)
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
	
	@POST
	@Path("/wizard/nai")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(
			value = "Retorna AITs que possam emitir NAIS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNAI() {
		try 
		{
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNAI();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}

			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
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
		try 
		{
			Result result = LoteImpressaoServices.save(lote);
			
			if(result.getCode() < 0) {
				return ResponseFactory.badRequest(result.getMessage());
			}
			
			return ResponseFactory.created((LoteImpressao) result.getObjects().get("LOTEIMPRESSAO"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError("Erro no servidor", e.getMessage());
		}
	}
	
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
	
	@GET
	@Path("/wizard/nip")
	@ApiOperation(
			value = "Retorna AITs que possam emitir NIPS via Lotes de Impressão"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "AITs para Lote de Impressão encontrados", response = Object[].class),
			@ApiResponse(code = 204, message = "AITs para Lote de Impressão não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public static Response retrieveAllAitsNIP() {
			
		try {
			
			ResultSetMap rsm = LoteImpressaoServices.getAllAitsEmitirNIP();
			if(rsm==null || rsm.getLines().size()<=0) {
				return ResponseFactory.noContent("Nenhuma ait encontrada");
			}
			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	//Wizar NAIs Correções

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
	//*********
	
	//Wizar NIPs Correções

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
	//*********
	
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
	public static Response enviarECarta(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao) {
			
		try {
			Result result = LoteImpressaoServices.enviarECarta(cdLoteImpressao);
			
			return ResponseFactory.ok(result);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
