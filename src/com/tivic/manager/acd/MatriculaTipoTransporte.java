package com.tivic.manager.acd;

public class MatriculaTipoTransporte {

	private int cdMatricula;
	private int cdTipoTransporte;

	public MatriculaTipoTransporte() { }

	public MatriculaTipoTransporte(int cdMatricula,
			int cdTipoTransporte) {
		setCdMatricula(cdMatricula);
		setCdTipoTransporte(cdTipoTransporte);
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setCdTipoTransporte(int cdTipoTransporte){
		this.cdTipoTransporte=cdTipoTransporte;
	}
	public int getCdTipoTransporte(){
		return this.cdTipoTransporte;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatricula: " +  getCdMatricula();
		valueToString += ", cdTipoTransporte: " +  getCdTipoTransporte();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaTipoTransporte(getCdMatricula(),
			getCdTipoTransporte());
	}

}