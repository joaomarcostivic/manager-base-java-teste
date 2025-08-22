package com.tivic.manager.cae;

public class PreparacaoIngrediente {

	private int cdPreparacao;
	private int cdIngrediente;
	private float vlUtilizado;

	public PreparacaoIngrediente(){ }

	public PreparacaoIngrediente(int cdPreparacao,
			int cdIngrediente,
			float vlUtilizado){
		setCdPreparacao(cdPreparacao);
		setCdIngrediente(cdIngrediente);
		setVlUtilizado(vlUtilizado);
	}
	public void setCdPreparacao(int cdPreparacao){
		this.cdPreparacao=cdPreparacao;
	}
	public int getCdPreparacao(){
		return this.cdPreparacao;
	}
	public void setCdIngrediente(int cdIngrediente){
		this.cdIngrediente=cdIngrediente;
	}
	public int getCdIngrediente(){
		return this.cdIngrediente;
	}
	public void setVlUtilizado(float vlUtilizado){
		this.vlUtilizado=vlUtilizado;
	}
	public float getVlUtilizado(){
		return this.vlUtilizado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPreparacao: " +  getCdPreparacao();
		valueToString += ", cdIngrediente: " +  getCdIngrediente();
		valueToString += ", vlUtilizado: " +  getVlUtilizado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PreparacaoIngrediente(getCdPreparacao(),
			getCdIngrediente(),
			getVlUtilizado());
	}

}
