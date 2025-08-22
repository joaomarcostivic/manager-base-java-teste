package com.tivic.manager.ptc.portal.cetran;

import com.tivic.manager.ptc.portal.builders.DocumentoPortalResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ProtocoloInsertDTODirector;
import com.tivic.manager.ptc.portal.comprovante.IComprovanteService;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.cetran.CetranService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CetranPortalService extends CetranService {
	
	private IComprovanteService comprovanteService;

	public CetranPortalService() throws Exception {
		super();
		this.comprovanteService = (IComprovanteService) BeansFactory.get(IComprovanteService.class);
	}
	
	public DocumentoPortalResponse solicitar(DocumentoPortalRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		ProtocoloInsertDTO protocoloInsertDTO = new ProtocoloInsertDTODirector(documentoRecurso).setProtocoloInsertDTO().build();
		ProtocoloDTO cetran = this.insert(protocoloInsertDTO, customConnection);
		cetran.setEmailSolicitante(documentoRecurso.getEmailSolicitante());
		cetran.setCpfSolicitante(documentoRecurso.getNrCpfSolicitante());
		byte[] comprovante = this.comprovanteService.imprimirComprovante(cetran, documentoRecurso.getReferer(), customConnection);
		return setdocumentoPortalResponse(cetran.getDocumento().getNrDocumento(), comprovante); 
	}
	
	private DocumentoPortalResponse setdocumentoPortalResponse(String nrDocumento, byte[] protocoloRecebimento) {
		return new DocumentoPortalResponseBuilder()
				.setNrDocumento(nrDocumento)
				.setProtocoloRecebimento(protocoloRecebimento)
				.build();
	}

}
