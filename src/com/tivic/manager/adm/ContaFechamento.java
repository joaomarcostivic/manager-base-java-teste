package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContaFechamento {

	private int cdConta;
	private int cdFechamento;
	private GregorianCalendar dtFechamento;
	private int cdSupervisor;
	private int cdResponsavel;
	private Double vlFechamento;
	private Double vlTotalEntradas;
	private Double vlTotalSaidas;
	private String txtObservacao;
	private Double vlSaldoAnterior;
	private int cdTurno;
	private int stFechamento;

	public ContaFechamento(){ }

	public ContaFechamento(int cdConta,
			int cdFechamento,
			GregorianCalendar dtFechamento,
			int cdSupervisor,
			int cdResponsavel,
			Double vlFechamento,
			Double vlTotalEntradas,
			Double vlTotalSaidas,
			String txtObservacao,
			Double vlSaldoAnterior,
			int cdTurno,
			int stFechamento){
		setCdConta(cdConta);
		setCdFechamento(cdFechamento);
		setDtFechamento(dtFechamento);
		setCdSupervisor(cdSupervisor);
		setCdResponsavel(cdResponsavel);
		setVlFechamento(vlFechamento);
		setVlTotalEntradas(vlTotalEntradas);
		setVlTotalSaidas(vlTotalSaidas);
		setTxtObservacao(txtObservacao);
		setVlSaldoAnterior(vlSaldoAnterior);
		setCdTurno(cdTurno);
		setStFechamento(stFechamento);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdFechamento(int cdFechamento){
		this.cdFechamento=cdFechamento;
	}
	public int getCdFechamento(){
		return this.cdFechamento;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setCdSupervisor(int cdSupervisor){
		this.cdSupervisor=cdSupervisor;
	}
	public int getCdSupervisor(){
		return this.cdSupervisor;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setVlFechamento(Double vlFechamento){
		this.vlFechamento=vlFechamento;
	}
	public Double getVlFechamento(){
		return this.vlFechamento;
	}
	public void setVlTotalEntradas(Double vlTotalEntradas){
		this.vlTotalEntradas=vlTotalEntradas;
	}
	public Double getVlTotalEntradas(){
		return this.vlTotalEntradas;
	}
	public void setVlTotalSaidas(Double vlTotalSaidas){
		this.vlTotalSaidas=vlTotalSaidas;
	}
	public Double getVlTotalSaidas(){
		return this.vlTotalSaidas;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setVlSaldoAnterior(Double vlSaldoAnterior){
		this.vlSaldoAnterior=vlSaldoAnterior;
	}
	public Double getVlSaldoAnterior(){
		return this.vlSaldoAnterior;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public void setStFechamento(int stFechamento){
		this.stFechamento=stFechamento;
	}
	public int getStFechamento(){
		return this.stFechamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdFechamento: " +  getCdFechamento();
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdSupervisor: " +  getCdSupervisor();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", vlFechamento: " +  getVlFechamento();
		valueToString += ", vlTotalEntradas: " +  getVlTotalEntradas();
		valueToString += ", vlTotalSaidas: " +  getVlTotalSaidas();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", vlSaldoAnterior: " +  getVlSaldoAnterior();
		valueToString += ", cdTurno: " +  getCdTurno();
		valueToString += ", stFechamento: " +  getStFechamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFechamento(getCdConta(),
			getCdFechamento(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getCdSupervisor(),
			getCdResponsavel(),
			getVlFechamento(),
			getVlTotalEntradas(),
			getVlTotalSaidas(),
			getTxtObservacao(),
			getVlSaldoAnterior(),
			getCdTurno(),
			getStFechamento());
	}

}