package com.tivic.manager.mob.AitInconsistencia;

import java.util.GregorianCalendar;

public class AitInconsistencia {

	private int cdAitInconsistencia;
	private int cdAit;
	private int cdMovimentoAtual;
	private int cdInconsistencia;
	private int tpStatusPretendido;
	private int tpStatusAtual;
	private GregorianCalendar dtInclusaoInconsistencia;
	private int stInconsistencia;
	private GregorianCalendar dtResolucaoInconsistencia;

	public AitInconsistencia() { }

	public AitInconsistencia(int cdAitInconsistencia,
			int cdAit,
			int cdMovimentoAtual,
			int cdInconsistencia,
			int tpStatusPretendido,
			int tpStatusAtual,
			GregorianCalendar dtInclusaoInconsistencia,
			int stInconsistencia,
			GregorianCalendar dtResolucaoInconsistencia) {
		setCdAitInconsistencia(cdAitInconsistencia);
		setCdAit(cdAit);
		setCdMovimentoAtual(cdMovimentoAtual);
		setCdInconsistencia(cdInconsistencia);
		setTpStatusPretendido(tpStatusPretendido);
		setTpStatusAtual(tpStatusAtual);
		setDtInclusaoInconsistencia(dtInclusaoInconsistencia);
		setStInconsistencia(stInconsistencia);
		setDtResolucaoInconsistencia(dtResolucaoInconsistencia);
	}
	public void setCdAitInconsistencia(int cdAitInconsistencia){
		this.cdAitInconsistencia=cdAitInconsistencia;
	}
	public int getCdAitInconsistencia(){
		return this.cdAitInconsistencia;
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdMovimentoAtual(int cdMovimentoAtual){
		this.cdMovimentoAtual=cdMovimentoAtual;
	}
	public int getCdMovimentoAtual(){
		return this.cdMovimentoAtual;
	}
	public void setCdInconsistencia(int cdInconsistencia){
		this.cdInconsistencia=cdInconsistencia;
	}
	public int getCdInconsistencia(){
		return this.cdInconsistencia;
	}
	public void setTpStatusPretendido(int tpStatusPretendido){
		this.tpStatusPretendido=tpStatusPretendido;
	}
	public int getTpStatusPretendido(){
		return this.tpStatusPretendido;
	}
	public void setTpStatusAtual(int tpStatusAtual){
		this.tpStatusAtual=tpStatusAtual;
	}
	public int getTpStatusAtual(){
		return this.tpStatusAtual;
	}
	public void setDtInclusaoInconsistencia(GregorianCalendar dtInclusaoInconsistencia){
		this.dtInclusaoInconsistencia=dtInclusaoInconsistencia;
	}
	public GregorianCalendar getDtInclusaoInconsistencia(){
		return this.dtInclusaoInconsistencia;
	}
	public void setStInconsistencia(int stInconsistencia){
		this.stInconsistencia=stInconsistencia;
	}
	public int getStInconsistencia(){
		return this.stInconsistencia;
	}
	public void setDtResolucaoInconsistencia(GregorianCalendar dtResolucaoInconsistencia){
		this.dtResolucaoInconsistencia=dtResolucaoInconsistencia;
	}
	public GregorianCalendar getDtResolucaoInconsistencia(){
		return this.dtResolucaoInconsistencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAitInconsistencia: " +  getCdAitInconsistencia();
		valueToString += ", cdAit: " +  getCdAit();
		valueToString += ", cdMovimentoAtual: " +  getCdMovimentoAtual();
		valueToString += ", cdInconsistencia: " +  getCdInconsistencia();
		valueToString += ", tpStatusPretendido: " +  getTpStatusPretendido();
		valueToString += ", tpStatusAtual: " +  getTpStatusAtual();
		valueToString += ", dtInclusaoInconsistencia: " +  sol.util.Util.formatDateTime(getDtInclusaoInconsistencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stInconsistencia: " +  getStInconsistencia();
		valueToString += ", dtResolucaoInconsistencia: " +  sol.util.Util.formatDateTime(getDtResolucaoInconsistencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitInconsistencia(getCdAitInconsistencia(),
			getCdAit(),
			getCdMovimentoAtual(),
			getCdInconsistencia(),
			getTpStatusPretendido(),
			getTpStatusAtual(),
			getDtInclusaoInconsistencia()==null ? null : (GregorianCalendar)getDtInclusaoInconsistencia().clone(),
			getStInconsistencia(),
			getDtResolucaoInconsistencia()==null ? null : (GregorianCalendar)getDtResolucaoInconsistencia().clone());
	}

}