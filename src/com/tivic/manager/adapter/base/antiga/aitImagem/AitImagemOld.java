package com.tivic.manager.adapter.base.antiga.aitImagem;

public class AitImagemOld {

	private int cdImagem;
	private int cdAit;
	private byte[] blbImagem;
	private int tpImagem;
	private int lgImpressao;

	public AitImagemOld() { }

	public AitImagemOld(int cdImagem,
			int cdAit,
			byte[] blbImagem,
			int tpImagem,
			int lgImpressao) {
		setCdImagem(cdImagem);
		setCdAit(cdAit);
		setBlbImagem(blbImagem);
		setTpImagem(tpImagem);
		setLgImpressao(lgImpressao);
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
	public void setTpImagem(int tpImagem){
		this.tpImagem=tpImagem;
	}
	public int getTpImagem(){
		return this.tpImagem;
	}
	public void setLgImpressao(int lgImpressao){
		this.lgImpressao=lgImpressao;
	}
	public int getLgImpressao(){
		return this.lgImpressao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", tpImagem: " +  getTpImagem();
		valueToString += ", lgImpressao: " +  getLgImpressao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitImagemOld(getCdImagem(),
			getCdAit(),
			getBlbImagem(),
			getTpImagem(),
			getLgImpressao());
	}

}