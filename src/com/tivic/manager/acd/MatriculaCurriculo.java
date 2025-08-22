package com.tivic.manager.acd;

public class MatriculaCurriculo {

	private int cdMatricula;
	private int cdMatriz;
	private int cdCurso;
	private int cdCursoModulo;
	private int cdDisciplina;

	public MatriculaCurriculo(){ }

	public MatriculaCurriculo(int cdMatricula,
			int cdMatriz,
			int cdCurso,
			int cdCursoModulo,
			int cdDisciplina){
		setCdMatricula(cdMatricula);
		setCdMatriz(cdMatriz);
		setCdCurso(cdCurso);
		setCdCursoModulo(cdCursoModulo);
		setCdDisciplina(cdDisciplina);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaCurriculo(getCdMatricula(),
			getCdMatriz(),
			getCdCurso(),
			getCdCursoModulo(),
			getCdDisciplina());
	}

}