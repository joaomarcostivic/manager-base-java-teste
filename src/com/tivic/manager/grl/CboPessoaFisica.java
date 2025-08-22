package com.tivic.manager.grl;

public class CboPessoaFisica {

	private int cdPessoa;
	private int cdCbo;
	private int lgPrincipal;

	public CboPessoaFisica(int cdPessoa,
			int cdCbo,
			int lgPrincipal){
		setCdPessoa(cdPessoa);
		setCdCbo(cdCbo);
		setLgPrincipal(lgPrincipal);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdCbo(int cdCbo){
		this.cdCbo=cdCbo;
	}
	public int getCdCbo(){
		return this.cdCbo;
	}
	public void setLgPrincipal(int lgPrincipal){
		this.lgPrincipal=lgPrincipal;
	}
	public int getLgPrincipal(){
		return this.lgPrincipal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdCbo: " +  getCdCbo();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CboPessoaFisica(getCdPessoa(),
			getCdCbo(),
			getLgPrincipal());
	}

}
