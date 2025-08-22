package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class Rescisao {

	private int cdRescisao;
	private int cdMatricula;
	private int cdTipoDesligamento;
	private GregorianCalendar dtDesligamento;
	private GregorianCalendar dtAvisoPrevio;
	private float vlSaldoFgts;

	public Rescisao(int cdRescisao,
			int cdMatricula,
			int cdTipoDesligamento,
			GregorianCalendar dtDesligamento,
			GregorianCalendar dtAvisoPrevio,
			float vlSaldoFgts){
		setCdRescisao(cdRescisao);
		setCdMatricula(cdMatricula);
		setCdTipoDesligamento(cdTipoDesligamento);
		setDtDesligamento(dtDesligamento);
		setDtAvisoPrevio(dtAvisoPrevio);
		setVlSaldoFgts(vlSaldoFgts);
	}
	public void setCdRescisao(int cdRescisao){
		this.cdRescisao=cdRescisao;
	}
	public int getCdRescisao(){
		return this.cdRescisao;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdTipoDesligamento(int cdTipoDesligamento){
		this.cdTipoDesligamento=cdTipoDesligamento;
	}
	public int getCdTipoDesligamento(){
		return this.cdTipoDesligamento;
	}
	public void setDtDesligamento(GregorianCalendar dtDesligamento){
		this.dtDesligamento=dtDesligamento;
	}
	public GregorianCalendar getDtDesligamento(){
		return this.dtDesligamento;
	}
	public void setDtAvisoPrevio(GregorianCalendar dtAvisoPrevio){
		this.dtAvisoPrevio=dtAvisoPrevio;
	}
	public GregorianCalendar getDtAvisoPrevio(){
		return this.dtAvisoPrevio;
	}
	public void setVlSaldoFgts(float vlSaldoFgts){
		this.vlSaldoFgts=vlSaldoFgts;
	}
	public float getVlSaldoFgts(){
		return this.vlSaldoFgts;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRescisao: " +  getCdRescisao();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", cdTipoDesligamento: " +  getCdTipoDesligamento();
		valueToString += ", dtDesligamento: " +  sol.util.Util.formatDateTime(getDtDesligamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAvisoPrevio: " +  sol.util.Util.formatDateTime(getDtAvisoPrevio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlSaldoFgts: " +  getVlSaldoFgts();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Rescisao(cdRescisao,
			cdMatricula,
			cdTipoDesligamento,
			dtDesligamento==null ? null : (GregorianCalendar)dtDesligamento.clone(),
			dtAvisoPrevio==null ? null : (GregorianCalendar)dtAvisoPrevio.clone(),
			vlSaldoFgts);
	}

}