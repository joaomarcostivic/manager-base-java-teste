package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Tributo {

	private int cdTributo;
	private String nmTributo;
	private String idTributo;
	private GregorianCalendar dtInicioValidade;
	private GregorianCalendar dtFinalValidade;
	private int tpTributo;
	private float prAliquotaPadrao;
	private int lgAliquotaProgressiva;
	private int tpEsferaAplicacao;
	private int nrOrdemCalculo;
	private int tpOperacao;
	private float vlVariacaoBase;
	private float vlVariacaoResultado;
	private int tpFatorVariacaoBase;
	private int tpFatorVariacaoResultado;
	private int tpCobranca;

	public Tributo(int cdTributo,
			String nmTributo,
			String idTributo,
			GregorianCalendar dtInicioValidade,
			GregorianCalendar dtFinalValidade,
			int tpTributo,
			float prAliquotaPadrao,
			int lgAliquotaProgressiva,
			int tpEsferaAplicacao,
			int nrOrdemCalculo,
			int tpOperacao,
			float vlVariacaoBase,
			float vlVariacaoResultado,
			int tpFatorVariacaoBase,
			int tpFatorVariacaoResultado,
			int tpCobranca){
		setCdTributo(cdTributo);
		setNmTributo(nmTributo);
		setIdTributo(idTributo);
		setDtInicioValidade(dtInicioValidade);
		setDtFinalValidade(dtFinalValidade);
		setTpTributo(tpTributo);
		setPrAliquotaPadrao(prAliquotaPadrao);
		setLgAliquotaProgressiva(lgAliquotaProgressiva);
		setTpEsferaAplicacao(tpEsferaAplicacao);
		setNrOrdemCalculo(nrOrdemCalculo);
		setTpOperacao(tpOperacao);
		setVlVariacaoBase(vlVariacaoBase);
		setVlVariacaoResultado(vlVariacaoResultado);
		setTpFatorVariacaoBase(tpFatorVariacaoBase);
		setTpFatorVariacaoResultado(tpFatorVariacaoResultado);
		setTpCobranca(tpCobranca);
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setNmTributo(String nmTributo){
		this.nmTributo=nmTributo;
	}
	public String getNmTributo(){
		return this.nmTributo;
	}
	public void setIdTributo(String idTributo){
		this.idTributo=idTributo;
	}
	public String getIdTributo(){
		return this.idTributo;
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
	public void setTpTributo(int tpTributo){
		this.tpTributo=tpTributo;
	}
	public int getTpTributo(){
		return this.tpTributo;
	}
	public void setPrAliquotaPadrao(float prAliquotaPadrao){
		this.prAliquotaPadrao=prAliquotaPadrao;
	}
	public float getPrAliquotaPadrao(){
		return this.prAliquotaPadrao;
	}
	public void setLgAliquotaProgressiva(int lgAliquotaProgressiva){
		this.lgAliquotaProgressiva=lgAliquotaProgressiva;
	}
	public int getLgAliquotaProgressiva(){
		return this.lgAliquotaProgressiva;
	}
	public void setTpEsferaAplicacao(int tpEsferaAplicacao){
		this.tpEsferaAplicacao=tpEsferaAplicacao;
	}
	public int getTpEsferaAplicacao(){
		return this.tpEsferaAplicacao;
	}
	public void setNrOrdemCalculo(int nrOrdemCalculo){
		this.nrOrdemCalculo=nrOrdemCalculo;
	}
	public int getNrOrdemCalculo(){
		return this.nrOrdemCalculo;
	}
	public void setTpOperacao(int tpOperacao){
		this.tpOperacao=tpOperacao;
	}
	public int getTpOperacao(){
		return this.tpOperacao;
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
	public void setTpFatorVariacaoResultado(int tpFatorVariacaoResultado){
		this.tpFatorVariacaoResultado=tpFatorVariacaoResultado;
	}
	public int getTpFatorVariacaoResultado(){
		return this.tpFatorVariacaoResultado;
	}
	public void setTpCobranca(int tpCobranca){
		this.tpCobranca=tpCobranca;
	}
	public int getTpCobranca(){
		return this.tpCobranca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTributo: " +  getCdTributo();
		valueToString += ", nmTributo: " +  getNmTributo();
		valueToString += ", idTributo: " +  getIdTributo();
		valueToString += ", dtInicioValidade: " +  sol.util.Util.formatDateTime(getDtInicioValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalValidade: " +  sol.util.Util.formatDateTime(getDtFinalValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpTributo: " +  getTpTributo();
		valueToString += ", prAliquotaPadrao: " +  getPrAliquotaPadrao();
		valueToString += ", lgAliquotaProgressiva: " +  getLgAliquotaProgressiva();
		valueToString += ", tpEsferaAplicacao: " +  getTpEsferaAplicacao();
		valueToString += ", nrOrdemCalculo: " +  getNrOrdemCalculo();
		valueToString += ", tpOperacao: " +  getTpOperacao();
		valueToString += ", vlVariacaoBase: " +  getVlVariacaoBase();
		valueToString += ", vlVariacaoResultado: " +  getVlVariacaoResultado();
		valueToString += ", tpFatorVariacaoBase: " +  getTpFatorVariacaoBase();
		valueToString += ", tpFatorVariacaoResultado: " +  getTpFatorVariacaoResultado();
		valueToString += ", tpCobranca: " +  getTpCobranca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Tributo(getCdTributo(),
			getNmTributo(),
			getIdTributo(),
			getDtInicioValidade()==null ? null : (GregorianCalendar)getDtInicioValidade().clone(),
			getDtFinalValidade()==null ? null : (GregorianCalendar)getDtFinalValidade().clone(),
			getTpTributo(),
			getPrAliquotaPadrao(),
			getLgAliquotaProgressiva(),
			getTpEsferaAplicacao(),
			getNrOrdemCalculo(),
			getTpOperacao(),
			getVlVariacaoBase(),
			getVlVariacaoResultado(),
			getTpFatorVariacaoBase(),
			getTpFatorVariacaoResultado(),
			getTpCobranca());
	}

}