package com.tivic.manager.grl;

public class OrgaoEmissor {

	private int cdOrgaoEmissor;
	private String nmOrgaoEmissor;
	private String idOrgaoEmissor;

	public OrgaoEmissor(){ }

	public OrgaoEmissor(int cdOrgaoEmissor,
			String nmOrgaoEmissor,
			String idOrgaoEmissor){
		setCdOrgaoEmissor(cdOrgaoEmissor);
		setNmOrgaoEmissor(nmOrgaoEmissor);
		setIdOrgaoEmissor(idOrgaoEmissor);
	}
	public void setCdOrgaoEmissor(int cdOrgaoEmissor){
		this.cdOrgaoEmissor=cdOrgaoEmissor;
	}
	public int getCdOrgaoEmissor(){
		return this.cdOrgaoEmissor;
	}
	public void setNmOrgaoEmissor(String nmOrgaoEmissor){
		this.nmOrgaoEmissor=nmOrgaoEmissor;
	}
	public String getNmOrgaoEmissor(){
		return this.nmOrgaoEmissor;
	}
	public void setIdOrgaoEmissor(String idOrgaoEmissor){
		this.idOrgaoEmissor=idOrgaoEmissor;
	}
	public String getIdOrgaoEmissor(){
		return this.idOrgaoEmissor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOrgaoEmissor: " +  getCdOrgaoEmissor();
		valueToString += ", nmOrgaoEmissor: " +  getNmOrgaoEmissor();
		valueToString += ", idOrgaoEmissor: " +  getIdOrgaoEmissor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OrgaoEmissor(getCdOrgaoEmissor(),
			getNmOrgaoEmissor(),
			getIdOrgaoEmissor());
	}

}