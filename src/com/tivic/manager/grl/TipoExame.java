package com.tivic.manager.grl;

public class TipoExame {

	private int cdTipoExame;
	private String idTipoExame;
	private String nmTipoExame;
	private int stTipoExame;

	public TipoExame() { }

	public TipoExame(int cdTipoExame,
			String idTipoExame,
			String nmTipoExame,
			int stTipoExame) {
		setCdTipoExame(cdTipoExame);
		setIdTipoExame(idTipoExame);
		setNmTipoExame(nmTipoExame);
		setStTipoExame(stTipoExame);
	}
	public void setCdTipoExame(int cdTipoExame){
		this.cdTipoExame=cdTipoExame;
	}
	public int getCdTipoExame(){
		return this.cdTipoExame;
	}
	public void setIdTipoExame(String idTipoExame){
		this.idTipoExame=idTipoExame;
	}
	public String getIdTipoExame(){
		return this.idTipoExame;
	}
	public void setNmTipoExame(String nmTipoExame){
		this.nmTipoExame=nmTipoExame;
	}
	public String getNmTipoExame(){
		return this.nmTipoExame;
	}
	public void setStTipoExame(int stTipoExame){
		this.stTipoExame=stTipoExame;
	}
	public int getStTipoExame(){
		return this.stTipoExame;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoExame: " +  getCdTipoExame();
		valueToString += ", idTipoExame: " +  getIdTipoExame();
		valueToString += ", nmTipoExame: " +  getNmTipoExame();
		valueToString += ", stTipoExame: " +  getStTipoExame();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoExame(getCdTipoExame(),
			getIdTipoExame(),
			getNmTipoExame(),
			getStTipoExame());
	}

}