package com.tivic.manager.ptc;

public class TipoPrazo {

	private int cdTipoPrazo;
	private String nmTipoPrazo;
	private int tpAgendaItem;
	private int lgDocumentoObrigatorio;
	private int cdModelo;
	private int lgUtilizaModelo;
	private int lgEmail;

	public TipoPrazo(){ }

	public TipoPrazo(int cdTipoPrazo,
			String nmTipoPrazo,
			int tpAgendaItem,
			int lgDocumentoObrigatorio,
			int cdModelo,
			int lgUtilizaModelo,
			int lgEmail){
		setCdTipoPrazo(cdTipoPrazo);
		setNmTipoPrazo(nmTipoPrazo);
		setTpAgendaItem(tpAgendaItem);
		setLgDocumentoObrigatorio(lgDocumentoObrigatorio);
		setCdModelo(cdModelo);
		setLgUtilizaModelo(lgUtilizaModelo);
		setLgEmail(lgEmail);
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setNmTipoPrazo(String nmTipoPrazo){
		this.nmTipoPrazo=nmTipoPrazo;
	}
	public String getNmTipoPrazo(){
		return this.nmTipoPrazo;
	}
	public void setTpAgendaItem(int tpAgendaItem){
		this.tpAgendaItem=tpAgendaItem;
	}
	public int getTpAgendaItem(){
		return this.tpAgendaItem;
	}
	public void setLgDocumentoObrigatorio(int lgDocumentoObrigatorio){
		this.lgDocumentoObrigatorio=lgDocumentoObrigatorio;
	}
	public int getLgDocumentoObrigatorio(){
		return this.lgDocumentoObrigatorio;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setLgUtilizaModelo(int lgUtilizaModelo){
		this.lgUtilizaModelo=lgUtilizaModelo;
	}
	public int getLgUtilizaModelo(){
		return this.lgUtilizaModelo;
	}
	public void setLgEmail(int lgEmail){
		this.lgEmail=lgEmail;
	}
	public int getLgEmail(){
		return this.lgEmail;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", nmTipoPrazo: " +  getNmTipoPrazo();
		valueToString += ", tpAgendaItem: " +  getTpAgendaItem();
		valueToString += ", lgDocumentoObrigatorio: " +  getLgDocumentoObrigatorio();
		valueToString += ", cdModelo: " +  getCdModelo();
		valueToString += ", lgUtilizaModelo: " +  getLgUtilizaModelo();
		valueToString += ", lgEmail: " +  getLgEmail();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPrazo(getCdTipoPrazo(),
			getNmTipoPrazo(),
			getTpAgendaItem(),
			getLgDocumentoObrigatorio(),
			getCdModelo(),
			getLgUtilizaModelo(),
			getLgEmail());
	}

}
