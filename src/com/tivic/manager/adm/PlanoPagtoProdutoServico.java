package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PlanoPagtoProdutoServico {

	private int cdPlanoPagtoProdutoServico;
	private int cdPlanoPagamento;
	private int cdTabelaPreco;
	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdGrupo;
	private GregorianCalendar dtInicioVigencia;
	private GregorianCalendar dtFinalVigencia;
	private int cdTipoOperacao;
	private int cdFormaPagamento;

	public PlanoPagtoProdutoServico(int cdPlanoPagtoProdutoServico,
			int cdPlanoPagamento,
			int cdTabelaPreco,
			int cdEmpresa,
			int cdProdutoServico,
			int cdGrupo,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia,
			int cdTipoOperacao,
			int cdFormaPagamento){
		setCdPlanoPagtoProdutoServico(cdPlanoPagtoProdutoServico);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdTabelaPreco(cdTabelaPreco);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
		setDtInicioVigencia(dtInicioVigencia);
		setDtFinalVigencia(dtFinalVigencia);
		setCdTipoOperacao(cdTipoOperacao);
		setCdFormaPagamento(cdFormaPagamento);
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
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoPagtoProdutoServico: " +  getCdPlanoPagtoProdutoServico();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", dtInicioVigencia: " +  sol.util.Util.formatDateTime(getDtInicioVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdTipoOperacao: " +  getCdTipoOperacao();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoPagtoProdutoServico(getCdPlanoPagtoProdutoServico(),
			getCdPlanoPagamento(),
			getCdTabelaPreco(),
			getCdEmpresa(),
			getCdProdutoServico(),
			getCdGrupo(),
			getDtInicioVigencia()==null ? null : (GregorianCalendar)getDtInicioVigencia().clone(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone(),
			getCdTipoOperacao(),
			getCdFormaPagamento());
	}

}