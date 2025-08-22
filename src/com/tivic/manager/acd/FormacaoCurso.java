package com.tivic.manager.acd;

public class FormacaoCurso {

	private int cdFormacaoCurso;
	private int cdCursoSuperior;
	private String nmCurso;
	private String idOcde;
	private int tpGrauAcademico;
	private int cdFormacaoAreaConhecimento;

	public FormacaoCurso(){ }

	public FormacaoCurso(int cdFormacaoCurso,
			int cdCursoSuperior,
			String nmCurso,
			String idOcde,
			int tpGrauAcademico,
			int cdFormacaoAreaConhecimento){
		setCdFormacaoCurso(cdFormacaoCurso);
		setCdCursoSuperior(cdCursoSuperior);
		setNmCurso(nmCurso);
		setIdOcde(idOcde);
		setTpGrauAcademico(tpGrauAcademico);
		setCdFormacaoAreaConhecimento(cdFormacaoAreaConhecimento);
	}
	public void setCdFormacaoCurso(int cdFormacaoCurso){
		this.cdFormacaoCurso=cdFormacaoCurso;
	}
	public int getCdFormacaoCurso(){
		return this.cdFormacaoCurso;
	}
	public void setCdCursoSuperior(int cdCursoSuperior){
		this.cdCursoSuperior=cdCursoSuperior;
	}
	public int getCdCursoSuperior(){
		return this.cdCursoSuperior;
	}
	public void setNmCurso(String nmCurso){
		this.nmCurso=nmCurso;
	}
	public String getNmCurso(){
		return this.nmCurso;
	}
	public void setIdOcde(String idOcde){
		this.idOcde=idOcde;
	}
	public String getIdOcde(){
		return this.idOcde;
	}
	public void setTpGrauAcademico(int tpGrauAcademico){
		this.tpGrauAcademico=tpGrauAcademico;
	}
	public int getTpGrauAcademico(){
		return this.tpGrauAcademico;
	}
	public void setCdFormacaoAreaConhecimento(int cdFormacaoAreaConhecimento){
		this.cdFormacaoAreaConhecimento=cdFormacaoAreaConhecimento;
	}
	public int getCdFormacaoAreaConhecimento(){
		return this.cdFormacaoAreaConhecimento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormacaoCurso: " +  getCdFormacaoCurso();
		valueToString += ", cdCursoSuperior: " +  getCdCursoSuperior();
		valueToString += ", nmCurso: " +  getNmCurso();
		valueToString += ", idOcde: " +  getIdOcde();
		valueToString += ", tpGrauAcademico: " +  getTpGrauAcademico();
		valueToString += ", cdFormacaoAreaConhecimento: " +  getCdFormacaoAreaConhecimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormacaoCurso(getCdFormacaoCurso(),
			getCdCursoSuperior(),
			getNmCurso(),
			getIdOcde(),
			getTpGrauAcademico(),
			getCdFormacaoAreaConhecimento());
	}

}