package com.tivic.manager.mob.agente.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class DeleteAgenteValidators {
	private int cdAgente;
	private List<Validator<Integer>> validators;
	
	public DeleteAgenteValidators(int cdAgente) throws Exception {
		this.cdAgente = cdAgente;
		validators = new ArrayList<Validator<Integer>>();
		validators.add(new ValidatorAgentePertecenteAit());
		validators.add(new ValidatorAgentePertecenteTalonario());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<Integer> validator : validators) {
			validator.validate(cdAgente, customConnection);
		}
	}
}
