package com.tivic.manager.triagem.controllers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.triagem.builders.EventoEstacionamentoDigitalSearchBuilder;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.manager.triagem.dtos.RetornoTriagemEventoEstacionamentoDTO;
import com.tivic.manager.triagem.exceptions.AitByEventoNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoEstacionamentoNotFoundException;
import com.tivic.manager.triagem.exceptions.EventoMotivoCancelamentoNotFoundException;
import com.tivic.manager.triagem.exceptions.ValidacaoSituacaoEventoEstacionamentoException;
import com.tivic.manager.triagem.services.eventos.IEventoEstacionamentoService;
import com.tivic.manager.triagem.usecase.CapturaEstacionamentoObservacaoUseCase;
import com.tivic.manager.triagem.usecase.ConfirmarEstacionamentoUseCase;
import com.tivic.manager.triagem.usecase.RejeitarEstacionamentoUseCase;
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
import sol.util.Result;

@Api(value = "triagem", tags = { "mob" }, authorizations = { @Authorization(value = "Bearer Auth", scopes = {
		@AuthorizationScope(scope = "Bearer", description = "JWT token") }) })
@Path("/v3/triagem/eventos/estacionamento")
@Produces(MediaType.APPLICATION_JSON)
public class EventoEstacionamentoDigitalController {
	
	private ManagerLog managerLog;
	private IEventoEstacionamentoService eventoEstacionamentoService;
	private ConfirmarEstacionamentoUseCase confirmarEstacionamentoUseCase;
	private RejeitarEstacionamentoUseCase rejeitarEstacionamentoUseCase;
	private CapturaEstacionamentoObservacaoUseCase capturaEstacionamentoObservacaoUseCase;

	public EventoEstacionamentoDigitalController() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		eventoEstacionamentoService = (IEventoEstacionamentoService) BeansFactory.get(IEventoEstacionamentoService.class);
		confirmarEstacionamentoUseCase = (ConfirmarEstacionamentoUseCase) BeansFactory.get(ConfirmarEstacionamentoUseCase.class);
		rejeitarEstacionamentoUseCase = (RejeitarEstacionamentoUseCase) BeansFactory.get(RejeitarEstacionamentoUseCase.class);
		capturaEstacionamentoObservacaoUseCase = (CapturaEstacionamentoObservacaoUseCase) BeansFactory.get(CapturaEstacionamentoObservacaoUseCase.class);
	}
	
	@GET
	@Path("")
	@ApiOperation(value = "Retorna a lista de eventos.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados com sucesso.", response = EventoEquipamento.class),
			@ApiResponse(code = 204, message = "Nenhum evento encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response find(
			@ApiParam(value = "Código do órgão") @QueryParam("idOrgao") String idOrgao,
			@ApiParam(value = "Situação dos eventos") @QueryParam("stEvento") int stEvento,
			@ApiParam(value = "Data dos eventos") @QueryParam("dtEvento") String dtEvento) {
		try {
			SearchCriterios searchCriterios = new EventoEstacionamentoDigitalSearchBuilder()
					.stEvento(stEvento)
					.dtConclusao(dtEvento)
					.build();
			return ResponseFactory.ok(eventoEstacionamentoService.find(searchCriterios));
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@GET
	@Path("evento")
	@ApiOperation(value = "Retorna o evento.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Evento encontrado com sucesso.", response = EventoEquipamento.class),
			@ApiResponse(code = 204, message = "Nenhum evento encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getEventos(
			@ApiParam(value = "Código do evento") @QueryParam("cdEvento") int cdEvento,
			@ApiParam(value = "Código do usuário") @QueryParam("cdUsuario") int cdUsuario) {
		try {
			return ResponseFactory.ok(eventoEstacionamentoService.get(cdEvento, cdUsuario));
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("")
	@ApiOperation(
			value = "Salva a lista de eventos de estacionamento digital"
		)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Lista de eventos salva", response = Result.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class)
		})
	public Response saveNotificacoesEstacionamento(List<NotificacaoEstacionamentoDigitalDTO> notificacaoEstacionamentoDigitalList) {	
		try {
			eventoEstacionamentoService.salvar(notificacaoEstacionamentoDigitalList);
			return ResponseFactory.created(notificacaoEstacionamentoDigitalList);
		}
		catch (Exception e) {
			managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
	
	@POST
	@Path("/confirmar")
	@ApiOperation(
	    value = "Confirma um evento e retorna DTO de AIT"
	)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Evento confirmado com sucesso", response = RetornoTriagemEventoEstacionamentoDTO.class),
	    @ApiResponse(code = 400, message = "Requisição inválida ou validação falhou", response = ResponseBody.class),
	    @ApiResponse(code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class)
	})
	public Response confirmar(@ApiParam(value = "Evento a ser confirmado") ProcessamentoEventoDTO processamentoEventoDTO) {
	    try {
	        return ResponseFactory.ok(confirmarEstacionamentoUseCase.execute(processamentoEventoDTO));
	    } catch (EventoEstacionamentoNotFoundException | AitByEventoNotFoundException | 
	    		ValidacaoSituacaoEventoEstacionamentoException e) {
	        return ResponseFactory.badRequest(e.getMessage());
	    } catch (Exception e) {
	        managerLog.showLog(e);
	        return ResponseFactory.internalServerError(e.getMessage());
	    }
	}

	@POST
	@Path("/rejeitar")
	@ApiOperation(
	    value = "Rejeita um evento e retorna DTO de AIT"
	)
	@ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Evento rejeitado com sucesso", response = RetornoTriagemEventoEstacionamentoDTO.class),
	    @ApiResponse(code = 400, message = "Requisição inválida ou validação falhou", response = ResponseBody.class),
	    @ApiResponse(code = 500, message = "Erro durante o processamento da requisição", response = ResponseBody.class)
	})
	public Response rejeitar(@ApiParam(value = "Evento a ser rejeitado") ProcessamentoEventoDTO processamentoEventoDTO) {
	    try {
	        return ResponseFactory.ok(rejeitarEstacionamentoUseCase.execute(processamentoEventoDTO));
	    } catch (EventoEstacionamentoNotFoundException | EventoMotivoCancelamentoNotFoundException |
	    		ValidacaoSituacaoEventoEstacionamentoException e) {
	        return ResponseFactory.badRequest(e.getMessage());
	    } catch (Exception e) {
	        managerLog.showLog(e);
	        return ResponseFactory.internalServerError(e.getMessage());
	    }
	}
	
	@GET
	@Path("/notificacao/{cdEvento}")
	@ApiOperation(value = "Retorna a lista de detalhes por evento.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Detalhes encontrados com sucesso.", response = EventoEquipamento.class),
			@ApiResponse(code = 204, message = "Nenhum detalhe encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response getNotificacaoByEvento(@ApiParam(value = "Código do Evento") @PathParam("cdEvento") int cdEvento) {
		try {
			return ResponseFactory.ok(eventoEstacionamentoService.getNotificacaoByEvento(cdEvento));
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}

	@GET
	@Path("/observacoes")
	@ApiOperation(value = "Retorna a lista observações de estacionamento.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Eventos encontrados com sucesso.", response = EventoEquipamento.class),
			@ApiResponse(code = 204, message = "Nenhum evento encontrado.", response = ResponseBody.class),
			@ApiResponse(code = 500, message = "Erro durante o processamento da requisição.", response = ResponseBody.class) })
	public Response findObservacoes() {
		try {
			return ResponseFactory.ok(capturaEstacionamentoObservacaoUseCase.execute());
		} catch (Exception e) {
			this.managerLog.showLog(e);
			return ResponseFactory.internalServerError(e.getMessage());
		}
	}
}
