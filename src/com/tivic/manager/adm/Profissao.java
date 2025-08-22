package com.tivic.manager.adm;

public class Profissao {

	private int cdProfissao;
	private String nmProfissao;

	public Profissao(int cdProfissao,
			String nmProfissao){
		setCdProfissao(cdProfissao);
		setNmProfissao(nmProfissao);
	}
	public void setCdProfissao(int cdProfissao){
		this.cdProfissao=cdProfissao;
	}
	public int getCdProfissao(){
		return this.cdProfissao;
	}
	public void setNmProfissao(String nmProfissao){
		this.nmProfissao=nmProfissao;
	}
	public String getNmProfissao(){
		return this.nmProfissao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProfissao: " +  getCdProfissao();
		valueToString += ", nmProfissao: " +  getNmProfissao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Profissao(getCdProfissao(),
			getNmProfissao());
	}

}