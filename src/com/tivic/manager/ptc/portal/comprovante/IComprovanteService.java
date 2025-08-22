package com.tivic.manager.ptc.portal.comprovante;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.sol.connection.CustomConnection;

public interface IComprovanteService {
	byte[] imprimirComprovante(ProtocoloDTO protocolo, String referer) throws Exception;
	byte[] imprimirComprovante(ProtocoloDTO protocolo,  String referer, CustomConnection customConnection) throws Exception;
	byte[] imprimirComprovanteCartaoIdoso(String nrDocumento) throws Exception;
	byte[] imprimirComprovanteCartaoIdoso(String nrDocumento, CustomConnection customConnection) throws Exception;
}
