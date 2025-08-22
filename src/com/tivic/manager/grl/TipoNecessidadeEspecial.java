package com.tivic.manager.grl;

public class TipoNecessidadeEspecial {

	private int cdTipoNecessidadeEspecial;
	private String nmTipoNecessidadeEspecial;
	private String idTipoNecessidadeEspecial;
	private int tpNecessidadeEspecial;

	public TipoNecessidadeEspecial() { }

	public TipoNecessidadeEspecial(int cdTipoNecessidadeEspecial,
			String nmTipoNecessidadeEspecial,
			String idTipoNecessidadeEspecial,
			int tpNecessidadeEspecial) {
		setCdTipoNecessidadeEspecial(cdTipoNecessidadeEspecial);
		setNmTipoNecessidadeEspecial(nmTipoNecessidadeEspecial);
		setIdTipoNecessidadeEspecial(idTipoNecessidadeEspecial);
		setTpNecessidadeEspecial(tpNecessidadeEspecial);
	}
	public void setCdTipoNecessidadeEspecial(int cdTipoNecessidadeEspecial){
		this.cdTipoNecessidadeEspecial=cdTipoNecessidadeEspecial;
	}
	public int getCdTipoNecessidadeEspecial(){
		return this.cdTipoNecessidadeEspecial;
	}
	public void setNmTipoNecessidadeEspecial(String nmTipoNecessidadeEspecial){
		this.nmTipoNecessidadeEspecial=nmTipoNecessidadeEspecial;
	}
	public String getNmTipoNecessidadeEspecial(){
		return this.nmTipoNecessidadeEspecial;
	}
	public void setIdTipoNecessidadeEspecial(String idTipoNecessidadeEspecial){
		this.idTipoNecessidadeEspecial=idTipoNecessidadeEspecial;
	}
	public String getIdTipoNecessidadeEspecial(){
		return this.idTipoNecessidadeEspecial;
	}
	public void setTpNecessidadeEspecial(int tpNecessidadeEspecial){
		this.tpNecessidadeEspecial=tpNecessidadeEspecial;
	}
	public int getTpNecessidadeEspecial(){
		return this.tpNecessidadeEspecial;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTipoNecessidadeEspecial: " +  getCdTipoNecessidadeEspecial();
		valueToString += ", nmTipoNecessidadeEspecial: " +  getNmTipoNecessidadeEspecial();
		valueToString += ", idTipoNecessidadeEspecial: " +  getIdTipoNecessidadeEspecial();
		valueToString += ", tpNecessidadeEspecial: " +  getTpNecessidadeEspecial();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TipoNecessidadeEspecial(getCdTipoNecessidadeEspecial(),
			getNmTipoNecessidadeEspecial(),
			getIdTipoNecessidadeEspecial(),
			getTpNecessidadeEspecial());
	}

}