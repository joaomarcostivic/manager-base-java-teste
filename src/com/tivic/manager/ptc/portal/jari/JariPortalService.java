package com.tivic.manager.ptc.portal.jari;

import com.tivic.manager.ptc.portal.builders.DocumentoPortalResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ProtocoloInsertDTODirector;
import com.tivic.manager.ptc.portal.comprovante.IComprovanteService;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.jari.JariService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class JariPortalService extends JariService{
	
	private IComprovanteService comprovanteService;

	public JariPortalService() throws Exception {
		super();
		this.comprovanteService = (IComprovanteService) BeansFactory.get(IComprovanteService.class);
	}
	
	public DocumentoPortalResponse solicitar(DocumentoPortalRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		ProtocoloInsertDTO protocoloInsertDTO =  new ProtocoloInsertDTODirector(documentoRecurso).setProtocoloInsertDTO().build();
		ProtocoloDTO jari = this.insert(protocoloInsertDTO, customConnection);
		jari.setEmailSolicitante(documentoRecurso.getEmailSolicitante());
		jari.setCpfSolicitante(documentoRecurso.getNrCpfSolicitante());
		byte[] comprovante = this.comprovanteService.imprimirComprovante(jari, documentoRecurso.getReferer(), customConnection);
		return setdocumentoPortalResponse(jari.getDocumento().getNrDocumento(), comprovante); 
	}
	
	private DocumentoPortalResponse setdocumentoPortalResponse(String nrDocumento, byte[] protocoloRecebimento) {
		return new DocumentoPortalResponseBuilder()
				.setNrDocumento(nrDocumento)
				.setProtocoloRecebimento(protocoloRecebimento)
				.build();
	}

}
