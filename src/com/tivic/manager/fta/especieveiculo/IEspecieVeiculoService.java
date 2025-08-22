package com.tivic.manager.fta.especieveiculo;

import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.sol.connection.CustomConnection;

public interface IEspecieVeiculoService {
	public EspecieVeiculo getByNome(String dsEspecie) throws Exception;
	public EspecieVeiculo get(int cdEspecie) throws Exception;
	public EspecieVeiculo get(int cdEspecie, CustomConnection customConnection) throws Exception;
}
