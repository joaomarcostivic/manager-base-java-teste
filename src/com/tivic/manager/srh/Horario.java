package com.tivic.manager.srh;

import java.util.GregorianCalendar;

public class Horario {

	private int cdHorario;
	private int cdTabelaHorario;
	private int nrDiaSemana;
	private GregorianCalendar hrEntrada;
	private GregorianCalendar hrSaida;
	private int tpHorario;

	public Horario() { }
	
	public Horario(int cdHorario,
			int cdTabelaHorario,
			int nrDiaSemana,
			GregorianCalendar hrEntrada,
			GregorianCalendar hrSaida,
			int tpHorario){
		setCdHorario(cdHorario);
		setCdTabelaHorario(cdTabelaHorario);
		setNrDiaSemana(nrDiaSemana);
		setHrEntrada(hrEntrada);
		setHrSaida(hrSaida);
		setTpHorario(tpHorario);
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
	public void setNrDiaSemana(int nrDiaSemana){
		this.nrDiaSemana=nrDiaSemana;
	}
	public int getNrDiaSemana(){
		return this.nrDiaSemana;
	}
	public void setHrEntrada(GregorianCalendar hrEntrada){
		this.hrEntrada=hrEntrada;
	}
	public GregorianCalendar getHrEntrada(){
		return this.hrEntrada;
	}
	public void setHrSaida(GregorianCalendar hrSaida){
		this.hrSaida=hrSaida;
	}
	public GregorianCalendar getHrSaida(){
		return this.hrSaida;
	}
	public void setTpHorario(int tpHorario){
		this.tpHorario=tpHorario;
	}
	public int getTpHorario(){
		return this.tpHorario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", cdTabelaHorario: " +  getCdTabelaHorario();
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", hrEntrada: " +  sol.util.Util.formatDateTime(getHrEntrada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrSaida: " +  sol.util.Util.formatDateTime(getHrSaida(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpHorario: " +  getTpHorario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Horario(cdHorario,
			cdTabelaHorario,
			nrDiaSemana,
			hrEntrada==null ? null : (GregorianCalendar)hrEntrada.clone(),
			hrSaida==null ? null : (GregorianCalendar)hrSaida.clone(),
			tpHorario);
	}

}