package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class QuadroVagas {

	private int cdQuadroVagas;
	private int cdInstituicao;
	private int cdPeriodoLetivo;
	private GregorianCalendar dtCadastro;
	private int stQuadroVagas;
	private String txtObservacao;

	public QuadroVagas(){ }

	public QuadroVagas(int cdQuadroVagas,
			int cdInstituicao,
			int cdPeriodoLetivo,
			GregorianCalendar dtCadastro,
			int stQuadroVagas,
			String txtObservacao){
		setCdQuadroVagas(cdQuadroVagas);
		setCdInstituicao(cdInstituicao);
		setCdPeriodoLetivo(cdPeriodoLetivo);
		setDtCadastro(dtCadastro);
		setStQuadroVagas(stQuadroVagas);
		setTxtObservacao(txtObservacao);
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
	public void setCdPeriodoLetivo(int cdPeriodoLetivo){
		this.cdPeriodoLetivo=cdPeriodoLetivo;
	}
	public int getCdPeriodoLetivo(){
		return this.cdPeriodoLetivo;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setStQuadroVagas(int stQuadroVagas){
		this.stQuadroVagas=stQuadroVagas;
	}
	public int getStQuadroVagas(){
		return this.stQuadroVagas;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuadroVagas: " +  getCdQuadroVagas();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdPeriodoLetivo: " +  getCdPeriodoLetivo();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stQuadroVagas: " +  getStQuadroVagas();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new QuadroVagas(getCdQuadroVagas(),
			getCdInstituicao(),
			getCdPeriodoLetivo(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getStQuadroVagas(),
			getTxtObservacao());
	}

}
