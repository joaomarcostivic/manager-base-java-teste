package com.tivic.manager.mob;

import com.tivic.sol.connection.CustomConnection;

public interface IGerarAitMovimento {
	AitMovimento generate(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception;
}
