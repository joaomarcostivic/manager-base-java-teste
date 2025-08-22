package com.tivic.manager.acd;

public class TurmaHorario {

	private int cdHorario;
	private int cdTurma;

	public TurmaHorario(){ }

	public TurmaHorario(int cdHorario,
			int cdTurma){
		setCdHorario(cdHorario);
		setCdTurma(cdTurma);
	}
	public void setCdHorario(int cdHorario){
		this.cdHorario=cdHorario;
	}
	public int getCdHorario(){
		return this.cdHorario;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorario: " +  getCdHorario();
		valueToString += ", cdTurma: " +  getCdTurma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TurmaHorario(getCdHorario(),
			getCdTurma());
	}

}