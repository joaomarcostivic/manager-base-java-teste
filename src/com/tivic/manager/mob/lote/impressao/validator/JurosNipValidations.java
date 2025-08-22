package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class JurosNipValidations {

private List<Validator<Ait>> validators;

	public JurosNipValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
	}

	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}

}