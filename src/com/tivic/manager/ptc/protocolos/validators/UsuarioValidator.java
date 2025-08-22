package com.tivic.manager.ptc.protocolos.validators;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class UsuarioValidator implements IValidator<DadosProtocoloDTO>{

	@Override
	public void validate(DadosProtocoloDTO obj, CustomConnection connection) throws ValidationException, Exception {
		if(obj.getCdUsuario() <= 0) {
			throw new ValidationException("É necessário indicar o usuário que registra o documento.");
		}
		
	}

}
