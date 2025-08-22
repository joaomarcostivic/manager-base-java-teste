package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class MatriculaMovimentacao {

	private int cdMatricula;
	private int cdMovimentacao;
	private int cdTipoMovimentacao;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private String nrDocumento;
	private int stMovimentacao;

	public MatriculaMovimentacao(int cdMatricula,
			int cdMovimentacao,
			int cdTipoMovimentacao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			String nrDocumento,
			int stMovimentacao){
		setCdMatricula(cdMatricula);
		setCdMovimentacao(cdMovimentacao);
		setCdTipoMovimentacao(cdTipoMovimentacao);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setNrDocumento(nrDocumento);
		setStMovimentacao(stMovimentacao);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdMovimentacao(int cdMovimentacao){
		this.cdMovimentacao=cdMovimentacao;
	}
	public int getCdMovimentacao(){
		return this.cdMovimentacao;
	}
	public void setCdTipoMovimentacao(int cdTipoMovimentacao){
		this.cdTipoMovimentacao=cdTipoMovimentacao;
	}
	public int getCdTipoMovimentacao(){
		return this.cdTipoMovimentacao;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setStMovimentacao(int stMovimentacao){
		this.stMovimentacao=stMovimentacao;
	}
	public int getStMovimentacao(){
		return this.stMovimentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdMovimentacao: " +  getCdMovimentacao();
		valueToString += ", cdTipoMovimentacao: " +  getCdTipoMovimentacao();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", stMovimentacao: " +  getStMovimentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaMovimentacao(getCdMatricula(),
			getCdMovimentacao(),
			getCdTipoMovimentacao(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getNrDocumento(),
			getStMovimentacao());
	}

}