package com.tivic.manager.ptc.protocolosv3.validators.documento;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.sol.connection.CustomConnection;

public class AitValidator implements IProtocoloValidator {
	
	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception {
		if(protocolo.getAit() == null) {
			throw new ValidationException("AIT inválido.");
		}
	}

}
