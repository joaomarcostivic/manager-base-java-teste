package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class AutorizacaoSaida {

	private int cdAutorizacao;
	private int cdVeiculo;
	private int cdViagem;
	private int cdManutencao;
	private int cdResponsavel;
	private float qtHodometroSaida;
	private float qtHodometroChegada;
	private GregorianCalendar dtAutorizacao;
	private GregorianCalendar dtSaida;
	private int tpMotivo;
	private String txtAutorizacao;
	private GregorianCalendar dtChegada;

	public AutorizacaoSaida(int cdAutorizacao,
			int cdVeiculo,
			int cdViagem,
			int cdManutencao,
			int cdResponsavel,
			float qtHodometroSaida,
			float qtHodometroChegada,
			GregorianCalendar dtAutorizacao,
			GregorianCalendar dtSaida,
			int tpMotivo,
			String txtAutorizacao,
			GregorianCalendar dtChegada){
		setCdAutorizacao(cdAutorizacao);
		setCdVeiculo(cdVeiculo);
		setCdViagem(cdViagem);
		setCdManutencao(cdManutencao);
		setCdResponsavel(cdResponsavel);
		setQtHodometroSaida(qtHodometroSaida);
		setQtHodometroChegada(qtHodometroChegada);
		setDtAutorizacao(dtAutorizacao);
		setDtSaida(dtSaida);
		setTpMotivo(tpMotivo);
		setTxtAutorizacao(txtAutorizacao);
		setDtChegada(dtChegada);
	}
	public void setCdAutorizacao(int cdAutorizacao){
		this.cdAutorizacao=cdAutorizacao;
	}
	public int getCdAutorizacao(){
		return this.cdAutorizacao;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdViagem(int cdViagem){
		this.cdViagem=cdViagem;
	}
	public int getCdViagem(){
		return this.cdViagem;
	}
	public void setCdManutencao(int cdManutencao){
		this.cdManutencao=cdManutencao;
	}
	public int getCdManutencao(){
		return this.cdManutencao;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setQtHodometroSaida(float qtHodometroSaida){
		this.qtHodometroSaida=qtHodometroSaida;
	}
	public float getQtHodometroSaida(){
		return this.qtHodometroSaida;
	}
	public void setQtHodometroChegada(float qtHodometroChegada){
		this.qtHodometroChegada=qtHodometroChegada;
	}
	public float getQtHodometroChegada(){
		return this.qtHodometroChegada;
	}
	public void setDtAutorizacao(GregorianCalendar dtAutorizacao){
		this.dtAutorizacao=dtAutorizacao;
	}
	public GregorianCalendar getDtAutorizacao(){
		return this.dtAutorizacao;
	}
	public void setDtSaida(GregorianCalendar dtSaida){
		this.dtSaida=dtSaida;
	}
	public GregorianCalendar getDtSaida(){
		return this.dtSaida;
	}
	public void setTpMotivo(int tpMotivo){
		this.tpMotivo=tpMotivo;
	}
	public int getTpMotivo(){
		return this.tpMotivo;
	}
	public void setTxtAutorizacao(String txtAutorizacao){
		this.txtAutorizacao=txtAutorizacao;
	}
	public String getTxtAutorizacao(){
		return this.txtAutorizacao;
	}
	public void setDtChegada(GregorianCalendar dtChegada){
		this.dtChegada=dtChegada;
	}
	public GregorianCalendar getDtChegada(){
		return this.dtChegada;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAutorizacao: " +  getCdAutorizacao();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdViagem: " +  getCdViagem();
		valueToString += ", cdManutencao: " +  getCdManutencao();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", qtHodometroSaida: " +  getQtHodometroSaida();
		valueToString += ", qtHodometroChegada: " +  getQtHodometroChegada();
		valueToString += ", dtAutorizacao: " +  sol.util.Util.formatDateTime(getDtAutorizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtSaida: " +  sol.util.Util.formatDateTime(getDtSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpMotivo: " +  getTpMotivo();
		valueToString += ", txtAutorizacao: " +  getTxtAutorizacao();
		valueToString += ", dtChegada: " +  sol.util.Util.formatDateTime(getDtChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AutorizacaoSaida(getCdAutorizacao(),
			getCdVeiculo(),
			getCdViagem(),
			getCdManutencao(),
			getCdResponsavel(),
			getQtHodometroSaida(),
			getQtHodometroChegada(),
			getDtAutorizacao()==null ? null : (GregorianCalendar)getDtAutorizacao().clone(),
			getDtSaida()==null ? null : (GregorianCalendar)getDtSaida().clone(),
			getTpMotivo(),
			getTxtAutorizacao(),
			getDtChegada()==null ? null : (GregorianCalendar)getDtChegada().clone());
	}

}
