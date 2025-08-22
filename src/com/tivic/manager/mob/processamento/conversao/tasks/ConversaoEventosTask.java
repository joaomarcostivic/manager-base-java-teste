package com.tivic.manager.mob.processamento.conversao.tasks;

import java.util.List;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.mob.processamento.conversao.services.IConversorEventoService;
import com.tivic.manager.mob.talonario.ITalonarioService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;
import com.tivic.sol.search.SearchCriterios;

public class ConversaoEventosTask implements IConversaoEventosTask {
	private ManagerLog managerLog;
	private IConversorEventoService conversorEventoHandlerService;
	private IParametroRepository parametroRepository;
	private IOrgaoService orgaoService;
	private ITalonarioService talonarioService;
	private EventoEquipamentoRepository eventoEquipamentoRepository;
	
	public ConversaoEventosTask() throws Exception {
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.conversorEventoHandlerService = (IConversorEventoService) BeansFactory.get(IConversorEventoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
	}
	
	public void converter() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			converter(customConnection);
			customConnection.finishConnection();
		} catch (Exception e) {
			managerLog.showLog(e);
			managerLog.info("A task de conversão de eventos falhou", e.getMessage());	
		} finally {
			managerLog.info("Task finalizada", "A task de conversão de eventos foi finalizada");	
			customConnection.closeConnection();
		}
	}
	
	private void converter(CustomConnection customConnection) throws Exception {
		managerLog.info("Task de conversão de eventos", "Iniciando task de conversão de eventos em AIT");
		Orgao orgao = orgaoService.getOrgaoUnico();
		boolean hasTaloes = !talonarioService.getDisponiveisByCdAgente(orgao.getCdAgenteResponsavel(), customConnection).isEmpty();
		if(!hasTaloes) {
			throw new Exception("Não há talões disponíveis para este agente.");
		}
		List<EventoEquipamento> eventos = eventoEquipamentoRepository.getNaoEmitidos(new SearchCriterios(), customConnection).getList(EventoEquipamento.class);
		for(EventoEquipamento evento : eventos) {
			try {
				conversorEventoHandlerService.convert(evento, orgao, parametroRepository.getValorOfParametroAsInt("MOB_USER_TIVIC"), customConnection);
			} catch (Exception e) {
				managerLog.showLog(e);
				managerLog.info("Evento: " + evento.getCdEvento(), e.getMessage());
				continue;
			}
		}
	}
}
