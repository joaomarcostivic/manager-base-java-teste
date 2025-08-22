package com.tivic.manager.str;

public class AitTransporteImagem {

	private int cdImagem;
	private byte[] blbImagem;
	private int cdAit;

	public AitTransporteImagem(){ }

	public AitTransporteImagem(int cdImagem,
			byte[] blbImagem,
			int cdAit){
		setCdImagem(cdImagem);
		setBlbImagem(blbImagem);
		setCdAit(cdAit);
	}
	public void setCdImagem(int cdImagem){
		this.cdImagem=cdImagem;
	}
	public int getCdImagem(){
		return this.cdImagem;
	}
	public void setBlbImagem(byte[] blbImagem){
		this.blbImagem=blbImagem;
	}
	public byte[] getBlbImagem(){
		return this.blbImagem;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", cdAit: " +  getCdAit();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitImagem(getCdImagem(),
			getBlbImagem(),
			0,
			getCdAit());
	}

}
