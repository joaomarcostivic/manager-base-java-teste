package com.tivic.manager.mob;

public class VistoriaPlanoItemDefeito {

	private int cdVistoriaPlanoItemDefeito;
	private int cdDefeitoVistoriaItem;
	private int cdVistoriaPlanoItem;
	private int cdPlanoVistoria;
	private int cdVistoriaItem;

	public VistoriaPlanoItemDefeito(){ }

	public VistoriaPlanoItemDefeito(int cdVistoriaPlanoItemDefeito,
			int cdDefeitoVistoriaItem,
			int cdVistoriaPlanoItem,
			int cdPlanoVistoria,
			int cdVistoriaItem){
		setCdVistoriaPlanoItemDefeito(cdVistoriaPlanoItemDefeito);
		setCdDefeitoVistoriaItem(cdDefeitoVistoriaItem);
		setCdVistoriaPlanoItem(cdVistoriaPlanoItem);
		setCdPlanoVistoria(cdPlanoVistoria);
		setCdVistoriaItem(cdVistoriaItem);
	}
	public void setCdVistoriaPlanoItemDefeito(int cdVistoriaPlanoItemDefeito){
		this.cdVistoriaPlanoItemDefeito=cdVistoriaPlanoItemDefeito;
	}
	public int getCdVistoriaPlanoItemDefeito(){
		return this.cdVistoriaPlanoItemDefeito;
	}
	public void setCdDefeitoVistoriaItem(int cdDefeitoVistoriaItem){
		this.cdDefeitoVistoriaItem=cdDefeitoVistoriaItem;
	}
	public int getCdDefeitoVistoriaItem(){
		return this.cdDefeitoVistoriaItem;
	}
	public void setCdVistoriaPlanoItem(int cdVistoriaPlanoItem){
		this.cdVistoriaPlanoItem=cdVistoriaPlanoItem;
	}
	public int getCdVistoriaPlanoItem(){
		return this.cdVistoriaPlanoItem;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoriaPlanoItemDefeito: " +  getCdVistoriaPlanoItemDefeito();
		valueToString += ", cdDefeitoVistoriaItem: " +  getCdDefeitoVistoriaItem();
		valueToString += ", cdVistoriaPlanoItem: " +  getCdVistoriaPlanoItem();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", cdVistoriaItem: " +  getCdVistoriaItem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaPlanoItemDefeito(getCdVistoriaPlanoItemDefeito(),
			getCdDefeitoVistoriaItem(),
			getCdVistoriaPlanoItem(),
			getCdPlanoVistoria(),
			getCdVistoriaItem());
	}

}