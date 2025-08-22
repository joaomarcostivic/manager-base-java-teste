package com.tivic.manager.adm;

public class Cobranca {

	private int cdCobranca;
	private String idCobranca;
	private String nmCobranca;
	private String nmDescricao;
	private int stCobranca;
	private int lgPadrao;
	private int lgEnviaCartasCobranca;
	
	public Cobranca(int cdCobranca,
			String idCobranca,
			String nmCobranca,
			String nmDescricao,
			int stCobranca,
			int lgPadrao,
			int lgEnviaCartasCobranca){
		setCdCobranca(cdCobranca);
		setIdCobranca(idCobranca);
		setNmCobranca(nmCobranca);
		setNmDescricao(nmDescricao);
		setStCobranca(stCobranca);
		setLgPadrao(lgPadrao);
		setLgEnviaCartasCobranca(lgEnviaCartasCobranca);
	}
	public void setCdCobranca(int cdCobranca){
		this.cdCobranca=cdCobranca;
	}
	public int getCdCobranca(){
		return this.cdCobranca;
	}
	public void setIdCobranca(String idCobranca){
		this.idCobranca=idCobranca;
	}
	public String getIdCobranca(){
		return this.idCobranca;
	}
	public void setNmCobranca(String nmCobranca){
		this.nmCobranca=nmCobranca;
	}
	public String getNmCobranca(){
		return this.nmCobranca;
	}
	public void setNmDescricao(String nmDescricao){
		this.nmDescricao=nmDescricao;
	}
	public String getNmDescricao(){
		return this.nmDescricao;
	}
	public void setStCobranca(int stCobranca){
		this.stCobranca=stCobranca;
	}
	public int getStCobranca(){
		return this.stCobranca;
	}
	public void setLgPadrao(int lgPadrao){
		this.lgPadrao=lgPadrao;
	}
	public int getLgPadrao(){
		return this.lgPadrao;
	}
	public void setLgEnviaCartasCobranca(int lgEnviaCartasCobranca){
		this.lgEnviaCartasCobranca=lgEnviaCartasCobranca;
	}
	public int getLgEnviaCartasCobranca(){
		return this.lgEnviaCartasCobranca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCobranca: " +  getCdCobranca();
		valueToString += ", idCobranca: " +  getIdCobranca();
		valueToString += ", nmCobranca: " +  getNmCobranca();
		valueToString += ", nmDescricao: " +  getNmDescricao();
		valueToString += ", stCobranca: " +  getStCobranca();
		valueToString += ", lgPadrao: " +  getLgPadrao();
		valueToString += ", lgEnviaCartasCobranca: " +  getLgEnviaCartasCobranca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cobranca(getCdCobranca(),
			getIdCobranca(),
			getNmCobranca(),
			getNmDescricao(),
			getStCobranca(),
			getLgPadrao(),
			getLgEnviaCartasCobranca());
	}

}