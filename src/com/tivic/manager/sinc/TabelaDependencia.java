package com.tivic.manager.sinc;

public class TabelaDependencia {

	private int cdDependente;
	private int cdProvedor;
	private String nmChaves;

	public TabelaDependencia(){ }

	public TabelaDependencia(int cdDependente,
			int cdProvedor,
			String nmChaves){
		setCdDependente(cdDependente);
		setCdProvedor(cdProvedor);
		setNmChaves(nmChaves);
	}
	public void setCdDependente(int cdDependente){
		this.cdDependente=cdDependente;
	}
	public int getCdDependente(){
		return this.cdDependente;
	}
	public void setCdProvedor(int cdProvedor){
		this.cdProvedor=cdProvedor;
	}
	public int getCdProvedor(){
		return this.cdProvedor;
	}
	public void setNmChaves(String nmChaves) {
		this.nmChaves = nmChaves;
	}
	public String getNmChaves() {
		return nmChaves;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDependente: " +  getCdDependente();
		valueToString += ", cdProvedor: " +  getCdProvedor();
		valueToString += ", nmChaves: " +  getNmChaves();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaDependencia(getCdDependente(),
			getCdProvedor(),
			getNmChaves());
	}

}