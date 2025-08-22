package com.tivic.manager.mob.concessaoveiculo.pessoa;

import java.sql.Connection;

import com.tivic.manager.mob.ConcessaoVeiculoDTO;

public interface ConcessaoVeiculoPessoa {
	
	public void save(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception;
}
