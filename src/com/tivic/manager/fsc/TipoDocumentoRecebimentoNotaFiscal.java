package com.tivic.manager.fsc;

public class TipoDocumentoRecebimentoNotaFiscal {

	private int cdTipoDocumento;
	private String nmTipoDocumento;
	private int stTipoDocumento;
	private int lgObrigatorio;

	public TipoDocumentoRecebimentoNotaFiscal() { }

	public TipoDocumentoRecebimentoNotaFiscal(int cdTipoDocumento,
			String nmTipoDocumento,
			int stTipoDocumento,
			int lgObrigatorio) {
		setCdTipoDocumento(cdTipoDocumento);
		setNmTipoDocumento(nmTipoDocumento);
		setStTipoDocumento(stTipoDocumento);
		setLgObrigatorio(lgObrigatorio);
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
	public void setStTipoDocumento(int stTipoDocumento){
		this.stTipoDocumento=stTipoDocumento;
	}
	public int getStTipoDocumento(){
		return this.stTipoDocumento;
	}
	public void setLgObrigatorio(int lgObrigatorio){
		this.lgObrigatorio=lgObrigatorio;
	}
	public int getLgObrigatorio(){
		return this.lgObrigatorio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", nmTipoDocumento: " +  getNmTipoDocumento();
		valueToString += ", stTipoDocumento: " +  getStTipoDocumento();
		valueToString += ", lgObrigatorio: " +  getLgObrigatorio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumentoRecebimentoNotaFiscal(getCdTipoDocumento(),
			getNmTipoDocumento(),
			getStTipoDocumento(),
			getLgObrigatorio());
	}

}