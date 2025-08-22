package com.tivic.manager.ptc.documentoocorrencia;

import com.tivic.manager.ptc.DocumentoOcorrencia;
import com.tivic.manager.ptc.portal.credencialestacionamento.DatabaseConnectionManager;
import com.tivic.manager.ptc.protocolos.documentoocorrencia.DocumentoOcorrenciaRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DocumentoOcorrenciaService implements IDocumentoOcorrenciaService {
	private DocumentoOcorrenciaRepository documentoOcorrenciaRepository;

	public DocumentoOcorrenciaService() throws Exception {
		documentoOcorrenciaRepository = (DocumentoOcorrenciaRepository) BeansFactory.get(DocumentoOcorrenciaRepository.class);
	}

	@Override
	public void insert(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception {
		documentoOcorrenciaRepository.insert(documentoOcorrencia, customConnection);
	}

	@Override
	public void insertWithCustomDb(DocumentoOcorrencia documentoOcorrencia, CustomConnection customConnection) throws Exception {
		try {
			new DatabaseConnectionManager().configConnection(customConnection, true);
			insert(documentoOcorrencia, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
}
