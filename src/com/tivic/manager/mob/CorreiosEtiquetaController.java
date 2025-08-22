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

import org.json.JSONObject;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.sol.response.ResponseBody;
import com.tivic.sol.response.ResponseFactory;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.manager.util.pagination.PagedResponse;

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




@Api(value = "Etiquetas", tags = {"mob"}, authorizations = {
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token")
		})
})
@Path("/v2/mob/correiosetiqueta/")
@Produces(MediaType.APPLICATION_JSON)
public class CorreiosEtiquetaController {
	private CorreiosEtiquetaServices correiosEtiquetaService; 
	
	public CorreiosEtiquetaController() throws Exception {
		correiosEtiquetaService = (CorreiosEtiquetaServices) BeansFactory.get(CorreiosEtiquetaServices.class);
	}

	@POST
	@Path("/")
	@ApiOperation(
			value = "Registra as etiquetas de um lote dos Correios."
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etiquetas Registrados", response = CorreiosEtiqueta.class),
			@ApiResponse(code = 400, message = "Etiquetas Inválidas", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
	})
	public static Response create(CorreiosEtiqueta correiosEtiqueta) {
		try {
			Result result = CorreiosEtiquetaServices.save(correiosEtiqueta);
			
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			return ResponseFactory.ok(result.getObjects().get("CORREIOSLOTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@PUT
	@Path("/{etiquetaid}")
	@ApiOperation(
			value = "Atualiza um etiqueta dos Correios"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Concessao atualizada", response = CorreiosEtiqueta.class),
			@ApiResponse(code = 400, message = "Concessao invalida", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response update(CorreiosEtiqueta correiosEtiqueta){
		try {
			Result result = CorreiosEtiquetaServices.save(correiosEtiqueta);
		
			if(result.getCode() < 0)
				return ResponseFactory.badRequest(result.getMessage());
			return ResponseFactory.ok(result.getObjects().get("CORREIOSLOTE"));
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}


	
	@GET
	@Path("")
	@ApiOperation(value = "Busca de etiquetas do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiquetaDTO[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findEtiqueta(
			@QueryParam("page") int nrPagina,
			@QueryParam("limit") int nrLimite,
			@ApiParam(value = "Codigo do Lote") @QueryParam("cdLote") int cdLote,
			@ApiParam(value = "Número da Etiqueta") @QueryParam("nrEtiqueta") int nrEtiqueta,
			@ApiParam(value = "Data de envio do lote") @QueryParam("dtEnvio") String dtEnvio,
			@ApiParam(value = "Sigla da etiqueta") @QueryParam("sgServico") String sgServico,
			@ApiParam(value = "Código da Ait vinculada") @QueryParam("cdAit") @DefaultValue("-1") int cdAit,
			@ApiParam(value = "Status") @QueryParam("tpStatus") @DefaultValue("-1") int tpStatus,
			@ApiParam(value = "Número do movimento") @QueryParam("nrMovimento") @DefaultValue("-1") int nrMovimento,
			@ApiParam(value = "Dígito verificador") @QueryParam("nrDigitoVerificador") @DefaultValue("-1") int nrDigitoVerificador)
			{
		
		try {	
			Criterios crt = new Criterios();
			
			if(nrPagina != 0) {
				crt.add("qtDeslocamento", Integer.toString((nrLimite * nrPagina) - nrLimite), ItemComparator.EQUAL, Types.INTEGER);	
			}
			
			if(nrLimite != 0) {
				crt.add("qtLimite", Integer.toString(nrLimite), ItemComparator.EQUAL, Types.INTEGER);				
			}

			if(cdLote > 0) {
				crt.add("cd_lote", Integer.toString(cdLote), ItemComparator.EQUAL, Types.INTEGER);
			}

			if(nrEtiqueta > 0) {
				crt.add("nr_etiqueta", Integer.toString(nrEtiqueta), ItemComparator.EQUAL, Types.INTEGER);
			}

			if(dtEnvio != null) {
				crt.add("dt_lote", dtEnvio.toString(), ItemComparator.EQUAL, Types.DATE);
			}
			
			if(sgServico != null) {
				crt.add("sg_servico", sgServico, ItemComparator.EQUAL, Types.VARCHAR);
			}
			
			if(cdAit > 0) {
				crt.add("cd_ait", Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			
			if(tpStatus > 0) {
				crt.add("tp_status", Integer.toString(tpStatus), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrMovimento > -1) {
				crt.add("nr_movimento", Integer.toString(nrMovimento), ItemComparator.EQUAL, Types.INTEGER);
			}
			
			if(nrDigitoVerificador > -1) {
				crt.add("nr_digito_verificador", Integer.toString(nrDigitoVerificador), ItemComparator.EQUAL, Types.INTEGER);
			}

			ResultSetMap correiosEtiqueta = CorreiosEtiquetaServices.getEtiquetas(crt);
			
			if(correiosEtiqueta == null || correiosEtiqueta.getLines().size() == 0) {
				return ResponseFactory.noContent("Nenhuma etiqueta encontrada");
			}
			
			List<CorreiosEtiquetaDTO> correiosEtiquetaDto = new ResultSetMapper<CorreiosEtiquetaDTO>(correiosEtiqueta, CorreiosEtiquetaDTO.class).toList();
			return ResponseFactory.ok(new PagedResponse<CorreiosEtiquetaDTO>(correiosEtiquetaDto, correiosEtiqueta.getTotal()));
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	
	@GET
	@Path("/etiquetaslivres/{cdLote}")
	@ApiOperation(value = "Busca de etiquetas do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = int[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findEtiquetasLivres(
			@ApiParam(value = "Código do lote", required = true) @PathParam("cdLote") int cdLote
			) {
		try {	
			
			int etiquetasLivres = CorreiosEtiquetaServices.etiquetasLivres(cdLote);
			
			if(etiquetasLivres == 0) {
				return ResponseFactory.noContent("Nenhuma etiqueta livre.");
			}
			JSONObject etiquetas = new JSONObject(etiquetasLivres).put("etiquetasLivres", etiquetasLivres);
			return ResponseFactory.ok(etiquetas);
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	
	@GET
	@Path("/{etiquetaId}")
	@ApiOperation(value = "Busca de etiquetas do Correios")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiqueta[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findAll() {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap correiosEtiqueta = CorreiosEtiquetaServices.find(crt);
			
			if(correiosEtiqueta == null || correiosEtiqueta.getLines().size() == 0) {
				return ResponseFactory.noContent("Nenhuma etiqueta encontrado");
			}
			
			return ResponseFactory.ok(correiosEtiqueta);
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

	@GET
	@Path("/lote/{loteId}")
	@ApiOperation(value = "Busca de etiquetas de um lote")
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiqueta[].class),
		@ApiResponse(code = 204, message = "Sem resultados", response = ResponseBody.class),
		@ApiResponse(code = 500, message = "Erro no servidor", response = ResponseBody.class)
	})
	public static Response findByLoteId() {
		try {	
			Criterios crt = new Criterios();
			ResultSetMap correiosEtiqueta = CorreiosEtiquetaServices.find(crt);
			
			if(correiosEtiqueta == null || correiosEtiqueta.getLines().size() == 0) {
				return ResponseFactory.noContent("Nenhum etiqueta encontrado");
			}
			
			return ResponseFactory.ok(correiosEtiqueta);
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@DELETE
	@Path("/{etiquetaId}")
	public String remove(CorreiosEtiqueta correiosEtiqueta){
		try {
			Result result = CorreiosEtiquetaServices.remove(correiosEtiqueta);
			return new JSONObject(result).toString();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@GET
	@Path("/etiquetas/{cdAit}")
	@ApiOperation(
			value = "Retorna etiquetas do AIT"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Etiquetas encontradas", response = CorreiosEtiqueta[].class),
			@ApiResponse(code = 204, message = "Etiquetas não encontrado", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response buscarEtiquetasAIT(
			@ApiParam(value = "Ait que buscaremos as etiquetas", required = true) @PathParam("cdAit") int cdAit
			) {	
		try {
			List<CorreiosEtiqueta> correiosEtiquetasList = correiosEtiquetaService.buscarEtiquetasMovimentos(cdAit);
			if(correiosEtiquetasList.isEmpty()) {
				return ResponseFactory.noContent("Nenhuma etiqueta encontrada");
			}
			return ResponseFactory.ok(correiosEtiquetasList);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	

}
