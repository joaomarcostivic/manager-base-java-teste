package com.tivic.manager.adm;

public class AvalistaContrato {

	private int cdContrato;
	private int cdPessoa;

	public AvalistaContrato(int cdContrato,
			int cdPessoa){
		setCdContrato(cdContrato);
		setCdPessoa(cdPessoa);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AvalistaContrato(getCdContrato(),
			getCdPessoa());
	}

}
