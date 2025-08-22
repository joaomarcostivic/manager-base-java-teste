package com.tivic.manager.util.parametro;

import com.tivic.sol.connection.CustomConnection;

public interface IParametroFacade {
	public int getVlParametroAsInt(String nmParametro, CustomConnection customConnection) throws Exception;
}
