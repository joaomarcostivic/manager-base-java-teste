package com.tivic.manager.mob;

public class AitImagem {

	private int cdImagem;
	private int cdAit;
	private byte[] blbImagem;
	private int lgImpressao;
	private int tpImagem;

	public AitImagem() { }

	public AitImagem(int cdImagem,
			int cdAit,
			byte[] blbImagem,
			int lgImpressao,
			int tpImagem) {
		setCdImagem(cdImagem);
		setCdAit(cdAit);
		setBlbImagem(blbImagem);
		setLgImpressao(lgImpressao);
		setTpImagem(tpImagem);
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
	public void setLgImpressao(int lgImpressao){
		this.lgImpressao=lgImpressao;
	}
	public int getLgImpressao(){
		return this.lgImpressao;
	}
	public void setTpImagem(int tpImagem){
		this.tpImagem=tpImagem;
	}
	public int getTpImagem(){
		return this.tpImagem;
	}
	
	public void tornarImagemImpressao() {
		this.lgImpressao = 1;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", lgImpressao: " +  getLgImpressao();
		valueToString += ", tpImagem: " +  getTpImagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitImagem(getCdImagem(),
			getCdAit(),
			getBlbImagem(),
			getLgImpressao(),
			getTpImagem());
	}

}
