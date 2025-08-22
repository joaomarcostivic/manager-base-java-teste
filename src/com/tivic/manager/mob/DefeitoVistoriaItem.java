package com.tivic.manager.mob;

public class DefeitoVistoriaItem {

	private int cdDefeitoVistoriaItem;
	private int cdCategoriaDefeitoVistoriaItem;
	private String nmDefeito;
	private String idDefeito;

	public DefeitoVistoriaItem(){ }

	public DefeitoVistoriaItem(int cdDefeitoVistoriaItem,
			int cdCategoriaDefeitoVistoriaItem,
			String nmDefeito,
			String idDefeito){
		setCdDefeitoVistoriaItem(cdDefeitoVistoriaItem);
		setCdCategoriaDefeitoVistoriaItem(cdCategoriaDefeitoVistoriaItem);
		setNmDefeito(nmDefeito);
		setIdDefeito(idDefeito);
	}
	public void setCdDefeitoVistoriaItem(int cdDefeitoVistoriaItem){
		this.cdDefeitoVistoriaItem=cdDefeitoVistoriaItem;
	}
	public int getCdDefeitoVistoriaItem(){
		return this.cdDefeitoVistoriaItem;
	}
	public void setCdCategoriaDefeitoVistoriaItem(int cdCategoriaDefeitoVistoriaItem){
		this.cdCategoriaDefeitoVistoriaItem=cdCategoriaDefeitoVistoriaItem;
	}
	public int getCdCategoriaDefeitoVistoriaItem(){
		return this.cdCategoriaDefeitoVistoriaItem;
	}
	public void setNmDefeito(String nmDefeito){
		this.nmDefeito=nmDefeito;
	}
	public String getNmDefeito(){
		return this.nmDefeito;
	}
	public void setIdDefeito(String idDefeito){
		this.idDefeito=idDefeito;
	}
	public String getIdDefeito(){
		return this.idDefeito;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDefeitoVistoriaItem: " +  getCdDefeitoVistoriaItem();
		valueToString += ", cdCategoriaDefeitoVistoriaItem: " +  getCdCategoriaDefeitoVistoriaItem();
		valueToString += ", nmDefeito: " +  getNmDefeito();
		valueToString += ", idDefeito: " +  getIdDefeito();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DefeitoVistoriaItem(getCdDefeitoVistoriaItem(),
			getCdCategoriaDefeitoVistoriaItem(),
			getNmDefeito(),
			getIdDefeito());
	}

}