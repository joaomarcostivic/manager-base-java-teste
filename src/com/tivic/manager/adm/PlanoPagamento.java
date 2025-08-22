package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class PlanoPagamento {

	private int cdPlanoPagamento;
	private String nmPlanoPagamento;
	private String idPlanoPagamento;
	private float prJurosNominal;
	private GregorianCalendar dtInicioVigencia;
	private GregorianCalendar dtFinalVigencia;

	public PlanoPagamento(int cdPlanoPagamento,
			String nmPlanoPagamento,
			String idPlanoPagamento,
			float prJurosNominal,
			GregorianCalendar dtInicioVigencia,
			GregorianCalendar dtFinalVigencia){
		setCdPlanoPagamento(cdPlanoPagamento);
		setNmPlanoPagamento(nmPlanoPagamento);
		setIdPlanoPagamento(idPlanoPagamento);
		setPrJurosNominal(prJurosNominal);
		setDtInicioVigencia(dtInicioVigencia);
		setDtFinalVigencia(dtFinalVigencia);
	}
	public void setCdPlanoPagamento(int cdPlanoPagamento){
		this.cdPlanoPagamento=cdPlanoPagamento;
	}
	public int getCdPlanoPagamento(){
		return this.cdPlanoPagamento;
	}
	public void setNmPlanoPagamento(String nmPlanoPagamento){
		this.nmPlanoPagamento=nmPlanoPagamento;
	}
	public String getNmPlanoPagamento(){
		return this.nmPlanoPagamento;
	}
	public void setIdPlanoPagamento(String idPlanoPagamento){
		this.idPlanoPagamento=idPlanoPagamento;
	}
	public String getIdPlanoPagamento(){
		return this.idPlanoPagamento;
	}
	public void setPrJurosNominal(float prJurosNominal){
		this.prJurosNominal=prJurosNominal;
	}
	public float getPrJurosNominal(){
		return this.prJurosNominal;
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
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoPagamento: " +  getCdPlanoPagamento();
		valueToString += ", nmPlanoPagamento: " +  getNmPlanoPagamento();
		valueToString += ", idPlanoPagamento: " +  getIdPlanoPagamento();
		valueToString += ", prJurosNominal: " +  getPrJurosNominal();
		valueToString += ", dtInicioVigencia: " +  sol.util.Util.formatDateTime(getDtInicioVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalVigencia: " +  sol.util.Util.formatDateTime(getDtFinalVigencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoPagamento(getCdPlanoPagamento(),
			getNmPlanoPagamento(),
			getIdPlanoPagamento(),
			getPrJurosNominal(),
			getDtInicioVigencia()==null ? null : (GregorianCalendar)getDtInicioVigencia().clone(),
			getDtFinalVigencia()==null ? null : (GregorianCalendar)getDtFinalVigencia().clone());
	}

}