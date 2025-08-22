package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class AitValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		if(obj.getAit() == null) {
			throw new ValidationException("AIT inválido.");
		}
		
		if(obj.getAit().getNrControle() == null) {
			throw new ValidationException("AIT não registrado no DETRAN.");
		}
	}

}
