package com.tivic.manager.adm;

public class TabelaComissaoCalculo {

	private int cdTabelaComissao;
	private int cdCalculo;
	private int cdCalculoOrigem;
	private float vlInicial;
	private float vlFinal;
	private int tpAplicacao;
	private float vlAplicacao;
	private float vlTeto;
	private int tpVariacaoBase;
	private float vlVariacaoBase;
	private int tpVariacaoResultado;
	private float vlVariacaoResultado;

	public TabelaComissaoCalculo(int cdTabelaComissao,
			int cdCalculo,
			int cdCalculoOrigem,
			float vlInicial,
			float vlFinal,
			int tpAplicacao,
			float vlAplicacao,
			float vlTeto,
			int tpVariacaoBase,
			float vlVariacaoBase,
			int tpVariacaoResultado,
			float vlVariacaoResultado){
		setCdTabelaComissao(cdTabelaComissao);
		setCdCalculo(cdCalculo);
		setCdCalculoOrigem(cdCalculoOrigem);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
		setTpAplicacao(tpAplicacao);
		setVlAplicacao(vlAplicacao);
		setVlTeto(vlTeto);
		setTpVariacaoBase(tpVariacaoBase);
		setVlVariacaoBase(vlVariacaoBase);
		setTpVariacaoResultado(tpVariacaoResultado);
		setVlVariacaoResultado(vlVariacaoResultado);
	}
	public void setCdTabelaComissao(int cdTabelaComissao){
		this.cdTabelaComissao=cdTabelaComissao;
	}
	public int getCdTabelaComissao(){
		return this.cdTabelaComissao;
	}
	public void setCdCalculo(int cdCalculo){
		this.cdCalculo=cdCalculo;
	}
	public int getCdCalculo(){
		return this.cdCalculo;
	}
	public void setCdCalculoOrigem(int cdCalculoOrigem){
		this.cdCalculoOrigem=cdCalculoOrigem;
	}
	public int getCdCalculoOrigem(){
		return this.cdCalculoOrigem;
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
	public void setTpAplicacao(int tpAplicacao){
		this.tpAplicacao=tpAplicacao;
	}
	public int getTpAplicacao(){
		return this.tpAplicacao;
	}
	public void setVlAplicacao(float vlAplicacao){
		this.vlAplicacao=vlAplicacao;
	}
	public float getVlAplicacao(){
		return this.vlAplicacao;
	}
	public void setVlTeto(float vlTeto){
		this.vlTeto=vlTeto;
	}
	public float getVlTeto(){
		return this.vlTeto;
	}
	public void setTpVariacaoBase(int tpVariacaoBase){
		this.tpVariacaoBase=tpVariacaoBase;
	}
	public int getTpVariacaoBase(){
		return this.tpVariacaoBase;
	}
	public void setVlVariacaoBase(float vlVariacaoBase){
		this.vlVariacaoBase=vlVariacaoBase;
	}
	public float getVlVariacaoBase(){
		return this.vlVariacaoBase;
	}
	public void setTpVariacaoResultado(int tpVariacaoResultado){
		this.tpVariacaoResultado=tpVariacaoResultado;
	}
	public int getTpVariacaoResultado(){
		return this.tpVariacaoResultado;
	}
	public void setVlVariacaoResultado(float vlVariacaoResultado){
		this.vlVariacaoResultado=vlVariacaoResultado;
	}
	public float getVlVariacaoResultado(){
		return this.vlVariacaoResultado;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaComissao: " +  getCdTabelaComissao();
		valueToString += ", cdCalculo: " +  getCdCalculo();
		valueToString += ", cdCalculoOrigem: " +  getCdCalculoOrigem();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", tpAplicacao: " +  getTpAplicacao();
		valueToString += ", vlAplicacao: " +  getVlAplicacao();
		valueToString += ", vlTeto: " +  getVlTeto();
		valueToString += ", tpVariacaoBase: " +  getTpVariacaoBase();
		valueToString += ", vlVariacaoBase: " +  getVlVariacaoBase();
		valueToString += ", tpVariacaoResultado: " +  getTpVariacaoResultado();
		valueToString += ", vlVariacaoResultado: " +  getVlVariacaoResultado();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaComissaoCalculo(getCdTabelaComissao(),
			getCdCalculo(),
			getCdCalculoOrigem(),
			getVlInicial(),
			getVlFinal(),
			getTpAplicacao(),
			getVlAplicacao(),
			getVlTeto(),
			getTpVariacaoBase(),
			getVlVariacaoBase(),
			getTpVariacaoResultado(),
			getVlVariacaoResultado());
	}

}
