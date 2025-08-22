package com.tivic.manager.acd;

public class AlunoRecursoProva {

	private int cdAluno;
	private int cdTipoRecursoProva;

	public AlunoRecursoProva(){ }

	public AlunoRecursoProva(int cdAluno,
			int cdTipoRecursoProva){
		setCdAluno(cdAluno);
		setCdTipoRecursoProva(cdTipoRecursoProva);
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdTipoRecursoProva(int cdTipoRecursoProva){
		this.cdTipoRecursoProva=cdTipoRecursoProva;
	}
	public int getCdTipoRecursoProva(){
		return this.cdTipoRecursoProva;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAluno: " +  getCdAluno();
		valueToString += ", cdTipoRecursoProva: " +  getCdTipoRecursoProva();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AlunoRecursoProva(getCdAluno(),
			getCdTipoRecursoProva());
	}

}