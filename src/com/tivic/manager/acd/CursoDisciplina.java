package com.tivic.manager.acd;

public class CursoDisciplina {

	private int cdCurso;
	private int cdCursoModulo;
	private int cdDisciplina;
	private int cdMatriz;
	private int cdNucleo;
	private int qtCargaHoraria;
	private int nrOrdem;
	private int tpClassificacao;
	private int cdInstituicao;

	public CursoDisciplina(){ }

	public CursoDisciplina(int cdCurso,
			int cdCursoModulo,
			int cdDisciplina,
			int cdMatriz,
			int cdNucleo,
			int qtCargaHoraria,
			int nrOrdem,
			int tpClassificacao,
			int cdInstituicao){
		setCdCurso(cdCurso);
		setCdCursoModulo(cdCursoModulo);
		setCdDisciplina(cdDisciplina);
		setCdMatriz(cdMatriz);
		setCdNucleo(cdNucleo);
		setQtCargaHoraria(qtCargaHoraria);
		setNrOrdem(nrOrdem);
		setTpClassificacao(tpClassificacao);
		setCdInstituicao(cdInstituicao);
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdCursoModulo(int cdCursoModulo){
		this.cdCursoModulo=cdCursoModulo;
	}
	public int getCdCursoModulo(){
		return this.cdCursoModulo;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdMatriz(int cdMatriz){
		this.cdMatriz=cdMatriz;
	}
	public int getCdMatriz(){
		return this.cdMatriz;
	}
	public void setCdNucleo(int cdNucleo){
		this.cdNucleo=cdNucleo;
	}
	public int getCdNucleo(){
		return this.cdNucleo;
	}
	public void setQtCargaHoraria(int qtCargaHoraria){
		this.qtCargaHoraria=qtCargaHoraria;
	}
	public int getQtCargaHoraria(){
		return this.qtCargaHoraria;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTpClassificacao(int tpClassificacao){
		this.tpClassificacao=tpClassificacao;
	}
	public int getTpClassificacao(){
		return this.tpClassificacao;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCurso: " +  getCdCurso();
		valueToString += ", cdCursoModulo: " +  getCdCursoModulo();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		valueToString += ", cdMatriz: " +  getCdMatriz();
		valueToString += ", cdNucleo: " +  getCdNucleo();
		valueToString += ", qtCargaHoraria: " +  getQtCargaHoraria();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", tpClassificacao: " +  getTpClassificacao();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CursoDisciplina(getCdCurso(),
			getCdCursoModulo(),
			getCdDisciplina(),
			getCdMatriz(),
			getCdNucleo(),
			getQtCargaHoraria(),
			getNrOrdem(),
			getTpClassificacao(),
			getCdInstituicao());
	}

}