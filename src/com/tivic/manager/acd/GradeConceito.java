package com.tivic.manager.acd;

public class GradeConceito {

	private int cdConceito;
	private int cdCurso;
	private int cdGrade;
	private String nmConceito;
	private float vlMinimo;
	private float vlMaximo;
	private int lgAprovado;

	public GradeConceito() { }
	
	public GradeConceito(int cdConceito,
			int cdCurso,
			int cdGrade,
			String nmConceito,
			float vlMinimo,
			float vlMaximo,
			int lgAprovado){
		setCdConceito(cdConceito);
		setCdCurso(cdCurso);
		setCdGrade(cdGrade);
		setNmConceito(nmConceito);
		setVlMinimo(vlMinimo);
		setVlMaximo(vlMaximo);
		setLgAprovado(lgAprovado);
	}
	public void setCdConceito(int cdConceito){
		this.cdConceito=cdConceito;
	}
	public int getCdConceito(){
		return this.cdConceito;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdGrade(int cdGrade){
		this.cdGrade=cdGrade;
	}
	public int getCdGrade(){
		return this.cdGrade;
	}
	public void setNmConceito(String nmConceito){
		this.nmConceito=nmConceito;
	}
	public String getNmConceito(){
		return this.nmConceito;
	}
	public void setVlMinimo(float vlMinimo){
		this.vlMinimo=vlMinimo;
	}
	public float getVlMinimo(){
		return this.vlMinimo;
	}
	public void setVlMaximo(float vlMaximo){
		this.vlMaximo=vlMaximo;
	}
	public float getVlMaximo(){
		return this.vlMaximo;
	}
	public void setLgAprovado(int lgAprovado){
		this.lgAprovado=lgAprovado;
	}
	public int getLgAprovado(){
		return this.lgAprovado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConceito: " +  getCdConceito();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdGrade: " +  getCdGrade();
		valueToString += ", nmConceito: " +  getNmConceito();
		valueToString += ", vlMinimo: " +  getVlMinimo();
		valueToString += ", vlMaximo: " +  getVlMaximo();
		valueToString += ", lgAprovado: " +  getLgAprovado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GradeConceito(cdConceito,
			cdCurso,
			cdGrade,
			nmConceito,
			vlMinimo,
			vlMaximo,
			lgAprovado);
	}

}