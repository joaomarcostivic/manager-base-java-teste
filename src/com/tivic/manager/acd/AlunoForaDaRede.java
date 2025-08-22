package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class AlunoForaDaRede {

	private int cdAluno;
	private int cdInstituicao;
	private int cdTurma;
	private int cdMatricula;
	private int tpSituacao;
	private GregorianCalendar dtRegistro;

	public AlunoForaDaRede() { }

	public AlunoForaDaRede(int cdAluno,
			int cdInstituicao,
			int cdTurma,
			int cdMatricula,
			int tpSituacao,
			GregorianCalendar dtRegistro) {
		setCdAluno(cdAluno);
		setCdInstituicao(cdInstituicao);
		setCdTurma(cdTurma);
		setCdMatricula(cdMatricula);
		setTpSituacao(tpSituacao);
		setDtRegistro(dtRegistro);
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdMatricula(int cdMatricula){
		this.cdMatricula=cdMatricula;
	}
	public int getCdMatricula(){
		return this.cdMatricula;
	}
	public void setTpSituacao(int tpSituacao){
		this.tpSituacao=tpSituacao;
	}
	public int getTpSituacao(){
		return this.tpSituacao;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro){
		this.dtRegistro=dtRegistro;
	}
	public GregorianCalendar getDtRegistro(){
		return this.dtRegistro;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAluno: " +  getCdAluno();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdMatricula: " +  getCdMatricula();
		valueToString += ", tpSituacao: " +  getTpSituacao();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AlunoForaDaRede(getCdAluno(),
			getCdInstituicao(),
			getCdTurma(),
			getCdMatricula(),
			getTpSituacao(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone());
	}

}