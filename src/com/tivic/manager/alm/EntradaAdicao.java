package com.tivic.manager.alm;

public class EntradaAdicao {

	private int cdEntradaAdicao;
	private int cdEntradaDeclaracaoImportacao;
	private int cdDocumentoEntrada;
	private int cdNcm;
	private float qtItens;
	private float vlTotal;
	private float vlBaseCalculoIcms;
	private float prAliquotaIcms;
	private float vlBaseCalculoIpi;
	private float prAliquotaIpi;
	private float vlBaseCalculoPis;
	private float prAliquotaPis;
	private float vlBaseCalculoCofins;
	private float prAliquotaCofins;
	private float vlBaseCalculoIi;
	private float prAliquotaIi;
	private float vlBaseCalculoAntiDumping;
	private float prAliquotaAntiDumping;

	public EntradaAdicao(){ }

	public EntradaAdicao(int cdEntradaAdicao,
			int cdEntradaDeclaracaoImportacao,
			int cdDocumentoEntrada,
			int cdNcm,
			float qtItens,
			float vlTotal,
			float vlBaseCalculoIcms,
			float prAliquotaIcms,
			float vlBaseCalculoIpi,
			float prAliquotaIpi,
			float vlBaseCalculoPis,
			float prAliquotaPis,
			float vlBaseCalculoCofins,
			float prAliquotaCofins,
			float vlBaseCalculoIi,
			float prAliquotaIi,
			float vlBaseCalculoAntiDumping,
			float prAliquotaAntiDumping){
		setCdEntradaAdicao(cdEntradaAdicao);
		setCdEntradaDeclaracaoImportacao(cdEntradaDeclaracaoImportacao);
		setCdDocumentoEntrada(cdDocumentoEntrada);
		setCdNcm(cdNcm);
		setQtItens(qtItens);
		setVlTotal(vlTotal);
		setVlBaseCalculoIcms(vlBaseCalculoIcms);
		setPrAliquotaIcms(prAliquotaIcms);
		setVlBaseCalculoIpi(vlBaseCalculoIpi);
		setPrAliquotaIpi(prAliquotaIpi);
		setVlBaseCalculoPis(vlBaseCalculoPis);
		setPrAliquotaPis(prAliquotaPis);
		setVlBaseCalculoCofins(vlBaseCalculoCofins);
		setPrAliquotaCofins(prAliquotaCofins);
		setVlBaseCalculoIi(vlBaseCalculoIi);
		setPrAliquotaIi(prAliquotaIi);
		setVlBaseCalculoAntiDumping(vlBaseCalculoAntiDumping);
		setPrAliquotaAntiDumping(prAliquotaAntiDumping);
	}
	public void setCdEntradaAdicao(int cdEntradaAdicao){
		this.cdEntradaAdicao=cdEntradaAdicao;
	}
	public int getCdEntradaAdicao(){
		return this.cdEntradaAdicao;
	}
	public void setCdEntradaDeclaracaoImportacao(int cdEntradaDeclaracaoImportacao){
		this.cdEntradaDeclaracaoImportacao=cdEntradaDeclaracaoImportacao;
	}
	public int getCdEntradaDeclaracaoImportacao(){
		return this.cdEntradaDeclaracaoImportacao;
	}
	public void setCdDocumentoEntrada(int cdDocumentoEntrada){
		this.cdDocumentoEntrada=cdDocumentoEntrada;
	}
	public int getCdDocumentoEntrada(){
		return this.cdDocumentoEntrada;
	}
	public void setCdNcm(int cdNcm){
		this.cdNcm=cdNcm;
	}
	public int getCdNcm(){
		return this.cdNcm;
	}
	public void setQtItens(float qtItens){
		this.qtItens=qtItens;
	}
	public float getQtItens(){
		return this.qtItens;
	}
	public void setVlTotal(float vlTotal){
		this.vlTotal=vlTotal;
	}
	public float getVlTotal(){
		return this.vlTotal;
	}
	public void setVlBaseCalculoIcms(float vlBaseCalculoIcms){
		this.vlBaseCalculoIcms=vlBaseCalculoIcms;
	}
	public float getVlBaseCalculoIcms(){
		return this.vlBaseCalculoIcms;
	}
	public void setPrAliquotaIcms(float prAliquotaIcms){
		this.prAliquotaIcms=prAliquotaIcms;
	}
	public float getPrAliquotaIcms(){
		return this.prAliquotaIcms;
	}
	public void setVlBaseCalculoIpi(float vlBaseCalculoIpi){
		this.vlBaseCalculoIpi=vlBaseCalculoIpi;
	}
	public float getVlBaseCalculoIpi(){
		return this.vlBaseCalculoIpi;
	}
	public void setPrAliquotaIpi(float prAliquotaIpi){
		this.prAliquotaIpi=prAliquotaIpi;
	}
	public float getPrAliquotaIpi(){
		return this.prAliquotaIpi;
	}
	public void setVlBaseCalculoPis(float vlBaseCalculoPis){
		this.vlBaseCalculoPis=vlBaseCalculoPis;
	}
	public float getVlBaseCalculoPis(){
		return this.vlBaseCalculoPis;
	}
	public void setPrAliquotaPis(float prAliquotaPis){
		this.prAliquotaPis=prAliquotaPis;
	}
	public float getPrAliquotaPis(){
		return this.prAliquotaPis;
	}
	public void setVlBaseCalculoCofins(float vlBaseCalculoCofins){
		this.vlBaseCalculoCofins=vlBaseCalculoCofins;
	}
	public float getVlBaseCalculoCofins(){
		return this.vlBaseCalculoCofins;
	}
	public void setPrAliquotaCofins(float prAliquotaCofins){
		this.prAliquotaCofins=prAliquotaCofins;
	}
	public float getPrAliquotaCofins(){
		return this.prAliquotaCofins;
	}
	public void setVlBaseCalculoIi(float vlBaseCalculoIi){
		this.vlBaseCalculoIi=vlBaseCalculoIi;
	}
	public float getVlBaseCalculoIi(){
		return this.vlBaseCalculoIi;
	}
	public void setPrAliquotaIi(float prAliquotaIi){
		this.prAliquotaIi=prAliquotaIi;
	}
	public float getPrAliquotaIi(){
		return this.prAliquotaIi;
	}
	public void setVlBaseCalculoAntiDumping(float vlBaseCalculoAntiDumping){
		this.vlBaseCalculoAntiDumping=vlBaseCalculoAntiDumping;
	}
	public float getVlBaseCalculoAntiDumping(){
		return this.vlBaseCalculoAntiDumping;
	}
	public void setPrAliquotaAntiDumping(float prAliquotaAntiDumping){
		this.prAliquotaAntiDumping=prAliquotaAntiDumping;
	}
	public float getPrAliquotaAntiDumping(){
		return this.prAliquotaAntiDumping;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEntradaAdicao: " +  getCdEntradaAdicao();
		valueToString += ", cdEntradaDeclaracaoImportacao: " +  getCdEntradaDeclaracaoImportacao();
		valueToString += ", cdDocumentoEntrada: " +  getCdDocumentoEntrada();
		valueToString += ", cdNcm: " +  getCdNcm();
		valueToString += ", qtItens: " +  getQtItens();
		valueToString += ", vlTotal: " +  getVlTotal();
		valueToString += ", vlBaseCalculoIcms: " +  getVlBaseCalculoIcms();
		valueToString += ", prAliquotaIcms: " +  getPrAliquotaIcms();
		valueToString += ", vlBaseCalculoIpi: " +  getVlBaseCalculoIpi();
		valueToString += ", prAliquotaIpi: " +  getPrAliquotaIpi();
		valueToString += ", vlBaseCalculoPis: " +  getVlBaseCalculoPis();
		valueToString += ", prAliquotaPis: " +  getPrAliquotaPis();
		valueToString += ", vlBaseCalculoCofins: " +  getVlBaseCalculoCofins();
		valueToString += ", prAliquotaCofins: " +  getPrAliquotaCofins();
		valueToString += ", vlBaseCalculoIi: " +  getVlBaseCalculoIi();
		valueToString += ", prAliquotaIi: " +  getPrAliquotaIi();
		valueToString += ", vlBaseCalculoAntiDumping: " +  getVlBaseCalculoAntiDumping();
		valueToString += ", prAliquotaAntiDumping: " +  getPrAliquotaAntiDumping();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EntradaAdicao(getCdEntradaAdicao(),
			getCdEntradaDeclaracaoImportacao(),
			getCdDocumentoEntrada(),
			getCdNcm(),
			getQtItens(),
			getVlTotal(),
			getVlBaseCalculoIcms(),
			getPrAliquotaIcms(),
			getVlBaseCalculoIpi(),
			getPrAliquotaIpi(),
			getVlBaseCalculoPis(),
			getPrAliquotaPis(),
			getVlBaseCalculoCofins(),
			getPrAliquotaCofins(),
			getVlBaseCalculoIi(),
			getPrAliquotaIi(),
			getVlBaseCalculoAntiDumping(),
			getPrAliquotaAntiDumping());
	}

}