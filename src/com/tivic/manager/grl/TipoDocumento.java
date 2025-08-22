package com.tivic.manager.grl;

public class TipoDocumento {

	private int cdTipoDocumento;
	private String nmTipoDocumento;
	private String sgTipoDocumento;
	private int cdEmpresa;

	public TipoDocumento() { }
			
	public TipoDocumento(int cdTipoDocumento,
			String nmTipoDocumento,
			String sgTipoDocumento,
			int cdEmpresa){
		setCdTipoDocumento(cdTipoDocumento);
		setNmTipoDocumento(nmTipoDocumento);
		setSgTipoDocumento(sgTipoDocumento);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setNmTipoDocumento(String nmTipoDocumento){
		this.nmTipoDocumento=nmTipoDocumento;
	}
	public String getNmTipoDocumento(){
		return this.nmTipoDocumento;
	}
	public void setSgTipoDocumento(String sgTipoDocumento){
		this.sgTipoDocumento=sgTipoDocumento;
	}
	public String getSgTipoDocumento(){
		return this.sgTipoDocumento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", nmTipoDocumento: " +  getNmTipoDocumento();
		valueToString += ", sgTipoDocumento: " +  getSgTipoDocumento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumento(getCdTipoDocumento(),
			getNmTipoDocumento(),
			getSgTipoDocumento(),
			getCdEmpresa());
	}

}
