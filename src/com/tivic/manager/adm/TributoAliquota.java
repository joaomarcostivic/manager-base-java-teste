package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class TributoAliquota {

	private int cdTributoAliquota;
	private int cdTributo;
	private float prAliquota;
	private float prCredito;
	private int stTributaria;
	private GregorianCalendar dtInicioValidade;
	private GregorianCalendar dtFinalValidade;
	private float vlInicioFaixa;
	private float vlVariacaoBase;
	private float vlVariacaoResultado;
	private int tpFatorVariacaoBase;
	private int tpOperacao;
	private int tpFatorVariacaoResultado;
	private int cdSituacaoTributaria;
	private int tpBaseCalculo;
	private float prReducaoBase;
	private int tpMotivoDesoneracao;
	private float prAliquotaSubstituicao;
	private int tpBaseCalculoSubstituicao;
	private float prReducaoBaseSubstituicao;
	private float vlVariacaoBaseSubstituicao;

	public TributoAliquota(int cdTributoAliquota,
			int cdTributo,
			float prAliquota,
			float prCredito,
			int stTributaria,
			GregorianCalendar dtInicioValidade,
			GregorianCalendar dtFinalValidade,
			float vlInicioFaixa,
			float vlVariacaoBase,
			float vlVariacaoResultado,
			int tpFatorVariacaoBase,
			int tpOperacao,
			int tpFatorVariacaoResultado,
			int cdSituacaoTributaria,
			int tpBaseCalculo,
			float prReducaoBase,
			int tpMotivoDesoneracao,
			float prAliquotaSubstituicao,
			int tpBaseCalculoSubstituicao,
			float prReducaoBaseSubstituicao,
			float vlVariacaoBaseSubstituicao){
		setCdTributoAliquota(cdTributoAliquota);
		setCdTributo(cdTributo);
		setPrAliquota(prAliquota);
		setPrCredito(prCredito);
		setStTributaria(stTributaria);
		setDtInicioValidade(dtInicioValidade);
		setDtFinalValidade(dtFinalValidade);
		setVlInicioFaixa(vlInicioFaixa);
		setVlVariacaoBase(vlVariacaoBase);
		setVlVariacaoResultado(vlVariacaoResultado);
		setTpFatorVariacaoBase(tpFatorVariacaoBase);
		setTpOperacao(tpOperacao);
		setTpFatorVariacaoResultado(tpFatorVariacaoResultado);
		setCdSituacaoTributaria(cdSituacaoTributaria);
		setTpBaseCalculo(tpBaseCalculo);
		setPrReducaoBase(prReducaoBase);
		setTpMotivoDesoneracao(tpMotivoDesoneracao);
		setPrAliquotaSubstituicao(prAliquotaSubstituicao);
		setTpBaseCalculoSubstituicao(tpBaseCalculoSubstituicao);
		setPrReducaoBaseSubstituicao(prReducaoBaseSubstituicao);
		setVlVariacaoBaseSubstituicao(vlVariacaoBaseSubstituicao);
	}
	public void setCdTributoAliquota(int cdTributoAliquota){
		this.cdTributoAliquota=cdTributoAliquota;
	}
	public int getCdTributoAliquota(){
		return this.cdTributoAliquota;
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setPrAliquota(float prAliquota){
		this.prAliquota=prAliquota;
	}
	public float getPrAliquota(){
		return this.prAliquota;
	}
	public void setPrCredito(float prCredito){
		this.prCredito=prCredito;
	}
	public float getPrCredito(){
		return this.prCredito;
	}
	public void setStTributaria(int stTributaria){
		this.stTributaria=stTributaria;
	}
	public int getStTributaria(){
		return this.stTributaria;
	}
	public void setDtInicioValidade(GregorianCalendar dtInicioValidade){
		this.dtInicioValidade=dtInicioValidade;
	}
	public GregorianCalendar getDtInicioValidade(){
		return this.dtInicioValidade;
	}
	public void setDtFinalValidade(GregorianCalendar dtFinalValidade){
		this.dtFinalValidade=dtFinalValidade;
	}
	public GregorianCalendar getDtFinalValidade(){
		return this.dtFinalValidade;
	}
	public void setVlInicioFaixa(float vlInicioFaixa){
		this.vlInicioFaixa=vlInicioFaixa;
	}
	public float getVlInicioFaixa(){
		return this.vlInicioFaixa;
	}
	public void setVlVariacaoBase(float vlVariacaoBase){
		this.vlVariacaoBase=vlVariacaoBase;
	}
	public float getVlVariacaoBase(){
		return this.vlVariacaoBase;
	}
	public void setVlVariacaoResultado(float vlVariacaoResultado){
		this.vlVariacaoResultado=vlVariacaoResultado;
	}
	public float getVlVariacaoResultado(){
		return this.vlVariacaoResultado;
	}
	public void setTpFatorVariacaoBase(int tpFatorVariacaoBase){
		this.tpFatorVariacaoBase=tpFatorVariacaoBase;
	}
	public int getTpFatorVariacaoBase(){
		return this.tpFatorVariacaoBase;
	}
	public void setTpOperacao(int tpOperacao){
		this.tpOperacao=tpOperacao;
	}
	public int getTpOperacao(){
		return this.tpOperacao;
	}
	public void setTpFatorVariacaoResultado(int tpFatorVariacaoResultado){
		this.tpFatorVariacaoResultado=tpFatorVariacaoResultado;
	}
	public int getTpFatorVariacaoResultado(){
		return this.tpFatorVariacaoResultado;
	}
	public void setCdSituacaoTributaria(int cdSituacaoTributaria){
		this.cdSituacaoTributaria=cdSituacaoTributaria;
	}
	public int getCdSituacaoTributaria(){
		return this.cdSituacaoTributaria;
	}
	public void setTpBaseCalculo(int tpBaseCalculo){
		this.tpBaseCalculo=tpBaseCalculo;
	}
	public int getTpBaseCalculo(){
		return this.tpBaseCalculo;
	}
	public void setPrReducaoBase(float prReducaoBase){
		this.prReducaoBase=prReducaoBase;
	}
	public float getPrReducaoBase(){
		return this.prReducaoBase;
	}
	public void setTpMotivoDesoneracao(int tpMotivoDesoneracao){
		this.tpMotivoDesoneracao=tpMotivoDesoneracao;
	}
	public int getTpMotivoDesoneracao(){
		return this.tpMotivoDesoneracao;
	}
	public void setPrAliquotaSubstituicao(float prAliquotaSubstituicao){
		this.prAliquotaSubstituicao=prAliquotaSubstituicao;
	}
	public float getPrAliquotaSubstituicao(){
		return this.prAliquotaSubstituicao;
	}
	public void setTpBaseCalculoSubstituicao(int tpBaseCalculoSubstituicao){
		this.tpBaseCalculoSubstituicao=tpBaseCalculoSubstituicao;
	}
	public int getTpBaseCalculoSubstituicao(){
		return this.tpBaseCalculoSubstituicao;
	}
	public void setPrReducaoBaseSubstituicao(float prReducaoBaseSubstituicao){
		this.prReducaoBaseSubstituicao=prReducaoBaseSubstituicao;
	}
	public float getPrReducaoBaseSubstituicao(){
		return this.prReducaoBaseSubstituicao;
	}
	public void setVlVariacaoBaseSubstituicao(float vlVariacaoBaseSubstituicao){
		this.vlVariacaoBaseSubstituicao=vlVariacaoBaseSubstituicao;
	}
	public float getVlVariacaoBaseSubstituicao(){
		return this.vlVariacaoBaseSubstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTributoAliquota: " +  getCdTributoAliquota();
		valueToString += ", cdTributo: " +  getCdTributo();
		valueToString += ", prAliquota: " +  getPrAliquota();
		valueToString += ", prCredito: " +  getPrCredito();
		valueToString += ", stTributaria: " +  getStTributaria();
		valueToString += ", dtInicioValidade: " +  sol.util.Util.formatDateTime(getDtInicioValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlInicioFaixa: " +  getVlInicioFaixa();
		valueToString += ", vlVariacaoBase: " +  getVlVariacaoBase();
		valueToString += ", vlVariacaoResultado: " +  getVlVariacaoResultado();
		valueToString += ", tpFatorVariacaoBase: " +  getTpFatorVariacaoBase();
		valueToString += ", tpOperacao: " +  getTpOperacao();
		valueToString += ", tpFatorVariacaoResultado: " +  getTpFatorVariacaoResultado();
		valueToString += ", cdSituacaoTributaria: " +  getCdSituacaoTributaria();
		valueToString += ", tpBaseCalculo: " +  getTpBaseCalculo();
		valueToString += ", prReducaoBase: " +  getPrReducaoBase();
		valueToString += ", tpMotivoDesoneracao: " +  getTpMotivoDesoneracao();
		valueToString += ", prAliquotaSubstituicao: " +  getPrAliquotaSubstituicao();
		valueToString += ", tpBaseCalculoSubstituicao: " +  getTpBaseCalculoSubstituicao();
		valueToString += ", prReducaoBaseSubstituicao: " +  getPrReducaoBaseSubstituicao();
		valueToString += ", vlVariacaoBaseSubstituicao: " +  getVlVariacaoBaseSubstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TributoAliquota(getCdTributoAliquota(),
			getCdTributo(),
			getPrAliquota(),
			getPrCredito(),
			getStTributaria(),
			getDtInicioValidade()==null ? null : (GregorianCalendar)getDtInicioValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone(),
			getVlInicioFaixa(),
			getVlVariacaoBase(),
			getVlVariacaoResultado(),
			getTpFatorVariacaoBase(),
			getTpOperacao(),
			getTpFatorVariacaoResultado(),
			getCdSituacaoTributaria(),
			getTpBaseCalculo(),
			getPrReducaoBase(),
			getTpMotivoDesoneracao(),
			getPrAliquotaSubstituicao(),
			getTpBaseCalculoSubstituicao(),
			getPrReducaoBaseSubstituicao(),
			getVlVariacaoBaseSubstituicao());
	}

}