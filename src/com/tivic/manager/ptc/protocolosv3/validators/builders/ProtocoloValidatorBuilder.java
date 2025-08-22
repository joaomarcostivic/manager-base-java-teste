package com.tivic.manager.ptc.protocolosv3.validators.builders;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.protocolosv3.validators.IProtocoloValidator;

public class ProtocoloValidatorBuilder {
	private List<IProtocoloValidator> validators;
	
	public ProtocoloValidatorBuilder() {
		this.validators = new ArrayList<IProtocoloValidator>();
	}
	
	public void addValidator(IProtocoloValidator validator) {
		this.validators.add(validator);
	}
	
	public List<IProtocoloValidator> build(){
		return this.validators;
	}
}
