package com.tivic.manager.mob.lotes.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.sol.connection.CustomConnection;

public class LoteNotificacaoImpressaoValidations {
	private List<IValidator<Ait>> validators;

	public LoteNotificacaoImpressaoValidations() throws Exception {
		this.validators = new ArrayList<IValidator<Ait>>();
		this.validators.add(new EnderecoCorrespondenciaValidator());
		this.validators.add(new NumeroRenavamValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (IValidator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
	
}
