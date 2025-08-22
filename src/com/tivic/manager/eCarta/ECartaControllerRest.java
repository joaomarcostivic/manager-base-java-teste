package com.tivic.manager.eCarta;

import java.io.IOException;
import java.sql.Types;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

@Path("/mob/ecartasrest/")
public class ECartaControllerRest {

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
			
			ResultSetMap rsm = ECartaServices.find(criterios);

			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return null;
		}
	}
	
	//Resposta
	@SuppressWarnings("static-access")
	@POST
	@Path("/ecarta/response")
	@ApiOperation(value = "Comunicação E-Carta resposta do cliente (RN)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response eCartaRespose
	(
		@ApiParam(value = "Confirmação ou rejeição da produção do lote", required = true) ECartaResponse response
	) throws JSONException {
		try {
			Result status = null;			
			if(ECartaServices.responseLot(response, EcartaTipoDocumento.NAI) != null || ECartaServices.responseLot(response, EcartaTipoDocumento.NIP) != null)
			{
				WriteResponseServices writeResponse = new WriteResponseServices();
				status = writeResponse.WriteResponseTransit(response, null);
			}
			return ResponseFactory.ok(status);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro Ao enviar arquivo de resposta");
		}
	}
	
	//Cancelamento
	@GET
	@Path("/ecarta/{id}/cancelar")
	@ApiOperation(value = "Comunicação E-Carta")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response eCartaCacelamento
	(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao
	) {
		try {
			Result status = null;
			status = ECartaServices.cancelLote(String.valueOf(cdLoteImpressao));
			return ResponseFactory.ok(status);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
