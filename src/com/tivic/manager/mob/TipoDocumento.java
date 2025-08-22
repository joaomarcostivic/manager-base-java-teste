package com.tivic.manager.mob;

public class TipoDocumento {

	private int cdTipoDocumento;

	public TipoDocumento() { }

	public TipoDocumento(int cdTipoDocumento) {
		setCdTipoDocumento(cdTipoDocumento);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumento(getCdTipoDocumento());
	}

}