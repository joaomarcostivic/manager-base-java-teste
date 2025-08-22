package com.tivic.manager.acd;

public class LocalFuncionamento {

	private int cdLocalFuncionamento;
	private String nmLocalFuncionamento;
	private String idLocalFuncionamento;
	
	public LocalFuncionamento() { }
	
	public LocalFuncionamento(int cdLocalFuncionamento,
			String nmLocalFuncionamento,
			String idLocalFuncionamento){
		setCdLocalFuncionamento(cdLocalFuncionamento);
		setNmLocalFuncionamento(nmLocalFuncionamento);
		setIdLocalFuncionamento(idLocalFuncionamento);
	}
	public void setCdLocalFuncionamento(int cdLocalFuncionamento){
		this.cdLocalFuncionamento=cdLocalFuncionamento;
	}
	public int getCdLocalFuncionamento(){
		return this.cdLocalFuncionamento;
	}
	public void setNmLocalFuncionamento(String nmLocalFuncionamento){
		this.nmLocalFuncionamento=nmLocalFuncionamento;
	}
	public String getNmLocalFuncionamento(){
		return this.nmLocalFuncionamento;
	}
	public void setIdLocalFuncionamento(String idLocalFuncionamento){
		this.idLocalFuncionamento=idLocalFuncionamento;
	}
	public String getIdLocalFuncionamento(){
		return this.idLocalFuncionamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLocalFuncionamento: " +  getCdLocalFuncionamento();
		valueToString += ", nmLocalFuncionamento: " +  getNmLocalFuncionamento();
		valueToString += ", idLocalFuncionamento: " +  getIdLocalFuncionamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LocalFuncionamento(getCdLocalFuncionamento(),
			getNmLocalFuncionamento(),
			getIdLocalFuncionamento());
	}

}