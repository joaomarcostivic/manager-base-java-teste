package com.tivic.manager.mob.correios.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.CorreiosLote;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class CorreiosLoteUpdateValidations {
	private List<Validator<CorreiosLote>> validators;
	
	public CorreiosLoteUpdateValidations(int cdLote) throws Exception {
		this.validators = new ArrayList<Validator<CorreiosLote>>();
		this.validators.add(new CorreiosLoteUpdateValidator());
	}
	
	public void validate(CorreiosLote correiosLote, CustomConnection customConnection) throws Exception {
		for( Validator<CorreiosLote> validator: validators) {
			validator.validate(correiosLote, customConnection);
		}
	}
}
