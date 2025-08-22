package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class MatriculaReserva {

	private int cdReserva;
	private String idReserva;
	private GregorianCalendar dtReserva;
	private GregorianCalendar dtValidade;
	private int stReserva;
	private int cdAluno;
	private int cdContrato;
	private String txtObservacao;
	private int cdInstituicao;
	private int cdCurso;
	private int cdTurma;
	private int cdUsuario;

	public MatriculaReserva(int cdReserva,
			String idReserva,
			GregorianCalendar dtReserva,
			GregorianCalendar dtValidade,
			int stReserva,
			int cdAluno,
			int cdContrato,
			String txtObservacao,
			int cdInstituicao,
			int cdCurso,
			int cdTurma,
			int cdUsuario){
		setCdReserva(cdReserva);
		setIdReserva(idReserva);
		setDtReserva(dtReserva);
		setDtValidade(dtValidade);
		setStReserva(stReserva);
		setCdAluno(cdAluno);
		setCdContrato(cdContrato);
		setTxtObservacao(txtObservacao);
		setCdInstituicao(cdInstituicao);
		setCdCurso(cdCurso);
		setCdTurma(cdTurma);
		setCdUsuario(cdUsuario);
	}
	public void setCdReserva(int cdReserva){
		this.cdReserva=cdReserva;
	}
	public int getCdReserva(){
		return this.cdReserva;
	}
	public void setIdReserva(String idReserva){
		this.idReserva=idReserva;
	}
	public String getIdReserva(){
		return this.idReserva;
	}
	public void setDtReserva(GregorianCalendar dtReserva){
		this.dtReserva=dtReserva;
	}
	public GregorianCalendar getDtReserva(){
		return this.dtReserva;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setStReserva(int stReserva){
		this.stReserva=stReserva;
	}
	public int getStReserva(){
		return this.stReserva;
	}
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
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
	public void setCdTurma(int cdTurma){
		this.cdTurma=cdTurma;
	}
	public int getCdTurma(){
		return this.cdTurma;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReserva: " +  getCdReserva();
		valueToString += ", idReserva: " +  getIdReserva();
		valueToString += ", dtReserva: " +  sol.util.Util.formatDateTime(getDtReserva(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stReserva: " +  getStReserva();
		valueToString += ", cdAluno: " +  getCdAluno();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdTurma: " +  getCdTurma();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MatriculaReserva(getCdReserva(),
			getIdReserva(),
			getDtReserva()==null ? null : (GregorianCalendar)getDtReserva().clone(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getStReserva(),
			getCdAluno(),
			getCdContrato(),
			getTxtObservacao(),
			getCdInstituicao(),
			getCdCurso(),
			getCdTurma(),
			getCdUsuario());
	}

}
