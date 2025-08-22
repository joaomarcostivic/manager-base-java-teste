package com.tivic.manager.mob.veiculo.validations;

import java.sql.Connection;

import com.tivic.manager.fta.Veiculo;

public interface Validator {
	
	public void validate(Veiculo veiculo, Connection connect) throws Exception;
}
