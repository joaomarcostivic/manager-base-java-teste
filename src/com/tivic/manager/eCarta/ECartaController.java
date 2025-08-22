package com.tivic.manager.eCarta;

import java.io.IOException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
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
import org.json.JSONObject;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.mob.AitPagamentoServices;
import com.tivic.manager.mob.ArquivoBancoDTO;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.comunicacao.ftp.ConfigFTP;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;
import sun.security.util.PropertyExpander.ExpandException;

@Path("/v2/mob/loteecartas/")
public class ECartaController {
	
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
			
			ResultSetMap rsm = ECartaServices.find(criterios);

			
			return ResponseFactory.ok(rsm);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("static-access")
	@GET
	@Path("/{id}/ecarta")
	@ApiOperation(value = "Comunicação E-Carta")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response eCartaEnvio
	(
			@ApiParam(value = "Código do Lote de Impressão") @PathParam("id") int cdLoteImpressao
	) {
		try 
		{

			ECartaServices eCartaServices = new ECartaServices();
			LoteImpressaoServices loteImpressaoServices = new LoteImpressaoServices();
			List<ECartaItem> listAits = loteImpressaoServices.listarArquivosLote(cdLoteImpressao);			
			HashMap<String, Object> lote = eCartaServices.generateLot(listAits);
			return Response.ok((byte[])lote.get("blob"), MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment;filename=\"" + String.valueOf(lote.get("name"))+"\"")
					.header("Access-Control-Expose-Headers", "Content-Disposition")
					.build();
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro Ao enviar arquivo de resposta");
		}
	}
	
	//RespostaFTP
	@SuppressWarnings("static-access")
	@POST
	@Path("/response/ftp")
	@ApiOperation(value = "Comunicação E-Carta resposta do cliente (RN)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Result eCartaResposeFTP
	(
		@ApiParam(value = "Confirmação ou rejeição da produção do lote", required = true) ECartaResponse response,
		@ApiParam(value = "Código do Lote de Impressão") @QueryParam("tpEnvio") int tpEnvio
	) 
	{
		try {
			Result r = new Result(-1);
			ECartaServices.responseLot(response, tpEnvio);
			
			r.setCode(1);
			r.setMessage("Resposta Enviada.");
			return r;

		} catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	//RespostaEDI
	@SuppressWarnings("static-access")
	@POST
	@Path("/response/edi")
	@ApiOperation(value = "Comunicação E-Carta resposta do cliente (RN)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response eCartaResposeEDI
	(
		@ApiParam(value = "Confirmação ou rejeição da produção do lote", required = true) ECartaResponse response,
		@ApiParam(value = "Código do Lote de Impressão") @QueryParam("tpEnvio") int tpEnvio
	) 
	{
		try 
		{		
			return ResponseFactory.ok(ECartaServices.responseLot(response, tpEnvio));
		} catch (Exception e) 
		{
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
	
	@POST
	@Path("/upload")
	@ApiOperation(
			value = "Importa o arquivo de resposta eCrtas para leitura"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resposta registradas"),
			@ApiResponse(code = 400, message = "Há algum parâmetro inválido"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	@Consumes(MediaType.APPLICATION_JSON)
	public static Response upload(@ApiParam(value = "Arquivo de resposta") ArquivoRespostaECarta arquivoRespostaECarta)
	{
		try 
		{
			
			if (arquivoRespostaECarta.getArquivoRecibo().equals("") || arquivoRespostaECarta.getArquivoInconsistencia().equals("")) 
			{
				return ResponseFactory.internalServerError("No Content");
			}
		
			return ResponseFactory.ok( ECartaServices.upload(arquivoRespostaECarta) );
			
		} 
		catch (ValidacaoException ve) 
		{
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/{nomeParametro}/ecarta-params")
	@ApiOperation(value = "Comunicação E-Carta")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Processamento não concluido."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response getParaneCarta
	(
			@ApiParam(value = "Nome do parametro para buscar o valor") @PathParam("nomeParametro") String nomeParametro
	) {
		try 
		{
			return ResponseFactory.ok(ParametroServices.getValorOfParametro(nomeParametro));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return ResponseFactory.internalServerError("Erro Ao enviar arquivo de resposta");
		}
	}
}


