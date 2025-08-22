package com.tivic.manager.prc;

public class TipoPedido {

	private int cdTipoPedido;
	private String nmTipoPedido;
	private String idTipoPedido;

	public TipoPedido() {}
	
	public TipoPedido(int cdTipoPedido,
			String nmTipoPedido,
			String idTipoPedido){
		setCdTipoPedido(cdTipoPedido);
		setNmTipoPedido(nmTipoPedido);
		setIdTipoPedido(idTipoPedido);
	}
	public void setCdTipoPedido(int cdTipoPedido){
		this.cdTipoPedido=cdTipoPedido;
	}
	public int getCdTipoPedido(){
		return this.cdTipoPedido;
	}
	public void setNmTipoPedido(String nmTipoPedido){
		this.nmTipoPedido=nmTipoPedido;
	}
	public String getNmTipoPedido(){
		return this.nmTipoPedido;
	}
	public void setIdTipoPedido(String idTipoPedido){
		this.idTipoPedido=idTipoPedido;
	}
	public String getIdTipoPedido(){
		return this.idTipoPedido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoPedido: " +  getCdTipoPedido();
		valueToString += ", nmTipoPedido: " +  getNmTipoPedido();
		valueToString += ", idTipoPedido: " +  getIdTipoPedido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoPedido(getCdTipoPedido(),
			getNmTipoPedido(),
			getIdTipoPedido());
	}

}