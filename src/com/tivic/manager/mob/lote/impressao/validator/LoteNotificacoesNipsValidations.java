package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;

public class LoteNotificacoesNipsValidations {
	private List<Validator<Ait>> validators;
	
	public LoteNotificacoesNipsValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
		this.validators.add(new SemDefesaValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
}
