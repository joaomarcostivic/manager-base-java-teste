package com.tivic.manager.adm;

public class CondicaoFormaPlanoPagamento {
	private int cdCondicaoPagamento;
	private int cdPlanoPagamento;
	private int cdFormaPagamento;
	private int cdEmpresa;

	public CondicaoFormaPlanoPagamento(int cdCondicaoPagamento,
			int cdPlanoPagamento,
			int cdFormaPagamento,
			int cdEmpresa){
		setCdCondicaoPagamento(cdCondicaoPagamento);
		setCdPlanoPagamento(cdPlanoPagamento);
		setCdFormaPagamento(cdFormaPagamento);
		setCdEmpresa(cdEmpresa);
	}
	
	public void setCdCondicaoPagamento(int cdCondicaoPagamento){
		this.cdCondicaoPagamento=cdCondicaoPagamento;
	}
	public int getCdCondicaoPagamento(){
		return this.cdCondicaoPagamento;
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdCondicaoPagamento: " +  getCdCondicaoPagamento();
		valueToString += ", cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CondicaoFormaPlanoPagamento(getCdCondicaoPagamento(),
			getCdPlanoPagamento(),
			getCdFormaPagamento(),
			getCdEmpresa());
	}
}
