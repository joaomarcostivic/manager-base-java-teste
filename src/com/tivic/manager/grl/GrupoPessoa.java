package com.tivic.manager.grl;

public class GrupoPessoa {

	private int cdPessoa;
	private int cdGrupo;
	private int lgResponsavel;

	public GrupoPessoa() { }

	public GrupoPessoa(int cdPessoa,
			int cdGrupo,
			int lgResponsavel) {
		setCdPessoa(cdPessoa);
		setCdGrupo(cdGrupo);
		setLgResponsavel(lgResponsavel);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setLgResponsavel(int lgResponsavel){
		this.lgResponsavel=lgResponsavel;
	}
	public int getLgResponsavel(){
		return this.lgResponsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", lgResponsavel: " +  getLgResponsavel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoPessoa(getCdPessoa(),
			getCdGrupo(),
			getLgResponsavel());
	}

}