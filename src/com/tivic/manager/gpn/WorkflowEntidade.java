package com.tivic.manager.gpn;

public class WorkflowEntidade {

	private int cdEntidade;
	private String nmEntidade;
	private String idEntidade;
	private String nmClasspath;

	public WorkflowEntidade() { }

	public WorkflowEntidade(int cdEntidade,
			String nmEntidade,
			String idEntidade,
			String nmClasspath) {
		setCdEntidade(cdEntidade);
		setNmEntidade(nmEntidade);
		setIdEntidade(idEntidade);
		setNmClasspath(nmClasspath);
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
	public void setIdEntidade(String idEntidade){
		this.idEntidade=idEntidade;
	}
	public String getIdEntidade(){
		return this.idEntidade;
	}
	public void setNmClasspath(String nmClasspath){
		this.nmClasspath=nmClasspath;
	}
	public String getNmClasspath(){
		return this.nmClasspath;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEntidade: " +  getCdEntidade();
		valueToString += ", nmEntidade: " +  getNmEntidade();
		valueToString += ", idEntidade: " +  getIdEntidade();
		valueToString += ", nmClasspath: " +  getNmClasspath();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowEntidade(getCdEntidade(),
			getNmEntidade(),
			getIdEntidade(),
			getNmClasspath());
	}

}