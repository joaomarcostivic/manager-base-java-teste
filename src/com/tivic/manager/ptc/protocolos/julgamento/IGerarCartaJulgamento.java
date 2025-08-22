package com.tivic.manager.ptc.protocolos.julgamento;


public interface IGerarCartaJulgamento {
	public byte[] gerarCartaProtocolo(int cdAit, int cdDocumento, int tpStatus) throws Exception;
}
