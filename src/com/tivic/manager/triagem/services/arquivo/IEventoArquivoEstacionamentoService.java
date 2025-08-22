package com.tivic.manager.triagem.services.arquivo;

import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.mob.EventoArquivo;
import com.tivic.sol.connection.CustomConnection;

public interface IEventoArquivoEstacionamentoService {
	public List<Arquivo> getArquivosByEvento(int cdEvento) throws Exception;
	public List<Arquivo> getArquivosByEvento(int cdEvento, CustomConnection customConnection) throws Exception;
	public List<EventoArquivo> getEventoArquivosByEvento(int cdEvento) throws Exception;
	public List<EventoArquivo> getEventoArquivosByEvento(int cdEvento, CustomConnection customConnection) throws Exception;
	public void update(EventoArquivo eventoArquivos) throws Exception;
	public void update(EventoArquivo eventoArquivos, CustomConnection customConnection) throws Exception;
}
