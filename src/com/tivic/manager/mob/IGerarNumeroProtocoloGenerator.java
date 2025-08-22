package com.tivic.manager.mob;

import com.tivic.manager.ptc.Documento;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarNumeroProtocoloGenerator {
	public Documento generate(Documento documento, int cdAit, CustomConnection customConnection) throws Exception;
}
