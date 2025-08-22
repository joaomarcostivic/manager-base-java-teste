package com.tivic.manager.gpn;

public class WorkflowAtributo {

	private int cdAtributo;
	private int cdEntidade;
	private String nmAtributo;
	private String idAtributo;
	private int tpAtributo;
	private String nmClasspath;

	public WorkflowAtributo() { }

	public WorkflowAtributo(int cdAtributo,
			int cdEntidade,
			String nmAtributo,
			String idAtributo,
			int tpAtributo,
			String nmClasspath) {
		setCdAtributo(cdAtributo);
		setCdEntidade(cdEntidade);
		setNmAtributo(nmAtributo);
		setIdAtributo(idAtributo);
		setTpAtributo(tpAtributo);
		setNmClasspath(nmClasspath);
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
	public void setIdAtributo(String idAtributo){
		this.idAtributo=idAtributo;
	}
	public String getIdAtributo(){
		return this.idAtributo;
	}
	public void setTpAtributo(int tpAtributo){
		this.tpAtributo=tpAtributo;
	}
	public int getTpAtributo(){
		return this.tpAtributo;
	}
	public void setNmClasspath(String nmClasspath){
		this.nmClasspath=nmClasspath;
	}
	public String getNmClasspath(){
		return this.nmClasspath;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtributo: " +  getCdAtributo();
		valueToString += ", cdEntidade: " +  getCdEntidade();
		valueToString += ", nmAtributo: " +  getNmAtributo();
		valueToString += ", idAtributo: " +  getIdAtributo();
		valueToString += ", tpAtributo: " +  getTpAtributo();
		valueToString += ", nmClasspath: " +  getNmClasspath();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowAtributo(getCdAtributo(),
			getCdEntidade(),
			getNmAtributo(),
			getIdAtributo(),
			getTpAtributo(),
			getNmClasspath());
	}

}