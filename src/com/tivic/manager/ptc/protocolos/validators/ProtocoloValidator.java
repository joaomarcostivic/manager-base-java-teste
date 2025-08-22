package com.tivic.manager.ptc.protocolos.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class ProtocoloValidator {
	private List<IValidator<DadosProtocoloDTO>> validators;
	
	public ProtocoloValidator() {
		this.validators = new ArrayList<IValidator<DadosProtocoloDTO>>();
		this.validators.add(new AitValidator());
		this.validators.add(new DtProtocoloValidator());
		this.validators.add(new ExistsValidator());
		this.validators.add(new NrDocumentoValidator());
		this.validators.add(new SituacaoValidator());
		this.validators.add(new UsuarioValidator());
		this.validators.add(new FaseValidator());
	}
	
	public void validate(DadosProtocoloDTO protocolo, CustomConnection connection) throws ValidationException, Exception {
		for(IValidator<DadosProtocoloDTO> validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}

}
