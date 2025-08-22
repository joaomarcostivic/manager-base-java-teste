package com.tivic.manager.mob.processamento.sincronizacao.services;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.connection.CustomConnection;

public interface IEventoRadarHandlerService {
	public void add(EventoEquipamento eventoRadar) throws Exception;
	public void add(EventoEquipamento eventoRadar, CustomConnection customConnection, CustomConnection customConnectionRadar) throws Exception;
}
