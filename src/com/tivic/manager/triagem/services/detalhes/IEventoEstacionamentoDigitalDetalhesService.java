package com.tivic.manager.triagem.services.detalhes;

import com.tivic.manager.triagem.entities.EventoEstacionamentoDigitalDetalhes;
import com.tivic.sol.connection.CustomConnection;

public interface IEventoEstacionamentoDigitalDetalhesService {
	public EventoEstacionamentoDigitalDetalhes getDetalhesByEvento(int cdEvento) throws Exception;
	public EventoEstacionamentoDigitalDetalhes getDetalhesByEvento(int cdEvento, CustomConnection customConnection) throws Exception;
	public void updateDetalhes(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes) throws Exception;
	public void updateDetalhes(EventoEstacionamentoDigitalDetalhes eventoEstacionamentoDigitalDetalhes, CustomConnection customConnection) throws Exception;
}
