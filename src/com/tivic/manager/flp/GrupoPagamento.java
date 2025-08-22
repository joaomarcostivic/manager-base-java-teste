package com.tivic.manager.flp;

public class GrupoPagamento {

	private int cdGrupoPagamento;
	private String nmGrupoPagamento;
	private int cdEmpresa;
	private String idGrupoPagamento;

	public GrupoPagamento(){ }

	public GrupoPagamento(int cdGrupoPagamento,
			String nmGrupoPagamento,
			int cdEmpresa,
			String idGrupoPagamento){
		setCdGrupoPagamento(cdGrupoPagamento);
		setNmGrupoPagamento(nmGrupoPagamento);
		setCdEmpresa(cdEmpresa);
		setIdGrupoPagamento(idGrupoPagamento);
	}
	public void setCdGrupoPagamento(int cdGrupoPagamento){
		this.cdGrupoPagamento=cdGrupoPagamento;
	}
	public int getCdGrupoPagamento(){
		return this.cdGrupoPagamento;
	}
	public void setNmGrupoPagamento(String nmGrupoPagamento){
		this.nmGrupoPagamento=nmGrupoPagamento;
	}
	public String getNmGrupoPagamento(){
		return this.nmGrupoPagamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setIdGrupoPagamento(String idGrupoPagamento){
		this.idGrupoPagamento=idGrupoPagamento;
	}
	public String getIdGrupoPagamento(){
		return this.idGrupoPagamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoPagamento: " +  getCdGrupoPagamento();
		valueToString += ", nmGrupoPagamento: " +  getNmGrupoPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", idGrupoPagamento: " +  getIdGrupoPagamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoPagamento(getCdGrupoPagamento(),
			getNmGrupoPagamento(),
			getCdEmpresa(),
			getIdGrupoPagamento());
	}

}
