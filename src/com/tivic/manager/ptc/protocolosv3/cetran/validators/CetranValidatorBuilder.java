package com.tivic.manager.ptc.protocolosv3.cetran.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.CetranExistsValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.DataCetranValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.JariIndeferidaExistsValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.NipExistsValidator;
import com.tivic.sol.connection.CustomConnection;

public class CetranValidatorBuilder {
	private List<IProtocoloValidator> validators;

	public CetranValidatorBuilder() throws Exception {
		this.validators = new ArrayList<IProtocoloValidator>();
		this.validators.add(new NipExistsValidator());
		this.validators.add(new JariIndeferidaExistsValidator());
		this.validators.add(new CetranExistsValidator());
		this.validators.add(new DataCetranValidator());
	}

	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		for(IProtocoloValidator validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}
}
