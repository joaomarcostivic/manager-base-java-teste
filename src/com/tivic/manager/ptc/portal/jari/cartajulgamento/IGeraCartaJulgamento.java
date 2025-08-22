package com.tivic.manager.ptc.portal.jari.cartajulgamento;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IGeraCartaJulgamento {
	byte[] gerar(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception, ValidacaoException;
}
	