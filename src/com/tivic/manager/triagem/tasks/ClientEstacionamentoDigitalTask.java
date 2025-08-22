package com.tivic.manager.triagem.tasks;

import java.util.List;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.services.eventos.IEventoEstacionamentoService;
import com.tivic.manager.triagem.webclients.estacionamento_digital.apis.EstacionamentoDigitalAPIFactory;
import com.tivic.manager.triagem.webclients.estacionamento_digital.apis.IEstacionamentoDigitalAPI;

public class ClientEstacionamentoDigitalTask {

	private IEstacionamentoDigitalAPI estacionamentoDigitalAPI;
	private IEventoEstacionamentoService estacionamentoService;
	
	public ClientEstacionamentoDigitalTask() throws Exception {
		estacionamentoDigitalAPI = new EstacionamentoDigitalAPIFactory().getStrategy();
	}
	
	public void registrarNotificacoes() throws Exception {
		List<NotificacaoEstacionamentoDigitalDTO> list = estacionamentoDigitalAPI.buscarNotificacoes();
		estacionamentoService.salvar(list);
	}
	
}
