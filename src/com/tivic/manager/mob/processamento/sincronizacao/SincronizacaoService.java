package com.tivic.manager.mob.processamento.sincronizacao;

import java.util.List;

import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.processamento.ProcessamentoResultDTO;
import com.tivic.manager.mob.processamento.ProcessamentoStatusDTO;
import com.tivic.manager.mob.processamento.ProcessamentoStatusBuilder;
import com.tivic.manager.mob.processamento.sincronizacao.services.IEventoRadarHandlerService;
import com.tivic.manager.util.radar.BancoRadarConnection;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.concurrent.AsyncListProcessor;

public class SincronizacaoService implements ISincronizacaoService {
	
	private BancoRadarConnection bancoRadarConnection;
	private ManagerLog managerLog;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private IEventoRadarHandlerService eventoRadarHandlerService;
	private AsyncListProcessor<EventoEquipamento> listProcessor = new AsyncListProcessor<>();
	private ProcessamentoResultDTO syncResult = new ProcessamentoResultDTO();

	public SincronizacaoService() throws Exception {
		this.bancoRadarConnection = new BancoRadarConnection();
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		this.eventoRadarHandlerService = (IEventoRadarHandlerService) BeansFactory.get(IEventoRadarHandlerService.class);
	}

	@Override
	public void sincronizar(SseEventSink eventSink, Sse sse) throws Exception {
		CustomConnection customConnectionRadar = new CustomConnection();
		try {
			customConnectionRadar.initConnection(true, bancoRadarConnection.getConnection());
			sincronizar(eventSink, sse, customConnectionRadar);
			customConnectionRadar.finishConnection();
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.error("Sincronização de eventos", e.getMessage());
			throw new Exception(e);
		} finally {
			customConnectionRadar.closeConnection();
		}
	}
	
	@Override
	public void sincronizar(SseEventSink eventSink, Sse sse, CustomConnection customConnectionRadar) throws Exception {
		if(!listProcessor.isProcessing()) {
			syncResult.startProcessamento();
		}
		List<EventoEquipamento> eventos = getDisponiveisSincronizacao(bancoRadarConnection.getNmOrgaoAutuador(), customConnectionRadar);
		managerLog.info("Sincronização de eventos", "Sincronizando " + eventos.size() + " evento(s) encontrados no banco do radar.");
		
		listProcessor
			.subscribe(eventSink, sse)
			.process(eventos, (eventoRadar) -> {
				try {
					eventoRadarHandlerService.add(eventoRadar);
				} catch (Exception e) {
					managerLog.showLog(e);
					syncResult.addEventoErro(eventoRadar.getCdEvento(), e.getMessage());
					throw new Exception(e.getMessage());
				}
			});
		
		syncResult.endProcessamento(eventos.size());
	}
	
	@Override
	public ProcessamentoStatusDTO getStatus() throws Exception {
		CustomConnection customConnectionRadar = new CustomConnection();
		try {
			customConnectionRadar.initConnection(false, bancoRadarConnection.getConnection());
			ProcessamentoStatusDTO syncStatusDTO = getStatus(customConnectionRadar);
			customConnectionRadar.finishConnection();
			return syncStatusDTO;
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception(e.getMessage());
		} finally {
			customConnectionRadar.closeConnection();
		}
	}
	
	@Override
	public ProcessamentoStatusDTO getStatus(CustomConnection customConnectionRadar) throws Exception {
	    List<EventoEquipamento> eventos = getDisponiveisSincronizacao(bancoRadarConnection.getNmOrgaoAutuador(), customConnectionRadar);
	    
	    ProcessamentoStatusBuilder processamentoStatusBuilder = new ProcessamentoStatusBuilder()
	            .setIsProcessing(listProcessor.isProcessing())
	            .setNrPendentesProcessamento(eventos.size());
	    
	    if(!listProcessor.isProcessing()) {
	    	processamentoStatusBuilder.setProcessamentoResult(syncResult);	    	
	    }
	            
	    return processamentoStatusBuilder.build();
	}
	
	private List<EventoEquipamento> getDisponiveisSincronizacao(String nmOrgaoAutuador, CustomConnection customConnectionRadar) throws Exception {
		return this.eventoEquipamentoRepository.getDisponiveisSincronizacao(nmOrgaoAutuador, customConnectionRadar);
	}

}