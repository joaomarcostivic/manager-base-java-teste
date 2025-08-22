package com.tivic.manager.mob;

public class RrdImagem {

	private int cdImagem;
	private int cdRrd;
	private byte[] blbImagem;
	private int tpImagem;
	private int lgImpressao;

	public RrdImagem() { }

	public RrdImagem(int cdImagem,
			int cdRrd,
			byte[] blbImagem,
			int tpImagem,
			int lgImpressao) {
		setCdImagem(cdImagem);
		setCdRrd(cdRrd);
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
	public void setCdRrd(int cdRrd){
		this.cdRrd=cdRrd;
	}
	public int getCdRrd(){
		return this.cdRrd;
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
		valueToString += ", cdRrd: " +  getCdRrd();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", tpImagem: " +  getTpImagem();
		valueToString += ", lgImpressao: " +  getLgImpressao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RrdImagem(getCdImagem(),
			getCdRrd(),
			getBlbImagem(),
			getTpImagem(),
			getLgImpressao());
	}

}