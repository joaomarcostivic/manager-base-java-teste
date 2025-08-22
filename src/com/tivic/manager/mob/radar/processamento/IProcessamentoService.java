package com.tivic.manager.mob.radar.processamento;

import com.tivic.sol.connection.CustomConnection;

public interface IProcessamentoService {
	public ProcessamentoEventosDTO processarEventos() throws Exception;
	public ProcessamentoEventosDTO processarEventos(CustomConnection customConnectionRadar) throws Exception;

}