package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class MetaPremiacao {
	public static final int META = 0;
	public static final int PREMIACAO = 1;

	private int cdMetaPremiacao;
	private int gnMetaPremiacao;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private float vlMeta;
	private int cdEmpresa;
	private int cdVinculo;
	private int cdOperacao;
	private int tpValor;
	private int tpMeta;
	private int tpPeriodo;
	private int cdPessoa;

	public MetaPremiacao(int cdMetaPremiacao,
			int gnMetaPremiacao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			float vlMeta,
			int cdEmpresa,
			int cdVinculo,
			int cdOperacao,
			int tpValor,
			int tpMeta,
			int tpPeriodo,
			int cdPessoa){
		setCdMetaPremiacao(cdMetaPremiacao);
		setGnMetaPremiacao(gnMetaPremiacao);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setVlMeta(vlMeta);
		setCdEmpresa(cdEmpresa);
		setCdVinculo(cdVinculo);
		setCdOperacao(cdOperacao);
		setTpValor(tpValor);
		setTpMeta(tpMeta);
		setTpPeriodo(tpPeriodo);
		setCdPessoa(cdPessoa);
	}
	public void setCdMetaPremiacao(int cdMetaPremiacao){
		this.cdMetaPremiacao=cdMetaPremiacao;
	}
	public int getCdMetaPremiacao(){
		return this.cdMetaPremiacao;
	}
	public void setGnMetaPremiacao(int gnMetaPremiacao){
		this.gnMetaPremiacao=gnMetaPremiacao;
	}
	public int getGnMetaPremiacao(){
		return this.gnMetaPremiacao;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setVlMeta(float vlMeta){
		this.vlMeta=vlMeta;
	}
	public float getVlMeta(){
		return this.vlMeta;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setCdOperacao(int cdOperacao){
		this.cdOperacao=cdOperacao;
	}
	public int getCdOperacao(){
		return this.cdOperacao;
	}
	public void setTpValor(int tpValor){
		this.tpValor=tpValor;
	}
	public int getTpValor(){
		return this.tpValor;
	}
	public void setTpMeta(int tpMeta){
		this.tpMeta=tpMeta;
	}
	public int getTpMeta(){
		return this.tpMeta;
	}
	public void setTpPeriodo(int tpPeriodo){
		this.tpPeriodo=tpPeriodo;
	}
	public int getTpPeriodo(){
		return this.tpPeriodo;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
}
