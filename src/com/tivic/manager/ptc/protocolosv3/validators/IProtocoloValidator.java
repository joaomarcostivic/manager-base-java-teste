package com.tivic.manager.ptc.protocolosv3.validators;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IProtocoloValidator {
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException;
}
