package com.tivic.manager.srh;

public class AgenteNocivo {

	private int cdAgenteNocivo;
	private String nmAgenteNocivo;
	private String idAgenteNocivo;

	public AgenteNocivo(){ }

	public AgenteNocivo(int cdAgenteNocivo,
			String nmAgenteNocivo,
			String idAgenteNocivo){
		setCdAgenteNocivo(cdAgenteNocivo);
		setNmAgenteNocivo(nmAgenteNocivo);
		setIdAgenteNocivo(idAgenteNocivo);
	}
	public void setCdAgenteNocivo(int cdAgenteNocivo){
		this.cdAgenteNocivo=cdAgenteNocivo;
	}
	public int getCdAgenteNocivo(){
		return this.cdAgenteNocivo;
	}
	public void setNmAgenteNocivo(String nmAgenteNocivo){
		this.nmAgenteNocivo=nmAgenteNocivo;
	}
	public String getNmAgenteNocivo(){
		return this.nmAgenteNocivo;
	}
	public void setIdAgenteNocivo(String idAgenteNocivo){
		this.idAgenteNocivo=idAgenteNocivo;
	}
	public String getIdAgenteNocivo(){
		return this.idAgenteNocivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgenteNocivo: " +  getCdAgenteNocivo();
		valueToString += ", nmAgenteNocivo: " +  getNmAgenteNocivo();
		valueToString += ", idAgenteNocivo: " +  getIdAgenteNocivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgenteNocivo(getCdAgenteNocivo(),
			getNmAgenteNocivo(),
			getIdAgenteNocivo());
	}

}
