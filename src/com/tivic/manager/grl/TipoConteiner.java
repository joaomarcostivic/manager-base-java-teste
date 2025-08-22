package com.tivic.manager.grl;

public class TipoConteiner {

	private int cdTipoConteiner;
	private String nmTipoConteiner;

	public TipoConteiner(int cdTipoConteiner,
			String nmTipoConteiner){
		setCdTipoConteiner(cdTipoConteiner);
		setNmTipoConteiner(nmTipoConteiner);
	}
	public void setCdTipoConteiner(int cdTipoConteiner){
		this.cdTipoConteiner=cdTipoConteiner;
	}
	public int getCdTipoConteiner(){
		return this.cdTipoConteiner;
	}
	public void setNmTipoConteiner(String nmTipoConteiner){
		this.nmTipoConteiner=nmTipoConteiner;
	}
	public String getNmTipoConteiner(){
		return this.nmTipoConteiner;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoConteiner: " +  getCdTipoConteiner();
		valueToString += ", nmTipoConteiner: " +  getNmTipoConteiner();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoConteiner(getCdTipoConteiner(),
			getNmTipoConteiner());
	}

}
