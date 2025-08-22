package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class InstituicaoPendencia {

	private int cdInstituicaoPendencia;
	private int cdInstituicao;
	private int cdTurma;
	private int cdAluno;
	private int cdProfessor;
	private int tpRegistro;
	private int tpPendencia;
	private GregorianCalendar dtCriacao;
	private GregorianCalendar dtAtualizacao;
	private int stInstituicaoPendencia;
	private String txtInstituicaoPendencia;
	private int cdUsuario;

	public InstituicaoPendencia() { }

	public InstituicaoPendencia(int cdInstituicaoPendencia,
			int cdInstituicao,
			int cdTurma,
			int cdAluno,
			int cdProfessor,
			int tpRegistro,
			int tpPendencia,
			GregorianCalendar dtCriacao,
			GregorianCalendar dtAtualizacao,
			int stInstituicaoPendencia,
			String txtInstituicaoPendencia,
			int cdUsuario) {
		setCdInstituicaoPendencia(cdInstituicaoPendencia);
		setCdInstituicao(cdInstituicao);
		setCdTurma(cdTurma);
		setCdAluno(cdAluno);
		setCdProfessor(cdProfessor);
		setTpRegistro(tpRegistro);
		setTpPendencia(tpPendencia);
		setDtCriacao(dtCriacao);
		setDtAtualizacao(dtAtualizacao);
		setStInstituicaoPendencia(stInstituicaoPendencia);
		setTxtInstituicaoPendencia(txtInstituicaoPendencia);
		setCdUsuario(cdUsuario);
	}
	public void setCdInstituicaoPendencia(int cdInstituicaoPendencia){
		this.cdInstituicaoPendencia=cdInstituicaoPendencia;
	}
	public int getCdInstituicaoPendencia(){
		return this.cdInstituicaoPendencia;
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
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdProfessor(int cdProfessor){
		this.cdProfessor=cdProfessor;
	}
	public int getCdProfessor(){
		return this.cdProfessor;
	}
	public void setTpRegistro(int tpRegistro){
		this.tpRegistro=tpRegistro;
	}
	public int getTpRegistro(){
		return this.tpRegistro;
	}
	public void setTpPendencia(int tpPendencia){
		this.tpPendencia=tpPendencia;
	}
	public int getTpPendencia(){
		return this.tpPendencia;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setDtAtualizacao(GregorianCalendar dtAtualizacao){
		this.dtAtualizacao=dtAtualizacao;
	}
	public GregorianCalendar getDtAtualizacao(){
		return this.dtAtualizacao;
	}
	public void setStInstituicaoPendencia(int stInstituicaoPendencia){
		this.stInstituicaoPendencia=stInstituicaoPendencia;
	}
	public int getStInstituicaoPendencia(){
		return this.stInstituicaoPendencia;
	}
	public void setTxtInstituicaoPendencia(String txtInstituicaoPendencia){
		this.txtInstituicaoPendencia=txtInstituicaoPendencia;
	}
	public String getTxtInstituicaoPendencia(){
		return this.txtInstituicaoPendencia;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicaoPendencia: " +  getCdInstituicaoPendencia();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdAluno: " +  getCdAluno();
		valueToString += ", cdProfessor: " +  getCdProfessor();
		valueToString += ", tpRegistro: " +  getTpRegistro();
		valueToString += ", tpPendencia: " +  getTpPendencia();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtAtualizacao: " +  sol.util.Util.formatDateTime(getDtAtualizacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stInstituicaoPendencia: " +  getStInstituicaoPendencia();
		valueToString += ", txtInstituicaoPendencia: " +  getTxtInstituicaoPendencia();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InstituicaoPendencia(getCdInstituicaoPendencia(),
			getCdInstituicao(),
			getCdTurma(),
			getCdAluno(),
			getCdProfessor(),
			getTpRegistro(),
			getTpPendencia(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getDtAtualizacao()==null ? null : (GregorianCalendar)getDtAtualizacao().clone(),
			getStInstituicaoPendencia(),
			getTxtInstituicaoPendencia(),
			getCdUsuario());
	}

}