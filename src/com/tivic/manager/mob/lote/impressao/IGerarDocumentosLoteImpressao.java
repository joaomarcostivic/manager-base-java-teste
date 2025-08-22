package com.tivic.manager.mob.lote.impressao;

import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public interface IGerarDocumentosLoteImpressao {
	byte[] gerarDocumentos(LoteImpressao loteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception, ValidacaoException;
}
