package com.tivic.manager.acd;

public class MatriculaModulo {

	private int cdMatricula;
	private int cdCursoModulo;
	private int cdPeriodoLetivo;

	public MatriculaModulo(){ }

	public MatriculaModulo(int cdMatricula,
			int cdCursoModulo,
			int cdPeriodoLetivo){
		setCdMatricula(cdMatricula);
		setCdCursoModulo(cdCursoModulo);
		setCdPeriodoLetivo(cdPeriodoLetivo);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaModulo(getCdMatricula(),
			getCdCursoModulo(),
			getCdPeriodoLetivo());
	}

}
