package com.tivic.manager.ptc.protocolosv3.parecer.relatorioparecer;

public interface IRelatorioParecerService {

	public byte[] gerarParecer(int cdParecer, int cdTipoDocumento) throws Exception;
	
}
