package com.tivic.manager.mob.aitpagamento.validatiors;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitPagamento;
import com.tivic.manager.util.validator.Validator;
import com.tivic.sol.connection.CustomConnection;

public class ValidationAitPagamento {
	private AitPagamento aitPagamento;
	private List<Validator<AitPagamento>> validators;
	
	
	public ValidationAitPagamento(AitPagamento aitPagamento) throws Exception {
		this.aitPagamento = aitPagamento;
		validators = new ArrayList<Validator<AitPagamento>>();
		validators.add(new CancelamentoAitPagamento());
	}
	
	public void validate(CustomConnection customConnection) throws Exception {
		for(Validator<AitPagamento> validator : validators) {
			validator.validate(aitPagamento, customConnection);
		}
	}

}
