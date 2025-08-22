package com.tivic.manager.grl.pais;

import java.util.List;

import com.tivic.manager.grl.Pais;
import com.tivic.sol.connection.CustomConnection;

public interface PaisRepository {
	public List<Pais> getAll() throws Exception;
	public List<Pais> getAll(CustomConnection customConnection) throws Exception;
}
