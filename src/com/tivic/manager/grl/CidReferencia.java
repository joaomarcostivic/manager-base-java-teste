package com.tivic.manager.grl;

public class CidReferencia {

	private int cdCid;
	private int cdCidReferencia;
	private String idCidReferencia;

	public CidReferencia() { }

	public CidReferencia(int cdCid,
			int cdCidReferencia,
			String idCidReferencia) {
		setCdCid(cdCid);
		setCdCidReferencia(cdCidReferencia);
		setIdCidReferencia(idCidReferencia);
	}
	public void setCdCid(int cdCid){
		this.cdCid=cdCid;
	}
	public int getCdCid(){
		return this.cdCid;
	}
	public void setCdCidReferencia(int cdCidReferencia){
		this.cdCidReferencia=cdCidReferencia;
	}
	public int getCdCidReferencia(){
		return this.cdCidReferencia;
	}
	public void setIdCidReferencia(String idCidReferencia){
		this.idCidReferencia=idCidReferencia;
	}
	public String getIdCidReferencia(){
		return this.idCidReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCid: " +  getCdCid();
		valueToString += ", cdCidReferencia: " +  getCdCidReferencia();
		valueToString += ", idCidReferencia: " +  getIdCidReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CidReferencia(getCdCid(),
			getCdCidReferencia(),
			getIdCidReferencia());
	}

}