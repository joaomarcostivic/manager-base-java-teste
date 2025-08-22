package com.tivic.manager.ptc.documentoocorrencia;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.sol.connection.CustomConnection;

public interface IDocumentoOcorrenciaService {
	public void insert(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception;
	public void insertWithCustomDb(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception;
}
