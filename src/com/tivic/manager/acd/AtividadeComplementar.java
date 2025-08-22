package com.tivic.manager.acd;

public class AtividadeComplementar {

	private int cdAtividadeComplementar;
	private String nmAtividadeComplementar;
	private String idAtividadeComplementar;
	private int cdAtividadeSuperior;
	private int cdDisciplina;

	public AtividadeComplementar(){ }

	public AtividadeComplementar(int cdAtividadeComplementar,
			String nmAtividadeComplementar,
			String idAtividadeComplementar,
			int cdAtividadeSuperior,
			int cdDisciplina){
		setCdAtividadeComplementar(cdAtividadeComplementar);
		setNmAtividadeComplementar(nmAtividadeComplementar);
		setIdAtividadeComplementar(idAtividadeComplementar);
		setCdAtividadeSuperior(cdAtividadeSuperior);
		setCdDisciplina(cdDisciplina);
	}
	public void setCdAtividadeComplementar(int cdAtividadeComplementar){
		this.cdAtividadeComplementar=cdAtividadeComplementar;
	}
	public int getCdAtividadeComplementar(){
		return this.cdAtividadeComplementar;
	}
	public void setNmAtividadeComplementar(String nmAtividadeComplementar){
		this.nmAtividadeComplementar=nmAtividadeComplementar;
	}
	public String getNmAtividadeComplementar(){
		return this.nmAtividadeComplementar;
	}
	public void setIdAtividadeComplementar(String idAtividadeComplementar){
		this.idAtividadeComplementar=idAtividadeComplementar;
	}
	public String getIdAtividadeComplementar(){
		return this.idAtividadeComplementar;
	}
	public void setCdAtividadeSuperior(int cdAtividadeSuperior){
		this.cdAtividadeSuperior=cdAtividadeSuperior;
	}
	public int getCdAtividadeSuperior(){
		return this.cdAtividadeSuperior;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtividadeComplementar: " +  getCdAtividadeComplementar();
		valueToString += ", nmAtividadeComplementar: " +  getNmAtividadeComplementar();
		valueToString += ", idAtividadeComplementar: " +  getIdAtividadeComplementar();
		valueToString += ", cdAtividadeSuperior: " +  getCdAtividadeSuperior();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtividadeComplementar(getCdAtividadeComplementar(),
			getNmAtividadeComplementar(),
			getIdAtividadeComplementar(),
			getCdAtividadeSuperior(),
			getCdDisciplina());
	}

}