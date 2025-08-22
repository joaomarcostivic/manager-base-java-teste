package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class LoteNotificacaoAdvertenciaValidations {
	private List<Validator<Ait>> validators;
	
	public LoteNotificacaoAdvertenciaValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
		this.validators.add(new DefesaProtocoladaValidator());
		this.validators.add(new NaiRegistradaValidator());
		this.validators.add(new PrazoEmissaoNipValidator());
		this.validators.add(new MultaPagaValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}

}
