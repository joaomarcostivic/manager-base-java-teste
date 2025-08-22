package com.tivic.manager.geo;

public class TipoCamada {

	private int cdTipo;
	private String nmTipo;
	private String txtTipo;
	private String idTipo;

	public TipoCamada(){ }

	public TipoCamada(int cdTipo,
			String nmTipo,
			String txtTipo,
			String idTipo){
		setCdTipo(cdTipo);
		setNmTipo(nmTipo);
		setTxtTipo(txtTipo);
		setIdTipo(idTipo);
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setNmTipo(String nmTipo){
		this.nmTipo=nmTipo;
	}
	public String getNmTipo(){
		return this.nmTipo;
	}
	public void setTxtTipo(String txtTipo){
		this.txtTipo=txtTipo;
	}
	public String getTxtTipo(){
		return this.txtTipo;
	}
	public void setIdTipo(String idTipo){
		this.idTipo=idTipo;
	}
	public String getIdTipo(){
		return this.idTipo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipo: " +  getCdTipo();
		valueToString += ", nmTipo: " +  getNmTipo();
		valueToString += ", txtTipo: " +  getTxtTipo();
		valueToString += ", idTipo: " +  getIdTipo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoCamada(getCdTipo(),
			getNmTipo(),
			getTxtTipo(),
			getIdTipo());
	}

}
