package com.tivic.manager.srh;

public class Ferias {

	private int cdFerias;
	private int cdMatricula;

	public Ferias(int cdFerias,
			int cdMatricula){
		setCdFerias(cdFerias);
		setCdMatricula(cdMatricula);
	}
	public void setCdFerias(int cdFerias){
		this.cdFerias=cdFerias;
	}
	public int getCdFerias(){
		return this.cdFerias;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFerias: " +  getCdFerias();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Ferias(cdFerias,
			cdMatricula);
	}

}