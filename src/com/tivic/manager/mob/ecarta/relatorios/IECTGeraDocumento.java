package com.tivic.manager.mob.ecarta.relatorios;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IECTGeraDocumento {
	byte[] gerar(int cdAit, CustomConnection customConnection) throws Exception, ValidacaoException;
}
