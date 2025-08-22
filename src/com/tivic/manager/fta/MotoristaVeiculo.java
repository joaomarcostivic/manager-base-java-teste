package com.tivic.manager.fta;

import java.util.GregorianCalendar;

public class MotoristaVeiculo {

	private int cdMotorista;
	private int cdVeiculo;
	private GregorianCalendar dtInicioAtividade;
	private GregorianCalendar dtFinalAtividade;
	private int lgAtivo;

	public MotoristaVeiculo(int cdMotorista,
			int cdVeiculo,
			GregorianCalendar dtInicioAtividade,
			GregorianCalendar dtFinalAtividade,
			int lgAtivo){
		setCdMotorista(cdMotorista);
		setCdVeiculo(cdVeiculo);
		setDtInicioAtividade(dtInicioAtividade);
		setDtFinalAtividade(dtFinalAtividade);
		setLgAtivo(lgAtivo);
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setCdVeiculo(int cdVeiculo){
		this.cdVeiculo=cdVeiculo;
	}
	public int getCdVeiculo(){
		return this.cdVeiculo;
	}
	public void setDtInicioAtividade(GregorianCalendar dtInicioAtividade){
		this.dtInicioAtividade=dtInicioAtividade;
	}
	public GregorianCalendar getDtInicioAtividade(){
		return this.dtInicioAtividade;
	}
	public void setDtFinalAtividade(GregorianCalendar dtFinalAtividade){
		this.dtFinalAtividade=dtFinalAtividade;
	}
	public GregorianCalendar getDtFinalAtividade(){
		return this.dtFinalAtividade;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMotorista: " +  getCdMotorista();
		valueToString += ", cdVeiculo: " +  getCdVeiculo();
		valueToString += ", dtInicioAtividade: " +  sol.util.Util.formatDateTime(getDtInicioAtividade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalAtividade: " +  sol.util.Util.formatDateTime(getDtFinalAtividade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAtivo: " +  getLgAtivo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotoristaVeiculo(getCdMotorista(),
			getCdVeiculo(),
			getDtInicioAtividade()==null ? null : (GregorianCalendar)getDtInicioAtividade().clone(),
			getDtFinalAtividade()==null ? null : (GregorianCalendar)getDtFinalAtividade().clone(),
			getLgAtivo());
	}

}