package com.tivic.manager.ptc.documento;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.sol.cdi.BeansFactory;

public class DocumentoService implements IDocumentoService {
	private DocumentoRepository documentoRepository;
	
	public DocumentoService() throws Exception {
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
	}
	
	@Override
	public boolean isDocumentoDeferido(int cdDocumento) throws Exception, BadRequestException {
		int cdDocumentoDeferido = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDO", 0, 0);
		
		if(cdDocumentoDeferido == 0)
			throw new BadRequestException("O parametro do documento deferido não foi configurado.");
		Documento documento = documentoRepository.get(cdDocumento);
		
		if(documento == null)
			throw new Exception("Nenhum documento encontrado.");
		
		if(documento.getCdFase() == cdDocumentoDeferido)
			return true;
		
		return false;
	}

}
