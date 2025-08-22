package com.tivic.manager.mob;

public class Inconsistencia {

	private int cdInconsistencia;
	private String nmInconsistencia;
	private int tpInconsistencia;

	public Inconsistencia() { }

	public Inconsistencia(int cdInconsistencia,
			String nmInconsistencia,
			int tpInconsistencia) {
		setCdInconsistencia(cdInconsistencia);
		setNmInconsistencia(nmInconsistencia);
		setTpInconsistencia(tpInconsistencia);
	}
	public void setCdInconsistencia(int cdInconsistencia){
		this.cdInconsistencia=cdInconsistencia;
	}
	public int getCdInconsistencia(){
		return this.cdInconsistencia;
	}
	public void setNmInconsistencia(String nmInconsistencia){
		this.nmInconsistencia=nmInconsistencia;
	}
	public String getNmInconsistencia(){
		return this.nmInconsistencia;
	}
	public void setTpInconsistencia(int tpInconsistencia){
		this.tpInconsistencia=tpInconsistencia;
	}
	public int getTpInconsistencia(){
		return this.tpInconsistencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInconsistencia: " +  getCdInconsistencia();
		valueToString += ", nmInconsistencia: " +  getNmInconsistencia();
		valueToString += ", tpInconsistencia: " +  getTpInconsistencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Inconsistencia(getCdInconsistencia(),
			getNmInconsistencia(),
			getTpInconsistencia());
	}

}