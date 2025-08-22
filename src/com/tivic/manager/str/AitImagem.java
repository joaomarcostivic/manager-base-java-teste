package com.tivic.manager.str;

public class AitImagem {

	private int cdImagem;
	private byte[] blbImagem;
	private int lgImpressao;
	private int cdAit;

	public AitImagem(){ }

	public AitImagem(int cdImagem,
			byte[] blbImagem,
			int lgImpressao,
			int cdAit){
		setCdImagem(cdImagem);
		setBlbImagem(blbImagem);
		setLgImpressao(lgImpressao);
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
	public int getLgImpressao() {
		return lgImpressao;
	}
	public void setLgImpressao(int lgImpressao) {
		this.lgImpressao = lgImpressao;
	}	
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", lgImpressao: " +  getLgImpressao();
		valueToString += ", cdAit: " +  getCdAit();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitImagem(getCdImagem(),
			getBlbImagem(),
			getLgImpressao(),
			getCdAit());
	}

}
