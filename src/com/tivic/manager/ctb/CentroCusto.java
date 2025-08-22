package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class CentroCusto {

	private int cdCentroCusto;
	private int cdCentroCustoSuperior;
	private int cdSetor;
	private int cdPlanoCentroCusto;
	private String nmCentroCusto;
	private String nrCentroCusto;
	private GregorianCalendar dtInativacao;
	private String idCentroCusto;
	private String txtObservacao;

	public CentroCusto() { }
	
	public CentroCusto(int cdCentroCusto,
			int cdCentroCustoSuperior,
			int cdSetor,
			int cdPlanoCentroCusto,
			String nmCentroCusto,
			String nrCentroCusto,
			GregorianCalendar dtInativacao,
			String idCentroCusto,
			String txtObservacao){
		setCdCentroCusto(cdCentroCusto);
		setCdCentroCustoSuperior(cdCentroCustoSuperior);
		setCdSetor(cdSetor);
		setCdPlanoCentroCusto(cdPlanoCentroCusto);
		setNmCentroCusto(nmCentroCusto);
		setNrCentroCusto(nrCentroCusto);
		setDtInativacao(dtInativacao);
		setIdCentroCusto(idCentroCusto);
		setTxtObservacao(txtObservacao);
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setCdCentroCustoSuperior(int cdCentroCustoSuperior){
		this.cdCentroCustoSuperior=cdCentroCustoSuperior;
	}
	public int getCdCentroCustoSuperior(){
		return this.cdCentroCustoSuperior;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdPlanoCentroCusto(int cdPlanoCentroCusto){
		this.cdPlanoCentroCusto=cdPlanoCentroCusto;
	}
	public int getCdPlanoCentroCusto(){
		return this.cdPlanoCentroCusto;
	}
	public void setNmCentroCusto(String nmCentroCusto){
		this.nmCentroCusto=nmCentroCusto;
	}
	public String getNmCentroCusto(){
		return this.nmCentroCusto;
	}
	public void setNrCentroCusto(String nrCentroCusto){
		this.nrCentroCusto=nrCentroCusto;
	}
	public String getNrCentroCusto(){
		return this.nrCentroCusto;
	}
	public void setDtInativacao(GregorianCalendar dtInativacao){
		this.dtInativacao=dtInativacao;
	}
	public GregorianCalendar getDtInativacao(){
		return this.dtInativacao;
	}
	public void setIdCentroCusto(String idCentroCusto){
		this.idCentroCusto=idCentroCusto;
	}
	public String getIdCentroCusto(){
		return this.idCentroCusto;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdCentroCustoSuperior: " +  getCdCentroCustoSuperior();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdPlanoCentroCusto: " +  getCdPlanoCentroCusto();
		valueToString += ", nmCentroCusto: " +  getNmCentroCusto();
		valueToString += ", nrCentroCusto: " +  getNrCentroCusto();
		valueToString += ", dtInativacao: " +  sol.util.Util.formatDateTime(getDtInativacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idCentroCusto: " +  getIdCentroCusto();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CentroCusto(getCdCentroCusto(),
			getCdCentroCustoSuperior(),
			getCdSetor(),
			getCdPlanoCentroCusto(),
			getNmCentroCusto(),
			getNrCentroCusto(),
			getDtInativacao()==null ? null : (GregorianCalendar)getDtInativacao().clone(),
			getIdCentroCusto(),
			getTxtObservacao());
	}

}
