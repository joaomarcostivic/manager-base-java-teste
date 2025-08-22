package com.tivic.manager.adm;

public class TabelaCatEconomica {

	private int cdTabelaCatEconomica;
	private String nmTabelaCatEconomica;

	public TabelaCatEconomica(int cdTabelaCatEconomica,
			String nmTabelaCatEconomica){
		setCdTabelaCatEconomica(cdTabelaCatEconomica);
		setNmTabelaCatEconomica(nmTabelaCatEconomica);
	}
	public void setCdTabelaCatEconomica(int cdTabelaCatEconomica){
		this.cdTabelaCatEconomica=cdTabelaCatEconomica;
	}
	public int getCdTabelaCatEconomica(){
		return this.cdTabelaCatEconomica;
	}
	public void setNmTabelaCatEconomica(String nmTabelaCatEconomica){
		this.nmTabelaCatEconomica=nmTabelaCatEconomica;
	}
	public String getNmTabelaCatEconomica(){
		return this.nmTabelaCatEconomica;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaCatEconomica: " +  getCdTabelaCatEconomica();
		valueToString += ", nmTabelaCatEconomica: " +  getNmTabelaCatEconomica();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaCatEconomica(getCdTabelaCatEconomica(),
			getNmTabelaCatEconomica());
	}

}
