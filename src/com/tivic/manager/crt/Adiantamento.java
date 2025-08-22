package com.tivic.manager.crt;

import java.util.GregorianCalendar;

public class Adiantamento {

	private int cdAdiantamento;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdVinculo;
	private GregorianCalendar dtAdiantamento;
	private float vlAdiantamento;
	private int qtParcelas;
	private int stAdiantamento;
	private int tpParcelamento;

	public Adiantamento(int cdAdiantamento,
			int cdEmpresa,
			int cdPessoa,
			int cdVinculo,
			GregorianCalendar dtAdiantamento,
			float vlAdiantamento,
			int qtParcelas,
			int stAdiantamento,
			int tpParcelamento){
		setCdAdiantamento(cdAdiantamento);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdVinculo(cdVinculo);
		setDtAdiantamento(dtAdiantamento);
		setVlAdiantamento(vlAdiantamento);
		setQtParcelas(qtParcelas);
		setStAdiantamento(stAdiantamento);
		setTpParcelamento(tpParcelamento);
	}
	public void setCdAdiantamento(int cdAdiantamento){
		this.cdAdiantamento=cdAdiantamento;
	}
	public int getCdAdiantamento(){
		return this.cdAdiantamento;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setDtAdiantamento(GregorianCalendar dtAdiantamento){
		this.dtAdiantamento=dtAdiantamento;
	}
	public GregorianCalendar getDtAdiantamento(){
		return this.dtAdiantamento;
	}
	public void setVlAdiantamento(float vlAdiantamento){
		this.vlAdiantamento=vlAdiantamento;
	}
	public float getVlAdiantamento(){
		return this.vlAdiantamento;
	}
	public void setQtParcelas(int qtParcelas){
		this.qtParcelas=qtParcelas;
	}
	public int getQtParcelas(){
		return this.qtParcelas;
	}
	public void setStAdiantamento(int stAdiantamento){
		this.stAdiantamento=stAdiantamento;
	}
	public int getStAdiantamento(){
		return this.stAdiantamento;
	}
	public void setTpParcelamento(int tpParcelamento){
		this.tpParcelamento=tpParcelamento;
	}
	public int getTpParcelamento(){
		return this.tpParcelamento;
	}
}