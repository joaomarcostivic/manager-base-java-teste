package com.tivic.manager.ptc.protocolosv3.print;

public interface IProtocoloPrintService {
	byte[] imprimirProtocolo(int cdAit, int cdDocumento) throws Exception;
}
