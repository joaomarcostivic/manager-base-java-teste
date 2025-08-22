package com.tivic.manager.ord;

public class TipoLaudo {

	private int cdTipoLaudo;
	private String nmTipoLaudo;
	private String idTipoLaudo;

	public TipoLaudo(){ }

	public TipoLaudo(int cdTipoLaudo,
			String nmTipoLaudo,
			String idTipoLaudo){
		setCdTipoLaudo(cdTipoLaudo);
		setNmTipoLaudo(nmTipoLaudo);
		setIdTipoLaudo(idTipoLaudo);
	}
	public void setCdTipoLaudo(int cdTipoLaudo){
		this.cdTipoLaudo=cdTipoLaudo;
	}
	public int getCdTipoLaudo(){
		return this.cdTipoLaudo;
	}
	public void setNmTipoLaudo(String nmTipoLaudo){
		this.nmTipoLaudo=nmTipoLaudo;
	}
	public String getNmTipoLaudo(){
		return this.nmTipoLaudo;
	}
	public void setIdTipoLaudo(String idTipoLaudo){
		this.idTipoLaudo=idTipoLaudo;
	}
	public String getIdTipoLaudo(){
		return this.idTipoLaudo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoLaudo: " +  getCdTipoLaudo();
		valueToString += ", nmTipoLaudo: " +  getNmTipoLaudo();
		valueToString += ", idTipoLaudo: " +  getIdTipoLaudo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoLaudo(getCdTipoLaudo(),
			getNmTipoLaudo(),
			getIdTipoLaudo());
	}

}
