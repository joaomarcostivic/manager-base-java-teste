package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class Programa {

	private int cdPrograma;
	private String nmPrograma;
	private int tpEsfera;
	private String txtPrograma;
	private GregorianCalendar dtRegistro;
	private int tpPrograma;
	private int stPrograma;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private String idPrograma;
	private int lgGerenciadoFinanceiramente;

	public Programa() { }

	public Programa(int cdPrograma,
			String nmPrograma,
			int tpEsfera,
			String txtPrograma,
			GregorianCalendar dtRegistro,
			int tpPrograma,
			int stPrograma,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			String idPrograma,
			int lgGerenciadoFinanceiramente) {
		setCdPrograma(cdPrograma);
		setNmPrograma(nmPrograma);
		setTpEsfera(tpEsfera);
		setTxtPrograma(txtPrograma);
		setDtRegistro(dtRegistro);
		setTpPrograma(tpPrograma);
		setStPrograma(stPrograma);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setIdPrograma(idPrograma);
		setLgGerenciadoFinanceiramente(lgGerenciadoFinanceiramente);
	}
	public void setCdPrograma(int cdPrograma){
		this.cdPrograma=cdPrograma;
	}
	public int getCdPrograma(){
		return this.cdPrograma;
	}
	public void setNmPrograma(String nmPrograma){
		this.nmPrograma=nmPrograma;
	}
	public String getNmPrograma(){
		return this.nmPrograma;
	}
	public void setTpEsfera(int tpEsfera){
		this.tpEsfera=tpEsfera;
	}
	public int getTpEsfera(){
		return this.tpEsfera;
	}
	public void setTxtPrograma(String txtPrograma){
		this.txtPrograma=txtPrograma;
	}
	public String getTxtPrograma(){
		return this.txtPrograma;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro){
		this.dtRegistro=dtRegistro;
	}
	public GregorianCalendar getDtRegistro(){
		return this.dtRegistro;
	}
	public void setTpPrograma(int tpPrograma){
		this.tpPrograma=tpPrograma;
	}
	public int getTpPrograma(){
		return this.tpPrograma;
	}
	public void setStPrograma(int stPrograma){
		this.stPrograma=stPrograma;
	}
	public int getStPrograma(){
		return this.stPrograma;
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
	public void setIdPrograma(String idPrograma){
		this.idPrograma=idPrograma;
	}
	public String getIdPrograma(){
		return this.idPrograma;
	}
	public void setLgGerenciadoFinanceiramente(int lgGerenciadoFinanceiramente){
		this.lgGerenciadoFinanceiramente=lgGerenciadoFinanceiramente;
	}
	public int getLgGerenciadoFinanceiramente(){
		return this.lgGerenciadoFinanceiramente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPrograma: " +  getCdPrograma();
		valueToString += ", nmPrograma: " +  getNmPrograma();
		valueToString += ", tpEsfera: " +  getTpEsfera();
		valueToString += ", txtPrograma: " +  getTxtPrograma();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpPrograma: " +  getTpPrograma();
		valueToString += ", stPrograma: " +  getStPrograma();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idPrograma: " +  getIdPrograma();
		valueToString += ", lgGerenciadoFinanceiramente: " +  getLgGerenciadoFinanceiramente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Programa(getCdPrograma(),
			getNmPrograma(),
			getTpEsfera(),
			getTxtPrograma(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone(),
			getTpPrograma(),
			getStPrograma(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getIdPrograma(),
			getLgGerenciadoFinanceiramente());
	}

}