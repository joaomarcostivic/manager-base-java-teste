package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Concessao {

	private int cdConcessao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicioConcessao;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtFinalConcessao;
	
	private String idConcessao;
	private int stConcessao;
	private String nrConcessao;
	private String txtObservacao;
	private int cdConcessionario;
	private int cdFrota;
	private int cdVeiculo;
	private int tpConcessao;
	private int lgRenovavel;
	private int nrPrazo;

	public Concessao(){ }

	public Concessao(int cdConcessao,
			GregorianCalendar dtInicioConcessao,
			GregorianCalendar dtFinalConcessao,
			String idConcessao,
			int stConcessao,
			String nrConcessao,
			String txtObservacao,
			int cdConcessionario,
			int cdFrota,
			int cdVeiculo,
			int tpConcessao,
			int lgRenovavel,
			int nrPrazo){
		setCdConcessao(cdConcessao);
		setDtInicioConcessao(dtInicioConcessao);
		setDtFinalConcessao(dtFinalConcessao);
		setIdConcessao(idConcessao);
		setStConcessao(stConcessao);
		setNrConcessao(nrConcessao);
		setTxtObservacao(txtObservacao);
		setCdConcessionario(cdConcessionario);
		setCdFrota(cdFrota);
		setCdVeiculo(cdVeiculo);
		setTpConcessao(tpConcessao);
		setLgRenovavel(lgRenovavel);
		setNrPrazo(nrPrazo);
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setDtInicioConcessao(GregorianCalendar dtInicioConcessao){
		this.dtInicioConcessao=dtInicioConcessao;
	}
	public GregorianCalendar getDtInicioConcessao(){
		return this.dtInicioConcessao;
	}
	public void setDtFinalConcessao(GregorianCalendar dtFinalConcessao){
		this.dtFinalConcessao=dtFinalConcessao;
	}
	public GregorianCalendar getDtFinalConcessao(){
		return this.dtFinalConcessao;
	}
	public void setIdConcessao(String idConcessao){
		this.idConcessao=idConcessao;
	}
	public String getIdConcessao(){
		return this.idConcessao;
	}
	public void setStConcessao(int stConcessao){
		this.stConcessao=stConcessao;
	}
	public int getStConcessao(){
		return this.stConcessao;
	}
	public void setNrConcessao(String nrConcessao){
		this.nrConcessao=nrConcessao;
	}
	public String getNrConcessao(){
		return this.nrConcessao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdConcessionario(int cdConcessionario){
		this.cdConcessionario=cdConcessionario;
	}
	public int getCdConcessionario(){
		return this.cdConcessionario;
	}

	public void setCdFrota(int cdFrota){
		this.cdFrota=cdFrota;
	}
	public int getCdFrota(){
		return this.cdFrota;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setTpConcessao(int tpConcessao){
		this.tpConcessao=tpConcessao;
	}
	public int getTpConcessao(){
		return this.tpConcessao;
	}
	public void setLgRenovavel(int lgRenovavel){
		this.lgRenovavel=lgRenovavel;
	}
	public int getLgRenovavel(){
		return this.lgRenovavel;
	}
	public void setNrPrazo(int nrPrazo){
		this.nrPrazo=nrPrazo;
	}
	public int getNrPrazo(){
		return this.nrPrazo;
	}
	
//	@Override
//	public String toString() {
//		JsonToStringBuilder builder = new JsonToStringBuilder(this);
//		builder.append("cdConcessao", cdConcessao);
//		builder.append("dtInicioConcessao", sol.util.Util.formatDateTime(dtInicioConcessao, "yyyy-MM-dd'T'HH:mm:ss.SSS", ""));
//		builder.append("dtFinalConcessao", sol.util.Util.formatDateTime(dtFinalConcessao, "yyyy-MM-dd'T'HH:mm:ss.SSS", ""));
//		builder.append("idConcessao", idConcessao);
//		builder.append("stConcessao", stConcessao);
//		builder.append("nrConcessao", nrConcessao);
//		builder.append("txtObservacao", txtObservacao);
//		builder.append("cdConcessionario", cdConcessionario);
//		builder.append("cdFrota", cdFrota);
//		builder.append("cdVeiculo", cdVeiculo);
//		builder.append("tpConcessao", tpConcessao);
//		builder.append("lgRenovavel", lgRenovavel);
//		builder.append("nrPrazo", nrPrazo);
//		return builder.toString();
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdConcessao: " +  getCdConcessao();
		valueToString += ", dtInicioConcessao: " +  sol.util.Util.formatDateTime(getDtInicioConcessao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalConcessao: " +  sol.util.Util.formatDateTime(getDtFinalConcessao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idConcessao: " +  getIdConcessao();
		valueToString += ", stConcessao: " +  getStConcessao();
		valueToString += ", nrConcessao: " +  getNrConcessao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdConcessionario: " +  getCdConcessionario();
		valueToString += ", cdFrota: " +  getCdFrota();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", tpConcessao: " +  getTpConcessao();
		valueToString += ", lgRenovavel: " +  getLgRenovavel();
		valueToString += ", nrPrazo: " +  getNrPrazo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Concessao(getCdConcessao(),
			getDtInicioConcessao()==null ? null : (GregorianCalendar)getDtInicioConcessao().clone(),
			getDtFinalConcessao()==null ? null : (GregorianCalendar)getDtFinalConcessao().clone(),
			getIdConcessao(),
			getStConcessao(),
			getNrConcessao(),
			getTxtObservacao(),
			getCdConcessionario(),
			getCdFrota(),
			getCdVeiculo(),
			getTpConcessao(),
			getLgRenovavel(),
			getNrPrazo());
	}

}