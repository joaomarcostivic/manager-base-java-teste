package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class FaixaDescontoRegra {

	private int cdTipoDesconto;
	private int cdEmpresa;
	private int cdFaixaDesconto;
	private int cdRegra;
	private int cdProdutoServico;
	private int cdPlanoPagamento;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;

	public FaixaDescontoRegra(int cdTipoDesconto,
			int cdEmpresa,
			int cdFaixaDesconto,
			int cdRegra,
			int cdProdutoServico,
			int cdPlanoPagamento,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal){
		setCdTipoDesconto(cdTipoDesconto);
		setCdEmpresa(cdEmpresa);
		setCdFaixaDesconto(cdFaixaDesconto);
		setCdRegra(cdRegra);
		setCdProdutoServico(cdProdutoServico);
		setCdPlanoPagamento(cdPlanoPagamento);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}
	public void setCdTipoDesconto(int cdTipoDesconto){
		this.cdTipoDesconto=cdTipoDesconto;
	}
	public int getCdTipoDesconto(){
		return this.cdTipoDesconto;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdFaixaDesconto(int cdFaixaDesconto){
		this.cdFaixaDesconto=cdFaixaDesconto;
	}
	public int getCdFaixaDesconto(){
		return this.cdFaixaDesconto;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
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
		valueToString += "cdTipoDesconto: " +  getCdTipoDesconto();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdFaixaDesconto: " +  getCdFaixaDesconto();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FaixaDescontoRegra(getCdTipoDesconto(),
			getCdEmpresa(),
			getCdFaixaDesconto(),
			getCdRegra(),
			getCdProdutoServico(),
			getCdPlanoPagamento(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone());
	}

}
