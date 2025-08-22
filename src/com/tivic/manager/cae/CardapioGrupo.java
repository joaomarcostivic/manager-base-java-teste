package com.tivic.manager.cae;

import java.util.GregorianCalendar;

public class CardapioGrupo {

	private int cdCardapioGrupo;
	private String nmCardapioGrupo;
	private GregorianCalendar dtInicialGrupo;
	private GregorianCalendar dtFinalGrupo;
	private int cdModalidade;
	private int tpCardapioGrupo;

	public CardapioGrupo(){ }

	public CardapioGrupo(int cdCardapioGrupo,
			String nmCardapioGrupo,
			GregorianCalendar dtInicialGrupo,
			GregorianCalendar dtFinalGrupo,
			int cdModalidade,
			int tpCardapioGrupo){
		setCdCardapioGrupo(cdCardapioGrupo);
		setNmCardapioGrupo(nmCardapioGrupo);
		setDtInicialGrupo(dtInicialGrupo);
		setDtFinalGrupo(dtFinalGrupo);
		setCdModalidade(cdModalidade);
		setTpCardapioGrupo(tpCardapioGrupo);
	}
	public void setCdCardapioGrupo(int cdCardapioGrupo){
		this.cdCardapioGrupo=cdCardapioGrupo;
	}
	public int getCdCardapioGrupo(){
		return this.cdCardapioGrupo;
	}
	public void setNmCardapioGrupo(String nmCardapioGrupo){
		this.nmCardapioGrupo=nmCardapioGrupo;
	}
	public String getNmCardapioGrupo(){
		return this.nmCardapioGrupo;
	}
	public void setDtInicialGrupo(GregorianCalendar dtInicialGrupo){
		this.dtInicialGrupo=dtInicialGrupo;
	}
	public GregorianCalendar getDtInicialGrupo(){
		return this.dtInicialGrupo;
	}
	public void setDtFinalGrupo(GregorianCalendar dtFinalGrupo){
		this.dtFinalGrupo=dtFinalGrupo;
	}
	public GregorianCalendar getDtFinalGrupo(){
		return this.dtFinalGrupo;
	}
	public void setCdModalidade(int cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public int getCdModalidade(){
		return this.cdModalidade;
	}
	public void setTpCardapioGrupo(int tpCardapioGrupo){
		this.tpCardapioGrupo=tpCardapioGrupo;
	}
	public int getTpCardapioGrupo(){
		return this.tpCardapioGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCardapioGrupo: " +  getCdCardapioGrupo();
		valueToString += ", nmCardapioGrupo: " +  getNmCardapioGrupo();
		valueToString += ", dtInicialGrupo: " +  sol.util.Util.formatDateTime(getDtInicialGrupo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalGrupo: " +  sol.util.Util.formatDateTime(getDtFinalGrupo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdModalidade: " +  getCdModalidade();
		valueToString += ", tpCardapioGrupo: " +  getTpCardapioGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CardapioGrupo(getCdCardapioGrupo(),
			getNmCardapioGrupo(),
			getDtInicialGrupo()==null ? null : (GregorianCalendar)getDtInicialGrupo().clone(),
			getDtFinalGrupo()==null ? null : (GregorianCalendar)getDtFinalGrupo().clone(),
			getCdModalidade(),
			getTpCardapioGrupo());
	}

}