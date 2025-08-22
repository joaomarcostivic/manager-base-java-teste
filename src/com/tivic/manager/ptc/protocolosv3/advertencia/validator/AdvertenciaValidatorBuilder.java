package com.tivic.manager.ptc.protocolosv3.advertencia.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.NaiExistsValidator;
import com.tivic.sol.connection.CustomConnection;

public class AdvertenciaValidatorBuilder {
	private List<IProtocoloValidator> validators;
	
	public AdvertenciaValidatorBuilder() throws Exception {
		this.validators = new ArrayList<IProtocoloValidator>();
		this.validators.add(new NaiExistsValidator());
	}
	
	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		for(IProtocoloValidator validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}
}
