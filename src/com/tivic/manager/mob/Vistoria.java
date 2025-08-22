package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vistoria {

	private int cdVistoria;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtVistoria;
	
	private int cdAgente;
	private int cdPessoa;
	private int cdEquipamento;
	private int cdVeiculo;
	private int cdPlanoVistoria;
	private int cdVistoriaAnterior;
	private int stVistoria;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAplicacao;
	
	private int tpVistoria;
	private String dsObservacao;
	private String idSelo;
	private int cdVistoriador;
	private int cdCondutor;
	private int cdConcessao;

	public Vistoria(){ }

	public Vistoria(int cdVistoria,
			GregorianCalendar dtVistoria,
			int cdAgente,
			int cdPessoa,
			int cdEquipamento,
			int cdVeiculo,
			int cdPlanoVistoria,
			int cdVistoriaAnterior,
			int stVistoria,
			GregorianCalendar dtAplicacao,
			int tpVistoria,
			String dsObservacao,
			String idSelo,
			int cdVistoriador,
			int cdCondutor,
			int cdConcessao){
		setCdVistoria(cdVistoria);
		setDtVistoria(dtVistoria);
		setCdAgente(cdAgente);
		setCdPessoa(cdPessoa);
		setCdEquipamento(cdEquipamento);
		setCdVeiculo(cdVeiculo);
		setCdPlanoVistoria(cdPlanoVistoria);
		setCdVistoriaAnterior(cdVistoriaAnterior);
		setStVistoria(stVistoria);
		setDtAplicacao(dtAplicacao);
		setTpVistoria(tpVistoria);
		setDsObservacao(dsObservacao);
		setIdSelo(idSelo);
		setCdVistoriador(cdVistoriador);
		setCdCondutor(cdCondutor);
		setCdConcessao(cdConcessao);
	}
	public void setCdVistoria(int cdVistoria){
		this.cdVistoria=cdVistoria;
	}
	public int getCdVistoria(){
		return this.cdVistoria;
	}
	public void setDtVistoria(GregorianCalendar dtVistoria){
		this.dtVistoria=dtVistoria;
	}
	public GregorianCalendar getDtVistoria(){
		return this.dtVistoria;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setCdPlanoVistoria(int cdPlanoVistoria){
		this.cdPlanoVistoria=cdPlanoVistoria;
	}
	public int getCdPlanoVistoria(){
		return this.cdPlanoVistoria;
	}
	public void setCdVistoriaAnterior(int cdVistoriaAnterior){
		this.cdVistoriaAnterior=cdVistoriaAnterior;
	}
	public int getCdVistoriaAnterior(){
		return this.cdVistoriaAnterior;
	}
	public void setStVistoria(int stVistoria){
		this.stVistoria=stVistoria;
	}
	public int getStVistoria(){
		return this.stVistoria;
	}
	public void setDtAplicacao(GregorianCalendar dtAplicacao){
		this.dtAplicacao=dtAplicacao;
	}
	public GregorianCalendar getDtAplicacao(){
		return this.dtAplicacao;
	}
	public void setTpVistoria(int tpVistoria){
		this.tpVistoria=tpVistoria;
	}
	public int getTpVistoria(){
		return this.tpVistoria;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setIdSelo(String idSelo){
		this.idSelo=idSelo;
	}
	public String getIdSelo(){
		return this.idSelo;
	}
	public void setCdVistoriador(int cdVistoriador){
		this.cdVistoriador=cdVistoriador;
	}
	public int getCdVistoriador(){
		return this.cdVistoriador;
	}
	public void setCdCondutor(int cdCondutor){
		this.cdCondutor=cdCondutor;
	}
	public int getCdCondutor(){
		return this.cdCondutor;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdVistoria: " +  getCdVistoria();
		valueToString += ", dtVistoria: " +  sol.util.Util.formatDateTime(getDtVistoria(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", cdPlanoVistoria: " +  getCdPlanoVistoria();
		valueToString += ", cdVistoriaAnterior: " +  getCdVistoriaAnterior();
		valueToString += ", stVistoria: " +  getStVistoria();
		valueToString += ", dtAplicacao: " +  sol.util.Util.formatDateTime(getDtAplicacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpVistoria: " +  getTpVistoria();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", idSelo: " +  getIdSelo();
		valueToString += ", cdVistoriador: " +  getCdVistoriador();
		valueToString += ", cdCondutor: " +  getCdCondutor();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Vistoria(getCdVistoria(),
			getDtVistoria()==null ? null : (GregorianCalendar)getDtVistoria().clone(),
			getCdAgente(),
			getCdPessoa(),
			getCdEquipamento(),
			getCdVeiculo(),
			getCdPlanoVistoria(),
			getCdVistoriaAnterior(),
			getStVistoria(),
			getDtAplicacao()==null ? null : (GregorianCalendar)getDtAplicacao().clone(),
			getTpVistoria(),
			getDsObservacao(),
			getIdSelo(),
			getCdVistoriador(),
			getCdCondutor(),
			getCdConcessao());
	}

}