package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class ExistsValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		Documento protocolo = DocumentoServices.getByNrDocumento(obj.getNrDocumento(), connection.getConnection());
		if(protocolo != null) {
			throw new ValidationException("Já existe um protocolo com essa númeração.");
		}
		
	}

}
