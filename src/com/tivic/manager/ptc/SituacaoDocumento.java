package com.tivic.manager.ptc;

public class SituacaoDocumento {

	private int cdSituacaoDocumento;
	private String nmSituacaoDocumento;

	public SituacaoDocumento() { }
			
	public SituacaoDocumento(int cdSituacaoDocumento,
			String nmSituacaoDocumento){
		setCdSituacaoDocumento(cdSituacaoDocumento);
		setNmSituacaoDocumento(nmSituacaoDocumento);
	}
	public void setCdSituacaoDocumento(int cdSituacaoDocumento){
		this.cdSituacaoDocumento=cdSituacaoDocumento;
	}
	public int getCdSituacaoDocumento(){
		return this.cdSituacaoDocumento;
	}
	public void setNmSituacaoDocumento(String nmSituacaoDocumento){
		this.nmSituacaoDocumento=nmSituacaoDocumento;
	}
	public String getNmSituacaoDocumento(){
		return this.nmSituacaoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSituacaoDocumento: " +  getCdSituacaoDocumento();
		valueToString += ", nmSituacaoDocumento: " +  getNmSituacaoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SituacaoDocumento(getCdSituacaoDocumento(),
			getNmSituacaoDocumento());
	}

}
