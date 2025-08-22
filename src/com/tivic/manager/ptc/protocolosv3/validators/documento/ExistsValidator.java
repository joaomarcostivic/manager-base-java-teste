package com.tivic.manager.ptc.protocolosv3.validators.documento;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public class ExistsValidator implements IProtocoloValidator {

	@Override
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception, ValidacaoException {
		Documento documento = DocumentoServices.getByNrDocumento(protocolo.getDocumento().getNrDocumento(), connection.getConnection());
		if(documento != null) {
			throw new ValidationException("Já existe um protocolo com essa númeração.");
		}
	}

}
