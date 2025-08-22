package com.tivic.manager.ptc;

public class WorkflowGatilho {

	private int cdGatilho;
	private int cdRegra;
	private int tpGatilho;
	private int vlInicial;
	private int vlFinal;
	private int cdAtributo;
	private int cdEntidade;

	public WorkflowGatilho(){ }

	public WorkflowGatilho(int cdGatilho,
			int cdRegra,
			int tpGatilho,
			int vlInicial,
			int vlFinal,
			int cdAtributo,
			int cdEntidade){
		setCdGatilho(cdGatilho);
		setCdRegra(cdRegra);
		setTpGatilho(tpGatilho);
		setVlInicial(vlInicial);
		setVlFinal(vlFinal);
		setCdAtributo(cdAtributo);
		setCdEntidade(cdEntidade);
	}
	public void setCdGatilho(int cdGatilho){
		this.cdGatilho=cdGatilho;
	}
	public int getCdGatilho(){
		return this.cdGatilho;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setTpGatilho(int tpGatilho){
		this.tpGatilho=tpGatilho;
	}
	public int getTpGatilho(){
		return this.tpGatilho;
	}
	public void setVlInicial(int vlInicial){
		this.vlInicial=vlInicial;
	}
	public int getVlInicial(){
		return this.vlInicial;
	}
	public void setVlFinal(int vlFinal){
		this.vlFinal=vlFinal;
	}
	public int getVlFinal(){
		return this.vlFinal;
	}
	public void setCdAtributo(int cdAtributo){
		this.cdAtributo=cdAtributo;
	}
	public int getCdAtributo(){
		return this.cdAtributo;
	}
	public void setCdEntidade(int cdEntidade){
		this.cdEntidade=cdEntidade;
	}
	public int getCdEntidade(){
		return this.cdEntidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGatilho: " +  getCdGatilho();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", tpGatilho: " +  getTpGatilho();
		valueToString += ", vlInicial: " +  getVlInicial();
		valueToString += ", vlFinal: " +  getVlFinal();
		valueToString += ", cdAtributo: " +  getCdAtributo();
		valueToString += ", cdEntidade: " +  getCdEntidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowGatilho(getCdGatilho(),
			getCdRegra(),
			getTpGatilho(),
			getVlInicial(),
			getVlFinal(),
			getCdAtributo(),
			getCdEntidade());
	}

}
