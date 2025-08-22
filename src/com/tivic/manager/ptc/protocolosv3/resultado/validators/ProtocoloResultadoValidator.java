package com.tivic.manager.ptc.protocolosv3.resultado.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.sol.connection.CustomConnection;

public class ProtocoloResultadoValidator {
	private List<IValidator<ResultadoDTO>> validators;
	
	public ProtocoloResultadoValidator() throws ValidationException, Exception {
		this.validators = new ArrayList<IValidator<ResultadoDTO>>();
		this.validators.add(new DocumentoJulgadoValidator());
		this.validators.add(new DateValidator());
		this.validators.add(new UsuarioValidator());
	}
	
	public void validate(ResultadoDTO resultado, CustomConnection connection) throws ValidationException, Exception {
		for(IValidator<ResultadoDTO> validator: this.validators) {
			validator.validate(resultado, connection);
		}
	}
}
