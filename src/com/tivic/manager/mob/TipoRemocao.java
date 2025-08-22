package com.tivic.manager.mob;

public class TipoRemocao {

	private int cdTipoRemocao;
	private String nmTipoRemocao;
	private int stTipoRemocao;

	public TipoRemocao() { }

	public TipoRemocao(int cdTipoRemocao,
			String nmTipoRemocao,
			int stTipoRemocao) {
		setCdTipoRemocao(cdTipoRemocao);
		setNmTipoRemocao(nmTipoRemocao);
		setStTipoRemocao(stTipoRemocao);
	}
	public void setCdTipoRemocao(int cdTipoRemocao){
		this.cdTipoRemocao=cdTipoRemocao;
	}
	public int getCdTipoRemocao(){
		return this.cdTipoRemocao;
	}
	public void setNmTipoRemocao(String nmTipoRemocao){
		this.nmTipoRemocao=nmTipoRemocao;
	}
	public String getNmTipoRemocao(){
		return this.nmTipoRemocao;
	}
	public void setStTipoRemocao(int stTipoRemocao){
		this.stTipoRemocao=stTipoRemocao;
	}
	public int getStTipoRemocao(){
		return this.stTipoRemocao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoRemocao: " +  getCdTipoRemocao();
		valueToString += ", nmTipoRemocao: " +  getNmTipoRemocao();
		valueToString += ", stTipoRemocao: " +  getStTipoRemocao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoRemocao(getCdTipoRemocao(),
			getNmTipoRemocao(),
			getStTipoRemocao());
	}

}