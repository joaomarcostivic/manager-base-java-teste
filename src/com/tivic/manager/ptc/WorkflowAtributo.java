package com.tivic.manager.ptc;

public class WorkflowAtributo {

	private int cdAtributo;
	private int cdEntidade;
	private String nmAtributo;

	public WorkflowAtributo(){ }

	public WorkflowAtributo(int cdAtributo,
			int cdEntidade,
			String nmAtributo){
		setCdAtributo(cdAtributo);
		setCdEntidade(cdEntidade);
		setNmAtributo(nmAtributo);
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
	public void setNmAtributo(String nmAtributo){
		this.nmAtributo=nmAtributo;
	}
	public String getNmAtributo(){
		return this.nmAtributo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtributo: " +  getCdAtributo();
		valueToString += ", cdEntidade: " +  getCdEntidade();
		valueToString += ", nmAtributo: " +  getNmAtributo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowAtributo(getCdAtributo(),
			getCdEntidade(),
			getNmAtributo());
	}

}
