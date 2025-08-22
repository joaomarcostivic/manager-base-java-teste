package com.tivic.manager.ptc.portal.builders;

import com.tivic.manager.ptc.portal.response.DocumentoPortalResponse;

public class DocumentoPortalResponseBuilder {
	
	private DocumentoPortalResponse documentoPortalResponse;
	
	public DocumentoPortalResponseBuilder() {
		this.documentoPortalResponse = new DocumentoPortalResponse();
	}
	
	public DocumentoPortalResponseBuilder setNrDocumento(String nrDocumento) {
		this.documentoPortalResponse.setNrDocumento(nrDocumento);
		return this;
	}
	
	public DocumentoPortalResponseBuilder setProtocoloRecebimento(byte[] protocoloRecebimento) {
		this.documentoPortalResponse.setProtocoloRecebimento(protocoloRecebimento);
		return this;
	}
	
	public DocumentoPortalResponse build() {
		return this.documentoPortalResponse;
	}
}
