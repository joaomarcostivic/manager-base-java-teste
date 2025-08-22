package com.tivic.manager.prc;

public class TribunalCidade {

	private int cdTribunal;
	private int cdCidade;
	private String idUnidade;

	public TribunalCidade(){ }

	public TribunalCidade(int cdTribunal,
			int cdCidade,
			String idUnidade){
		setCdTribunal(cdTribunal);
		setCdCidade(cdCidade);
		setIdUnidade(idUnidade);
	}
	public void setCdTribunal(int cdTribunal){
		this.cdTribunal=cdTribunal;
	}
	public int getCdTribunal(){
		return this.cdTribunal;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setIdUnidade(String idUnidade){
		this.idUnidade=idUnidade;
	}
	public String getIdUnidade(){
		return this.idUnidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTribunal: " +  getCdTribunal();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", idUnidade: " +  getIdUnidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TribunalCidade(getCdTribunal(),
			getCdCidade(),
			getIdUnidade());
	}

}
