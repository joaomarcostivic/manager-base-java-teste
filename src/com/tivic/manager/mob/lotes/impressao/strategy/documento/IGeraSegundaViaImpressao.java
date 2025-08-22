package com.tivic.manager.mob.lotes.impressao.strategy.documento;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IGeraSegundaViaImpressao {
	byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException;
}
