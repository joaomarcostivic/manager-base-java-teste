package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class Horario {

	private int cdHorario;
	private int cdTabelaHorario;
	private int cdLinha;
	private int cdRota;
	private int cdTrecho;
	private int lgRecolhe;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar hrPartida;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar hrChegada;
	private int cdParadaChegada;
	private int cdParadaPartida;
	private int cdVariacao;

	public Horario() {}

	public Horario(int cdHorario,
			int cdTabelaHorario,
			int cdLinha,
			int cdRota,
			int cdTrecho,
			int lgRecolhe,
			GregorianCalendar hrPartida,
			GregorianCalendar hrChegada,
			int cdParadaChegada,
			int cdParadaPartida,
			int cdVariacao) {
		setCdHorario(cdHorario);
		setCdTabelaHorario(cdTabelaHorario);
		setCdLinha(cdLinha);
		setCdRota(cdRota);
		setCdTrecho(cdTrecho);
		setLgRecolhe(lgRecolhe);
		setHrPartida(hrPartida);
		setHrChegada(hrChegada);
		setCdParadaChegada(cdParadaChegada);
		setCdParadaPartida(cdParadaPartida);
		setCdVariacao(cdVariacao);
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
	public void setLgRecolhe(int lgRecolhe){
		this.lgRecolhe=lgRecolhe;
	}
	public int getLgRecolhe(){
		return this.lgRecolhe;
	}
	public void setHrPartida(GregorianCalendar hrPartida){
		this.hrPartida=hrPartida;
	}
	public GregorianCalendar getHrPartida(){
		return this.hrPartida;
	}
	public void setHrChegada(GregorianCalendar hrChegada){
		this.hrChegada=hrChegada;
	}
	public GregorianCalendar getHrChegada(){
		return this.hrChegada;
	}
	public void setCdParadaChegada(int cdParadaChegada){
		this.cdParadaChegada=cdParadaChegada;
	}
	public int getCdParadaChegada(){
		return this.cdParadaChegada;
	}
	public void setCdParadaPartida(int cdParadaPartida){
		this.cdParadaPartida=cdParadaPartida;
	}
	public int getCdParadaPartida(){
		return this.cdParadaPartida;
	}
	public void setCdVariacao(int cdVariacao){
		this.cdVariacao=cdVariacao;
	}
	public int getCdVariacao(){
		return this.cdVariacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", cdLinha: " +  getCdLinha();
		valueToString += ", cdRota: " +  getCdRota();
		valueToString += ", cdTrecho: " +  getCdTrecho();
		valueToString += ", lgRecolhe: " +  getLgRecolhe();
		valueToString += ", hrPartida: " +  sol.util.Util.formatDateTime(getHrPartida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrChegada: " +  sol.util.Util.formatDateTime(getHrChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdParadaChegada: " +  getCdParadaChegada();
		valueToString += ", cdParadaPartida: " +  getCdParadaPartida();
		valueToString += ", cdVariacao: " +  getCdVariacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Horario(getCdHorario(),
			getCdTabelaHorario(),
			getCdLinha(),
			getCdRota(),
			getCdTrecho(),
			getLgRecolhe(),
			getHrPartida()==null ? null : (GregorianCalendar)getHrPartida().clone(),
			getHrChegada()==null ? null : (GregorianCalendar)getHrChegada().clone(),
			getCdParadaChegada(),
			getCdParadaPartida(),
			getCdVariacao());
	}

}