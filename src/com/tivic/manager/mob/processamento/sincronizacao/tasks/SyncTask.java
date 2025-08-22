package com.tivic.manager.mob.processamento.sincronizacao.tasks;

import java.util.List;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.processamento.sincronizacao.services.IEventoRadarHandlerService;
import com.tivic.manager.util.radar.BancoRadarConnection;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class SyncTask implements ISyncTask {
	private ManagerLog managerLog;
	private IEventoRadarHandlerService eventoRadarHandlerService;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private BancoRadarConnection bancoRadarConnection;
	
	public SyncTask() throws Exception {
		this.bancoRadarConnection = new BancoRadarConnection();
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.eventoRadarHandlerService = (IEventoRadarHandlerService) BeansFactory.get(IEventoRadarHandlerService.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
	}
	
	@Override
	public void sincronizar() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		CustomConnection customConnectionRadar = new CustomConnection();
		try {
			customConnection.initConnection(true);
			customConnectionRadar.initConnection(true, bancoRadarConnection.getConnection());
			sincronizar(customConnection, customConnectionRadar);
			customConnection.finishConnection();
			customConnectionRadar.finishConnection();
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.error("Sincronização de eventos", e.getMessage());
			throw new Exception(e);
		} finally {
			customConnection.closeConnection();
			customConnectionRadar.closeConnection();
		}
	}
	
	private void sincronizar(CustomConnection customConnection, CustomConnection customConnectionRadar) throws Exception {
		String nmOrgaoAutuador = bancoRadarConnection.getNmOrgaoAutuador();		
		List<EventoEquipamento> eventos = this.eventoEquipamentoRepository.getDisponiveisSincronizacao(nmOrgaoAutuador, customConnectionRadar);
		
		for(EventoEquipamento evento : eventos) {
			try {
				eventoRadarHandlerService.add(evento, customConnection, customConnectionRadar);
			} catch (Exception e) {
				managerLog.showLog(e);
				managerLog.info("Evento: " + evento.getCdEvento(), e.getMessage());
				continue;
			}
		}
	}
	

}
