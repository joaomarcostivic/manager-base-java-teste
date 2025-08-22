package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaTipoDocumentacao {

	private int cdMatricula;
	private int cdTipoDocumentacao;
	private int lgApresentacao;
	private byte[] blbCopia;
	private GregorianCalendar dtApresentacao;

	public MatriculaTipoDocumentacao(){ }

	public MatriculaTipoDocumentacao(int cdMatricula,
			int cdTipoDocumentacao,
			int lgApresentacao,
			byte[] blbCopia,
			GregorianCalendar dtApresentacao){
		setCdMatricula(cdMatricula);
		setCdTipoDocumentacao(cdTipoDocumentacao);
		setLgApresentacao(lgApresentacao);
		setBlbCopia(blbCopia);
		setDtApresentacao(dtApresentacao);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public void setLgApresentacao(int lgApresentacao){
		this.lgApresentacao=lgApresentacao;
	}
	public int getLgApresentacao(){
		return this.lgApresentacao;
	}
	public void setBlbCopia(byte[] blbCopia){
		this.blbCopia=blbCopia;
	}
	public byte[] getBlbCopia(){
		return this.blbCopia;
	}
	public void setDtApresentacao(GregorianCalendar dtApresentacao){
		this.dtApresentacao=dtApresentacao;
	}
	public GregorianCalendar getDtApresentacao(){
		return this.dtApresentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		valueToString += ", lgApresentacao: " +  getLgApresentacao();
		valueToString += ", blbCopia: " +  getBlbCopia();
		valueToString += ", dtApresentacao: " +  sol.util.Util.formatDateTime(getDtApresentacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaTipoDocumentacao(getCdMatricula(),
			getCdTipoDocumentacao(),
			getLgApresentacao(),
			getBlbCopia(),
			getDtApresentacao()==null ? null : (GregorianCalendar)getDtApresentacao().clone());
	}

}