package com.tivic.manager.ptc.protocolosv3.gerardornumerodocumento;

import com.tivic.sol.connection.CustomConnection;

public interface INumeroDocumentoGenerator {
	String gerarNumero(int cdTipoDocumento, CustomConnection customConnection) throws Exception;
}
