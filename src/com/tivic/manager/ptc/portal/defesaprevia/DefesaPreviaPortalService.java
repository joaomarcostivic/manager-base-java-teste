package com.tivic.manager.ptc.portal.defesaprevia;

import com.tivic.manager.ptc.portal.builders.DocumentoPortalResponseBuilder;
import com.tivic.manager.ptc.portal.builders.ProtocoloInsertDTODirector;
import com.tivic.manager.ptc.portal.comprovante.IComprovanteService;
import com.tivic.manager.ptc.portal.request.DocumentoPortalRequest;
import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DefesaPreviaPortalService extends ProtocoloService {
	
	private IComprovanteService comprovanteService;
	
	public DefesaPreviaPortalService() throws Exception {
		super();
		this.comprovanteService = (IComprovanteService) BeansFactory.get(IComprovanteService.class);
	}
	
	public DocumentoPortalResponse solicitar(DocumentoPortalRequest documentoRecurso, CustomConnection customConnection) throws Exception {
		ProtocoloInsertDTO protocoInsertloDTO = new ProtocoloInsertDTODirector(documentoRecurso).setProtocoloInsertDTO().build();
		ProtocoloDTO defesaPrevia = this.insert(protocoInsertloDTO, customConnection);
		defesaPrevia.setEmailSolicitante(documentoRecurso.getEmailSolicitante());
		defesaPrevia.setCpfSolicitante(documentoRecurso.getNrCpfSolicitante());
		byte[] comprovante = this.comprovanteService.imprimirComprovante(defesaPrevia, documentoRecurso.getReferer(), customConnection);
		return setdocumentoPortalResponse(defesaPrevia.getDocumento().getNrDocumento(), comprovante);
	}
	
	private DocumentoPortalResponse setdocumentoPortalResponse(String nrDocumento, byte[] protocoloRecebimento) {
		return new DocumentoPortalResponseBuilder()
				.setNrDocumento(nrDocumento)
				.setProtocoloRecebimento(protocoloRecebimento)
				.build();
	}
}
