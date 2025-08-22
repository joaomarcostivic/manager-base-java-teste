package com.tivic.manager.mob.veiculo.validations;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.fta.Veiculo;
import com.tivic.manager.mob.veiculo.validations.Validator;

public class SaveValidationVeiculo {
	private Veiculo veiculo;
	private List<Validator> validators;

	public SaveValidationVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
		validators = new ArrayList<Validator>();
		validators.add(new ValidatorPlaca());
		validators.add(new ValidatorChassi());
		validators.add(new ValidatorRenavam());
	}

	public void validate(Connection connect) throws Exception {
		for (Validator validator : validators) {
			validator.validate(veiculo, connect);
		}
	}
}
