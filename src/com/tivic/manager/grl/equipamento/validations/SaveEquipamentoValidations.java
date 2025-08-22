package com.tivic.manager.grl.equipamento.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.util.validator.Validator;

public class SaveEquipamentoValidations {

	private List<Validator<Equipamento>> validators;
	
    public SaveEquipamentoValidations() throws Exception{
    	this.validators = new ArrayList<Validator<Equipamento>>();
		this.validators.add(new IdEquipamentoValidation());
    }
    
	public void validate(Equipamento equipamento, CustomConnection customConnection) throws Exception {
    	for(Validator<Equipamento> validator : validators) {
    		validator.validate(equipamento, customConnection);
    	}
    }
    
}
