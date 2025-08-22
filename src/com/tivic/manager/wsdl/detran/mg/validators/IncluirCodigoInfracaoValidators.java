package com.tivic.manager.wsdl.detran.mg.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class IncluirCodigoInfracaoValidators {
	private List<Validator<Infracao>> validators;
	
	public IncluirCodigoInfracaoValidators() {
		this.validators = new ArrayList<Validator<Infracao>>();
		this.validators.add(new CodigoInfracaoValidator());
	}
	
	public void validate(Infracao infracao, CustomConnection customConnection) throws Exception {
		for(Validator<Infracao> validator: this.validators) {
			validator.validate(infracao, customConnection);
		}
	}
}
