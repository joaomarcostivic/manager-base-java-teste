package com.tivic.manager.acd;

public class TipoProfissionaisEscolares {

	private int cdTipoProfissionaisEscolares;
	private String nmTipoProfissionaisEscolares;
	private String idTipoProfissionaisEscolares;
	private int stTipoProfissionaisEscolares;

	public TipoProfissionaisEscolares() { }

	public TipoProfissionaisEscolares(int cdTipoProfissionaisEscolares,
			String nmTipoProfissionaisEscolares,
			String idTipoProfissionaisEscolares,
			int stTipoProfissionaisEscolares) {
		setCdTipoProfissionaisEscolares(cdTipoProfissionaisEscolares);
		setNmTipoProfissionaisEscolares(nmTipoProfissionaisEscolares);
		setIdTipoProfissionaisEscolares(idTipoProfissionaisEscolares);
		setStTipoProfissionaisEscolares(stTipoProfissionaisEscolares);
	}
	public void setCdTipoProfissionaisEscolares(int cdTipoProfissionaisEscolares){
		this.cdTipoProfissionaisEscolares=cdTipoProfissionaisEscolares;
	}
	public int getCdTipoProfissionaisEscolares(){
		return this.cdTipoProfissionaisEscolares;
	}
	public void setNmTipoProfissionaisEscolares(String nmTipoProfissionaisEscolares){
		this.nmTipoProfissionaisEscolares=nmTipoProfissionaisEscolares;
	}
	public String getNmTipoProfissionaisEscolares(){
		return this.nmTipoProfissionaisEscolares;
	}
	public void setIdTipoProfissionaisEscolares(String idTipoProfissionaisEscolares){
		this.idTipoProfissionaisEscolares=idTipoProfissionaisEscolares;
	}
	public String getIdTipoProfissionaisEscolares(){
		return this.idTipoProfissionaisEscolares;
	}
	public void setStTipoProfissionaisEscolares(int stTipoProfissionaisEscolares){
		this.stTipoProfissionaisEscolares=stTipoProfissionaisEscolares;
	}
	public int getStTipoProfissionaisEscolares(){
		return this.stTipoProfissionaisEscolares;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoProfissionaisEscolares: " +  getCdTipoProfissionaisEscolares();
		valueToString += ", nmTipoProfissionaisEscolares: " +  getNmTipoProfissionaisEscolares();
		valueToString += ", idTipoProfissionaisEscolares: " +  getIdTipoProfissionaisEscolares();
		valueToString += ", stTipoProfissionaisEscolares: " +  getStTipoProfissionaisEscolares();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoProfissionaisEscolares(getCdTipoProfissionaisEscolares(),
			getNmTipoProfissionaisEscolares(),
			getIdTipoProfissionaisEscolares(),
			getStTipoProfissionaisEscolares());
	}

}