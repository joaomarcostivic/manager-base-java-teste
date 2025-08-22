package com.tivic.manager.mob.boat.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.Boat;
import com.tivic.manager.util.validator.Validator;

public class SaveDeclaracaoValidations {
	
	private List<Validator<Boat>> validators;
	
    public SaveDeclaracaoValidations() throws Exception{
    	this.validators = new ArrayList<Validator<Boat>>();
		this.validators.add(new DeclaranteValidation());
		this.validators.add(new DeclaracaoPeriodoValidation());
		this.validators.add(new DeclaracaoVeiculoValidation());
		this.validators.add(new DeclaracaoVeiculoPrincipalValidation());
		this.validators.add(new DeclaracaoVeiculosPlacasValidation());
		this.validators.add(new DeclaracaoVeiculosImagensValidation());
    }
    
	public void validate(Boat equipamento, CustomConnection customConnection) throws Exception {
    	for(Validator<Boat> validator : validators) {
    		validator.validate(equipamento, customConnection);
    	}
    }
}

