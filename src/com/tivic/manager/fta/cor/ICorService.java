package com.tivic.manager.fta.cor;

import com.tivic.manager.fta.Cor;
import com.tivic.sol.connection.CustomConnection;

public interface ICorService {
	public Cor getByNome(String nmCor) throws Exception;
	public Cor get(int cdCor) throws Exception;
	public Cor get(int cdCor, CustomConnection customConnection) throws Exception;
}
