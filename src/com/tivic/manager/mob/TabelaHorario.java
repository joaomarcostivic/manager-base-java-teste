package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TabelaHorario {

	private int cdTabelaHorario;
	private int cdLinha;
	private int nrTabelaHorario;
	private int tpTabelaHorario;
	private int nrDiaSemana;
	private int nrDiaMes;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtEspecial;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicioVigencia;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtFinalVigencia;
	
	private int stTabelaHorario;
	private String idTabelaHorario;
	private String txtObservacao;
	private String nmTabelaHorario;
	private int cdRota;
	private int lgAdaptado;
	private int cdConcessaoVeiculo;

	public TabelaHorario() { }

	public TabelaHorario(int cdTabelaHorario,
			int cdLinha,
			int nrTabelaHorario,
			int tpTabelaHorario,
			int nrDiaSemana,
			int nrDiaMes,
			GregorianCalendar dtEspecial,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia,
			int stTabelaHorario,
			String idTabelaHorario,
			String txtObservacao,
			String nmTabelaHorario,
			int cdRota,
			int lgAdaptado,
			int cdConcessaoVeiculo) {
		setCdTabelaHorario(cdTabelaHorario);
		setCdLinha(cdLinha);
		setNrTabelaHorario(nrTabelaHorario);
		setTpTabelaHorario(tpTabelaHorario);
		setNrDiaSemana(nrDiaSemana);
		setNrDiaMes(nrDiaMes);
		setDtEspecial(dtEspecial);
		setDtInicioVigencia(dtInicioVigencia);
		setDtFinalVigencia(dtFinalVigencia);
		setStTabelaHorario(stTabelaHorario);
		setIdTabelaHorario(idTabelaHorario);
		setTxtObservacao(txtObservacao);
		setNmTabelaHorario(nmTabelaHorario);
		setCdRota(cdRota);
		setLgAdaptado(lgAdaptado);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
	}
	public void setCdTabelaHorario(int cdTabelaHorario){
		this.cdTabelaHorario=cdTabelaHorario;
	}
	public int getCdTabelaHorario(){
		return this.cdTabelaHorario;
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setNrTabelaHorario(int nrTabelaHorario){
		this.nrTabelaHorario=nrTabelaHorario;
	}
	public int getNrTabelaHorario(){
		return this.nrTabelaHorario;
	}
	public void setTpTabelaHorario(int tpTabelaHorario){
		this.tpTabelaHorario=tpTabelaHorario;
	}
	public int getTpTabelaHorario(){
		return this.tpTabelaHorario;
	}
	public void setNrDiaSemana(int nrDiaSemana){
		this.nrDiaSemana=nrDiaSemana;
	}
	public int getNrDiaSemana(){
		return this.nrDiaSemana;
	}
	public void setNrDiaMes(int nrDiaMes){
		this.nrDiaMes=nrDiaMes;
	}
	public int getNrDiaMes(){
		return this.nrDiaMes;
	}
	public void setDtEspecial(GregorianCalendar dtEspecial){
		this.dtEspecial=dtEspecial;
	}
	public GregorianCalendar getDtEspecial(){
		return this.dtEspecial;
	}
	public void setDtInicioVigencia(GregorianCalendar dtInicioVigencia){
		this.dtInicioVigencia=dtInicioVigencia;
	}
	public GregorianCalendar getDtInicioVigencia(){
		return this.dtInicioVigencia;
	}
	public void setDtFinalVigencia(GregorianCalendar dtFinalVigencia){
		this.dtFinalVigencia=dtFinalVigencia;
	}
	public GregorianCalendar getDtFinalVigencia(){
		return this.dtFinalVigencia;
	}
	public void setStTabelaHorario(int stTabelaHorario){
		this.stTabelaHorario=stTabelaHorario;
	}
	public int getStTabelaHorario(){
		return this.stTabelaHorario;
	}
	public void setIdTabelaHorario(String idTabelaHorario){
		this.idTabelaHorario=idTabelaHorario;
	}
	public String getIdTabelaHorario(){
		return this.idTabelaHorario;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNmTabelaHorario(String nmTabelaHorario){
		this.nmTabelaHorario=nmTabelaHorario;
	}
	public String getNmTabelaHorario(){
		return this.nmTabelaHorario;
	}
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setLgAdaptado(int lgAdaptado){
		this.lgAdaptado=lgAdaptado;
	}
	public int getLgAdaptado(){
		return this.lgAdaptado;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", nrTabelaHorario: " +  getNrTabelaHorario();
		valueToString += ", tpTabelaHorario: " +  getTpTabelaHorario();
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", nrDiaMes: " +  getNrDiaMes();
		valueToString += ", dtEspecial: " +  sol.util.Util.formatDateTime(getDtEspecial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtInicioVigencia: " +  sol.util.Util.formatDateTime(getDtInicioVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stTabelaHorario: " +  getStTabelaHorario();
		valueToString += ", idTabelaHorario: " +  getIdTabelaHorario();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nmTabelaHorario: " +  getNmTabelaHorario();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", lgAdaptado: " +  getLgAdaptado();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaHorario(getCdTabelaHorario(),
			getCdLinha(),
			getNrTabelaHorario(),
			getTpTabelaHorario(),
			getNrDiaSemana(),
			getNrDiaMes(),
			getDtEspecial()==null ? null : (GregorianCalendar)getDtEspecial().clone(),
			getDtInicioVigencia()==null ? null : (GregorianCalendar)getDtInicioVigencia().clone(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone(),
			getStTabelaHorario(),
			getIdTabelaHorario(),
			getTxtObservacao(),
			getNmTabelaHorario(),
			getCdRota(),
			getLgAdaptado(),
			getCdConcessaoVeiculo());
	}

}