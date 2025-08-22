package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class UnidadeExecutoraInstituicao {

	private int cdInstituicao;
	private int cdUnidadeExecutora;
	private int cdExercicio;
	private GregorianCalendar dtVinculacao;
	private int stVinculacao;

	public UnidadeExecutoraInstituicao(){ }

	public UnidadeExecutoraInstituicao(int cdInstituicao,
			int cdUnidadeExecutora,
			int cdExercicio,
			GregorianCalendar dtVinculacao,
			int stVinculacao){
		setCdInstituicao(cdInstituicao);
		setCdUnidadeExecutora(cdUnidadeExecutora);
		setCdExercicio(cdExercicio);
		setDtVinculacao(dtVinculacao);
		setStVinculacao(stVinculacao);
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdUnidadeExecutora(int cdUnidadeExecutora){
		this.cdUnidadeExecutora=cdUnidadeExecutora;
	}
	public int getCdUnidadeExecutora(){
		return this.cdUnidadeExecutora;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public void setDtVinculacao(GregorianCalendar dtVinculacao){
		this.dtVinculacao=dtVinculacao;
	}
	public GregorianCalendar getDtVinculacao(){
		return this.dtVinculacao;
	}
	public void setStVinculacao(int stVinculacao){
		this.stVinculacao=stVinculacao;
	}
	public int getStVinculacao(){
		return this.stVinculacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdUnidadeExecutora: " +  getCdUnidadeExecutora();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		valueToString += ", dtVinculacao: " +  sol.util.Util.formatDateTime(getDtVinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stVinculacao: " +  getStVinculacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UnidadeExecutoraInstituicao(getCdInstituicao(),
			getCdUnidadeExecutora(),
			getCdExercicio(),
			getDtVinculacao()==null ? null : (GregorianCalendar)getDtVinculacao().clone(),
			getStVinculacao());
	}

}