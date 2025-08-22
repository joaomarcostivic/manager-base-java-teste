package com.tivic.manager.mob;

public class Faixa {

	private int cdVia;
	private int cdFaixa;
	private int nrFaixa;
	private int tpSentido;

	public Faixa() { }

	public Faixa(int cdVia,
			int cdFaixa,
			int nrFaixa,
			int tpSentido) {
		setCdVia(cdVia);
		setCdFaixa(cdFaixa);
		setNrFaixa(nrFaixa);
		setTpSentido(tpSentido);
	}
	public void setCdVia(int cdVia){
		this.cdVia=cdVia;
	}
	public int getCdVia(){
		return this.cdVia;
	}
	public void setCdFaixa(int cdFaixa){
		this.cdFaixa=cdFaixa;
	}
	public int getCdFaixa(){
		return this.cdFaixa;
	}
	public void setNrFaixa(int nrFaixa){
		this.nrFaixa=nrFaixa;
	}
	public int getNrFaixa(){
		return this.nrFaixa;
	}
	public void setTpSentido(int tpSentido){
		this.tpSentido=tpSentido;
	}
	public int getTpSentido(){
		return this.tpSentido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVia: " +  getCdVia();
		valueToString += ", cdFaixa: " +  getCdFaixa();
		valueToString += ", nrFaixa: " +  getNrFaixa();
		valueToString += ", tpSentido: " +  getTpSentido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Faixa(getCdVia(),
			getCdFaixa(),
			getNrFaixa(),
			getTpSentido());
	}

}