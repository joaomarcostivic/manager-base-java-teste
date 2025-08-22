package com.tivic.manager.mob;

public class Parada {

	private int cdParada;
	private int cdGrupoParada;
	private int cdConcessao;
	private int cdLogradouro;
	private String dsReferencia;
	private int cdGeorreferencia;
	private String nmPontoReferencia;

	public Parada() { }

	public Parada(int cdParada,
			int cdGrupoParada,
			int cdConcessao,
			int cdLogradouro,
			String dsReferencia,
			int cdGeorreferencia,
			String nmPontoReferencia) {
		setCdParada(cdParada);
		setCdGrupoParada(cdGrupoParada);
		setCdConcessao(cdConcessao);
		setCdLogradouro(cdLogradouro);
		setDsReferencia(dsReferencia);
		setCdGeorreferencia(cdGeorreferencia);
		setNmPontoReferencia(nmPontoReferencia);
	}
	public void setCdParada(int cdParada){
		this.cdParada=cdParada;
	}
	public int getCdParada(){
		return this.cdParada;
	}
	public void setCdGrupoParada(int cdGrupoParada){
		this.cdGrupoParada=cdGrupoParada;
	}
	public int getCdGrupoParada(){
		return this.cdGrupoParada;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public void setDsReferencia(String dsReferencia){
		this.dsReferencia=dsReferencia;
	}
	public String getDsReferencia(){
		return this.dsReferencia;
	}
	public void setCdGeorreferencia(int cdGeorreferencia){
		this.cdGeorreferencia=cdGeorreferencia;
	}
	public int getCdGeorreferencia(){
		return this.cdGeorreferencia;
	}
	public void setNmPontoReferencia(String nmPontoReferencia){
		this.nmPontoReferencia=nmPontoReferencia;
	}
	public String getNmPontoReferencia(){
		return this.nmPontoReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdParada: " +  getCdParada();
		valueToString += ", cdGrupoParada: " +  getCdGrupoParada();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		valueToString += ", dsReferencia: " +  getDsReferencia();
		valueToString += ", cdGeorreferencia: " +  getCdGeorreferencia();
		valueToString += ", nmPontoReferencia: " +  getNmPontoReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Parada(getCdParada(),
			getCdGrupoParada(),
			getCdConcessao(),
			getCdLogradouro(),
			getDsReferencia(),
			getCdGeorreferencia(),
			getNmPontoReferencia());
	}

}