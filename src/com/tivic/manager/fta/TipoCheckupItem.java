package com.tivic.manager.fta;

public class TipoCheckupItem {

	private int cdTipoCheckup;
	private int cdItem;
	private float vlMinimo;
	private float vlMaximo;
	private int cdTipoComponente;
	private String nmItem;
	private String txtObservacao;

	public TipoCheckupItem(int cdTipoCheckup,
			int cdItem,
			float vlMinimo,
			float vlMaximo,
			int cdTipoComponente,
			String nmItem,
			String txtObservacao){
		setCdTipoCheckup(cdTipoCheckup);
		setCdItem(cdItem);
		setVlMinimo(vlMinimo);
		setVlMaximo(vlMaximo);
		setCdTipoComponente(cdTipoComponente);
		setNmItem(nmItem);
		setTxtObservacao(txtObservacao);
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
	public void setVlMinimo(float vlMinimo){
		this.vlMinimo=vlMinimo;
	}
	public float getVlMinimo(){
		return this.vlMinimo;
	}
	public void setVlMaximo(float vlMaximo){
		this.vlMaximo=vlMaximo;
	}
	public float getVlMaximo(){
		return this.vlMaximo;
	}
	public void setCdTipoComponente(int cdTipoComponente){
		this.cdTipoComponente=cdTipoComponente;
	}
	public int getCdTipoComponente(){
		return this.cdTipoComponente;
	}
	public void setNmItem(String nmItem){
		this.nmItem=nmItem;
	}
	public String getNmItem(){
		return this.nmItem;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoCheckup: " +  getCdTipoCheckup();
		valueToString += ", cdItem: " +  getCdItem();
		valueToString += ", vlMinimo: " +  getVlMinimo();
		valueToString += ", vlMaximo: " +  getVlMaximo();
		valueToString += ", cdTipoComponente: " +  getCdTipoComponente();
		valueToString += ", nmItem: " +  getNmItem();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoCheckupItem(getCdTipoCheckup(),
			getCdItem(),
			getVlMinimo(),
			getVlMaximo(),
			getCdTipoComponente(),
			getNmItem(),
			getTxtObservacao());
	}

}
