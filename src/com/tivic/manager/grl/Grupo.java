package com.tivic.manager.grl;

public class Grupo {

	private int cdGrupo;
	private int cdEmpresa;
	private String nmGrupo;

	public Grupo() { }

	public Grupo(int cdGrupo,
			int cdEmpresa,
			String nmGrupo) {
		setCdGrupo(cdGrupo);
		setCdEmpresa(cdEmpresa);
		setNmGrupo(nmGrupo);
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmGrupo(String nmGrupo){
		this.nmGrupo=nmGrupo;
	}
	public String getNmGrupo(){
		return this.nmGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdGrupo: " +  getCdGrupo();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmGrupo: " +  getNmGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Grupo(getCdGrupo(),
			getCdEmpresa(),
			getNmGrupo());
	}

}