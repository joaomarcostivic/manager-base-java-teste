package com.tivic.manager.ptc.protocolosv3.validators;

import java.util.List;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.builders.ProtocoloValidatorBuilder;
import com.tivic.sol.connection.CustomConnection;

public class ProtocoloValidator {

	private List<IProtocoloValidator> validators;
	
	public ProtocoloValidator(ProtocoloValidatorBuilder builder) {
		validators = builder.build();
	}
	
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		for(IProtocoloValidator validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}

}
