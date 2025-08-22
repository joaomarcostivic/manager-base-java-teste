package com.tivic.manager.grl;

public class FormaDivulgacao {

	private int cdFormaDivulgacao;
	private String nmFormaDivulgacao;
	
	public FormaDivulgacao(){}
	
	public FormaDivulgacao(int cdFormaDivulgacao,
			String nmFormaDivulgacao){
		setCdFormaDivulgacao(cdFormaDivulgacao);
		setNmFormaDivulgacao(nmFormaDivulgacao);
	}
	public void setCdFormaDivulgacao(int cdFormaDivulgacao){
		this.cdFormaDivulgacao=cdFormaDivulgacao;
	}
	public int getCdFormaDivulgacao(){
		return this.cdFormaDivulgacao;
	}
	public void setNmFormaDivulgacao(String nmFormaDivulgacao){
		this.nmFormaDivulgacao=nmFormaDivulgacao;
	}
	public String getNmFormaDivulgacao(){
		return this.nmFormaDivulgacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormaDivulgacao: " +  getCdFormaDivulgacao();
		valueToString += ", nmFormaDivulgacao: " +  getNmFormaDivulgacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormaDivulgacao(getCdFormaDivulgacao(),
			getNmFormaDivulgacao());
	}

}
