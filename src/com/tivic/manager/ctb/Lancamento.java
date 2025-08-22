package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class Lancamento {

	private int cdLancamento;
	private int cdLote;
	private int cdLancamentoAuto;
	private GregorianCalendar dtLancamento;
	private float vlTotal;
	private int lgProvisao;
	private int cdEmpresa;
	private String idLancamento;
	private int cdMovimentoConta;
	private int cdContaFinanceira;
	private int cdContaReceber;
	private int cdContaPagar;

	public Lancamento(int cdLancamento,
			int cdLote,
			int cdLancamentoAuto,
			GregorianCalendar dtLancamento,
			float vlTotal,
			int lgProvisao,
			int cdEmpresa,
			String idLancamento,
			int cdMovimentoConta,
			int cdContaFinanceira,
			int cdContaReceber,
			int cdContaPagar){
		setCdLancamento(cdLancamento);
		setCdLote(cdLote);
		setCdLancamentoAuto(cdLancamentoAuto);
		setDtLancamento(dtLancamento);
		setVlTotal(vlTotal);
		setLgProvisao(lgProvisao);
		setCdEmpresa(cdEmpresa);
		setIdLancamento(idLancamento);
		setCdMovimentoConta(cdMovimentoConta);
		setCdContaFinanceira(cdContaFinanceira);
		setCdContaReceber(cdContaReceber);
		setCdContaPagar(cdContaPagar);
	}
	public void setCdLancamento(int cdLancamento){
		this.cdLancamento=cdLancamento;
	}
	public int getCdLancamento(){
		return this.cdLancamento;
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setCdLancamentoAuto(int cdLancamentoAuto){
		this.cdLancamentoAuto=cdLancamentoAuto;
	}
	public int getCdLancamentoAuto(){
		return this.cdLancamentoAuto;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setVlTotal(float vlTotal){
		this.vlTotal=vlTotal;
	}
	public float getVlTotal(){
		return this.vlTotal;
	}
	public void setLgProvisao(int lgProvisao){
		this.lgProvisao=lgProvisao;
	}
	public int getLgProvisao(){
		return this.lgProvisao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setIdLancamento(String idLancamento){
		this.idLancamento=idLancamento;
	}
	public String getIdLancamento(){
		return this.idLancamento;
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdContaFinanceira(int cdContaFinanceira){
		this.cdContaFinanceira=cdContaFinanceira;
	}
	public int getCdContaFinanceira(){
		return this.cdContaFinanceira;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLancamento: " +  getCdLancamento();
		valueToString += ", cdLote: " +  getCdLote();
		valueToString += ", cdLancamentoAuto: " +  getCdLancamentoAuto();
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", lgProvisao: " +  getLgProvisao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", idLancamento: " +  getIdLancamento();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdContaFinanceira: " +  getCdContaFinanceira();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lancamento(getCdLancamento(),
			getCdLote(),
			getCdLancamentoAuto(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getVlTotal(),
			getLgProvisao(),
			getCdEmpresa(),
			getIdLancamento(),
			getCdMovimentoConta(),
			getCdContaFinanceira(),
			getCdContaReceber(),
			getCdContaPagar());
	}

}
