package com.tivic.manager.grl;

public class CnaePessoaJuridica {

	private int cdPessoa;
	private int cdCnae;
	private int lgPrincipal;

	public CnaePessoaJuridica(int cdPessoa,
			int cdCnae,
			int lgPrincipal){
		setCdPessoa(cdPessoa);
		setCdCnae(cdCnae);
		setLgPrincipal(lgPrincipal);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdCnae(int cdCnae){
		this.cdCnae=cdCnae;
	}
	public int getCdCnae(){
		return this.cdCnae;
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
		valueToString += ", cdCnae: " +  getCdCnae();
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CnaePessoaJuridica(getCdPessoa(),
			getCdCnae(),
			getLgPrincipal());
	}

}
