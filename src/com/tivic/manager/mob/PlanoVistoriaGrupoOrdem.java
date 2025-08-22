package com.tivic.manager.mob;

public class PlanoVistoriaGrupoOrdem {

	private int cdPlanoVistoriaGrupoOrdem;
	private int cdVistoriaItemGrupo;
	private int cdPlanoVistoria;
	private int nrOrdemGrupo;

	public PlanoVistoriaGrupoOrdem(){ }

	public PlanoVistoriaGrupoOrdem(int cdPlanoVistoriaGrupoOrdem,
			int cdVistoriaItemGrupo,
			int cdPlanoVistoria,
			int nrOrdemGrupo){
		setCdPlanoVistoriaGrupoOrdem(cdPlanoVistoriaGrupoOrdem);
		setCdVistoriaItemGrupo(cdVistoriaItemGrupo);
		setCdPlanoVistoria(cdPlanoVistoria);
		setNrOrdemGrupo(nrOrdemGrupo);
	}
	public void setCdPlanoVistoriaGrupoOrdem(int cdPlanoVistoriaGrupoOrdem){
		this.cdPlanoVistoriaGrupoOrdem=cdPlanoVistoriaGrupoOrdem;
	}
	public int getCdPlanoVistoriaGrupoOrdem(){
		return this.cdPlanoVistoriaGrupoOrdem;
	}
	public void setCdVistoriaItemGrupo(int cdVistoriaItemGrupo){
		this.cdVistoriaItemGrupo=cdVistoriaItemGrupo;
	}
	public int getCdVistoriaItemGrupo(){
		return this.cdVistoriaItemGrupo;
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public void setNrOrdemGrupo(int nrOrdemGrupo){
		this.nrOrdemGrupo=nrOrdemGrupo;
	}
	public int getNrOrdemGrupo(){
		return this.nrOrdemGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoVistoriaGrupoOrdem: " +  getCdPlanoVistoriaGrupoOrdem();
		valueToString += ", cdVistoriaItemGrupo: " +  getCdVistoriaItemGrupo();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", nrOrdemGrupo: " +  getNrOrdemGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoVistoriaGrupoOrdem(getCdPlanoVistoriaGrupoOrdem(),
			getCdVistoriaItemGrupo(),
			getCdPlanoVistoria(),
			getNrOrdemGrupo());
	}

}