package com.tivic.manager.ptc.protocolosv3.validators.documento;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class AitRegistradoDetranValidator implements IProtocoloValidator {

	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		if(protocolo.getAit().getNrControle() == null) {
			throw new ValidationException("AIT não registrado no DETRAN.");
		}
	}

}
