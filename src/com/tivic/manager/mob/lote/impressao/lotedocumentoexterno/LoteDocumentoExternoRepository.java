package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import com.tivic.sol.connection.CustomConnection;

public interface LoteDocumentoExternoRepository {

	public LoteDocumentoExterno insert(LoteDocumentoExterno documentosExternos, CustomConnection customConnection) throws Exception;
	public LoteDocumentoExterno update(LoteDocumentoExterno documentosExternos, CustomConnection customConnection) throws Exception;
	public LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento) throws Exception;
	
}
