package com.tivic.manager.srh;

public class PlanoCargo {

	private int cdPlano;
	private String nmPlano;
	private Double vlSalarioBase;
	private String dsAtributo;
	private int qtVagas;
	private int cdCbo;
	private int cdEmpresa;

	public PlanoCargo() { }

	public PlanoCargo(int cdPlano,
			String nmPlano,
			Double vlSalarioBase,
			String dsAtributo,
			int qtVagas,
			int cdCbo,
			int cdEmpresa) {
		setCdPlano(cdPlano);
		setNmPlano(nmPlano);
		setVlSalarioBase(vlSalarioBase);
		setDsAtributo(dsAtributo);
		setQtVagas(qtVagas);
		setCdCbo(cdCbo);
		setCdEmpresa(cdEmpresa);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setNmPlano(String nmPlano){
		this.nmPlano=nmPlano;
	}
	public String getNmPlano(){
		return this.nmPlano;
	}
	public void setVlSalarioBase(Double vlSalarioBase){
		this.vlSalarioBase=vlSalarioBase;
	}
	public Double getVlSalarioBase(){
		return this.vlSalarioBase;
	}
	public void setDsAtributo(String dsAtributo){
		this.dsAtributo=dsAtributo;
	}
	public String getDsAtributo(){
		return this.dsAtributo;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public void setCdCbo(int cdCbo){
		this.cdCbo=cdCbo;
	}
	public int getCdCbo(){
		return this.cdCbo;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", nmPlano: " +  getNmPlano();
		valueToString += ", vlSalarioBase: " +  getVlSalarioBase();
		valueToString += ", dsAtributo: " +  getDsAtributo();
		valueToString += ", qtVagas: " +  getQtVagas();
		valueToString += ", cdCbo: " +  getCdCbo();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoCargo(getCdPlano(),
			getNmPlano(),
			getVlSalarioBase(),
			getDsAtributo(),
			getQtVagas(),
			getCdCbo(),
			getCdEmpresa());
	}

}