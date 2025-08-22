package com.tivic.manager.ptc.protocolos;

import com.tivic.sol.connection.CustomConnection;
import javax.ws.rs.BadRequestException;

public interface IProtocoloRecursoServices {
	public DadosProtocoloDTO insertProtocolo (DadosProtocoloDTO protocolo) throws BadRequestException, Exception;
	public DadosProtocoloDTO insertProtocolo (DadosProtocoloDTO protocolo, CustomConnection connection) throws BadRequestException, Exception;
}
