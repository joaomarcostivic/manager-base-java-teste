package com.tivic.manager.mob.lotes.repository.documentoexterno;

import com.tivic.manager.mob.lotes.model.documentoexterno.LoteDocumentoExterno;
import com.tivic.sol.connection.CustomConnection;

public interface LoteDocumentoExternoRepository {

	public LoteDocumentoExterno insert(LoteDocumentoExterno documentosExternos, CustomConnection customConnection) throws Exception;
	public LoteDocumentoExterno update(LoteDocumentoExterno documentosExternos, CustomConnection customConnection) throws Exception;
	public LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento) throws Exception;
	
}
