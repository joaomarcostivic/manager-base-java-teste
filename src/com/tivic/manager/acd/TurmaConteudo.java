package com.tivic.manager.acd;

public class TurmaConteudo {

	private int cdTurma;
	private int cdConteudo;

	public TurmaConteudo(){ }

	public TurmaConteudo(int cdTurma,
			int cdConteudo){
		setCdTurma(cdTurma);
		setCdConteudo(cdConteudo);
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdConteudo(int cdConteudo){
		this.cdConteudo=cdConteudo;
	}
	public int getCdConteudo(){
		return this.cdConteudo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTurma: " +  getCdTurma();
		valueToString += ", cdConteudo: " +  getCdConteudo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TurmaConteudo(getCdTurma(),
			getCdConteudo());
	}

}
