package com.tivic.manager.acd;

public class CursoAreaInteresse {

	private int cdAreaInteresse;
	private int cdCurso;

	public CursoAreaInteresse(){}
			
	public CursoAreaInteresse(int cdAreaInteresse,
			int cdCurso){
		setCdAreaInteresse(cdAreaInteresse);
		setCdCurso(cdCurso);
	}
	public void setCdAreaInteresse(int cdAreaInteresse){
		this.cdAreaInteresse=cdAreaInteresse;
	}
	public int getCdAreaInteresse(){
		return this.cdAreaInteresse;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAreaInteresse: " +  getCdAreaInteresse();
		valueToString += ", cdCurso: " +  getCdCurso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoAreaInteresse(getCdAreaInteresse(),
			getCdCurso());
	}

}
