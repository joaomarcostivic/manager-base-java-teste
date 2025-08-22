package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class ContratoPrdSrvDep {

	private int cdContrato;
	private int cdProdutoServico;
	private int cdDependente;
	private int cdDependencia;
	private GregorianCalendar dtContratacao;
	private GregorianCalendar dtPrimeiraParcela;

	public ContratoPrdSrvDep(int cdContrato,
			int cdProdutoServico,
			int cdDependente,
			int cdDependencia,
			GregorianCalendar dtContratacao,
			GregorianCalendar dtPrimeiraParcela){
		setCdContrato(cdContrato);
		setCdProdutoServico(cdProdutoServico);
		setCdDependente(cdDependente);
		setCdDependencia(cdDependencia);
		setDtContratacao(dtContratacao);
		setDtPrimeiraParcela(dtPrimeiraParcela);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdDependente(int cdDependente){
		this.cdDependente=cdDependente;
	}
	public int getCdDependente(){
		return this.cdDependente;
	}
	public void setCdDependencia(int cdDependencia){
		this.cdDependencia=cdDependencia;
	}
	public int getCdDependencia(){
		return this.cdDependencia;
	}
	public void setDtContratacao(GregorianCalendar dtContratacao){
		this.dtContratacao=dtContratacao;
	}
	public GregorianCalendar getDtContratacao(){
		return this.dtContratacao;
	}
	public void setDtPrimeiraParcela(GregorianCalendar dtPrimeiraParcela){
		this.dtPrimeiraParcela=dtPrimeiraParcela;
	}
	public GregorianCalendar getDtPrimeiraParcela(){
		return this.dtPrimeiraParcela;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdDependente: " +  getCdDependente();
		valueToString += ", cdDependencia: " +  getCdDependencia();
		valueToString += ", dtContratacao: " +  sol.util.Util.formatDateTime(getDtContratacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtPrimeiraParcela: " +  sol.util.Util.formatDateTime(getDtPrimeiraParcela(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContratoPrdSrvDep(getCdContrato(),
			getCdProdutoServico(),
			getCdDependente(),
			getCdDependencia(),
			getDtContratacao()==null ? null : (GregorianCalendar)getDtContratacao().clone(),
			getDtPrimeiraParcela()==null ? null : (GregorianCalendar)getDtPrimeiraParcela().clone());
	}

}
