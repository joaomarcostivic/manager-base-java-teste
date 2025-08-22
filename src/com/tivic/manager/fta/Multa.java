package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Multa {

	private int cdMulta;
	private int cdContaPagar;
	private int cdVeiculo;
	private GregorianCalendar dtInfracao;
	private String nmLocalInfracao;
	private String nmInfracao;
	private String idInfracao;
	private float vlMulta;
	private String txtObservacao;
	private String nrAgenteTransito;
	private String nmAgenteTransito;
	private int stMulta;
	private GregorianCalendar dtFinalRecurso;
	private int lgAdvertencia;
	private GregorianCalendar dtPagamentoDesconto;
	private GregorianCalendar dtNotificacao;
	private GregorianCalendar dtVencimento;
	private int cdResponsavel;

	public Multa(int cdMulta,
			int cdContaPagar,
			int cdVeiculo,
			GregorianCalendar dtInfracao,
			String nmLocalInfracao,
			String nmInfracao,
			String idInfracao,
			float vlMulta,
			String txtObservacao,
			String nrAgenteTransito,
			String nmAgenteTransito,
			int stMulta,
			GregorianCalendar dtFinalRecurso,
			int lgAdvertencia,
			GregorianCalendar dtPagamentoDesconto,
			GregorianCalendar dtNotificacao,
			GregorianCalendar dtVencimento,
			int cdResponsavel){
		setCdMulta(cdMulta);
		setCdContaPagar(cdContaPagar);
		setCdVeiculo(cdVeiculo);
		setDtInfracao(dtInfracao);
		setNmLocalInfracao(nmLocalInfracao);
		setNmInfracao(nmInfracao);
		setIdInfracao(idInfracao);
		setVlMulta(vlMulta);
		setTxtObservacao(txtObservacao);
		setNrAgenteTransito(nrAgenteTransito);
		setNmAgenteTransito(nmAgenteTransito);
		setStMulta(stMulta);
		setDtFinalRecurso(dtFinalRecurso);
		setLgAdvertencia(lgAdvertencia);
		setDtPagamentoDesconto(dtPagamentoDesconto);
		setDtNotificacao(dtNotificacao);
		setDtVencimento(dtVencimento);
		setCdResponsavel(cdResponsavel);
	}
	public void setCdMulta(int cdMulta){
		this.cdMulta=cdMulta;
	}
	public int getCdMulta(){
		return this.cdMulta;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao=dtInfracao;
	}
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	public void setNmLocalInfracao(String nmLocalInfracao){
		this.nmLocalInfracao=nmLocalInfracao;
	}
	public String getNmLocalInfracao(){
		return this.nmLocalInfracao;
	}
	public void setNmInfracao(String nmInfracao){
		this.nmInfracao=nmInfracao;
	}
	public String getNmInfracao(){
		return this.nmInfracao;
	}
	public void setIdInfracao(String idInfracao){
		this.idInfracao=idInfracao;
	}
	public String getIdInfracao(){
		return this.idInfracao;
	}
	public void setVlMulta(float vlMulta){
		this.vlMulta=vlMulta;
	}
	public float getVlMulta(){
		return this.vlMulta;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrAgenteTransito(String nrAgenteTransito){
		this.nrAgenteTransito=nrAgenteTransito;
	}
	public String getNrAgenteTransito(){
		return this.nrAgenteTransito;
	}
	public void setNmAgenteTransito(String nmAgenteTransito){
		this.nmAgenteTransito=nmAgenteTransito;
	}
	public String getNmAgenteTransito(){
		return this.nmAgenteTransito;
	}
	public void setStMulta(int stMulta){
		this.stMulta=stMulta;
	}
	public int getStMulta(){
		return this.stMulta;
	}
	public void setDtFinalRecurso(GregorianCalendar dtFinalRecurso){
		this.dtFinalRecurso=dtFinalRecurso;
	}
	public GregorianCalendar getDtFinalRecurso(){
		return this.dtFinalRecurso;
	}
	public void setLgAdvertencia(int lgAdvertencia){
		this.lgAdvertencia=lgAdvertencia;
	}
	public int getLgAdvertencia(){
		return this.lgAdvertencia;
	}
	public void setDtPagamentoDesconto(GregorianCalendar dtPagamentoDesconto){
		this.dtPagamentoDesconto=dtPagamentoDesconto;
	}
	public GregorianCalendar getDtPagamentoDesconto(){
		return this.dtPagamentoDesconto;
	}
	public void setDtNotificacao(GregorianCalendar dtNotificacao){
		this.dtNotificacao=dtNotificacao;
	}
	public GregorianCalendar getDtNotificacao(){
		return this.dtNotificacao;
	}
	public void setDtVencimento(GregorianCalendar dtVencimento){
		this.dtVencimento=dtVencimento;
	}
	public GregorianCalendar getDtVencimento(){
		return this.dtVencimento;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMulta: " +  getCdMulta();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", dtInfracao: " +  sol.util.Util.formatDateTime(getDtInfracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nmLocalInfracao: " +  getNmLocalInfracao();
		valueToString += ", nmInfracao: " +  getNmInfracao();
		valueToString += ", idInfracao: " +  getIdInfracao();
		valueToString += ", vlMulta: " +  getVlMulta();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrAgenteTransito: " +  getNrAgenteTransito();
		valueToString += ", nmAgenteTransito: " +  getNmAgenteTransito();
		valueToString += ", stMulta: " +  getStMulta();
		valueToString += ", dtFinalRecurso: " +  sol.util.Util.formatDateTime(getDtFinalRecurso(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAdvertencia: " +  getLgAdvertencia();
		valueToString += ", dtPagamentoDesconto: " +  sol.util.Util.formatDateTime(getDtPagamentoDesconto(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtNotificacao: " +  sol.util.Util.formatDateTime(getDtNotificacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtVencimento: " +  sol.util.Util.formatDateTime(getDtVencimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Multa(getCdMulta(),
			getCdContaPagar(),
			getCdVeiculo(),
			getDtInfracao()==null ? null : (GregorianCalendar)getDtInfracao().clone(),
			getNmLocalInfracao(),
			getNmInfracao(),
			getIdInfracao(),
			getVlMulta(),
			getTxtObservacao(),
			getNrAgenteTransito(),
			getNmAgenteTransito(),
			getStMulta(),
			getDtFinalRecurso()==null ? null : (GregorianCalendar)getDtFinalRecurso().clone(),
			getLgAdvertencia(),
			getDtPagamentoDesconto()==null ? null : (GregorianCalendar)getDtPagamentoDesconto().clone(),
			getDtNotificacao()==null ? null : (GregorianCalendar)getDtNotificacao().clone(),
			getDtVencimento()==null ? null : (GregorianCalendar)getDtVencimento().clone(),
			getCdResponsavel());
	}

}