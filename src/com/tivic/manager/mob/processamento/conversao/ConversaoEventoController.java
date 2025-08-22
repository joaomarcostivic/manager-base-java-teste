package com.tivic.manager.mob.processamento.conversao;

import java.sql.Types;
import java.util.GregorianCalendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.processamento.conversao.dtos.GrupoEventoDTO;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;
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

@Api(value = "Sincronização de eventos e Transformação de eventos em AITs", tags = {"mob"}, authorizations = { 
		@Authorization(value = "Bearer Auth", scopes = {
				@AuthorizationScope(scope = "Bearer", description = "JWT token") 
		}) 
})

@Path("/v3/conversao-eventos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class ConversaoEventoController {
	
	private ManagerLog managerLog;
	private IConversaoEventoService sincronizacaoService;
	
	public ConversaoEventoController() throws Exception {
		this.sincronizacaoService = (IConversaoEventoService) BeansFactory.get(IConversaoEventoService.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	@GET
	@Path("/converter")
	@ApiOperation(value = "Converte eventos em AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos convertidos com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Status da conversão de eventos não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	@Produces(MediaType.SERVER_SENT_EVENTS)
	public void converter(
			@Context SseEventSink sseEventSink, 
			@Context Sse sse,
			@ApiParam(value = "Código do usuário") @QueryParam("cdUsuario") int cdUsuario
			) throws Exception {
		try {
			sincronizacaoService.converter(sseEventSink, sse, cdUsuario);
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception(e.getMessage());
		}
	}
	
	@GET
	@Path("/status")
	@ApiOperation(value = "Converte eventos em AIT")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos convertidos com sucesso.", response = ResponseBody.class),
			@ApiResponse(code = 204, message = "Status da conversão de eventos não encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getStatus() {
		try {
			return ResponseFactory.ok(sincronizacaoService.getStatus());
		} catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/nao-convertidos")
	@ApiOperation(value = "Retorna eventos não emitidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados", response = GrupoEventoDTO[].class),
			@ApiResponse(code = 204, message = "Eventos não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getNaoConvertidosAgrupados(
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int nrLimite,
			@ApiParam(value = "Número da página") @QueryParam("page") int nrPagina
			) {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			
			PagedResponse<GrupoEventoDTO> grupos = sincronizacaoService.getNaoConvertidosAgrupados(searchCriterios);
			return ResponseFactory.ok(grupos);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("/nao-convertidos/{dtGrupoEvento}")
	@ApiOperation(value = "Retorna eventos não emitidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados", response = EventoEquipamento[].class),
			@ApiResponse(code = 204, message = "Eventos não encontrados", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response getNaoConvertidos(
			@ApiParam(value = "Data dos eventos") @PathParam("dtGrupoEvento") Long timestampGrupoEvento,
			@ApiParam(value = "Quantidade máxima de registros", defaultValue = "10") @DefaultValue("10") @QueryParam("limit") int nrLimite,
			@ApiParam(value = "Número da página") @QueryParam("page") int nrPagina
			) {
		try {
			GregorianCalendar dtGrupoEvento = new GregorianCalendar();
			dtGrupoEvento.setTimeInMillis(timestampGrupoEvento);
			
			
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriterios("dt_conclusao", Util.formatDate(dtGrupoEvento, "yyyy-MM-dd") + " 00:00:00", ItemComparator.GREATER_EQUAL, Types.VARCHAR);
			searchCriterios.addCriterios("dt_conclusao", Util.formatDate(dtGrupoEvento, "yyyy-MM-dd") + " 23:59:59", ItemComparator.MINOR_EQUAL, Types.VARCHAR);
			searchCriterios.setQtDeslocamento(((nrLimite * nrPagina) - nrLimite));
			searchCriterios.setQtLimite(nrLimite);
			
			
			PagedResponse<EventoEquipamento> grupos = sincronizacaoService.getNaoConvertidos(searchCriterios);
			return ResponseFactory.ok(grupos);
		} catch (Exception e) {
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
