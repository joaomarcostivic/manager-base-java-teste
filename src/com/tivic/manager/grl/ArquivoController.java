package com.tivic.manager.grl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.seg.Acao;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

@Path("/v2/grl/arquivos")
@Produces(MediaType.APPLICATION_JSON)
public class ArquivoController {

	@POST
	@Path("/find")
	@ApiOperation(
			value = "Retorna uma lista de arquivos filtrada"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivos econtrados", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhum arquivo encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {		
		try {

			ResultSetMap rsm = ArquivoServices.find(criterios);
			return rsm;			

		} catch (Exception e) {
			
			e.printStackTrace(System.out);
			return null;
			
		}
	}
	
	@POST
	@Path("/get")
	@ApiOperation(
			value = "Retorna uma lista de arquivos filtrada"
			)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivos econtrados", response = Acao.class),
			@ApiResponse(code = 400, message = "Nenhum arquivo encontrada", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static ResultSetMap get(ArrayList<ItemComparator> criterios) {		
		try {

			ResultSetMap rsm = ArquivoDAO.find(criterios);
			return rsm;			

		} catch (Exception e) {
			
			e.printStackTrace(System.out);
			return null;
			
		}
	}
	
	@GET
	@Path("/publicados")
	@ApiOperation(value = "Busca arquivos publicados")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Resultado"),
			@ApiResponse(code = 204, message = "Nenhum resultado"),
			@ApiResponse(code = 204, message = "Nao ha estatistica para os parametros fornecidos."),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response getArquivosPublicadoDO(
			@ApiParam(value = "Inicio do periodo (dd/mm/yyyy)", required = true) @QueryParam("dataPublicacaoInicial") String dataPublicacaoInicial,
			@ApiParam(value = "Fim do periodo (dd/mm/yyyy)", required = true) @QueryParam("dataPublicacaoFinal") String dataPublicacaoFinal,
			@ApiParam(value = "Tipo de documento", required = true) @QueryParam("tpDocumento") int tpDocumento) {
		
		try {
			GregorianCalendar dtInicial = Util.convStringToCalendar(dataPublicacaoInicial + " 00:00:00");
			GregorianCalendar dtFinal   = Util.convStringToCalendar(dataPublicacaoFinal + " 23:59:59");
			
			return ResponseFactory.ok(ArquivoServices.buscarArquivosPublicados(dtInicial, dtFinal, tpDocumento));
		} 
		catch (ValidacaoException ve) {
			System.out.println("Exception validator: " + ve.getMessage());
			return ResponseFactory.badRequest(ve.getMessage());
		} 
		catch (Exception e) {
			return ResponseFactory.internalServerError("Erro durante o processamento da requisicao.", e.getLocalizedMessage());
		}
	}
	
	@GET
	@Path("/{id}/download/arquivospublicados")
	@ApiOperation(
			value = "Gera arquivos de publicação"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Arquivo gerado"),
			@ApiResponse(code = 204, message = "Nao ha arquivo com o código indicado"),
			@ApiResponse(code = 400, message = "Dados invalidos"),
			@ApiResponse(code = 500, message = "Erro no servidor")
		})
	public static Response downloadDocumentoPublicacao(
			@ApiParam(value = "Código do documento") @PathParam("id") int cdArquivo
		) {
		try {		
			
			JSONObject response = new JSONObject();
			response.put("blob", ArquivoServices.pegarArquivoPublicado(cdArquivo));
			return ResponseFactory.ok(response);

		}
		catch (ValidacaoException e) {
			return ResponseFactory.badRequest(e.getMessage());
		}
		catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}

	}
	
}
