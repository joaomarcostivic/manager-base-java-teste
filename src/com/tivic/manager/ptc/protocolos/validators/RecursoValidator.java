package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public interface RecursoValidator {
	
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception;

}
