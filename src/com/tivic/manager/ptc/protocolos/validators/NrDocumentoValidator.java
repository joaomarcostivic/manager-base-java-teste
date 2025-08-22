package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class NrDocumentoValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		if(obj.getNrDocumento() == null && !obj.getNrDocumento().trim().equals("")) {
			throw new ValidationException("O número do documento deve ser preenchido.");
		}
		
	}

}
