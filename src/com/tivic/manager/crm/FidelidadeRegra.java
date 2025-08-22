package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class FidelidadeRegra {

	private int cdFidelidadeRegra;
	private int cdTabelaPreco;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdPlanoPagtoProdutoServico;
	private int cdPlanoPagamento;
	private int cdTipoOperacao;
	private int cdFormaPagamento;
	private GregorianCalendar dtInicioVigencia;
	private GregorianCalendar dtFinalVigencia;
	private int tpAplicacao;
	private int tpBaseCalculo;

	public FidelidadeRegra(int cdFidelidadeRegra,
			int cdTabelaPreco,
			int cdEmpresa,
			int cdProdutoServico,
			int cdPlanoPagtoProdutoServico,
			int cdPlanoPagamento,
			int cdTipoOperacao,
			int cdFormaPagamento,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia,
			int tpAplicacao,
			int tpBaseCalculo){
		setCdFidelidadeRegra(cdFidelidadeRegra);
		setCdTabelaPreco(cdTabelaPreco);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdPlanoPagtoProdutoServico(cdPlanoPagtoProdutoServico);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdTipoOperacao(cdTipoOperacao);
		setCdFormaPagamento(cdFormaPagamento);
		setDtInicioVigencia(dtInicioVigencia);
		setDtFinalVigencia(dtFinalVigencia);
		setTpAplicacao(tpAplicacao);
		setTpBaseCalculo(tpBaseCalculo);
	}
	public void setCdFidelidadeRegra(int cdFidelidadeRegra){
		this.cdFidelidadeRegra=cdFidelidadeRegra;
	}
	public int getCdFidelidadeRegra(){
		return this.cdFidelidadeRegra;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPlanoPagtoProdutoServico(int cdPlanoPagtoProdutoServico){
		this.cdPlanoPagtoProdutoServico=cdPlanoPagtoProdutoServico;
	}
	public int getCdPlanoPagtoProdutoServico(){
		return this.cdPlanoPagtoProdutoServico;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setCdTipoOperacao(int cdTipoOperacao){
		this.cdTipoOperacao=cdTipoOperacao;
	}
	public int getCdTipoOperacao(){
		return this.cdTipoOperacao;
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setDtInicioVigencia(GregorianCalendar dtInicioVigencia){
		this.dtInicioVigencia=dtInicioVigencia;
	}
	public GregorianCalendar getDtInicioVigencia(){
		return this.dtInicioVigencia;
	}
	public void setDtFinalVigencia(GregorianCalendar dtFinalVigencia){
		this.dtFinalVigencia=dtFinalVigencia;
	}
	public GregorianCalendar getDtFinalVigencia(){
		return this.dtFinalVigencia;
	}
	public void setTpAplicacao(int tpAplicacao){
		this.tpAplicacao=tpAplicacao;
	}
	public int getTpAplicacao(){
		return this.tpAplicacao;
	}
	public void setTpBaseCalculo(int tpBaseCalculo){
		this.tpBaseCalculo=tpBaseCalculo;
	}
	public int getTpBaseCalculo(){
		return this.tpBaseCalculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFidelidadeRegra: " +  getCdFidelidadeRegra();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPlanoPagtoProdutoServico: " +  getCdPlanoPagtoProdutoServico();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", dtInicioVigencia: " +  sol.util.Util.formatDateTime(getDtInicioVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpAplicacao: " +  getTpAplicacao();
		valueToString += ", tpBaseCalculo: " +  getTpBaseCalculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FidelidadeRegra(getCdFidelidadeRegra(),
			getCdTabelaPreco(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdPlanoPagtoProdutoServico(),
			getCdPlanoPagamento(),
			getCdTipoOperacao(),
			getCdFormaPagamento(),
			getDtInicioVigencia()==null ? null : (GregorianCalendar)getDtInicioVigencia().clone(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone(),
			getTpAplicacao(),
			getTpBaseCalculo());
	}

}
