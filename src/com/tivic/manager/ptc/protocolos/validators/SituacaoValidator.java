package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.crt.DocumentoServices;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class SituacaoValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		if(obj.getCdSituacaoDocumento() != DocumentoServices.ST_ARQUIVADO) {
			throw new ValidationException("Situação do documento inválida.");
		}
		
	}

}
