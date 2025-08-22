package com.tivic.manager.acd;

public class TurmaAtividadeComplementar {

	private int cdAtividadeComplementar;
	private int cdTurma;

	public TurmaAtividadeComplementar(){ }

	public TurmaAtividadeComplementar(int cdAtividadeComplementar,
			int cdTurma){
		setCdAtividadeComplementar(cdAtividadeComplementar);
		setCdTurma(cdTurma);
	}
	public void setCdAtividadeComplementar(int cdAtividadeComplementar){
		this.cdAtividadeComplementar=cdAtividadeComplementar;
	}
	public int getCdAtividadeComplementar(){
		return this.cdAtividadeComplementar;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAtividadeComplementar: " +  getCdAtividadeComplementar();
		valueToString += ", cdTurma: " +  getCdTurma();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TurmaAtividadeComplementar(getCdAtividadeComplementar(),
			getCdTurma());
	}

}
