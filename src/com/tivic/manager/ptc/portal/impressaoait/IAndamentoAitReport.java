package com.tivic.manager.ptc.portal.impressaoait;

import com.tivic.manager.ptc.portal.response.AndamentoAitResponse;
import com.tivic.sol.connection.CustomConnection;

public interface IAndamentoAitReport {
	public AndamentoAitResponse imprimir(int cdAit, CustomConnection customConnection) throws Exception;
}
