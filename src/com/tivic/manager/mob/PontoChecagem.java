package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class PontoChecagem {

	private int cdPontoChecagem;
	private int cdReferencia;
	private int cdPessoa;
	private String nmPontoChecagem;
	private int tpPontoChecagem;
	private int stPontoChecagem;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;

	public PontoChecagem() { }

	public PontoChecagem(int cdPontoChecagem,
			int cdReferencia,
			int cdPessoa,
			String nmPontoChecagem,
			int tpPontoChecagem,
			int stPontoChecagem,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal) {
		setCdPontoChecagem(cdPontoChecagem);
		setCdReferencia(cdReferencia);
		setCdPessoa(cdPessoa);
		setNmPontoChecagem(nmPontoChecagem);
		setTpPontoChecagem(tpPontoChecagem);
		setStPontoChecagem(stPontoChecagem);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}
	public void setCdPontoChecagem(int cdPontoChecagem){
		this.cdPontoChecagem=cdPontoChecagem;
	}
	public int getCdPontoChecagem(){
		return this.cdPontoChecagem;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNmPontoChecagem(String nmPontoChecagem){
		this.nmPontoChecagem=nmPontoChecagem;
	}
	public String getNmPontoChecagem(){
		return this.nmPontoChecagem;
	}
	public void setTpPontoChecagem(int tpPontoChecagem){
		this.tpPontoChecagem=tpPontoChecagem;
	}
	public int getTpPontoChecagem(){
		return this.tpPontoChecagem;
	}
	public void setStPontoChecagem(int stPontoChecagem){
		this.stPontoChecagem=stPontoChecagem;
	}
	public int getStPontoChecagem(){
		return this.stPontoChecagem;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdPontoChecagem: " +  getCdPontoChecagem();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nmPontoChecagem: " +  getNmPontoChecagem();
		valueToString += ", tpPontoChecagem: " +  getTpPontoChecagem();
		valueToString += ", stPontoChecagem: " +  getStPontoChecagem();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PontoChecagem(getCdPontoChecagem(),
			getCdReferencia(),
			getCdPessoa(),
			getNmPontoChecagem(),
			getTpPontoChecagem(),
			getStPontoChecagem(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone());
	}

}