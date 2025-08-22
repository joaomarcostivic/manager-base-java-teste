package com.tivic.manager.mob;

public class GrupoParada {

	private int cdGrupoParada;
	private int tpGrupoParada;
	private String nmGrupoParada;
	private int cdGeorreferencia;
	private int cdGrupoParadaSuperior;
	private int qtVagas;

	public GrupoParada(){ }

	public GrupoParada(int cdGrupoParada,
			int tpGrupoParada,
			String nmGrupoParada,
			int cdGeorreferencia,
			int cdGrupoParadaSuperior,
			int qtVagas){
		setCdGrupoParada(cdGrupoParada);
		setTpGrupoParada(tpGrupoParada);
		setNmGrupoParada(nmGrupoParada);
		setCdGeorreferencia(cdGeorreferencia);
		setCdGrupoParadaSuperior(cdGrupoParadaSuperior);
		setQtVagas(qtVagas);
	}

	public void setCdGrupoParada(int cdGrupoParada){
		this.cdGrupoParada=cdGrupoParada;
	}
	public int getCdGrupoParada(){
		return this.cdGrupoParada;
	}
	public void setTpGrupoParada(int tpGrupoParada){
		this.tpGrupoParada=tpGrupoParada;
	}
	public int getTpGrupoParada(){
		return this.tpGrupoParada;
	}
	public void setNmGrupoParada(String nmGrupoParada){
		this.nmGrupoParada=nmGrupoParada;
	}
	public String getNmGrupoParada(){
		return this.nmGrupoParada;
	}
	public void setCdGeorreferencia(int cdGeorreferencia){
		this.cdGeorreferencia=cdGeorreferencia;
	}
	public int getCdGeorreferencia(){
		return this.cdGeorreferencia;
	}
	public void setCdGrupoParadaSuperior(int cdGrupoParadaSuperior){
		this.cdGrupoParadaSuperior=cdGrupoParadaSuperior;
	}
	public int getCdGrupoParadaSuperior(){
		return this.cdGrupoParadaSuperior;
	}
	public int getQtVagas() {
		return this.qtVagas;
	}

	public void setQtVagas(int qtVagas) {
		this.qtVagas = qtVagas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoParada: " +  getCdGrupoParada();
		valueToString += ", tpGrupoParada: " +  getTpGrupoParada();
		valueToString += ", nmGrupoParada: " +  getNmGrupoParada();
		valueToString += ", cdGeorreferencia: " +  getCdGeorreferencia();
		valueToString += ", cdGrupoParadaSuperior: " +  getCdGrupoParadaSuperior();
		valueToString += ", qtVagas: " + getQtVagas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoParada(getCdGrupoParada(),
			getTpGrupoParada(),
			getNmGrupoParada(),
			getCdGeorreferencia(),
			getCdGrupoParadaSuperior(),
			getQtVagas());
	}

}
