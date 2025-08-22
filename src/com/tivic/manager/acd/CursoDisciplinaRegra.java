package com.tivic.manager.acd;

public class CursoDisciplinaRegra {

	private int cdCurso;
	private int cdDisciplina;
	private int tpPermissao;

	public CursoDisciplinaRegra(){ }

	public CursoDisciplinaRegra(int cdCurso,
			int cdDisciplina,
			int tpPermissao){
		setCdCurso(cdCurso);
		setCdDisciplina(cdDisciplina);
		setTpPermissao(tpPermissao);
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setTpPermissao(int tpPermissao){
		this.tpPermissao=tpPermissao;
	}
	public int getTpPermissao(){
		return this.tpPermissao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCurso: " +  getCdCurso();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		valueToString += ", tpPermissao: " +  getTpPermissao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoDisciplinaRegra(getCdCurso(),
			getCdDisciplina(),
			getTpPermissao());
	}

}