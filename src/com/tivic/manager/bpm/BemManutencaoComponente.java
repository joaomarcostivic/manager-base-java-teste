package com.tivic.manager.bpm;

public class BemManutencaoComponente {

	private int cdManutencao;
	private int cdComponente;
	private int qtComponente;

	public BemManutencaoComponente(int cdManutencao,
			int cdComponente,
			int qtComponente){
		setCdManutencao(cdManutencao);
		setCdComponente(cdComponente);
		setQtComponente(qtComponente);
	}
	public void setCdManutencao(int cdManutencao){
		this.cdManutencao=cdManutencao;
	}
	public int getCdManutencao(){
		return this.cdManutencao;
	}
	public void setCdComponente(int cdComponente){
		this.cdComponente=cdComponente;
	}
	public int getCdComponente(){
		return this.cdComponente;
	}
	public void setQtComponente(int qtComponente){
		this.qtComponente=qtComponente;
	}
	public int getQtComponente(){
		return this.qtComponente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdManutencao: " +  getCdManutencao();
		valueToString += ", cdComponente: " +  getCdComponente();
		valueToString += ", qtComponente: " +  getQtComponente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BemManutencaoComponente(getCdManutencao(),
			getCdComponente(),
			getQtComponente());
	}

}