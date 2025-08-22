package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaUnidade {

	private int cdMatricula;
	private int cdUnidade;
	private int cdCurso;
	private String txtParecer;
	private GregorianCalendar dtParecer;

	public MatriculaUnidade() { }

	public MatriculaUnidade(int cdMatricula,
			int cdUnidade,
			int cdCurso,
			String txtParecer,
			GregorianCalendar dtParecer) {
		setCdMatricula(cdMatricula);
		setCdUnidade(cdUnidade);
		setCdCurso(cdCurso);
		setTxtParecer(txtParecer);
		setDtParecer(dtParecer);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdUnidade(int cdUnidade){
		this.cdUnidade=cdUnidade;
	}
	public int getCdUnidade(){
		return this.cdUnidade;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setTxtParecer(String txtParecer){
		this.txtParecer=txtParecer;
	}
	public String getTxtParecer(){
		return this.txtParecer;
	}
	public void setDtParecer(GregorianCalendar dtParecer){
		this.dtParecer=dtParecer;
	}
	public GregorianCalendar getDtParecer(){
		return this.dtParecer;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdUnidade: " +  getCdUnidade();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", txtParecer: " +  getTxtParecer();
		valueToString += ", dtParecer: " +  sol.util.Util.formatDateTime(getDtParecer(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaUnidade(getCdMatricula(),
			getCdUnidade(),
			getCdCurso(),
			getTxtParecer(),
			getDtParecer()==null ? null : (GregorianCalendar)getDtParecer().clone());
	}

}