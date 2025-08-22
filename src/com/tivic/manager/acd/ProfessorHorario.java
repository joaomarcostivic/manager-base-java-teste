package com.tivic.manager.acd;

public class ProfessorHorario {

	private int cdHorario;
	private int cdProfessor;

	public ProfessorHorario(){ }

	public ProfessorHorario(int cdHorario,
			int cdProfessor){
		setCdHorario(cdHorario);
		setCdProfessor(cdProfessor);
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", cdProfessor: " +  getCdProfessor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProfessorHorario(getCdHorario(),
			getCdProfessor());
	}

}