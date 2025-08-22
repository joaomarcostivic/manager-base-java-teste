package com.tivic.manager.mob;

public class PlanoVistoriaItemDefeito {

	private int cdDefeitoVistoriaItem;
	private int cdPlanoVistoria;
	private int cdVistoriaItem;
	private int vlPrazo;
	private int tpPrazo;

	public PlanoVistoriaItemDefeito(){ }

	public PlanoVistoriaItemDefeito(int cdDefeitoVistoriaItem,
			int cdPlanoVistoria,
			int cdVistoriaItem,
			int vlPrazo,
			int tpPrazo){
		setCdDefeitoVistoriaItem(cdDefeitoVistoriaItem);
		setCdPlanoVistoria(cdPlanoVistoria);
		setCdVistoriaItem(cdVistoriaItem);
		setVlPrazo(vlPrazo);
		setTpPrazo(tpPrazo);
	}
	public void setCdDefeitoVistoriaItem(int cdDefeitoVistoriaItem){
		this.cdDefeitoVistoriaItem=cdDefeitoVistoriaItem;
	}
	public int getCdDefeitoVistoriaItem(){
		return this.cdDefeitoVistoriaItem;
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
	public void setVlPrazo(int vlPrazo){
		this.vlPrazo=vlPrazo;
	}
	public int getVlPrazo(){
		return this.vlPrazo;
	}
	public void setTpPrazo(int tpPrazo){
		this.tpPrazo=tpPrazo;
	}
	public int getTpPrazo(){
		return this.tpPrazo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDefeitoVistoriaItem: " +  getCdDefeitoVistoriaItem();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", cdVistoriaItem: " +  getCdVistoriaItem();
		valueToString += ", vlPrazo: " +  getVlPrazo();
		valueToString += ", tpPrazo: " +  getTpPrazo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoVistoriaItemDefeito(getCdDefeitoVistoriaItem(),
			getCdPlanoVistoria(),
			getCdVistoriaItem(),
			getVlPrazo(),
			getTpPrazo());
	}

}