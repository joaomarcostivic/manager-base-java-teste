package com.tivic.manager.ctb;

public class ContaCentroCusto {

	private int cdCentroCusto;
	private int cdContaPlanoContas;
	private float prRateio;

	public ContaCentroCusto(int cdCentroCusto,
			int cdContaPlanoContas,
			float prRateio){
		setCdCentroCusto(cdCentroCusto);
		setCdContaPlanoContas(cdContaPlanoContas);
		setPrRateio(prRateio);
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setCdContaPlanoContas(int cdContaPlanoContas){
		this.cdContaPlanoContas=cdContaPlanoContas;
	}
	public int getCdContaPlanoContas(){
		return this.cdContaPlanoContas;
	}
	public void setPrRateio(float prRateio){
		this.prRateio=prRateio;
	}
	public float getPrRateio(){
		return this.prRateio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdContaPlanoContas: " +  getCdContaPlanoContas();
		valueToString += ", prRateio: " +  getPrRateio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaCentroCusto(getCdCentroCusto(),
			getCdContaPlanoContas(),
			getPrRateio());
	}

}
