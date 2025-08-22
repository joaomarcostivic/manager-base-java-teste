package com.tivic.manager.adm;

public class FaixaRenda {

	private int cdFaixaRenda;
	private String nmFaixaRenda;
	private String idFaixaRenda;
	private float vlInicial;
	private float vlFinal;
	private int stFaixaRenda;
	private int tpRenda;

	public FaixaRenda(int cdFaixaRenda,
			String nmFaixaRenda,
			String idFaixaRenda,
			float vlInicial,
			float vlFinal,
			int stFaixaRenda,
			int tpRenda){
		setCdFaixaRenda(cdFaixaRenda);
		setNmFaixaRenda(nmFaixaRenda);
		setIdFaixaRenda(idFaixaRenda);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
		setStFaixaRenda(stFaixaRenda);
		setTpRenda(tpRenda);
	}
	public void setCdFaixaRenda(int cdFaixaRenda){
		this.cdFaixaRenda=cdFaixaRenda;
	}
	public int getCdFaixaRenda(){
		return this.cdFaixaRenda;
	}
	public void setNmFaixaRenda(String nmFaixaRenda){
		this.nmFaixaRenda=nmFaixaRenda;
	}
	public String getNmFaixaRenda(){
		return this.nmFaixaRenda;
	}
	public void setIdFaixaRenda(String idFaixaRenda){
		this.idFaixaRenda=idFaixaRenda;
	}
	public String getIdFaixaRenda(){
		return this.idFaixaRenda;
	}
	public void setVlInicial(float vlInicial){
		this.vlInicial=vlInicial;
	}
	public float getVlInicial(){
		return this.vlInicial;
	}
	public void setVlFinal(float vlFinal){
		this.vlFinal=vlFinal;
	}
	public float getVlFinal(){
		return this.vlFinal;
	}
	public void setStFaixaRenda(int stFaixaRenda){
		this.stFaixaRenda=stFaixaRenda;
	}
	public int getStFaixaRenda(){
		return this.stFaixaRenda;
	}
	public void setTpRenda(int tpRenda){
		this.tpRenda=tpRenda;
	}
	public int getTpRenda(){
		return this.tpRenda;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFaixaRenda: " +  getCdFaixaRenda();
		valueToString += ", nmFaixaRenda: " +  getNmFaixaRenda();
		valueToString += ", idFaixaRenda: " +  getIdFaixaRenda();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", stFaixaRenda: " +  getStFaixaRenda();
		valueToString += ", tpRenda: " +  getTpRenda();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FaixaRenda(getCdFaixaRenda(),
			getNmFaixaRenda(),
			getIdFaixaRenda(),
			getVlInicial(),
			getVlFinal(),
			getStFaixaRenda(),
			getTpRenda());
	}

}