package com.tivic.manager.acd;

public class QuadroVagasCurso {

	private int cdQuadroVagas;
	private int cdInstituicao;
	private int cdCurso;
	private int tpTurno;
	private int qtTurmas;
	private int qtVagas;

	public QuadroVagasCurso(){ }

	public QuadroVagasCurso(int cdQuadroVagas,
			int cdInstituicao,
			int cdCurso,
			int tpTurno,
			int qtTurmas,
			int qtVagas){
		setCdQuadroVagas(cdQuadroVagas);
		setCdInstituicao(cdInstituicao);
		setCdCurso(cdCurso);
		setTpTurno(tpTurno);
		setQtTurmas(qtTurmas);
		setQtVagas(qtVagas);
	}
	public void setCdQuadroVagas(int cdQuadroVagas){
		this.cdQuadroVagas=cdQuadroVagas;
	}
	public int getCdQuadroVagas(){
		return this.cdQuadroVagas;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdCurso(int cdCurso){
		this.cdCurso=cdCurso;
	}
	public int getCdCurso(){
		return this.cdCurso;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setQtTurmas(int qtTurmas){
		this.qtTurmas=qtTurmas;
	}
	public int getQtTurmas(){
		return this.qtTurmas;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuadroVagas: " +  getCdQuadroVagas();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", tpTurno: " +  getTpTurno();
		valueToString += ", qtTurmas: " +  getQtTurmas();
		valueToString += ", qtVagas: " +  getQtVagas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new QuadroVagasCurso(getCdQuadroVagas(),
			getCdInstituicao(),
			getCdCurso(),
			getTpTurno(),
			getQtTurmas(),
			getQtVagas());
	}

}