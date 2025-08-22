package com.tivic.manager.acd;

public class CursoNucleo {

	private int cdNucleo;
	private int cdCurso;
	private String nmNucleo;
	private float vlCargaHoraria;
	private int lgObrigatorio;

	public CursoNucleo(){}
	
	public CursoNucleo(int cdNucleo,
			int cdCurso,
			String nmNucleo,
			float vlCargaHoraria,
			int lgObrigatorio){
		setCdNucleo(cdNucleo);
		setCdCurso(cdCurso);
		setNmNucleo(nmNucleo);
		setVlCargaHoraria(vlCargaHoraria);
		setLgObrigatorio(lgObrigatorio);
	}
	public void setCdNucleo(int cdNucleo){
		this.cdNucleo=cdNucleo;
	}
	public int getCdNucleo(){
		return this.cdNucleo;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setNmNucleo(String nmNucleo){
		this.nmNucleo=nmNucleo;
	}
	public String getNmNucleo(){
		return this.nmNucleo;
	}
	public void setVlCargaHoraria(float vlCargaHoraria){
		this.vlCargaHoraria=vlCargaHoraria;
	}
	public float getVlCargaHoraria(){
		return this.vlCargaHoraria;
	}
	public void setLgObrigatorio(int lgObrigatorio){
		this.lgObrigatorio=lgObrigatorio;
	}
	public int getLgObrigatorio(){
		return this.lgObrigatorio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdNucleo: " +  getCdNucleo();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", nmNucleo: " +  getNmNucleo();
		valueToString += ", vlCargaHoraria: " +  getVlCargaHoraria();
		valueToString += ", lgObrigatorio: " +  getLgObrigatorio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoNucleo(getCdNucleo(),
			getCdCurso(),
			getNmNucleo(),
			getVlCargaHoraria(),
			getLgObrigatorio());
	}

}
