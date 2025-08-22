package com.tivic.manager.srh;

public class Funcao {

	private int cdFuncao;
	private int cdEmpresa;
	private String nmFuncao;
	private String idFuncao;

	public Funcao() { }
			
	public Funcao(int cdFuncao,
			int cdEmpresa,
			String nmFuncao,
			String idFuncao){
		setCdFuncao(cdFuncao);
		setCdEmpresa(cdEmpresa);
		setNmFuncao(nmFuncao);
		setIdFuncao(idFuncao);
	}
	public void setCdFuncao(int cdFuncao){
		this.cdFuncao=cdFuncao;
	}
	public int getCdFuncao(){
		return this.cdFuncao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmFuncao(String nmFuncao){
		this.nmFuncao=nmFuncao;
	}
	public String getNmFuncao(){
		return this.nmFuncao;
	}
	public void setIdFuncao(String idFuncao){
		this.idFuncao=idFuncao;
	}
	public String getIdFuncao(){
		return this.idFuncao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFuncao: " +  getCdFuncao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmFuncao: " +  getNmFuncao();
		valueToString += ", idFuncao: " +  getIdFuncao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Funcao(getCdFuncao(),
			getCdEmpresa(),
			getNmFuncao(),
			getIdFuncao());
	}

}