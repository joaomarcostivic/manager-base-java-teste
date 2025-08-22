package com.tivic.manager.ptc;

public class Fluxo {

	private int cdFluxo;
	private String nmFluxo;
	private int cdFluxoAnterior;

	public Fluxo(){ }

	public Fluxo(int cdFluxo,
			String nmFluxo,
			int cdFluxoAnterior){
		setCdFluxo(cdFluxo);
		setNmFluxo(nmFluxo);
		setCdFluxoAnterior(cdFluxoAnterior);
	}
	public void setCdFluxo(int cdFluxo){
		this.cdFluxo=cdFluxo;
	}
	public int getCdFluxo(){
		return this.cdFluxo;
	}
	public void setNmFluxo(String nmFluxo){
		this.nmFluxo=nmFluxo;
	}
	public String getNmFluxo(){
		return this.nmFluxo;
	}
	public void setCdFluxoAnterior(int cdFluxoAnterior){
		this.cdFluxoAnterior=cdFluxoAnterior;
	}
	public int getCdFluxoAnterior(){
		return this.cdFluxoAnterior;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFluxo: " +  getCdFluxo();
		valueToString += ", nmFluxo: " +  getNmFluxo();
		valueToString += ", cdFluxoAnterior: " +  getCdFluxoAnterior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Fluxo(getCdFluxo(),
			getNmFluxo(),
			getCdFluxoAnterior());
	}

}