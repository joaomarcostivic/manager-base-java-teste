package com.tivic.manager.triagem.services.eventos;

import java.util.List;

import com.tivic.manager.triagem.dtos.EventoTriagemDTO;
import com.tivic.manager.triagem.dtos.NotificacaoEstacionamentoDigitalDTO;
import com.tivic.manager.triagem.dtos.ProcessamentoEventoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface IEventoEstacionamentoService {
	void salvar(List<NotificacaoEstacionamentoDigitalDTO> notificacaoList) throws Exception;
	public List<EventoTriagemDTO> find(SearchCriterios searchCriterios) throws Exception;
	public List<EventoTriagemDTO> find(SearchCriterios searchCriterios, CustomConnection customConnection) throws Exception;
	public EventoTriagemDTO get(int cdEvento, int cdUsuario) throws Exception;
	public EventoTriagemDTO get(int cdEvento, int cdUsuario, CustomConnection customConnection) throws Exception;
	public void confirmar(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception;
	public void confirmar(ProcessamentoEventoDTO processamentoEventoDTO, CustomConnection customConnection) throws Exception;
	public void rejeitar(ProcessamentoEventoDTO processamentoEventoDTO) throws Exception;
	public void rejeitar(ProcessamentoEventoDTO processamentoEventoDTO, CustomConnection customConnection) throws Exception;
	public NotificacaoEstacionamentoDigitalDTO getNotificacaoByEvento(int cdEvento) throws Exception;
	public NotificacaoEstacionamentoDigitalDTO getNotificacaoByEvento(int cdEvento, CustomConnection customConnection) throws Exception;
}
