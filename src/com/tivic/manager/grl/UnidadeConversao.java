package com.tivic.manager.grl;

public class UnidadeConversao {

	private int cdUnidadeOrigem;
	private int cdUnidadeDestino;
	private float vlFatorConversao;
	private int tpOperacaoConversao;

	public UnidadeConversao(int cdUnidadeOrigem,
			int cdUnidadeDestino,
			float vlFatorConversao,
			int tpOperacaoConversao){
		setCdUnidadeOrigem(cdUnidadeOrigem);
		setCdUnidadeDestino(cdUnidadeDestino);
		setVlFatorConversao(vlFatorConversao);
		setTpOperacaoConversao(tpOperacaoConversao);
	}
	public void setCdUnidadeOrigem(int cdUnidadeOrigem){
		this.cdUnidadeOrigem=cdUnidadeOrigem;
	}
	public int getCdUnidadeOrigem(){
		return this.cdUnidadeOrigem;
	}
	public void setCdUnidadeDestino(int cdUnidadeDestino){
		this.cdUnidadeDestino=cdUnidadeDestino;
	}
	public int getCdUnidadeDestino(){
		return this.cdUnidadeDestino;
	}
	public void setVlFatorConversao(float vlFatorConversao){
		this.vlFatorConversao=vlFatorConversao;
	}
	public float getVlFatorConversao(){
		return this.vlFatorConversao;
	}
	public void setTpOperacaoConversao(int tpOperacaoConversao){
		this.tpOperacaoConversao=tpOperacaoConversao;
	}
	public int getTpOperacaoConversao(){
		return this.tpOperacaoConversao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUnidadeOrigem: " +  getCdUnidadeOrigem();
		valueToString += ", cdUnidadeDestino: " +  getCdUnidadeDestino();
		valueToString += ", vlFatorConversao: " +  getVlFatorConversao();
		valueToString += ", tpOperacaoConversao: " +  getTpOperacaoConversao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UnidadeConversao(getCdUnidadeOrigem(),
			getCdUnidadeDestino(),
			getVlFatorConversao(),
			getTpOperacaoConversao());
	}

}
