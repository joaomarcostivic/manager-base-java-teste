package com.tivic.manager.mob.aitmovimento.validators;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.protocolos.validators.IValidator;
import com.tivic.sol.connection.CustomConnection;

public class ResultadoDefesaValidator {
	private List<IValidator<AitMovimento>> validators;
	
	public ResultadoDefesaValidator() throws ValidationException, Exception {
		this.validators = new ArrayList<IValidator<AitMovimento>>();
		this.validators.add(new UniqueResultadoValidator());
	}
	
	public void validate(AitMovimento movimento, CustomConnection connection) throws ValidationException, Exception {
		for(IValidator<AitMovimento> validator: this.validators) {
			validator.validate(movimento, connection);
		}
	}
}
