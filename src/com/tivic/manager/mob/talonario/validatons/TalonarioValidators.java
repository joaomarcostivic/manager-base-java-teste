package com.tivic.manager.mob.talonario.validatons;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Talonario;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class TalonarioValidators {
	private Talonario talonario;
	private List<Validator<Talonario>> validators;
	
	
	public TalonarioValidators(Talonario talonario) throws Exception {
		this.talonario = talonario;
		validators = new ArrayList<Validator<Talonario>>();
		validators.add(new FaixaNumeroValidator());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<Talonario> validator : validators) {
			validator.validate(talonario, customConnection);
		}
	}
}
