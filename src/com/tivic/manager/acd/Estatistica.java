package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class Estatistica {

	private int cdApuracao;
	private int cdPeriodoLetivo;
	private int cdInstituicao;
	private GregorianCalendar dtApuracao;
	private int qtTotalMatriculas;
	private int qtMatriculasZonaRural;
	private int qtMatriculasZonaUrbana;
	private int qtVagas;
	private int qtVagasZonaRural;
	private int qtVagasZonaUrbana;
	private int qtTurmas;

	public Estatistica() { }

	public Estatistica(int cdApuracao,
			int cdPeriodoLetivo,
			int cdInstituicao,
			GregorianCalendar dtApuracao,
			int qtTotalMatriculas,
			int qtMatriculasZonaRural,
			int qtMatriculasZonaUrbana,
			int qtVagas,
			int qtVagasZonaRural,
			int qtVagasZonaUrbana,
			int qtTurmas) {
		setCdApuracao(cdApuracao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setCdInstituicao(cdInstituicao);
		setDtApuracao(dtApuracao);
		setQtTotalMatriculas(qtTotalMatriculas);
		setQtMatriculasZonaRural(qtMatriculasZonaRural);
		setQtMatriculasZonaUrbana(qtMatriculasZonaUrbana);
		setQtVagas(qtVagas);
		setQtVagasZonaRural(qtVagasZonaRural);
		setQtVagasZonaUrbana(qtVagasZonaUrbana);
		setQtTurmas(qtTurmas);
	}
	public void setCdApuracao(int cdApuracao){
		this.cdApuracao=cdApuracao;
	}
	public int getCdApuracao(){
		return this.cdApuracao;
	}
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setDtApuracao(GregorianCalendar dtApuracao){
		this.dtApuracao=dtApuracao;
	}
	public GregorianCalendar getDtApuracao(){
		return this.dtApuracao;
	}
	public void setQtTotalMatriculas(int qtTotalMatriculas){
		this.qtTotalMatriculas=qtTotalMatriculas;
	}
	public int getQtTotalMatriculas(){
		return this.qtTotalMatriculas;
	}
	public void setQtMatriculasZonaRural(int qtMatriculasZonaRural){
		this.qtMatriculasZonaRural=qtMatriculasZonaRural;
	}
	public int getQtMatriculasZonaRural(){
		return this.qtMatriculasZonaRural;
	}
	public void setQtMatriculasZonaUrbana(int qtMatriculasZonaUrbana){
		this.qtMatriculasZonaUrbana=qtMatriculasZonaUrbana;
	}
	public int getQtMatriculasZonaUrbana(){
		return this.qtMatriculasZonaUrbana;
	}
	public void setQtVagas(int qtVagas){
		this.qtVagas=qtVagas;
	}
	public int getQtVagas(){
		return this.qtVagas;
	}
	public void setQtVagasZonaRural(int qtVagasZonaRural){
		this.qtVagasZonaRural=qtVagasZonaRural;
	}
	public int getQtVagasZonaRural(){
		return this.qtVagasZonaRural;
	}
	public void setQtVagasZonaUrbana(int qtVagasZonaUrbana){
		this.qtVagasZonaUrbana=qtVagasZonaUrbana;
	}
	public int getQtVagasZonaUrbana(){
		return this.qtVagasZonaUrbana;
	}
	public void setQtTurmas(int qtTurmas){
		this.qtTurmas=qtTurmas;
	}
	public int getQtTurmas(){
		return this.qtTurmas;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdApuracao: " +  getCdApuracao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", dtApuracao: " +  sol.util.Util.formatDateTime(getDtApuracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtTotalMatriculas: " +  getQtTotalMatriculas();
		valueToString += ", qtMatriculasZonaRural: " +  getQtMatriculasZonaRural();
		valueToString += ", qtMatriculasZonaUrbana: " +  getQtMatriculasZonaUrbana();
		valueToString += ", qtVagas: " +  getQtVagas();
		valueToString += ", qtVagasZonaRural: " +  getQtVagasZonaRural();
		valueToString += ", qtVagasZonaUrbana: " +  getQtVagasZonaUrbana();
		valueToString += ", qtTurmas: " +  getQtTurmas();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Estatistica(getCdApuracao(),
			getCdPeriodoLetivo(),
			getCdInstituicao(),
			getDtApuracao()==null ? null : (GregorianCalendar)getDtApuracao().clone(),
			getQtTotalMatriculas(),
			getQtMatriculasZonaRural(),
			getQtMatriculasZonaUrbana(),
			getQtVagas(),
			getQtVagasZonaRural(),
			getQtVagasZonaUrbana(),
			getQtTurmas());
	}

}