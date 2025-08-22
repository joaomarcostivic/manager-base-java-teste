package com.tivic.manager.prc;

public class ObjetoAcao {

	private int cdObjetoAcao;
	private String nmObjetoAcao;
	private String idObjetoAcao;

	public ObjetoAcao() { }
			
	public ObjetoAcao(int cdObjetoAcao,
			String nmObjetoAcao,
			String idObjetoAcao){
		setCdObjetoAcao(cdObjetoAcao);
		setNmObjetoAcao(nmObjetoAcao);
		setIdObjetoAcao(idObjetoAcao);
	}
	public void setCdObjetoAcao(int cdObjetoAcao){
		this.cdObjetoAcao=cdObjetoAcao;
	}
	public int getCdObjetoAcao(){
		return this.cdObjetoAcao;
	}
	public void setNmObjetoAcao(String nmObjetoAcao){
		this.nmObjetoAcao=nmObjetoAcao;
	}
	public String getNmObjetoAcao(){
		return this.nmObjetoAcao;
	}
	public void setIdObjetoAcao(String idObjetoAcao){
		this.idObjetoAcao=idObjetoAcao;
	}
	public String getIdObjetoAcao(){
		return this.idObjetoAcao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdObjetoAcao: " +  getCdObjetoAcao();
		valueToString += ", nmObjetoAcao: " +  getNmObjetoAcao();
		valueToString += ", idObjetoAcao: " +  getIdObjetoAcao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ObjetoAcao(getCdObjetoAcao(),
			getNmObjetoAcao(),
			getIdObjetoAcao());
	}

}