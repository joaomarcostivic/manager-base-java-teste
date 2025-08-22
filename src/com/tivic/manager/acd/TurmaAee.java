package com.tivic.manager.acd;

public class TurmaAee {

	private int cdAtendimentoEspecializado;
	private int cdTurma;

	public TurmaAee(){ }

	public TurmaAee(int cdAtendimentoEspecializado,
			int cdTurma){
		setCdAtendimentoEspecializado(cdAtendimentoEspecializado);
		setCdTurma(cdTurma);
	}
	public void setCdAtendimentoEspecializado(int cdAtendimentoEspecializado){
		this.cdAtendimentoEspecializado=cdAtendimentoEspecializado;
	}
	public int getCdAtendimentoEspecializado(){
		return this.cdAtendimentoEspecializado;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtendimentoEspecializado: " +  getCdAtendimentoEspecializado();
		valueToString += ", cdTurma: " +  getCdTurma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TurmaAee(getCdAtendimentoEspecializado(),
			getCdTurma());
	}

}
