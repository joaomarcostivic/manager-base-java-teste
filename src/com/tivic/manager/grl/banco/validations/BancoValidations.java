package com.tivic.manager.grl.banco.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Banco;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class BancoValidations {

	private List<Validator<Banco>> validators;
	
    public BancoValidations() throws Exception{
    	this.validators = new ArrayList<Validator<Banco>>();
		this.validators.add(new SaveBancoValidation());
    }
    
	public void validate(Banco banco, CustomConnection customConnection) throws Exception {
    	for(Validator<Banco> validator : validators) {
    		validator.validate(banco, customConnection);
    	}
    }
    
}
