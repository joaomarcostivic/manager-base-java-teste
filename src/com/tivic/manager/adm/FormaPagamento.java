package com.tivic.manager.adm;

public class FormaPagamento {

	private int cdFormaPagamento;
	private String nmFormaPagamento;
	private String sgFormaPagamento;
	private String idFormaPagamento;
	private int tpFormaPagamento;
	private int lgTransferencia;

	public FormaPagamento(){};
	
	public FormaPagamento(int cdFormaPagamento,
			String nmFormaPagamento,
			String sgFormaPagamento,
			String idFormaPagamento,
			int tpFormaPagamento,
			int lgTransferencia){
		setCdFormaPagamento(cdFormaPagamento);
		setNmFormaPagamento(nmFormaPagamento);
		setSgFormaPagamento(sgFormaPagamento);
		setIdFormaPagamento(idFormaPagamento);
		setTpFormaPagamento(tpFormaPagamento);
		setLgTransferencia(lgTransferencia);
	}
	public void setCdFormaPagamento(int cdFormaPagamento){
		this.cdFormaPagamento=cdFormaPagamento;
	}
	public int getCdFormaPagamento(){
		return this.cdFormaPagamento;
	}
	public void setNmFormaPagamento(String nmFormaPagamento){
		this.nmFormaPagamento=nmFormaPagamento;
	}
	public String getNmFormaPagamento(){
		return this.nmFormaPagamento;
	}
	public void setSgFormaPagamento(String sgFormaPagamento){
		this.sgFormaPagamento=sgFormaPagamento;
	}
	public String getSgFormaPagamento(){
		return this.sgFormaPagamento;
	}
	public void setIdFormaPagamento(String idFormaPagamento){
		this.idFormaPagamento=idFormaPagamento;
	}
	public String getIdFormaPagamento(){
		return this.idFormaPagamento;
	}
	public void setTpFormaPagamento(int tpFormaPagamento){
		this.tpFormaPagamento=tpFormaPagamento;
	}
	public int getTpFormaPagamento(){
		return this.tpFormaPagamento;
	}
	public void setLgTransferencia(int lgTransferencia){
		this.lgTransferencia=lgTransferencia;
	}
	public int getLgTransferencia(){
		return this.lgTransferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormaPagamento: " +  getCdFormaPagamento();
		valueToString += ", nmFormaPagamento: " +  getNmFormaPagamento();
		valueToString += ", sgFormaPagamento: " +  getSgFormaPagamento();
		valueToString += ", idFormaPagamento: " +  getIdFormaPagamento();
		valueToString += ", tpFormaPagamento: " +  getTpFormaPagamento();
		valueToString += ", lgTransferencia: " +  getLgTransferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormaPagamento(getCdFormaPagamento(),
			getNmFormaPagamento(),
			getSgFormaPagamento(),
			getIdFormaPagamento(),
			getTpFormaPagamento(),
			getLgTransferencia());
	}

}
