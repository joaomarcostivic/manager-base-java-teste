package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContaFinanceira {

	private int cdConta;
	private int cdResponsavel;
	private int cdEmpresa;
	private int cdAgencia;
	private String nmConta;
	private int tpConta;
	private String nrConta;
	private String nrDv;
	private int tpOperacao;
	private float vlLimite;
	private GregorianCalendar dtFechamento;
	private String idConta;
	private GregorianCalendar dtVencimentoLimite;
	private float vlSaldoInicial;
	private GregorianCalendar dtSaldoInicial;
	private int cdTurno;

	public ContaFinanceira() {}
	
	public ContaFinanceira(int cdConta,
			int cdResponsavel,
			int cdEmpresa,
			int cdAgencia,
			String nmConta,
			int tpConta,
			String nrConta,
			String nrDv,
			int tpOperacao,
			float vlLimite,
			GregorianCalendar dtFechamento,
			String idConta,
			GregorianCalendar dtVencimentoLimite,
			float vlSaldoInicial,
			GregorianCalendar dtSaldoInicial,
			int cdTurno){
		setCdConta(cdConta);
		setCdResponsavel(cdResponsavel);
		setCdEmpresa(cdEmpresa);
		setCdAgencia(cdAgencia);
		setNmConta(nmConta);
		setTpConta(tpConta);
		setNrConta(nrConta);
		setNrDv(nrDv);
		setTpOperacao(tpOperacao);
		setVlLimite(vlLimite);
		setDtFechamento(dtFechamento);
		setIdConta(idConta);
		setDtVencimentoLimite(dtVencimentoLimite);
		setVlSaldoInicial(vlSaldoInicial);
		setDtSaldoInicial(dtSaldoInicial);
		setCdTurno(cdTurno);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdAgencia(int cdAgencia){
		this.cdAgencia=cdAgencia;
	}
	public int getCdAgencia(){
		return this.cdAgencia;
	}
	public void setNmConta(String nmConta){
		this.nmConta=nmConta;
	}
	public String getNmConta(){
		return this.nmConta;
	}
	public void setTpConta(int tpConta){
		this.tpConta=tpConta;
	}
	public int getTpConta(){
		return this.tpConta;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setNrDv(String nrDv){
		this.nrDv=nrDv;
	}
	public String getNrDv(){
		return this.nrDv;
	}
	public void setTpOperacao(int tpOperacao){
		this.tpOperacao=tpOperacao;
	}
	public int getTpOperacao(){
		return this.tpOperacao;
	}
	public void setVlLimite(float vlLimite){
		this.vlLimite=vlLimite;
	}
	public float getVlLimite(){
		return this.vlLimite;
	}
	public void setDtFechamento(GregorianCalendar dtFechamento){
		this.dtFechamento=dtFechamento;
	}
	public GregorianCalendar getDtFechamento(){
		return this.dtFechamento;
	}
	public void setIdConta(String idConta){
		this.idConta=idConta;
	}
	public String getIdConta(){
		return this.idConta;
	}
	public void setDtVencimentoLimite(GregorianCalendar dtVencimentoLimite){
		this.dtVencimentoLimite=dtVencimentoLimite;
	}
	public GregorianCalendar getDtVencimentoLimite(){
		return this.dtVencimentoLimite;
	}
	public void setVlSaldoInicial(float vlSaldoInicial){
		this.vlSaldoInicial=vlSaldoInicial;
	}
	public float getVlSaldoInicial(){
		return this.vlSaldoInicial;
	}
	public void setDtSaldoInicial(GregorianCalendar dtSaldoInicial){
		this.dtSaldoInicial=dtSaldoInicial;
	}
	public GregorianCalendar getDtSaldoInicial(){
		return this.dtSaldoInicial;
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdAgencia: " +  getCdAgencia();
		valueToString += ", nmConta: " +  getNmConta();
		valueToString += ", tpConta: " +  getTpConta();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", nrDv: " +  getNrDv();
		valueToString += ", tpOperacao: " +  getTpOperacao();
		valueToString += ", vlLimite: " +  getVlLimite();
		valueToString += ", dtFechamento: " +  sol.util.Util.formatDateTime(getDtFechamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idConta: " +  getIdConta();
		valueToString += ", dtVencimentoLimite: " +  sol.util.Util.formatDateTime(getDtVencimentoLimite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlSaldoInicial: " +  getVlSaldoInicial();
		valueToString += ", dtSaldoInicial: " +  sol.util.Util.formatDateTime(getDtSaldoInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTurno: " +  getCdTurno();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaFinanceira(getCdConta(),
			getCdResponsavel(),
			getCdEmpresa(),
			getCdAgencia(),
			getNmConta(),
			getTpConta(),
			getNrConta(),
			getNrDv(),
			getTpOperacao(),
			getVlLimite(),
			getDtFechamento()==null ? null : (GregorianCalendar)getDtFechamento().clone(),
			getIdConta(),
			getDtVencimentoLimite()==null ? null : (GregorianCalendar)getDtVencimentoLimite().clone(),
			getVlSaldoInicial(),
			getDtSaldoInicial()==null ? null : (GregorianCalendar)getDtSaldoInicial().clone(),
			getCdTurno());
	}

}