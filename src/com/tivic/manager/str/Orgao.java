package com.tivic.manager.str;

public class Orgao {

	private int cdOrgao;
	private String nmOrgao;
	private String idOrgao;
	private String nmSuborgao;
	private String nmCoordenador;
	private int cdCidade;

	public Orgao(){ }

	public Orgao(int cdOrgao,
			String nmOrgao,
			String idOrgao,
			String nmSuborgao,
			String nmCoordenador,
			int cdCidade){
		setCdOrgao(cdOrgao);
		setNmOrgao(nmOrgao);
		setIdOrgao(idOrgao);
		setNmSuborgao(nmSuborgao);
		setNmCoordenador(nmCoordenador);
		setCdCidade(cdCidade);
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNmOrgao(String nmOrgao){
		this.nmOrgao=nmOrgao;
	}
	public String getNmOrgao(){
		return this.nmOrgao;
	}
	public void setIdOrgao(String idOrgao){
		this.idOrgao=idOrgao;
	}
	public String getIdOrgao(){
		return this.idOrgao;
	}
	public void setNmSuborgao(String nmSuborgao){
		this.nmSuborgao=nmSuborgao;
	}
	public String getNmSuborgao(){
		return this.nmSuborgao;
	}
	public void setNmCoordenador(String nmCoordenador){
		this.nmCoordenador=nmCoordenador;
	}
	public String getNmCoordenador(){
		return this.nmCoordenador;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgao: " +  getCdOrgao();
		valueToString += ", nmOrgao: " +  getNmOrgao();
		valueToString += ", idOrgao: " +  getIdOrgao();
		valueToString += ", nmSuborgao: " +  getNmSuborgao();
		valueToString += ", nmCoordenador: " +  getNmCoordenador();
		valueToString += ", cdCidade: " +  getCdCidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Orgao(getCdOrgao(),
			getNmOrgao(),
			getIdOrgao(),
			getNmSuborgao(),
			getNmCoordenador(),
			getCdCidade());
	}

}
