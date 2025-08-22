package com.tivic.manager.mob;

public class BoatImagem {

	private int cdImagem;
	private int cdBoat;
	private byte[] blbImagem;
	private int tpPosicao;
	private int cdBoatVeiculo;

	public BoatImagem() { }

	public BoatImagem(int cdImagem,
			int cdBoat,
			byte[] blbImagem,
			int tpPosicao,
			int cdBoatVeiculo) {
		setCdImagem(cdImagem);
		setCdBoat(cdBoat);
		setBlbImagem(blbImagem);
		setTpPosicao(tpPosicao);
		setCdBoatVeiculo(cdBoatVeiculo);
	}
	public void setCdImagem(int cdImagem){
		this.cdImagem=cdImagem;
	}
	public int getCdImagem(){
		return this.cdImagem;
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setBlbImagem(byte[] blbImagem){
		this.blbImagem=blbImagem;
	}
	public byte[] getBlbImagem(){
		return this.blbImagem;
	}
	public void setTpPosicao(int tpPosicao){
		this.tpPosicao=tpPosicao;
	}
	public int getTpPosicao(){
		return this.tpPosicao;
	}
	public void setCdBoatVeiculo(int cdBoatVeiculo){
		this.cdBoatVeiculo=cdBoatVeiculo;
	}
	public int getCdBoatVeiculo(){
		return this.cdBoatVeiculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImagem: " +  getCdImagem();
		valueToString += ", cdBoat: " +  getCdBoat();
		valueToString += ", blbImagem: " +  getBlbImagem();
		valueToString += ", tpPosicao: " +  getTpPosicao();
		valueToString += ", cdBoatVeiculo: " +  getCdBoatVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatImagem(getCdImagem(),
			getCdBoat(),
			getBlbImagem(),
			getTpPosicao(),
			getCdBoatVeiculo());
	}

}