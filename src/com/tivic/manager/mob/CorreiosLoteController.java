package com.tivic.manager.mob;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
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

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.pagination.PagedResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;



@Path("/v2/mob/correioslote/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CorreiosLoteController {
	
		
	public CorreiosLoteController() {
	
	}

	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra um novo lote dos Correios."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote Registrado", response = CorreiosLote.class),
			@ApiResponse(code = 400, message = "Lote Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response create(CorreiosLote correiosLote) {
		try {
			Result result = CorreiosLoteServices.salvarLote(correiosLote, null, null);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			return ResponseFactory.ok(result.getObjects().get("CORREIOSLOTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/{CorreiosLoteId}")
	@ApiOperation(
			value = "Atualiza um lote dos Correios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lote atualizado", response = CorreiosLote.class),
			@ApiResponse(code = 400, message = "Lote Inválido", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(CorreiosLote correiosLote){
		try {
			Result result = CorreiosLoteServices.salvarLote(correiosLote, null, null);

			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			return ResponseFactory.ok(result.getObjects().get("CORREIOSLOTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}


		
	@GET
	@Path("/")
	@ApiOperation(value = "Busca de lotes do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lotes encontrados", response = CorreiosLoteDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findLote(
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite,
			@ApiParam(value = "Sigla do Lote") @QueryParam("sgLote") String sgLote,
			@ApiParam(value = "Número Inicial") @QueryParam("nrInicial") int nrInicial,
			@ApiParam(value = "Número Final") @QueryParam("nrFinal") int nrFinal,
			@ApiParam(value = "Tipo do lote", allowableValues = "0, 1" ) @QueryParam("tpLote") @DefaultValue("-1") int tpLote,
			@ApiParam(value = "Situação do lote", allowableValues = "0, 1, 2") @QueryParam("stLote") @DefaultValue("-1") int stLote,
			@ApiParam(value = "Data de envio do lote") @QueryParam("dtLote") String dtLote,
			@ApiParam(value = "Data de validade do lote") @QueryParam("dtVencimento") String dtVencimento,
			@ApiParam(value = "Observação de cancelamento") @QueryParam("dsObservacao") String dsObservacao){
		
		try {	
			Criterios crt = new Criterios();
			
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}
			
			if(sgLote != null) {
				crt.add("A.sg_lote", sgLote, ItemComparator.EQUAL, Types.VARCHAR);
			}

			if(nrInicial > 0) {
				crt.add("A.nr_inicial", Integer.toString(nrInicial), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrFinal > 0) {
				crt.add("A.nr_final", Integer.toString(nrFinal), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(tpLote > -1) {
				crt.add("A.tp_lote", Integer.toString(tpLote), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(stLote > -1) {
				crt.add("A.st_lote", Integer.toString(stLote), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(dtLote != null) {
				crt.add("A.dt_lote", dtLote + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.DATE);
				crt.add("A.dt_lote", dtLote + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			if(dtVencimento != null){
				crt.add("A.dt_vencimento", dtVencimento + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.DATE);
				crt.add("A.dt_vencimento", dtVencimento + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.DATE);
			}
			
			ResultSetMap correiosLote2 = CorreiosLoteServices.getLoteEtiquetasLivres(crt);
			
			List<CorreiosLoteDTO> correiosLotesDto = new ResultSetMapper<CorreiosLoteDTO>(correiosLote2, CorreiosLoteDTO.class).toList();
			CorreiosLoteServices.verificarVencimento(correiosLotesDto);
			CorreiosLoteServices.verificarConclusao(correiosLotesDto);

			return ResponseFactory.ok(new PagedResponse<CorreiosLoteDTO>(correiosLotesDto, correiosLote2.getTotal()));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	

	
	@GET
	@Path("/{loteId}")
	@ApiOperation(value = "Busca de lotes do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Lotes encontrados", response = CorreiosLoteDTO.class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response get(
			@ApiParam(value = "Código do lote", required = true) @PathParam("loteId") int cdLote
			) {
		try {
			CorreiosLote correiosLote = CorreiosLoteDAO.get(cdLote);
			CorreiosLoteDTO correiosLoteDTO = new CorreiosLoteDTO.Builder(correiosLote).getEtiquetasLivres().build(); 

			if(correiosLote == null) {
				return ResponseFactory.noContent("Nenhum lote encontrado");
			}
			
			return ResponseFactory.ok(correiosLoteDTO);
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	

	@PUT
	@Path("/cancelar/{loteId}")
	
	public String cancel(CorreiosLote correiosLote){
		
		try {	
			Result result = CorreiosLoteServices.cancelarLote(correiosLote);
			
			return new JSONObject(result).toString();
			//return "funcionou";
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	
	
	
	@DELETE
	@Path("/{loteId}")
	
	public String remove(CorreiosLote correiosLote){
		try {
			Result result = CorreiosLoteServices.remove(correiosLote);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}


}
