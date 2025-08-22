package com.tivic.manager.fsc;

public class SituacaoTributaria {

	private int cdTributo;
	private int cdSituacaoTributaria;
	private String nrSituacaoTributaria;
	private String nmSituacaoTributaria;
	private String sgSituacaoTributaria;
	private int tpSituacaoTributariaEcf;
	private int lgSimples;
	private int lgSubstituicao;
	private int lgGeraCredito;
	private int lgPartilha;
	private int lgMotivoIsencao;
	private int lgReducaoBase;
	private int lgRetido;

	public SituacaoTributaria(int cdTributo,
			int cdSituacaoTributaria,
			String nrSituacaoTributaria,
			String nmSituacaoTributaria,
			String sgSituacaoTributaria,
			int tpSituacaoTributariaEcf,
			int lgSimples,
			int lgSubstituicao,
			int lgGeraCredito,
			int lgPartilha,
			int lgMotivoIsencao,
			int lgReducaoBase,
			int lgRetido){
		setCdTributo(cdTributo);
		setCdSituacaoTributaria(cdSituacaoTributaria);
		setNrSituacaoTributaria(nrSituacaoTributaria);
		setNmSituacaoTributaria(nmSituacaoTributaria);
		setSgSituacaoTributaria(sgSituacaoTributaria);
		setTpSituacaoTributariaEcf(tpSituacaoTributariaEcf);
		setLgSimples(lgSimples);
		setLgSubstituicao(lgSubstituicao);
		setLgGeraCredito(lgGeraCredito);
		setLgPartilha(lgPartilha);
		setLgMotivoIsencao(lgMotivoIsencao);
		setLgReducaoBase(lgReducaoBase);
		setLgRetido(lgRetido);
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setCdSituacaoTributaria(int cdSituacaoTributaria){
		this.cdSituacaoTributaria=cdSituacaoTributaria;
	}
	public int getCdSituacaoTributaria(){
		return this.cdSituacaoTributaria;
	}
	public void setNrSituacaoTributaria(String nrSituacaoTributaria){
		this.nrSituacaoTributaria=nrSituacaoTributaria;
	}
	public String getNrSituacaoTributaria(){
		return this.nrSituacaoTributaria;
	}
	public void setNmSituacaoTributaria(String nmSituacaoTributaria){
		this.nmSituacaoTributaria=nmSituacaoTributaria;
	}
	public String getNmSituacaoTributaria(){
		return this.nmSituacaoTributaria;
	}
	public void setSgSituacaoTributaria(String sgSituacaoTributaria){
		this.sgSituacaoTributaria=sgSituacaoTributaria;
	}
	public String getSgSituacaoTributaria(){
		return this.sgSituacaoTributaria;
	}
	public void setTpSituacaoTributariaEcf(int tpSituacaoTributariaEcf){
		this.tpSituacaoTributariaEcf=tpSituacaoTributariaEcf;
	}
	public int getTpSituacaoTributariaEcf(){
		return this.tpSituacaoTributariaEcf;
	}
	public void setLgSimples(int lgSimples){
		this.lgSimples=lgSimples;
	}
	public int getLgSimples(){
		return this.lgSimples;
	}
	public void setLgSubstituicao(int lgSubstituicao){
		this.lgSubstituicao=lgSubstituicao;
	}
	public int getLgSubstituicao(){
		return this.lgSubstituicao;
	}
	public void setLgGeraCredito(int lgGeraCredito){
		this.lgGeraCredito=lgGeraCredito;
	}
	public int getLgGeraCredito(){
		return this.lgGeraCredito;
	}
	public void setLgPartilha(int lgPartilha){
		this.lgPartilha=lgPartilha;
	}
	public int getLgPartilha(){
		return this.lgPartilha;
	}
	public void setLgMotivoIsencao(int lgMotivoIsencao){
		this.lgMotivoIsencao=lgMotivoIsencao;
	}
	public int getLgMotivoIsencao(){
		return this.lgMotivoIsencao;
	}
	public void setLgReducaoBase(int lgReducaoBase){
		this.lgReducaoBase=lgReducaoBase;
	}
	public int getLgReducaoBase(){
		return this.lgReducaoBase;
	}
	public void setLgRetido(int lgRetido) {
		this.lgRetido = lgRetido;
	}
	public int getLgRetido() {
		return lgRetido;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTributo: " +  getCdTributo();
		valueToString += ", cdSituacaoTributaria: " +  getCdSituacaoTributaria();
		valueToString += ", nrSituacaoTributaria: " +  getNrSituacaoTributaria();
		valueToString += ", nmSituacaoTributaria: " +  getNmSituacaoTributaria();
		valueToString += ", sgSituacaoTributaria: " +  getSgSituacaoTributaria();
		valueToString += ", tpSituacaoTributariaEcf: " +  getTpSituacaoTributariaEcf();
		valueToString += ", lgSimples: " +  getLgSimples();
		valueToString += ", lgSubstituicao: " +  getLgSubstituicao();
		valueToString += ", lgGeraCredito: " +  getLgGeraCredito();
		valueToString += ", lgPartilha: " +  getLgPartilha();
		valueToString += ", lgMotivoIsencao: " +  getLgMotivoIsencao();
		valueToString += ", lgReducaoBase: " +  getLgReducaoBase();
		valueToString += ", lgRetido: " +  getLgRetido();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SituacaoTributaria(getCdTributo(),
			getCdSituacaoTributaria(),
			getNrSituacaoTributaria(),
			getNmSituacaoTributaria(),
			getSgSituacaoTributaria(),
			getTpSituacaoTributariaEcf(),
			getLgSimples(),
			getLgSubstituicao(),
			getLgGeraCredito(),
			getLgPartilha(),
			getLgMotivoIsencao(),
			getLgReducaoBase(),
			getLgRetido());
	}

}