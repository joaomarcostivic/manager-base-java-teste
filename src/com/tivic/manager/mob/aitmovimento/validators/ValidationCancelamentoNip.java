package com.tivic.manager.mob.aitmovimento.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class ValidationCancelamentoNip {
	private AitMovimento aitMovimento;
	private List<Validator<AitMovimento>> validators;
	
	public ValidationCancelamentoNip(AitMovimento aitMovimento) throws Exception {
		this.aitMovimento = aitMovimento;
		validators = new ArrayList<Validator<AitMovimento>>();
		validators.add(new CancelamentoNip());
		validators.add(new ValidadorMovimentoCancelamentoNip());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<AitMovimento> validator : validators) {
			validator.validate(aitMovimento, customConnection);
		}
	}

}
