package com.tivic.manager.mob.concessaoveiculo.validations;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.ConcessaoVeiculoDTO;

public class SaveValidationConcessaoVeiculo {

	private ConcessaoVeiculoDTO concessaoVeiculoDTO;
	private List<Validator> validators;

	public SaveValidationConcessaoVeiculo(ConcessaoVeiculoDTO concessaoVeiculoDTO) {
		this.concessaoVeiculoDTO = concessaoVeiculoDTO;
		validators = new ArrayList<Validator>();
		validators.add(new ValidatorPrefixo());
		validators.add(new ValidatorSituacao());
	}

	public void validate(Connection connect) throws Exception {
		for (Validator validator : validators) {
			validator.validate(concessaoVeiculoDTO, connect);
		}
	}
}
