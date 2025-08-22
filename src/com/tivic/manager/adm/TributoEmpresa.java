package com.tivic.manager.adm;

public class TributoEmpresa {

	private int cdTributo;
	private int cdEmpresa;
	private int cdAgenteTributador;

	public TributoEmpresa(int cdTributo,
			int cdEmpresa,
			int cdAgenteTributador){
		setCdTributo(cdTributo);
		setCdEmpresa(cdEmpresa);
		setCdAgenteTributador(cdAgenteTributador);
	}
	public void setCdTributo(int cdTributo){
		this.cdTributo=cdTributo;
	}
	public int getCdTributo(){
		return this.cdTributo;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdAgenteTributador(int cdAgenteTributador){
		this.cdAgenteTributador=cdAgenteTributador;
	}
	public int getCdAgenteTributador(){
		return this.cdAgenteTributador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTributo: " +  getCdTributo();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdAgenteTributador: " +  getCdAgenteTributador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TributoEmpresa(getCdTributo(),
			getCdEmpresa(),
			getCdAgenteTributador());
	}

}
