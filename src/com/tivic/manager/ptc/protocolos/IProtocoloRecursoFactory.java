package com.tivic.manager.ptc.protocolos;

public interface IProtocoloRecursoFactory {
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception;
}
