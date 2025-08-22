package com.tivic.manager.mob.eventoequipamento;

import java.util.List;

import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.processamento.conversao.dtos.GrupoEventoDTO;
import com.tivic.manager.triagem.dtos.EventoTriagemDTO;
import com.tivic.manager.triagem.dtos.GrupoEventoParamsDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public interface EventoEquipamentoRepository {
	public EventoEquipamento insert(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception;
	public EventoEquipamento update(EventoEquipamento eventoEquipamento, CustomConnection customConnection) throws Exception;
	public EventoEquipamento get(int cdEvento) throws Exception;
	public EventoEquipamento get(int cdEvento, CustomConnection customConnection) throws Exception;
	public List<EventoEquipamento> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<GrupoEventoParamsDTO> getGruposDeEventos(CustomConnection customConnection) throws Exception;
	public List<EventoTriagemDTO> findTriagem(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<GrupoEventoDTO> getNaoEmitidosAgrupados(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public Search<EventoEquipamento> getNaoEmitidos(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public List<EventoEquipamento> getDisponiveisSincronizacao(String nmOrgaoAutuador, CustomConnection customConnection) throws Exception;
}
