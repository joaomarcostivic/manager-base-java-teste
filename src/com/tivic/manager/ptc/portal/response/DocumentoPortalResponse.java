package com.tivic.manager.ptc.portal.response;

public class DocumentoPortalResponse {

	private int cdDocumento;
	private String nrDocumento;
	private byte[] protocoloRecebimento;

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public byte[] getProtocoloRecebimento() {
		return protocoloRecebimento;
	}

	public void setProtocoloRecebimento(byte[] protocoloRecebimento) {
		this.protocoloRecebimento = protocoloRecebimento;
	}
}
