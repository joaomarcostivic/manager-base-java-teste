package com.tivic.manager.srh;

public class PlanoCargoNivel {

	private int cdPlano;
	private int cdNivel;
	private Double vlSalarioBase;
	private int qtVagas;
	private String nmNivel;

	public PlanoCargoNivel() { }

	public PlanoCargoNivel(int cdPlano,
			int cdNivel,
			Double vlSalarioBase,
			int qtVagas,
			String nmNivel) {
		setCdPlano(cdPlano);
		setCdNivel(cdNivel);
		setVlSalarioBase(vlSalarioBase);
		setQtVagas(qtVagas);
		setNmNivel(nmNivel);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setCdNivel(int cdNivel){
		this.cdNivel=cdNivel;
	}
	public int getCdNivel(){
		return this.cdNivel;
	}
	public void setVlSalarioBase(Double vlSalarioBase){
		this.vlSalarioBase=vlSalarioBase;
	}
	public Double getVlSalarioBase(){
		return this.vlSalarioBase;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public void setNmNivel(String nmNivel){
		this.nmNivel=nmNivel;
	}
	public String getNmNivel(){
		return this.nmNivel;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", cdNivel: " +  getCdNivel();
		valueToString += ", vlSalarioBase: " +  getVlSalarioBase();
		valueToString += ", qtVagas: " +  getQtVagas();
		valueToString += ", nmNivel: " +  getNmNivel();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PlanoCargoNivel(getCdPlano(),
			getCdNivel(),
			getVlSalarioBase(),
			getQtVagas(),
			getNmNivel());
	}

}