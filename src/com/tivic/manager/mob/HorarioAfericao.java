package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class HorarioAfericao {

	private int cdControle;
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtLancamento;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar hrChegada;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar hrPartida;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar hrPrevisto;
	private int cdHorario;
	private int cdTabelaHorario;
	private int cdLinha;
	private int cdRota;
	private int cdTrecho;
	private int cdAgente;
	private int cdConcessaoVeiculo;
	private int stHorarioAfericao;

	

	public HorarioAfericao() { }

	public HorarioAfericao(int cdControle,
			GregorianCalendar dtLancamento,
			GregorianCalendar hrChegada,
			GregorianCalendar hrPartida,
			GregorianCalendar hrPrevisto,
			int cdHorario,
			int cdTabelaHorario,
			int cdLinha,
			int cdRota,
			int cdTrecho,
			int cdAgente,
			int cdConcessaoVeiculo,
			int stHorarioAfericao) {
		setCdControle(cdControle);
		setDtLancamento(dtLancamento);
		setHrChegada(hrChegada);
		setHrPartida(hrPartida);
		setHrPrevisto(hrPrevisto);
		setCdHorario(cdHorario);
		setCdTabelaHorario(cdTabelaHorario);
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdTrecho(cdTrecho);
		setCdAgente(cdAgente);
		setCdConcessaoVeiculo(cdConcessaoVeiculo);
		setStHorarioAfericao(stHorarioAfericao);
	}
	public void setCdControle(int cdControle){
		this.cdControle=cdControle;
	}
	public int getCdControle(){
		return this.cdControle;
	}
	public void setDtLancamento(GregorianCalendar dtLancamento){
		this.dtLancamento=dtLancamento;
	}
	public GregorianCalendar getDtLancamento(){
		return this.dtLancamento;
	}
	public void setHrChegada(GregorianCalendar hrChegada){
		this.hrChegada=hrChegada;
	}
	public GregorianCalendar getHrChegada(){
		return this.hrChegada;
	}
	public void setHrPartida(GregorianCalendar hrPartida){
		this.hrPartida=hrPartida;
	}
	public GregorianCalendar getHrPartida(){
		return this.hrPartida;
	}
	public void setHrPrevisto(GregorianCalendar hrPrevisto){
		this.hrPrevisto=hrPrevisto;
	}
	public GregorianCalendar getHrPrevisto(){
		return this.hrPrevisto;
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
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
	public void setCdRota(int cdRota){
		this.cdRota=cdRota;
	}
	public int getCdRota(){
		return this.cdRota;
	}
	public void setCdTrecho(int cdTrecho){
		this.cdTrecho=cdTrecho;
	}
	public int getCdTrecho(){
		return this.cdTrecho;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setCdConcessaoVeiculo(int cdConcessaoVeiculo){
		this.cdConcessaoVeiculo=cdConcessaoVeiculo;
	}
	public int getCdConcessaoVeiculo(){
		return this.cdConcessaoVeiculo;
	}
	
	public int getStHorarioAfericao() {
		return stHorarioAfericao;
	}

	public void setStHorarioAfericao(int stHorarioAfericao) {
		this.stHorarioAfericao = stHorarioAfericao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdControle: " +  getCdControle();
		valueToString += ", dtLancamento: " +  sol.util.Util.formatDateTime(getDtLancamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegada: " +  sol.util.Util.formatDateTime(getHrChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPartida: " +  sol.util.Util.formatDateTime(getHrPartida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrPrevisto: " +  sol.util.Util.formatDateTime(getHrPrevisto(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdHorario: " +  getCdHorario();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdTrecho: " +  getCdTrecho();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", cdConcessaoVeiculo: " +  getCdConcessaoVeiculo();
		valueToString += ", stHorarioAfericao: " + getStHorarioAfericao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new HorarioAfericao(getCdControle(),
			getDtLancamento()==null ? null : (GregorianCalendar)getDtLancamento().clone(),
			getHrChegada()==null ? null : (GregorianCalendar)getHrChegada().clone(),
			getHrPartida()==null ? null : (GregorianCalendar)getHrPartida().clone(),
			getHrPrevisto()==null ? null : (GregorianCalendar)getHrPrevisto().clone(),
			getCdHorario(),
			getCdTabelaHorario(),
			getCdLinha(),
			getCdRota(),
			getCdTrecho(),
			getCdAgente(),
			getCdConcessaoVeiculo(),
			getStHorarioAfericao());
	}

}