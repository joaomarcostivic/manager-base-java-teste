package com.tivic.manager.acd;

public class CursoMulti {

	private int cdCursoMulti;
	private int cdCurso;

	public CursoMulti(){ }

	public CursoMulti(int cdCursoMulti,
			int cdCurso){
		setCdCursoMulti(cdCursoMulti);
		setCdCurso(cdCurso);
	}
	public void setCdCursoMulti(int cdCursoMulti){
		this.cdCursoMulti=cdCursoMulti;
	}
	public int getCdCursoMulti(){
		return this.cdCursoMulti;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCursoMulti: " +  getCdCursoMulti();
		valueToString += ", cdCurso: " +  getCdCurso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoMulti(getCdCursoMulti(),
			getCdCurso());
	}

}