package com.tivic.manager.acd;

public class InstituicaoCirculo {

	private int cdCirculo;
	private int cdInstituicao;

	public InstituicaoCirculo(){ }

	public InstituicaoCirculo(int cdCirculo,
			int cdInstituicao){
		setCdCirculo(cdCirculo);
		setCdInstituicao(cdInstituicao);
	}
	public void setCdCirculo(int cdCirculo){
		this.cdCirculo=cdCirculo;
	}
	public int getCdCirculo(){
		return this.cdCirculo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCirculo: " +  getCdCirculo();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoCirculo(getCdCirculo(),
			getCdInstituicao());
	}

}