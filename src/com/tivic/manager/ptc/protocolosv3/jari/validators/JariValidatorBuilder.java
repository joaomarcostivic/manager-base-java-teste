package com.tivic.manager.ptc.protocolosv3.jari.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.CetranExistsValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.DataJariValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.NaiExistsValidator;
import com.tivic.manager.ptc.protocolosv3.validators.ait.NipExistsValidator;
import com.tivic.sol.connection.CustomConnection;

public class JariValidatorBuilder {
	private List<IProtocoloValidator> validators;

	public JariValidatorBuilder() throws Exception {
		this.validators = new ArrayList<IProtocoloValidator>();
		this.validators.add(new NaiExistsValidator());
		this.validators.add(new CetranExistsValidator());
		this.validators.add(new NipExistsValidator());
		this.validators.add(new DataJariValidator());
	}

	public void validate(ProtocoloDTO protocolo, CustomConnection connection) throws Exception {
		for(IProtocoloValidator validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}
}
