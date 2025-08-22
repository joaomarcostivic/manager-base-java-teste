package com.tivic.manager.grl;

import java.io.Serializable;

public class ParametroDTO extends ParametroValor implements Serializable{
	
	private byte[] valor64;
	
	public ParametroDTO() {}
	
	public ParametroDTO(ParametroValor paramVal, byte[] image) {
		super(paramVal);
		valor64 = image;
	}
	
	public void setValor64(byte[] image) {
		valor64 = image;
	}
	
	public byte[] getValor64() {
		return valor64;
	}
	
	public void convertValor64() {
		
	}

}
