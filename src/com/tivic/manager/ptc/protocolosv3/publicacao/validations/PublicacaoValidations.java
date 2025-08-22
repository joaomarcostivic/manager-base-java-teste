package com.tivic.manager.ptc.protocolosv3.publicacao.validations;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class PublicacaoValidations {
	
	private List<Validator<AitMovimento>> validators;
	
    public PublicacaoValidations() throws Exception{
    	this.validators = new ArrayList<Validator<AitMovimento>>();
		this.validators.add(new VerificaPublicacaoValidator());
    }
    
	public void validate(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
    	for(Validator<AitMovimento> validator : validators) {
    		validator.validate(aitMovimento, customConnection);
    	}
    }
}
