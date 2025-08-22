package com.tivic.manager.prc;

public class EstadoOrgao {

	private int cdEstado;
	private int cdOrgao;

	public EstadoOrgao(){ }

	public EstadoOrgao(int cdEstado,
			int cdOrgao){
		setCdEstado(cdEstado);
		setCdOrgao(cdOrgao);
	}
	public void setCdEstado(int cdEstado){
		this.cdEstado=cdEstado;
	}
	public int getCdEstado(){
		return this.cdEstado;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEstado: " +  getCdEstado();
		valueToString += ", cdOrgao: " +  getCdOrgao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EstadoOrgao(getCdEstado(),
			getCdOrgao());
	}

}
