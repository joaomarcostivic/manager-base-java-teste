package com.tivic.manager.mob;

public class TrravImagem {

	private int cdImagem;
	private int cdTrrav;
	private byte[] blbImagem;
	private int tpImagem;
	private int lgImpressao;

	public TrravImagem() { }

	public TrravImagem(int cdImagem,
			int cdTrrav,
			byte[] blbImagem,
			int tpImagem,
			int lgImpressao) {
		setCdImagem(cdImagem);
		setCdTrrav(cdTrrav);
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
	public void setCdTrrav(int cdTrrav){
		this.cdTrrav=cdTrrav;
	}
	public int getCdTrrav(){
		return this.cdTrrav;
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
		valueToString += ", cdTrrav: " +  getCdTrrav();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", tpImagem: " +  getTpImagem();
		valueToString += ", lgImpressao: " +  getLgImpressao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TrravImagem(getCdImagem(),
			getCdTrrav(),
			getBlbImagem(),
			getTpImagem(),
			getLgImpressao());
	}

}