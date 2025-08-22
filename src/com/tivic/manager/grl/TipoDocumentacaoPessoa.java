package com.tivic.manager.grl;

public class TipoDocumentacaoPessoa {

	private int cdTipoDocumento;
	private int cdPessoa;
	private String nrDocumento;

	public TipoDocumentacaoPessoa(){ }

	public TipoDocumentacaoPessoa(int cdTipoDocumento,
			int cdPessoa,
			String nrDocumento){
		setCdTipoDocumento(cdTipoDocumento);
		setCdPessoa(cdPessoa);
		setNrDocumento(nrDocumento);
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoDocumentacaoPessoa(getCdTipoDocumento(),
			getCdPessoa(),
			getNrDocumento());
	}

}