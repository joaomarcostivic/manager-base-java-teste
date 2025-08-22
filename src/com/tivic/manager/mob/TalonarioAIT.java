package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TalonarioAIT {

	private int cdTalao;
	private int nrTalao;
	private int nrInicial;
	private int nrFinal;
	private int cdAgente;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtEntrega;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtDevolucao;
	private int stTalao;
	private int tpTalao;
	private String sgTalao;
	private int stLogin;

	public TalonarioAIT(){ }

	public TalonarioAIT(int cdTalao,
			int nrTalao,
			int nrInicial,
			int nrFinal,
			int cdAgente,
			GregorianCalendar dtEntrega,
			GregorianCalendar dtDevolucao,
			int stTalao,
			int tpTalao,
			String sgTalao,
			int stLogin){
		setCdTalao(cdTalao);
		setNrTalao(nrTalao);
		setNrInicial(nrInicial);
		setNrFinal(nrFinal);
		setCdAgente(cdAgente);
		setDtEntrega(dtEntrega);
		setDtDevolucao(dtDevolucao);
		setStTalao(stTalao);
		setTpTalao(tpTalao);
		setSgTalao(sgTalao);
		setStLogin(stLogin);
	}
	public void setCdTalao(int cdTalao){
		this.cdTalao=cdTalao;
	}
	public int getCdTalao(){
		return this.cdTalao;
	}
	public void setNrTalao(int nrTalao){
		this.nrTalao=nrTalao;
	}
	public int getNrTalao(){
		return this.nrTalao;
	}
	public void setNrInicial(int nrInicial){
		this.nrInicial=nrInicial;
	}
	public int getNrInicial(){
		return this.nrInicial;
	}
	public void setNrFinal(int nrFinal){
		this.nrFinal=nrFinal;
	}
	public int getNrFinal(){
		return this.nrFinal;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setDtEntrega(GregorianCalendar dtEntrega){
		this.dtEntrega=dtEntrega;
	}
	public GregorianCalendar getDtEntrega(){
		return this.dtEntrega;
	}
	public void setDtDevolucao(GregorianCalendar dtDevolucao){
		this.dtDevolucao=dtDevolucao;
	}
	public GregorianCalendar getDtDevolucao(){
		return this.dtDevolucao;
	}
	public void setStTalao(int stTalao){
		this.stTalao=stTalao;
	}
	public int getStTalao(){
		return this.stTalao;
	}
	public void setTpTalao(int tpTalao){
		this.tpTalao=tpTalao;
	}
	public int getTpTalao(){
		return this.tpTalao;
	}
	public void setSgTalao(String sgTalao){
		this.sgTalao=sgTalao;
	}
	public String getSgTalao(){
		return this.sgTalao;
	}
	public void setStLogin(int stLogin){
		this.stLogin=stLogin;
	}
	public int getStLogin(){
		return this.stLogin;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTalao: " +  getCdTalao();
		valueToString += ", nrTalao: " +  getNrTalao();
		valueToString += ", nrInicial: " +  getNrInicial();
		valueToString += ", nrFinal: " +  getNrFinal();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", dtEntrega: " +  sol.util.Util.formatDateTime(getDtEntrega(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtDevolucao: " +  sol.util.Util.formatDateTime(getDtDevolucao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stTalao: " +  getStTalao();
		valueToString += ", tpTalao: " +  getTpTalao();
		valueToString += ", sgTalao: " +  getSgTalao();
		valueToString += ", stLogin: " +  getStLogin();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TalonarioAIT(getCdTalao(),
			getNrTalao(),
			getNrInicial(),
			getNrFinal(),
			getCdAgente(),
			getDtEntrega()==null ? null : (GregorianCalendar)getDtEntrega().clone(),
			getDtDevolucao()==null ? null : (GregorianCalendar)getDtDevolucao().clone(),
			getStTalao(),
			getTpTalao(),
			getSgTalao(),
			getStLogin());
	}

}