package com.tivic.manager.mob;

public class VistoriaPlanoItem {

	private int cdVistoriaPlanoItem;
	private int cdVistoriaItem;
	private int cdPlanoVistoria;
	private int cdVistoria;
	private int stItem;
	private String dsObservacao;

	public VistoriaPlanoItem(){ }

	public VistoriaPlanoItem(int cdVistoriaPlanoItem,
			int cdVistoriaItem,
			int cdPlanoVistoria,
			int cdVistoria,
			int stItem,
			String dsObservacao){
		setCdVistoriaPlanoItem(cdVistoriaPlanoItem);
		setCdVistoriaItem(cdVistoriaItem);
		setCdPlanoVistoria(cdPlanoVistoria);
		setCdVistoria(cdVistoria);
		setStItem(stItem);
		setDsObservacao(dsObservacao);
	}
	public void setCdVistoriaPlanoItem(int cdVistoriaPlanoItem){
		this.cdVistoriaPlanoItem=cdVistoriaPlanoItem;
	}
	public int getCdVistoriaPlanoItem(){
		return this.cdVistoriaPlanoItem;
	}
	public void setCdVistoriaItem(int cdVistoriaItem){
		this.cdVistoriaItem=cdVistoriaItem;
	}
	public int getCdVistoriaItem(){
		return this.cdVistoriaItem;
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public void setCdVistoria(int cdVistoria){
		this.cdVistoria=cdVistoria;
	}
	public int getCdVistoria(){
		return this.cdVistoria;
	}
	public void setStItem(int stItem){
		this.stItem=stItem;
	}
	public int getStItem(){
		return this.stItem;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoriaPlanoItem: " +  getCdVistoriaPlanoItem();
		valueToString += ", cdVistoriaItem: " +  getCdVistoriaItem();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", cdVistoria: " +  getCdVistoria();
		valueToString += ", stItem: " +  getStItem();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new VistoriaPlanoItem(getCdVistoriaPlanoItem(),
			getCdVistoriaItem(),
			getCdPlanoVistoria(),
			getCdVistoria(),
			getStItem(),
			getDsObservacao());
	}

}