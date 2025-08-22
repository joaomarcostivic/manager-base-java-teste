package com.tivic.manager.acd;

import java.util.GregorianCalendar;

public class ProgramaContaFinanceira {

	private int cdConta;
	private int cdPrograma;
	private int cdUnidadeExecutora;
	private GregorianCalendar dtVinculacao;
	private int stVinculacao;

	public ProgramaContaFinanceira(){ }

	public ProgramaContaFinanceira(int cdConta,
			int cdPrograma,
			int cdUnidadeExecutora,
			GregorianCalendar dtVinculacao,
			int stVinculacao){
		setCdConta(cdConta);
		setCdPrograma(cdPrograma);
		setCdUnidadeExecutora(cdUnidadeExecutora);
		setDtVinculacao(dtVinculacao);
		setStVinculacao(stVinculacao);
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdPrograma(int cdPrograma){
		this.cdPrograma=cdPrograma;
	}
	public int getCdPrograma(){
		return this.cdPrograma;
	}
	public void setCdUnidadeExecutora(int cdUnidadeExecutora){
		this.cdUnidadeExecutora=cdUnidadeExecutora;
	}
	public int getCdUnidadeExecutora(){
		return this.cdUnidadeExecutora;
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
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdPrograma: " +  getCdPrograma();
		valueToString += ", cdUnidadeExecutora: " +  getCdUnidadeExecutora();
		valueToString += ", dtVinculacao: " +  sol.util.Util.formatDateTime(getDtVinculacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stVinculacao: " +  getStVinculacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProgramaContaFinanceira(getCdConta(),
			getCdPrograma(),
			getCdUnidadeExecutora(),
			getDtVinculacao()==null ? null : (GregorianCalendar)getDtVinculacao().clone(),
			getStVinculacao());
	}

}