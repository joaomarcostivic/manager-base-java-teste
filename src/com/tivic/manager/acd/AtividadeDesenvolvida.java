package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class AtividadeDesenvolvida {

	private int cdAtividadeDesenvolvida;
	private GregorianCalendar dtAtividadeDesenvolvida;
	private String txtAtividadeDesenvolvida;
	private int qtAulas;
	private int cdTurma;
	private int cdOferta;
	private int cdProfessor;

	public AtividadeDesenvolvida() { }

	public AtividadeDesenvolvida(int cdAtividadeDesenvolvida,
			GregorianCalendar dtAtividadeDesenvolvida,
			String txtAtividadeDesenvolvida,
			int qtAulas,
			int cdTurma,
			int cdOferta,
			int cdProfessor) {
		setCdAtividadeDesenvolvida(cdAtividadeDesenvolvida);
		setDtAtividadeDesenvolvida(dtAtividadeDesenvolvida);
		setTxtAtividadeDesenvolvida(txtAtividadeDesenvolvida);
		setQtAulas(qtAulas);
		setCdTurma(cdTurma);
		setCdOferta(cdOferta);
		setCdProfessor(cdProfessor);
	}
	public void setCdAtividadeDesenvolvida(int cdAtividadeDesenvolvida){
		this.cdAtividadeDesenvolvida=cdAtividadeDesenvolvida;
	}
	public int getCdAtividadeDesenvolvida(){
		return this.cdAtividadeDesenvolvida;
	}
	public void setDtAtividadeDesenvolvida(GregorianCalendar dtAtividadeDesenvolvida){
		this.dtAtividadeDesenvolvida=dtAtividadeDesenvolvida;
	}
	public GregorianCalendar getDtAtividadeDesenvolvida(){
		return this.dtAtividadeDesenvolvida;
	}
	public void setTxtAtividadeDesenvolvida(String txtAtividadeDesenvolvida){
		this.txtAtividadeDesenvolvida=txtAtividadeDesenvolvida;
	}
	public String getTxtAtividadeDesenvolvida(){
		return this.txtAtividadeDesenvolvida;
	}
	public void setQtAulas(int qtAulas){
		this.qtAulas=qtAulas;
	}
	public int getQtAulas(){
		return this.qtAulas;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "\"cdAtividadeDesenvolvida\": " +  getCdAtividadeDesenvolvida();
		valueToString += ", \"dtAtividadeDesenvolvida\": \"" +  sol.util.Util.formatDateTime(getDtAtividadeDesenvolvida(), "yyyy-MM-dd", "")+"\"";
		valueToString += ", \"txtAtividadeDesenvolvida\": \"" +  getTxtAtividadeDesenvolvida()+ "\"";
		valueToString += ", \"qtAulas\": " +  getQtAulas();
		valueToString += ", \"cdTurma\": " +  getCdTurma();
		valueToString += ", \"cdOferta\": " +  getCdOferta();
		valueToString += ", \"cdProfessor\": " +  getCdProfessor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AtividadeDesenvolvida(getCdAtividadeDesenvolvida(),
			getDtAtividadeDesenvolvida()==null ? null : (GregorianCalendar)getDtAtividadeDesenvolvida().clone(),
			getTxtAtividadeDesenvolvida(),
			getQtAulas(),
			getCdTurma(),
			getCdOferta(),
			getCdProfessor());
	}

}