package com.tivic.manager.grl;

public class CidExcluidos {

	private int cdCidExcluido;
	private int cdCid;
	private String idCidExcluido;

	public CidExcluidos() { }

	public CidExcluidos(int cdCidExcluido,
			int cdCid,
			String idCidExcluido) {
		setCdCidExcluido(cdCidExcluido);
		setCdCid(cdCid);
		setIdCidExcluido(idCidExcluido);
	}
	public void setCdCidExcluido(int cdCidExcluido){
		this.cdCidExcluido=cdCidExcluido;
	}
	public int getCdCidExcluido(){
		return this.cdCidExcluido;
	}
	public void setCdCid(int cdCid){
		this.cdCid=cdCid;
	}
	public int getCdCid(){
		return this.cdCid;
	}
	public void setIdCidExcluido(String idCidExcluido){
		this.idCidExcluido=idCidExcluido;
	}
	public String getIdCidExcluido(){
		return this.idCidExcluido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCidExcluido: " +  getCdCidExcluido();
		valueToString += ", cdCid: " +  getCdCid();
		valueToString += ", idCidExcluido: " +  getIdCidExcluido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CidExcluidos(getCdCidExcluido(),
			getCdCid(),
			getIdCidExcluido());
	}

}