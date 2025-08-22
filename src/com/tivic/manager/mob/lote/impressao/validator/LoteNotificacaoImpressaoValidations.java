package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class LoteNotificacaoImpressaoValidations {
	private List<Validator<Ait>> validators;

	public LoteNotificacaoImpressaoValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
		this.validators.add(new EnderecoCorrespondenciaValidator());
		this.validators.add(new NumeroRenavamValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
	
}
