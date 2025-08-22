package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class Viagem {

	private int cdViagem;
	private int cdRota;
	private int cdVeiculo;
	private int cdMotivo;
	private GregorianCalendar dtSaida;
	private GregorianCalendar dtChegada;
	private String txtObservacao;
	private int tpViagem;
	private GregorianCalendar dtPrevisaoChegada;
	private int stViagem;

	public Viagem(int cdViagem,
			int cdRota,
			int cdVeiculo,
			int cdMotivo,
			GregorianCalendar dtSaida,
			GregorianCalendar dtChegada,
			String txtObservacao,
			int tpViagem,
			GregorianCalendar dtPrevisaoChegada,
			int stViagem){
		setCdViagem(cdViagem);
		setCdRota(cdRota);
		setCdVeiculo(cdVeiculo);
		setCdMotivo(cdMotivo);
		setDtSaida(dtSaida);
		setDtChegada(dtChegada);
		setTxtObservacao(txtObservacao);
		setTpViagem(tpViagem);
		setDtPrevisaoChegada(dtPrevisaoChegada);
		setStViagem(stViagem);
	}
	
	public Viagem(int cdViagem,
			int cdVeiculo,
			GregorianCalendar dtSaida,
			GregorianCalendar dtChegada,
			String txtObservacao,
			GregorianCalendar dtPrevisaoChegada,
			int stViagem){
		setCdViagem(cdViagem);
		setCdVeiculo(cdVeiculo);
		setDtSaida(dtSaida);
		setDtChegada(dtChegada);
		setTxtObservacao(txtObservacao);
		setTpViagem(ViagemServices.TP_NEGOCIOS);
		setDtPrevisaoChegada(dtPrevisaoChegada);
		setStViagem(stViagem);
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setDtSaida(GregorianCalendar dtSaida){
		this.dtSaida=dtSaida;
	}
	public GregorianCalendar getDtSaida(){
		return this.dtSaida;
	}
	public void setDtChegada(GregorianCalendar dtChegada){
		this.dtChegada=dtChegada;
	}
	public GregorianCalendar getDtChegada(){
		return this.dtChegada;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setTpViagem(int tpViagem){
		this.tpViagem=tpViagem;
	}
	public int getTpViagem(){
		return this.tpViagem;
	}
	public void setDtPrevisaoChegada(GregorianCalendar dtPrevisaoChegada){
		this.dtPrevisaoChegada=dtPrevisaoChegada;
	}
	public GregorianCalendar getDtPrevisaoChegada(){
		return this.dtPrevisaoChegada;
	}
	public void setStViagem(int stViagem) {
		this.stViagem = stViagem;
	}
	public int getStViagem() {
		return stViagem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdViagem: " +  getCdViagem();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdMotivo: " +  getCdMotivo();
		valueToString += ", dtSaida: " +  sol.util.Util.formatDateTime(getDtSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtChegada: " +  sol.util.Util.formatDateTime(getDtChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", tpViagem: " +  getTpViagem();
		valueToString += ", dtPrevisaoChegada: " +  sol.util.Util.formatDateTime(getDtPrevisaoChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stViagem: " +  getStViagem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Viagem(getCdViagem(),
			getCdRota(),
			getCdVeiculo(),
			getCdMotivo(),
			getDtSaida()==null ? null : (GregorianCalendar)getDtSaida().clone(),
			getDtChegada()==null ? null : (GregorianCalendar)getDtChegada().clone(),
			getTxtObservacao(),
			getTpViagem(),
			getDtPrevisaoChegada()==null ? null : (GregorianCalendar)getDtPrevisaoChegada().clone(),
			getStViagem());
	}

}
