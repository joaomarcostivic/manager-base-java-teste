package com.tivic.manager.ptc.portal.credencialestacionamento;

import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.protocolos.documentoarquivo.DocumentoArquivoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class InsereDocumentoArquivo {
	
	private DocumentoArquivoRepository documentoArquivoRepository;
	
	public InsereDocumentoArquivo() throws Exception {
		documentoArquivoRepository = (DocumentoArquivoRepository) BeansFactory.get(DocumentoArquivoRepository.class);
	}
	
	public void inserir(int cdDocumento, int cdArquivo, CustomConnection customConnection) throws Exception {
		documentoArquivoRepository.insert(setDocumentoArquivo(cdDocumento, cdArquivo), customConnection);
	}
	
	private DocumentoArquivo setDocumentoArquivo(int cdDocumento, int cdArquivo) {
		DocumentoArquivo documentoArquivo = new DocumentoArquivo();
		documentoArquivo.setCdArquivo(cdArquivo);
		documentoArquivo.setCdDocumento(cdDocumento);
		
		return documentoArquivo;
	}
}
