package com.tivic.manager.fta;

public class VeiculoCheckupItem {

	private int cdCheckupItem;
	private int cdTipoCheckup;
	private int cdItem;
	private int cdCheckup;
	private int cdComponente;
	private float vlItem;
	private String txtObservacao;
	private String txtDiagnostico;
	private int stCheckupItem;

	public VeiculoCheckupItem(int cdCheckupItem,
			int cdTipoCheckup,
			int cdItem,
			int cdCheckup,
			int cdComponente,
			float vlItem,
			String txtObservacao,
			String txtDiagnostico,
			int stCheckupItem){
		setCdCheckupItem(cdCheckupItem);
		setCdTipoCheckup(cdTipoCheckup);
		setCdItem(cdItem);
		setCdCheckup(cdCheckup);
		setCdComponente(cdComponente);
		setVlItem(vlItem);
		setTxtObservacao(txtObservacao);
		setTxtDiagnostico(txtDiagnostico);
		setStCheckupItem(stCheckupItem);
	}
	public void setCdCheckupItem(int cdCheckupItem){
		this.cdCheckupItem=cdCheckupItem;
	}
	public int getCdCheckupItem(){
		return this.cdCheckupItem;
	}
	public void setCdTipoCheckup(int cdTipoCheckup){
		this.cdTipoCheckup=cdTipoCheckup;
	}
	public int getCdTipoCheckup(){
		return this.cdTipoCheckup;
	}
	public void setCdItem(int cdItem){
		this.cdItem=cdItem;
	}
	public int getCdItem(){
		return this.cdItem;
	}
	public void setCdCheckup(int cdCheckup){
		this.cdCheckup=cdCheckup;
	}
	public int getCdCheckup(){
		return this.cdCheckup;
	}
	public void setCdComponente(int cdComponente){
		this.cdComponente=cdComponente;
	}
	public int getCdComponente(){
		return this.cdComponente;
	}
	public void setVlItem(float vlItem){
		this.vlItem=vlItem;
	}
	public float getVlItem(){
		return this.vlItem;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setTxtDiagnostico(String txtDiagnostico){
		this.txtDiagnostico=txtDiagnostico;
	}
	public String getTxtDiagnostico(){
		return this.txtDiagnostico;
	}
	public void setStCheckupItem(int stCheckupItem){
		this.stCheckupItem=stCheckupItem;
	}
	public int getStCheckupItem(){
		return this.stCheckupItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCheckupItem: " +  getCdCheckupItem();
		valueToString += ", cdTipoCheckup: " +  getCdTipoCheckup();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", cdCheckup: " +  getCdCheckup();
		valueToString += ", cdComponente: " +  getCdComponente();
		valueToString += ", vlItem: " +  getVlItem();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", txtDiagnostico: " +  getTxtDiagnostico();
		valueToString += ", stCheckupItem: " +  getStCheckupItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VeiculoCheckupItem(getCdCheckupItem(),
			getCdTipoCheckup(),
			getCdItem(),
			getCdCheckup(),
			getCdComponente(),
			getVlItem(),
			getTxtObservacao(),
			getTxtDiagnostico(),
			getStCheckupItem());
	}

}
