package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class FaseValidator implements IValidator<DadosProtocoloDTO> {
	
	@Override
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connect) throws ValidationException {
		if(protocolo.getCdFase() <= 0)
			throw new ValidationException("A fase deve ser selecionada");
	}
	

}
