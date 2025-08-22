package com.tivic.manager.acd;

public class ProfessorDisciplina {

	private int cdProfessor;
	private int cdDisciplina;

	public ProfessorDisciplina(){ }

	public ProfessorDisciplina(int cdProfessor,
			int cdDisciplina){
		setCdProfessor(cdProfessor);
		setCdDisciplina(cdDisciplina);
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdProfessor: " +  getCdProfessor();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProfessorDisciplina(getCdProfessor(),
			getCdDisciplina());
	}

}