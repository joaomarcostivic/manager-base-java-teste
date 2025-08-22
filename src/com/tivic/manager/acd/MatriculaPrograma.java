package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaPrograma {

	private int cdMatricula;
	private int cdPrograma;
	private String nrMatriculaPrograma;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtTermino;
	private int stMatriculaPrograma;
	private int cdTurma;

	public MatriculaPrograma(){ }

	public MatriculaPrograma(int cdMatricula,
			int cdPrograma,
			String nrMatriculaPrograma,
			GregorianCalendar dtInicio,
			GregorianCalendar dtTermino,
			int stMatriculaPrograma,
			int cdTurma){
		setCdMatricula(cdMatricula);
		setCdPrograma(cdPrograma);
		setNrMatriculaPrograma(nrMatriculaPrograma);
		setDtInicio(dtInicio);
		setDtTermino(dtTermino);
		setStMatriculaPrograma(stMatriculaPrograma);
		setCdTurma(cdTurma);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdPrograma(int cdPrograma){
		this.cdPrograma=cdPrograma;
	}
	public int getCdPrograma(){
		return this.cdPrograma;
	}
	public void setNrMatriculaPrograma(String nrMatriculaPrograma){
		this.nrMatriculaPrograma=nrMatriculaPrograma;
	}
	public String getNrMatriculaPrograma(){
		return this.nrMatriculaPrograma;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setStMatriculaPrograma(int stMatriculaPrograma){
		this.stMatriculaPrograma=stMatriculaPrograma;
	}
	public int getStMatriculaPrograma(){
		return this.stMatriculaPrograma;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdPrograma: " +  getCdPrograma();
		valueToString += ", nrMatriculaPrograma: " +  getNrMatriculaPrograma();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtTermino: " +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stMatriculaPrograma: " +  getStMatriculaPrograma();
		valueToString += ", cdTurma: " +  getCdTurma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaPrograma(getCdMatricula(),
			getCdPrograma(),
			getNrMatriculaPrograma(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getStMatriculaPrograma(),
			getCdTurma());
	}

}