package com.tivic.manager.ctb;

public class SaldoContaExercicio {

	private int cdEmpresa;
	private String nrAnoExercicio;
	private int cdContaPlanoContas;
	private float vlSaldo;

	public SaldoContaExercicio(int cdEmpresa,
			String nrAnoExercicio,
			int cdContaPlanoContas,
			float vlSaldo){
		setCdEmpresa(cdEmpresa);
		setNrAnoExercicio(nrAnoExercicio);
		setCdContaPlanoContas(cdContaPlanoContas);
		setVlSaldo(vlSaldo);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNrAnoExercicio(String nrAnoExercicio){
		this.nrAnoExercicio=nrAnoExercicio;
	}
	public String getNrAnoExercicio(){
		return this.nrAnoExercicio;
	}
	public void setCdContaPlanoContas(int cdContaPlanoContas){
		this.cdContaPlanoContas=cdContaPlanoContas;
	}
	public int getCdContaPlanoContas(){
		return this.cdContaPlanoContas;
	}
	public void setVlSaldo(float vlSaldo){
		this.vlSaldo=vlSaldo;
	}
	public float getVlSaldo(){
		return this.vlSaldo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nrAnoExercicio: " +  getNrAnoExercicio();
		valueToString += ", cdContaPlanoContas: " +  getCdContaPlanoContas();
		valueToString += ", vlSaldo: " +  getVlSaldo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new SaldoContaExercicio(getCdEmpresa(),
			getNrAnoExercicio(),
			getCdContaPlanoContas(),
			getVlSaldo());
	}

}
