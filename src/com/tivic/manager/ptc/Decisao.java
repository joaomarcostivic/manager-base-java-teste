package com.tivic.manager.ptc;

public class Decisao {

	private int cdDecisao;
	private String nmDecisao;
	private int cdFluxo;

	public Decisao(){ }

	public Decisao(int cdDecisao,
			String nmDecisao,
			int cdFluxo){
		setCdDecisao(cdDecisao);
		setNmDecisao(nmDecisao);
		setCdFluxo(cdFluxo);
	}
	public void setCdDecisao(int cdDecisao){
		this.cdDecisao=cdDecisao;
	}
	public int getCdDecisao(){
		return this.cdDecisao;
	}
	public void setNmDecisao(String nmDecisao){
		this.nmDecisao=nmDecisao;
	}
	public String getNmDecisao(){
		return this.nmDecisao;
	}
	public void setCdFluxo(int cdFluxo){
		this.cdFluxo=cdFluxo;
	}
	public int getCdFluxo(){
		return this.cdFluxo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDecisao: " +  getCdDecisao();
		valueToString += ", nmDecisao: " +  getNmDecisao();
		valueToString += ", cdFluxo: " +  getCdFluxo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Decisao(getCdDecisao(),
			getNmDecisao(),
			getCdFluxo());
	}

}
