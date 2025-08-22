package com.tivic.manager.ptc;

public class WorkflowEntidade {

	private int cdEntidade;
	private String nmEntidade;

	public WorkflowEntidade(){ }

	public WorkflowEntidade(int cdEntidade,
			String nmEntidade){
		setCdEntidade(cdEntidade);
		setNmEntidade(nmEntidade);
	}
	public void setCdEntidade(int cdEntidade){
		this.cdEntidade=cdEntidade;
	}
	public int getCdEntidade(){
		return this.cdEntidade;
	}
	public void setNmEntidade(String nmEntidade){
		this.nmEntidade=nmEntidade;
	}
	public String getNmEntidade(){
		return this.nmEntidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEntidade: " +  getCdEntidade();
		valueToString += ", nmEntidade: " +  getNmEntidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowEntidade(getCdEntidade(),
			getNmEntidade());
	}

}
