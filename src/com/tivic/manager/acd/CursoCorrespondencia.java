package com.tivic.manager.acd;

public class CursoCorrespondencia {

	private int cdCurso;
	private int cdCursoCorrespondencia;

	public CursoCorrespondencia() { }

	public CursoCorrespondencia(int cdCurso,
			int cdCursoCorrespondencia) {
		setCdCurso(cdCurso);
		setCdCursoCorrespondencia(cdCursoCorrespondencia);
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoCorrespondencia(int cdCursoCorrespondencia){
		this.cdCursoCorrespondencia=cdCursoCorrespondencia;
	}
	public int getCdCursoCorrespondencia(){
		return this.cdCursoCorrespondencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoCorrespondencia: " +  getCdCursoCorrespondencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoCorrespondencia(getCdCurso(),
			getCdCursoCorrespondencia());
	}

}