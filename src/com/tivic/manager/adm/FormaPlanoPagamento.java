package com.tivic.manager.adm;

public class FormaPlanoPagamento {

	private int cdFormaPagamento;
	private int cdEmpresa;
	private int cdPlanoPagamento;
	private Double prTaxaDesconto;
	private Double prDescontoMaximo;
	private Double vlMinimo;

	public FormaPlanoPagamento(){ }

	public FormaPlanoPagamento(int cdFormaPagamento,
			int cdEmpresa,
			int cdPlanoPagamento,
			Double prTaxaDesconto,
			Double prDescontoMaximo,
			Double vlMinimo){
		setCdFormaPagamento(cdFormaPagamento);
		setCdEmpresa(cdEmpresa);
		setCdPlanoPagamento(cdPlanoPagamento);
		setPrTaxaDesconto(prTaxaDesconto);
		setPrDescontoMaximo(prDescontoMaximo);
		setVlMinimo(vlMinimo);
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setPrTaxaDesconto(Double prTaxaDesconto){
		this.prTaxaDesconto=prTaxaDesconto;
	}
	public Double getPrTaxaDesconto(){
		return this.prTaxaDesconto;
	}
	public void setPrDescontoMaximo(Double prDescontoMaximo){
		this.prDescontoMaximo=prDescontoMaximo;
	}
	public Double getPrDescontoMaximo(){
		return this.prDescontoMaximo;
	}
	public void setVlMinimo(Double vlMinimo){
		this.vlMinimo=vlMinimo;
	}
	public Double getVlMinimo(){
		return this.vlMinimo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", prTaxaDesconto: " +  getPrTaxaDesconto();
		valueToString += ", prDescontoMaximo: " +  getPrDescontoMaximo();
		valueToString += ", vlMinimo: " +  getVlMinimo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormaPlanoPagamento(getCdFormaPagamento(),
			getCdEmpresa(),
			getCdPlanoPagamento(),
			getPrTaxaDesconto(),
			getPrDescontoMaximo(),
			getVlMinimo());
	}

}