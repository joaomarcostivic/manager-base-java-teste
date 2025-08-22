package com.tivic.manager.mob.lotes.model.aitimagem;

public class AitImagem {
	private int cdImagem;
	private int cdAit;
	private byte[] blbImagem;
	private int lgImpressao;
	private int tpImagem;

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
}
