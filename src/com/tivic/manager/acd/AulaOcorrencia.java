package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class AulaOcorrencia {

	private int cdAula;
	private int cdOcorrencia;
	private String txtOcorrencia;
	private GregorianCalendar dtOcorrencia;

	public AulaOcorrencia(){ }

	public AulaOcorrencia(int cdAula,
			int cdOcorrencia,
			String txtOcorrencia,
			GregorianCalendar dtOcorrencia){
		setCdAula(cdAula);
		setCdOcorrencia(cdOcorrencia);
		setTxtOcorrencia(txtOcorrencia);
		setDtOcorrencia(dtOcorrencia);
	}
	public void setCdAula(int cdAula){
		this.cdAula=cdAula;
	}
	public int getCdAula(){
		return this.cdAula;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setTxtOcorrencia(String txtOcorrencia){
		this.txtOcorrencia=txtOcorrencia;
	}
	public String getTxtOcorrencia(){
		return this.txtOcorrencia;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAula: " +  getCdAula();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", txtOcorrencia: " +  getTxtOcorrencia();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AulaOcorrencia(getCdAula(),
			getCdOcorrencia(),
			getTxtOcorrencia(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone());
	}

}