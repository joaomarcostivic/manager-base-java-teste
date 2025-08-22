package com.tivic.manager.mob.processamento.conversao;

import java.util.List;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.mob.processamento.ProcessamentoResultDTO;
import com.tivic.manager.mob.processamento.ProcessamentoStatusDTO;
import com.tivic.manager.mob.processamento.ProcessamentoStatusBuilder;
import com.tivic.manager.mob.processamento.conversao.dtos.GrupoEventoDTO;
import com.tivic.manager.mob.processamento.conversao.services.IConversorEventoService;
import com.tivic.manager.mob.processamento.sincronizacao.builders.PaginatorBuilder;
import com.tivic.manager.mob.talonario.ITalonarioService;
import com.tivic.manager.util.pagination.PagedResponse;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.concurrent.AsyncListProcessor;

public class ConversaoEventoService implements IConversaoEventoService {

	private ManagerLog managerLog;
	private IOrgaoService orgaoService;
	private ITalonarioService talonarioService;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private IConversorEventoService conversorEventoService;
	
	private AsyncListProcessor<EventoEquipamento> listProcessor = new AsyncListProcessor<>();
	private ProcessamentoResultDTO conversaoResult = new ProcessamentoResultDTO();
	
	public ConversaoEventoService() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		this.conversorEventoService = (IConversorEventoService) BeansFactory.get(IConversorEventoService.class);
	}
	
	@Override
	public void converter(SseEventSink eventSink, Sse sse, int cdUsuario) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			converter(eventSink, sse, cdUsuario, customConnection);
			customConnection.finishConnection();
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.error("Sincronização de eventos", e.getMessage());
			throw new Exception(e);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public void converter(SseEventSink eventSink, Sse sse, int cdUsuario, CustomConnection customConnection) throws Exception {
		Orgao orgao = orgaoService.getOrgaoUnico();
		if(orgao.getCdAgenteResponsavel() <= 0) {
			throw new Exception("Para que a conversão seja realizada, o órgão precisa ter um agente responsável.");
		}
		
		boolean hasTaloes = !talonarioService.getDisponiveisByCdAgente(orgao.getCdAgenteResponsavel(), customConnection).isEmpty();
		if(!hasTaloes) {
			throw new Exception("Não há talões disponíveis para este agente.");
		}
		
		if(!listProcessor.isProcessing()) {
			conversaoResult.startProcessamento();
		}
		
		List<EventoEquipamento> eventos = this.eventoEquipamentoRepository.getNaoEmitidos(new SearchCriterios(), customConnection).getList(EventoEquipamento.class);
		listProcessor
			.subscribe(eventSink, sse)
			.process(eventos, (evento) -> {
				try {
					conversorEventoService.convert(evento, orgao, cdUsuario, customConnection);
				} catch (Exception e) {
					managerLog.showLog(e);
					conversaoResult.addEventoErro(evento.getCdEvento(), e.getMessage());
					throw new Exception(e);
				}
			});
		
		conversaoResult.endProcessamento(eventos.size());
	}
	
	@Override
	public ProcessamentoStatusDTO getStatus() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			ProcessamentoStatusDTO syncStatusDTO = getStatus(customConnection);
			customConnection.finishConnection();
			return syncStatusDTO;
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception(e.getMessage());
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public ProcessamentoStatusDTO getStatus(CustomConnection customConnection) throws Exception {
		List<EventoEquipamento> eventos = this.eventoEquipamentoRepository.getNaoEmitidos(new SearchCriterios(), customConnection).getList(EventoEquipamento.class);
	    
	    ProcessamentoStatusBuilder processamentoStatusBuilder = new ProcessamentoStatusBuilder()
	            .setIsProcessing(listProcessor.isProcessing())
	            .setNrPendentesProcessamento(eventos.size());
	    
	    if(!listProcessor.isProcessing()) {
	    	processamentoStatusBuilder.setProcessamentoResult(conversaoResult);	    	
	    }
	            
	    return processamentoStatusBuilder.build();
	}
	
	@Override
	public PagedResponse<GrupoEventoDTO> getNaoConvertidosAgrupados(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PagedResponse<GrupoEventoDTO> grupos = getNaoConvertidosAgrupados(searchCriterios, customConnection);
			customConnection.finishConnection();
			return grupos;
		} catch(Exception e) {
			managerLog.showLog(e);
			throw new Exception("Erro ao obter os eventos.");
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<GrupoEventoDTO> getNaoConvertidosAgrupados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<GrupoEventoDTO> grupoEventoSearch = this.eventoEquipamentoRepository.getNaoEmitidosAgrupados(searchCriterios, customConnection);
		
		List<GrupoEventoDTO> grupos = grupoEventoSearch.getList(GrupoEventoDTO.class);
		int total = grupoEventoSearch.getRsm().getTotal();
		
		return new PaginatorBuilder<GrupoEventoDTO>(grupos, total).build();
	}
	
	@Override
	public PagedResponse<EventoEquipamento> getNaoConvertidos(SearchCriterios searchCriterios) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			PagedResponse<EventoEquipamento> eventos = getNaoConvertidos(searchCriterios, customConnection);
			customConnection.finishConnection();
			return eventos;
		} catch(Exception e) {
			managerLog.showLog(e);
			throw new Exception("Erro ao obter os eventos.");
		} finally {
			customConnection.closeConnection();
		}
	}

	@Override
	public PagedResponse<EventoEquipamento> getNaoConvertidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception {
		Search<EventoEquipamento> eventoSearch = this.eventoEquipamentoRepository.getNaoEmitidos(searchCriterios, customConnection);
		
		List<EventoEquipamento> eventos = eventoSearch.getList(EventoEquipamento.class);
		int total = eventoSearch.getRsm().getTotal();
		
		return new PaginatorBuilder<EventoEquipamento>(eventos, total).build();
	}
}
