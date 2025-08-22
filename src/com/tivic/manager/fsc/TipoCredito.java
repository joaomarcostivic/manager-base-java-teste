package com.tivic.manager.fsc;

public class TipoCredito {

	private int cdTipoCredito;
	private String nmTipoCredito;
	private String nrTipoCredito;
	private int tpGrupoTipoCredito;

	public TipoCredito(int cdTipoCredito,
			String nmTipoCredito,
			String nrTipoCredito,
			int tpGrupoTipoCredito){
		setCdTipoCredito(cdTipoCredito);
		setNmTipoCredito(nmTipoCredito);
		setNrTipoCredito(nrTipoCredito);
		setTpGrupoTipoCredito(tpGrupoTipoCredito);
	}
	public void setCdTipoCredito(int cdTipoCredito){
		this.cdTipoCredito=cdTipoCredito;
	}
	public int getCdTipoCredito(){
		return this.cdTipoCredito;
	}
	public void setNmTipoCredito(String nmTipoCredito){
		this.nmTipoCredito=nmTipoCredito;
	}
	public String getNmTipoCredito(){
		return this.nmTipoCredito;
	}
	public void setNrTipoCredito(String nrTipoCredito){
		this.nrTipoCredito=nrTipoCredito;
	}
	public String getNrTipoCredito(){
		return this.nrTipoCredito;
	}
	public void setTpGrupoTipoCredito(int tpGrupoTipoCredito){
		this.tpGrupoTipoCredito=tpGrupoTipoCredito;
	}
	public int getTpGrupoTipoCredito(){
		return this.tpGrupoTipoCredito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoCredito: " +  getCdTipoCredito();
		valueToString += ", nmTipoCredito: " +  getNmTipoCredito();
		valueToString += ", nrTipoCredito: " +  getNrTipoCredito();
		valueToString += ", tpGrupoTipoCredito: " +  getTpGrupoTipoCredito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoCredito(getCdTipoCredito(),
			getNmTipoCredito(),
			getNrTipoCredito(),
			getTpGrupoTipoCredito());
	}

}