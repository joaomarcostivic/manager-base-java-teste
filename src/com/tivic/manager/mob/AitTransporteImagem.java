package com.tivic.manager.mob;

public class AitTransporteImagem {

	private int cdImagem;
	private int cdAit;
	private byte[] blbImagem;

	public AitTransporteImagem(){ }

	public AitTransporteImagem(int cdImagem,
			int cdAit,
			byte[] blbImagem){
		setCdImagem(cdImagem);
		setCdAit(cdAit);
		setBlbImagem(blbImagem);
	}
	public void setCdImagem(int cdImagem){
		this.cdImagem=cdImagem;
	}
	public int getCdImagem(){
		return this.cdImagem;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setBlbImagem(byte[] blbImagem){
		this.blbImagem=blbImagem;
	}
	public byte[] getBlbImagem(){
		return this.blbImagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", blbImagem: " +  getBlbImagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitTransporteImagem(getCdImagem(),
			getCdAit(),
			getBlbImagem());
	}

}