package com.tivic.manager.ptc.protocolos.julgamento;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;

public interface IGerarDocumentosLoteJulgamento {
	byte[] gerarDocumentos(LoteImpressao loteImpressao, int cdUsuario, CustomConnection customConnection) throws Exception, ValidacaoException;
}
