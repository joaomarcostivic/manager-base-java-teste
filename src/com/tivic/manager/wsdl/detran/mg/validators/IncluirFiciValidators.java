package com.tivic.manager.wsdl.detran.mg.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class IncluirFiciValidators {
	private List<Validator<ApresentacaoCondutor>> validators;
	
	public IncluirFiciValidators() throws ValidacaoException{
		this.validators = new ArrayList<Validator<ApresentacaoCondutor>>();	
		this.validators.add(new IncluirModeloCnhValidator());
	}
	
	public void validate(ApresentacaoCondutor protocolo, CustomConnection connection) throws ValidacaoException, Exception {
		for(Validator<ApresentacaoCondutor> validator: this.validators) {
			validator.validate(protocolo, connection);
		}
	}
}
