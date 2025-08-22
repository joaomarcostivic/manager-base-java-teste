package com.tivic.manager.mob.eventoequipamento;

import java.util.List;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEventoEquipamentoService {

	public List<EventoEquipamento> find(SearchCriterios searchCriterios) throws Exception;
	public List<EventoEquipamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public EventoEquipamento get(int cdEvento) throws Exception;
	public EventoEquipamento get(int cdEvento, CustomConnection customConnection) throws Exception;
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento) throws Exception;
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception;
	public EventoEquipamento update(EventoEquipamento eventoEquipamento) throws Exception;
	public EventoEquipamento update(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception;

}
