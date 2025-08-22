package com.tivic.manager.mob;

public class GrupoParadaInstituicao {

	private int cdGrupoParada;
	private int cdParada;
	private int cdInstituicao;

	public GrupoParadaInstituicao() { }

	public GrupoParadaInstituicao(int cdGrupoParada,
			int cdParada,
			int cdInstituicao) {
		setCdGrupoParada(cdGrupoParada);
		setCdParada(cdParada);
		setCdInstituicao(cdInstituicao);
	}
	public void setCdGrupoParada(int cdGrupoParada){
		this.cdGrupoParada=cdGrupoParada;
	}
	public int getCdGrupoParada(){
		return this.cdGrupoParada;
	}
	public void setCdParada(int cdParada){
		this.cdParada=cdParada;
	}
	public int getCdParada(){
		return this.cdParada;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupoParada: " +  getCdGrupoParada();
		valueToString += ", cdParada: " +  getCdParada();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GrupoParadaInstituicao(getCdGrupoParada(),
			getCdParada(),
			getCdInstituicao());
	}

}