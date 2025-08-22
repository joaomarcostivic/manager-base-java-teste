package com.tivic.manager.acd;

public class TipoMantenedora {

	private int cdTipoMantenedora;
	private String nmTipoMantenedora;
	private String idTipoMantenedora;

	public TipoMantenedora() { }
			
	public TipoMantenedora(int cdTipoMantenedora,
			String nmTipoMantenedora,
			String idTipoMantenedora){
		setCdTipoMantenedora(cdTipoMantenedora);
		setNmTipoMantenedora(nmTipoMantenedora);
		setIdTipoMantenedora(idTipoMantenedora);
	}
	public void setCdTipoMantenedora(int cdTipoMantenedora){
		this.cdTipoMantenedora=cdTipoMantenedora;
	}
	public int getCdTipoMantenedora(){
		return this.cdTipoMantenedora;
	}
	public void setNmTipoMantenedora(String nmTipoMantenedora){
		this.nmTipoMantenedora=nmTipoMantenedora;
	}
	public String getNmTipoMantenedora(){
		return this.nmTipoMantenedora;
	}
	public void setIdTipoMantenedora(String idTipoMantenedora){
		this.idTipoMantenedora=idTipoMantenedora;
	}
	public String getIdTipoMantenedora(){
		return this.idTipoMantenedora;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoMantenedora: " +  getCdTipoMantenedora();
		valueToString += ", nmTipoMantenedora: " +  getNmTipoMantenedora();
		valueToString += ", idTipoMantenedora: " +  getIdTipoMantenedora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoMantenedora(getCdTipoMantenedora(),
			getNmTipoMantenedora(),
			getIdTipoMantenedora());
	}

}