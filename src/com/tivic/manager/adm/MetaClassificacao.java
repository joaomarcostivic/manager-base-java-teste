package com.tivic.manager.adm;

public class MetaClassificacao {

	private int cdTabelaComissao;
	private int cdMeta;
	private int cdClassificacao;
	private String nmClassificacao;
	private float vlInicial;
	private float vlFinal;
	private int tpCalculo;

	public MetaClassificacao(int cdTabelaComissao,
			int cdMeta,
			int cdClassificacao,
			String nmClassificacao,
			float vlInicial,
			float vlFinal,
			int tpCalculo){
		setCdTabelaComissao(cdTabelaComissao);
		setCdMeta(cdMeta);
		setCdClassificacao(cdClassificacao);
		setNmClassificacao(nmClassificacao);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
		setTpCalculo(tpCalculo);
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setCdMeta(int cdMeta){
		this.cdMeta=cdMeta;
	}
	public int getCdMeta(){
		return this.cdMeta;
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setNmClassificacao(String nmClassificacao){
		this.nmClassificacao=nmClassificacao;
	}
	public String getNmClassificacao(){
		return this.nmClassificacao;
	}
	public void setVlInicial(float vlInicial){
		this.vlInicial=vlInicial;
	}
	public float getVlInicial(){
		return this.vlInicial;
	}
	public void setVlFinal(float vlFinal){
		this.vlFinal=vlFinal;
	}
	public float getVlFinal(){
		return this.vlFinal;
	}
	public void setTpCalculo(int tpCalculo){
		this.tpCalculo=tpCalculo;
	}
	public int getTpCalculo(){
		return this.tpCalculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaComissao: " +  getCdTabelaComissao();
		valueToString += ", cdMeta: " +  getCdMeta();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		valueToString += ", nmClassificacao: " +  getNmClassificacao();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", tpCalculo: " +  getTpCalculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MetaClassificacao(getCdTabelaComissao(),
			getCdMeta(),
			getCdClassificacao(),
			getNmClassificacao(),
			getVlInicial(),
			getVlFinal(),
			getTpCalculo());
	}

}
