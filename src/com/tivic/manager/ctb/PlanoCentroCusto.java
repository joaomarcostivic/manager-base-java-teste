package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class PlanoCentroCusto {

	private int cdPlanoCentroCusto;
	private String nmPlanoCentroCusto;
	private GregorianCalendar dtInativacao;
	private String dsMascaraCentroCusto;
	private String idPlanoCentroCusto;
	private int cdExercicio;

	public PlanoCentroCusto(){ }

	public PlanoCentroCusto(int cdPlanoCentroCusto,
			String nmPlanoCentroCusto,
			GregorianCalendar dtInativacao,
			String dsMascaraCentroCusto,
			String idPlanoCentroCusto,
			int cdExercicio){
		setCdPlanoCentroCusto(cdPlanoCentroCusto);
		setNmPlanoCentroCusto(nmPlanoCentroCusto);
		setDtInativacao(dtInativacao);
		setDsMascaraCentroCusto(dsMascaraCentroCusto);
		setIdPlanoCentroCusto(idPlanoCentroCusto);
		setCdExercicio(cdExercicio);
	}
	public void setCdPlanoCentroCusto(int cdPlanoCentroCusto){
		this.cdPlanoCentroCusto=cdPlanoCentroCusto;
	}
	public int getCdPlanoCentroCusto(){
		return this.cdPlanoCentroCusto;
	}
	public void setNmPlanoCentroCusto(String nmPlanoCentroCusto){
		this.nmPlanoCentroCusto=nmPlanoCentroCusto;
	}
	public String getNmPlanoCentroCusto(){
		return this.nmPlanoCentroCusto;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setDsMascaraCentroCusto(String dsMascaraCentroCusto){
		this.dsMascaraCentroCusto=dsMascaraCentroCusto;
	}
	public String getDsMascaraCentroCusto(){
		return this.dsMascaraCentroCusto;
	}
	public void setIdPlanoCentroCusto(String idPlanoCentroCusto){
		this.idPlanoCentroCusto=idPlanoCentroCusto;
	}
	public String getIdPlanoCentroCusto(){
		return this.idPlanoCentroCusto;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanoCentroCusto: " +  getCdPlanoCentroCusto();
		valueToString += ", nmPlanoCentroCusto: " +  getNmPlanoCentroCusto();
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsMascaraCentroCusto: " +  getDsMascaraCentroCusto();
		valueToString += ", idPlanoCentroCusto: " +  getIdPlanoCentroCusto();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoCentroCusto(getCdPlanoCentroCusto(),
			getNmPlanoCentroCusto(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getDsMascaraCentroCusto(),
			getIdPlanoCentroCusto(),
			getCdExercicio());
	}

}