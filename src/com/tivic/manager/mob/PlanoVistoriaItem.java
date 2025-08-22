package com.tivic.manager.mob;

public class PlanoVistoriaItem {

	private int cdPlanoVistoria;
	private int cdVistoriaItem;
	private int nrOrdemGrupo;
	private int nrOrdemItem;

	public PlanoVistoriaItem(){ }

	public PlanoVistoriaItem(int cdPlanoVistoria,
			int cdVistoriaItem,
			int nrOrdemGrupo,
			int nrOrdemItem){
		setCdPlanoVistoria(cdPlanoVistoria);
		setCdVistoriaItem(cdVistoriaItem);
		setNrOrdemGrupo(nrOrdemGrupo);
		setNrOrdemItem(nrOrdemItem);
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public void setCdVistoriaItem(int cdVistoriaItem){
		this.cdVistoriaItem=cdVistoriaItem;
	}
	public int getCdVistoriaItem(){
		return this.cdVistoriaItem;
	}
	public void setNrOrdemGrupo(int nrOrdemGrupo){
		this.nrOrdemGrupo=nrOrdemGrupo;
	}
	public int getNrOrdemGrupo(){
		return this.nrOrdemGrupo;
	}
	public void setNrOrdemItem(int nrOrdemItem){
		this.nrOrdemItem=nrOrdemItem;
	}
	public int getNrOrdemItem(){
		return this.nrOrdemItem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", cdVistoriaItem: " +  getCdVistoriaItem();
		valueToString += ", nrOrdemGrupo: " +  getNrOrdemGrupo();
		valueToString += ", nrOrdemItem: " +  getNrOrdemItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoVistoriaItem(getCdPlanoVistoria(),
			getCdVistoriaItem(),
			getNrOrdemGrupo(),
			getNrOrdemItem());
	}

}