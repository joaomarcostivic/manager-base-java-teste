package com.tivic.manager.acd;

public class Plano {

	private int cdPlano;
	private String nmPlano;
	private int tpPlano;
	private int cdInstituicao;
	private int cdPeriodoLetivo;
	private int cdCurso;
	private int cdTurma;
	private int cdDisciplina;
	private int cdProfessor;
	private int stPlano;
	private int lgCompartilhado;
	private int qtCargaHoraria;

	public Plano(){ }

	public Plano(int cdPlano,
			String nmPlano,
			int tpPlano,
			int cdInstituicao,
			int cdPeriodoLetivo,
			int cdCurso,
			int cdTurma,
			int cdDisciplina,
			int cdProfessor,
			int stPlano,
			int lgCompartilhado,
			int qtCargaHoraria){
		setCdPlano(cdPlano);
		setNmPlano(nmPlano);
		setTpPlano(tpPlano);
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdCurso(cdCurso);
		setCdTurma(cdTurma);
		setCdDisciplina(cdDisciplina);
		setCdProfessor(cdProfessor);
		setStPlano(stPlano);
		setLgCompartilhado(lgCompartilhado);
		setQtCargaHoraria(qtCargaHoraria);
	}
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setNmPlano(String nmPlano){
		this.nmPlano=nmPlano;
	}
	public String getNmPlano(){
		return this.nmPlano;
	}
	public void setTpPlano(int tpPlano){
		this.tpPlano=tpPlano;
	}
	public int getTpPlano(){
		return this.tpPlano;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdDisciplina(int cdDisciplina){
		this.cdDisciplina=cdDisciplina;
	}
	public int getCdDisciplina(){
		return this.cdDisciplina;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setStPlano(int stPlano){
		this.stPlano=stPlano;
	}
	public int getStPlano(){
		return this.stPlano;
	}
	public void setLgCompartilhado(int lgCompartilhado){
		this.lgCompartilhado=lgCompartilhado;
	}
	public int getLgCompartilhado(){
		return this.lgCompartilhado;
	}
	public void setQtCargaHoraria(int qtCargaHoraria){
		this.qtCargaHoraria=qtCargaHoraria;
	}
	public int getQtCargaHoraria(){
		return this.qtCargaHoraria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlano: " +  getCdPlano();
		valueToString += ", nmPlano: " +  getNmPlano();
		valueToString += ", tpPlano: " +  getTpPlano();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdDisciplina: " +  getCdDisciplina();
		valueToString += ", cdProfessor: " +  getCdProfessor();
		valueToString += ", stPlano: " +  getStPlano();
		valueToString += ", lgCompartilhado: " +  getLgCompartilhado();
		valueToString += ", qtCargaHoraria: " +  getQtCargaHoraria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Plano(getCdPlano(),
			getNmPlano(),
			getTpPlano(),
			getCdInstituicao(),
			getCdPeriodoLetivo(),
			getCdCurso(),
			getCdTurma(),
			getCdDisciplina(),
			getCdProfessor(),
			getStPlano(),
			getLgCompartilhado(),
			getQtCargaHoraria());
	}

}