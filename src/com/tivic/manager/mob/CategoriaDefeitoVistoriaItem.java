package com.tivic.manager.mob;

public class CategoriaDefeitoVistoriaItem {

	private int cdCategoriaDefeitoVistoriaItem;
	private String nmCategoriaDefeito;
	private int cdCategoriaSuperior;

	public CategoriaDefeitoVistoriaItem(){ }

	public CategoriaDefeitoVistoriaItem(int cdCategoriaDefeitoVistoriaItem,
			String nmCategoriaDefeito,
			int cdCategoriaSuperior){
		setCdCategoriaDefeitoVistoriaItem(cdCategoriaDefeitoVistoriaItem);
		setNmCategoriaDefeito(nmCategoriaDefeito);
		setCdCategoriaSuperior(cdCategoriaSuperior);
	}
	public void setCdCategoriaDefeitoVistoriaItem(int cdCategoriaDefeitoVistoriaItem){
		this.cdCategoriaDefeitoVistoriaItem=cdCategoriaDefeitoVistoriaItem;
	}
	public int getCdCategoriaDefeitoVistoriaItem(){
		return this.cdCategoriaDefeitoVistoriaItem;
	}
	public void setNmCategoriaDefeito(String nmCategoriaDefeito){
		this.nmCategoriaDefeito=nmCategoriaDefeito;
	}
	public String getNmCategoriaDefeito(){
		return this.nmCategoriaDefeito;
	}
	public void setCdCategoriaSuperior(int cdCategoriaSuperior){
		this.cdCategoriaSuperior=cdCategoriaSuperior;
	}
	public int getCdCategoriaSuperior(){
		return this.cdCategoriaSuperior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCategoriaDefeitoVistoriaItem: " +  getCdCategoriaDefeitoVistoriaItem();
		valueToString += ", nmCategoriaDefeito: " +  getNmCategoriaDefeito();
		valueToString += ", cdCategoriaSuperior: " +  getCdCategoriaSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CategoriaDefeitoVistoriaItem(getCdCategoriaDefeitoVistoriaItem(),
			getNmCategoriaDefeito(),
			getCdCategoriaSuperior());
	}

}