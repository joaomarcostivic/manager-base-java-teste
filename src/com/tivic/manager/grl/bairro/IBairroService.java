package com.tivic.manager.grl.bairro;

import com.tivic.manager.grl.Bairro;
import com.tivic.sol.connection.CustomConnection;

public interface IBairroService {
	public void insert(Bairro bairro) throws Exception;
	public void insert(Bairro bairro, CustomConnection customConnection) throws Exception;
	public Bairro getBairroByNomeCidade(String nmBairro, int cdCidade) throws Exception;
}
