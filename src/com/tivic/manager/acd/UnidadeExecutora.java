package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class UnidadeExecutora {

	private int cdUnidadeExecutora;
	private GregorianCalendar dtCriacao;
	private int cdDirigente;
	private int tpUnidadeExecutora;

	public UnidadeExecutora(){ }

	public UnidadeExecutora(int cdUnidadeExecutora,
			GregorianCalendar dtCriacao,
			int cdDirigente,
			int tpUnidadeExecutora){
		setCdUnidadeExecutora(cdUnidadeExecutora);
		setDtCriacao(dtCriacao);
		setCdDirigente(cdDirigente);
		setTpUnidadeExecutora(tpUnidadeExecutora);
	}
	public void setCdUnidadeExecutora(int cdUnidadeExecutora){
		this.cdUnidadeExecutora=cdUnidadeExecutora;
	}
	public int getCdUnidadeExecutora(){
		return this.cdUnidadeExecutora;
	}
	public void setDtCriacao(GregorianCalendar dtCriacao){
		this.dtCriacao=dtCriacao;
	}
	public GregorianCalendar getDtCriacao(){
		return this.dtCriacao;
	}
	public void setCdDirigente(int cdDirigente){
		this.cdDirigente=cdDirigente;
	}
	public int getCdDirigente(){
		return this.cdDirigente;
	}
	public void setTpUnidadeExecutora(int tpUnidadeExecutora){
		this.tpUnidadeExecutora=tpUnidadeExecutora;
	}
	public int getTpUnidadeExecutora(){
		return this.tpUnidadeExecutora;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdUnidadeExecutora: " +  getCdUnidadeExecutora();
		valueToString += ", dtCriacao: " +  sol.util.Util.formatDateTime(getDtCriacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdDirigente: " +  getCdDirigente();
		valueToString += ", tpUnidadeExecutora: " +  getTpUnidadeExecutora();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UnidadeExecutora(getCdUnidadeExecutora(),
			getDtCriacao()==null ? null : (GregorianCalendar)getDtCriacao().clone(),
			getCdDirigente(),
			getTpUnidadeExecutora());
	}

}