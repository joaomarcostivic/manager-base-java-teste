package com.tivic.manager.mob.convenio;

import com.tivic.sol.connection.CustomConnection;

public interface IMudaConvenioDefault {
	void removerDefault(CustomConnection customConnection) throws Exception;
	Convenio aplicarDefault(int cdConvenio, CustomConnection customConnection) throws Exception;
}
