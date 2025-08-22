package com.tivic.manager.grl;

public class ModeloFonteDados {

	private int cdModelo;
	private int cdFonte;
	private int cdFontePai;

	public ModeloFonteDados(int cdModelo,
			int cdFonte,
			int cdFontePai){
		setCdModelo(cdModelo);
		setCdFonte(cdFonte);
		setCdFontePai(cdFontePai);
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setCdFonte(int cdFonte){
		this.cdFonte=cdFonte;
	}
	public int getCdFonte(){
		return this.cdFonte;
	}
	public void setCdFontePai(int cdFontePai){
		this.cdFontePai=cdFontePai;
	}
	public int getCdFontePai(){
		return this.cdFontePai;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModelo: " +  getCdModelo();
		valueToString += ", cdFonte: " +  getCdFonte();
		valueToString += ", cdFontePai: " +  getCdFontePai();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloFonteDados(getCdModelo(),
			getCdFonte(),
			getCdFontePai());
	}

}
