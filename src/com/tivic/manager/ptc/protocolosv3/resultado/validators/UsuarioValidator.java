package com.tivic.manager.ptc.protocolosv3.resultado.validators;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.sol.connection.CustomConnection;

public class UsuarioValidator implements IValidator<ResultadoDTO> {

	@Override
	public void validate(ResultadoDTO resultado, CustomConnection connection) throws ValidationException, Exception {
		
		if(resultado.getCdUsuario() <= 0 || resultado.getCdUsuario() == null)
			throw new BadRequestException("Código de Usuário Inválido.");

	}

}
