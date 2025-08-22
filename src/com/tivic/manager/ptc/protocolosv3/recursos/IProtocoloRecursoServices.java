package com.tivic.manager.ptc.protocolosv3.recursos;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.sol.connection.CustomConnection;

public interface IProtocoloRecursoServices {
	public ProtocoloDTO insertProtocolo (ProtocoloDTO protocolo) throws BadRequestException, Exception;
	public ProtocoloDTO insertProtocolo (ProtocoloDTO protocolo, CustomConnection connection) throws BadRequestException, Exception;

}
