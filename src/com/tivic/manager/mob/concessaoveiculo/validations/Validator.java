package com.tivic.manager.mob.concessaoveiculo.validations;

import java.sql.Connection;

import com.tivic.manager.mob.ConcessaoVeiculoDTO;

public interface Validator {
	
	public void validate(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception;
}
