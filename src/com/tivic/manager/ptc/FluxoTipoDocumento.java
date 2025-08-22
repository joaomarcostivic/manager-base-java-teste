package com.tivic.manager.ptc;

public class FluxoTipoDocumento {

	private int cdFluxo;
	private int cdTipoDocumento;

	public FluxoTipoDocumento(){ }

	public FluxoTipoDocumento(int cdFluxo,
			int cdTipoDocumento){
		setCdFluxo(cdFluxo);
		setCdTipoDocumento(cdTipoDocumento);
	}
	public void setCdFluxo(int cdFluxo){
		this.cdFluxo=cdFluxo;
	}
	public int getCdFluxo(){
		return this.cdFluxo;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFluxo: " +  getCdFluxo();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FluxoTipoDocumento(getCdFluxo(),
			getCdTipoDocumento());
	}

}
