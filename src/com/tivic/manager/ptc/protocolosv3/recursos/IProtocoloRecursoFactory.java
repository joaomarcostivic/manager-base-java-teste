package com.tivic.manager.ptc.protocolosv3.recursos;

import com.tivic.manager.ptc.protocolosv3.recursos.IProtocoloRecursoServices;

public interface IProtocoloRecursoFactory {
	public IProtocoloRecursoServices gerarServico(int tpStatus) throws Exception;
}
