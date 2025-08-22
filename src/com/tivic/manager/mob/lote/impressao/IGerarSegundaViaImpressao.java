package com.tivic.manager.mob.lote.impressao;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarSegundaViaImpressao {
	byte[] gerarDocumentos(int cdAit, Boolean printPortal, CustomConnection customConnection) throws Exception, ValidacaoException;
}
