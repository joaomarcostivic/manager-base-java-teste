package com.tivic.manager.ptc.portal.formulario;

public class FormularioDTO {
	private byte[] formulario;
	private int tpDocumento;

	public byte[] getFormulario() {
		return formulario;
	}

	public void setFormulario(byte[] formulario) {
		this.formulario = formulario;
	}

	public int getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(int tpDocumento) {
		this.tpDocumento = tpDocumento;
	}
}
