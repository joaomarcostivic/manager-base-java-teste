package com.tivic.manager.srh;

public class Turma {

	private int cdTurma;
	private String nmTurma;
	private String idTurma;

	public Turma(){ }

	public Turma(int cdTurma,
			String nmTurma,
			String idTurma){
		setCdTurma(cdTurma);
		setNmTurma(nmTurma);
		setIdTurma(idTurma);
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setNmTurma(String nmTurma){
		this.nmTurma=nmTurma;
	}
	public String getNmTurma(){
		return this.nmTurma;
	}
	public void setIdTurma(String idTurma){
		this.idTurma=idTurma;
	}
	public String getIdTurma(){
		return this.idTurma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTurma: " +  getCdTurma();
		valueToString += ", nmTurma: " +  getNmTurma();
		valueToString += ", idTurma: " +  getIdTurma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Turma(getCdTurma(),
			getNmTurma(),
			getIdTurma());
	}

}
