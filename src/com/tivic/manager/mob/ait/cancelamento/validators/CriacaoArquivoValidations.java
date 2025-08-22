package com.tivic.manager.mob.ait.cancelamento.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Arquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.validation.Validator;

public class CriacaoArquivoValidations {
	private List<Validator<Arquivo>> validators;
	
	public CriacaoArquivoValidations() throws Exception {
		this.validators = new ArrayList<Validator<Arquivo>>();
		this.validators.add(new ArquivoIsNullValidator());
		this.validators.add(new TamanhoArquivoValidator());
	}
	
	public void validate(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		for (Validator<Arquivo> validator: validators) {
			validator.validate(arquivo, customConnection);
		}
	}
}
