package com.tivic.manager.acd;

public class AlunoTipoTransporte {

	private int cdAluno;
	private int cdTipoTransporte;

	public AlunoTipoTransporte(){ }

	public AlunoTipoTransporte(int cdAluno,
			int cdTipoTransporte){
		setCdAluno(cdAluno);
		setCdTipoTransporte(cdTipoTransporte);
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdTipoTransporte(int cdTipoTransporte){
		this.cdTipoTransporte=cdTipoTransporte;
	}
	public int getCdTipoTransporte(){
		return this.cdTipoTransporte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAluno: " +  getCdAluno();
		valueToString += ", cdTipoTransporte: " +  getCdTipoTransporte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AlunoTipoTransporte(getCdAluno(),
			getCdTipoTransporte());
	}

}