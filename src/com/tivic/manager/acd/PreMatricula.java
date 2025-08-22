package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class PreMatricula {

	private int cdPreMatricula;
	private int cdInstituicaoInscricao;
	private int cdInstituicao;
	private int cdCurso;
	private int cdAluno;
	private int stPreMatricula;
	private int tpPreMatricula;
	private GregorianCalendar dtInscricao;
	private int tpProcedencia;
	private int lgIrmaoInstituicao;
	private int lgResponsavelTrabalhador;
	private String idPreMatricula;

	public PreMatricula(){ }

	public PreMatricula(int cdPreMatricula,
			int cdInstituicaoInscricao,
			int cdInstituicao,
			int cdCurso,
			int cdAluno,
			int stPreMatricula,
			int tpPreMatricula,
			GregorianCalendar dtInscricao,
			int tpProcedencia,
			int lgIrmaoInstituicao,
			int lgResponsavelTrabalhador,
			String idPreMatricula){
		setCdPreMatricula(cdPreMatricula);
		setCdInstituicaoInscricao(cdInstituicaoInscricao);
		setCdInstituicao(cdInstituicao);
		setCdCurso(cdCurso);
		setCdAluno(cdAluno);
		setStPreMatricula(stPreMatricula);
		setTpPreMatricula(tpPreMatricula);
		setDtInscricao(dtInscricao);
		setTpProcedencia(tpProcedencia);
		setLgIrmaoInstituicao(lgIrmaoInstituicao);
		setLgResponsavelTrabalhador(lgResponsavelTrabalhador);
		setIdPreMatricula(idPreMatricula);
	}
	public void setCdPreMatricula(int cdPreMatricula){
		this.cdPreMatricula=cdPreMatricula;
	}
	public int getCdPreMatricula(){
		return this.cdPreMatricula;
	}
	public void setCdInstituicaoInscricao(int cdInstituicaoInscricao){
		this.cdInstituicaoInscricao=cdInstituicaoInscricao;
	}
	public int getCdInstituicaoInscricao(){
		return this.cdInstituicaoInscricao;
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
	public void setCdAluno(int cdAluno){
		this.cdAluno=cdAluno;
	}
	public int getCdAluno(){
		return this.cdAluno;
	}
	public void setStPreMatricula(int stPreMatricula){
		this.stPreMatricula=stPreMatricula;
	}
	public int getStPreMatricula(){
		return this.stPreMatricula;
	}
	public void setTpPreMatricula(int tpPreMatricula){
		this.tpPreMatricula=tpPreMatricula;
	}
	public int getTpPreMatricula(){
		return this.tpPreMatricula;
	}
	public void setDtInscricao(GregorianCalendar dtInscricao){
		this.dtInscricao=dtInscricao;
	}
	public GregorianCalendar getDtInscricao(){
		return this.dtInscricao;
	}
	public void setTpProcedencia(int tpProcedencia){
		this.tpProcedencia=tpProcedencia;
	}
	public int getTpProcedencia(){
		return this.tpProcedencia;
	}
	public void setLgIrmaoInstituicao(int lgIrmaoInstituicao){
		this.lgIrmaoInstituicao=lgIrmaoInstituicao;
	}
	public int getLgIrmaoInstituicao(){
		return this.lgIrmaoInstituicao;
	}
	public void setLgResponsavelTrabalhador(int lgResponsavelTrabalhador){
		this.lgResponsavelTrabalhador=lgResponsavelTrabalhador;
	}
	public int getLgResponsavelTrabalhador(){
		return this.lgResponsavelTrabalhador;
	}
	public void setIdPreMatricula(String idPreMatricula){
		this.idPreMatricula=idPreMatricula;
	}
	public String getIdPreMatricula(){
		return this.idPreMatricula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPreMatricula: " +  getCdPreMatricula();
		valueToString += ", cdInstituicaoInscricao: " +  getCdInstituicaoInscricao();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdCurso: " +  getCdCurso();
		valueToString += ", cdAluno: " +  getCdAluno();
		valueToString += ", stPreMatricula: " +  getStPreMatricula();
		valueToString += ", tpPreMatricula: " +  getTpPreMatricula();
		valueToString += ", dtInscricao: " +  sol.util.Util.formatDateTime(getDtInscricao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpProcedencia: " +  getTpProcedencia();
		valueToString += ", lgIrmaoInstituicao: " +  getLgIrmaoInstituicao();
		valueToString += ", lgResponsavelTrabalhador: " +  getLgResponsavelTrabalhador();
		valueToString += ", idPreMatricula: " +  getIdPreMatricula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PreMatricula(getCdPreMatricula(),
			getCdInstituicaoInscricao(),
			getCdInstituicao(),
			getCdCurso(),
			getCdAluno(),
			getStPreMatricula(),
			getTpPreMatricula(),
			getDtInscricao()==null ? null : (GregorianCalendar)getDtInscricao().clone(),
			getTpProcedencia(),
			getLgIrmaoInstituicao(),
			getLgResponsavelTrabalhador(),
			getIdPreMatricula());
	}

}
