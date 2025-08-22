package com.tivic.manager.mob.lote.impressao.validator;

import java.util.ArrayList;
import java.util.List;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.util.validator.Validator;

public class LoteNotificacaoNaiValidations {
	private List<Validator<Ait>> validators;
	
	public LoteNotificacaoNaiValidations() throws Exception {
		this.validators = new ArrayList<Validator<Ait>>();
		this.validators.add(new ExistenciaNicValidator());
		this.validators.add(new RegistroInfracaoValidator());
		this.validators.add(new PrazoEmissaoNaiValidator());
	}
	
	public void validate(Ait ait, CustomConnection customConnection) throws Exception {
		for (Validator<Ait> validator: validators) {
			validator.validate(ait, customConnection);
		}
	}
}
