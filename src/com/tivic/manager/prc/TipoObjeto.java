package com.tivic.manager.prc;

public class TipoObjeto {

	private int cdTipoObjeto;
	private String nmTipoObjeto;
	private String idTipoObjeto;

	public TipoObjeto() { }
	
	public TipoObjeto(int cdTipoObjeto,
			String nmTipoObjeto,
			String idTipoObjeto){
		setCdTipoObjeto(cdTipoObjeto);
		setNmTipoObjeto(nmTipoObjeto);
		setIdTipoObjeto(idTipoObjeto);
	}
	
	public void setCdTipoObjeto(int cdTipoObjeto){
		this.cdTipoObjeto=cdTipoObjeto;
	}
	public int getCdTipoObjeto(){
		return this.cdTipoObjeto;
	}
	public void setNmTipoObjeto(String nmTipoObjeto){
		this.nmTipoObjeto=nmTipoObjeto;
	}
	public String getNmTipoObjeto(){
		return this.nmTipoObjeto;
	}
	public void setIdTipoObjeto(String idTipoObjeto){
		this.idTipoObjeto=idTipoObjeto;
	}
	public String getIdTipoObjeto(){
		return this.idTipoObjeto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoObjeto: " +  getCdTipoObjeto();
		valueToString += ", nmTipoObjeto: " +  getNmTipoObjeto();
		valueToString += ", idTipoObjeto: " +  getIdTipoObjeto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoObjeto(getCdTipoObjeto(),
			getNmTipoObjeto(),
			getIdTipoObjeto());
	}

}