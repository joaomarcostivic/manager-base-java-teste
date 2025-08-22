package com.tivic.manager.crm;

public class CentralAtendimento {

	private int cdCentral;
	private int cdEmpresa;
	private String nmCentral;
	private String dsCentral;
	private String idCentral;
	private String txtMensagemInicial;

	public CentralAtendimento(int cdCentral,
			int cdEmpresa,
			String nmCentral,
			String dsCentral,
			String idCentral,
			String txtMensagemInicial){
		setCdCentral(cdCentral);
		setCdEmpresa(cdEmpresa);
		setNmCentral(nmCentral);
		setDsCentral(dsCentral);
		setIdCentral(idCentral);
		setTxtMensagemInicial(txtMensagemInicial);
	}
	public void setCdCentral(int cdCentral){
		this.cdCentral=cdCentral;
	}
	public int getCdCentral(){
		return this.cdCentral;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmCentral(String nmCentral){
		this.nmCentral=nmCentral;
	}
	public String getNmCentral(){
		return this.nmCentral;
	}
	public void setDsCentral(String dsCentral){
		this.dsCentral=dsCentral;
	}
	public String getDsCentral(){
		return this.dsCentral;
	}
	public void setIdCentral(String idCentral){
		this.idCentral=idCentral;
	}
	public String getIdCentral(){
		return this.idCentral;
	}
	public void setTxtMensagemInicial(String txtMensagemInicial){
		this.txtMensagemInicial=txtMensagemInicial;
	}
	public String getTxtMensagemInicial(){
		return this.txtMensagemInicial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentral: " +  getCdCentral();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmCentral: " +  getNmCentral();
		valueToString += ", dsCentral: " +  getDsCentral();
		valueToString += ", idCentral: " +  getIdCentral();
		valueToString += ", txtMensagemInicial: " +  getTxtMensagemInicial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CentralAtendimento(getCdCentral(),
			getCdEmpresa(),
			getNmCentral(),
			getDsCentral(),
			getIdCentral(),
			getTxtMensagemInicial());
	}

}