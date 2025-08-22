package com.tivic.manager.ptc;

import java.util.GregorianCalendar;

public class WorkflowRegra {

	private int cdRegra;
	private String nmRegra;
	private int stRegra;
	private GregorianCalendar dtCadastro;
	private String idRegra;
	private String txtDescricao;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;

	public WorkflowRegra(){ }

	public WorkflowRegra(int cdRegra,
			String nmRegra,
			int stRegra,
			GregorianCalendar dtCadastro,
			String idRegra,
			String txtDescricao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal){
		setCdRegra(cdRegra);
		setNmRegra(nmRegra);
		setStRegra(stRegra);
		setDtCadastro(dtCadastro);
		setIdRegra(idRegra);
		setTxtDescricao(txtDescricao);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setNmRegra(String nmRegra){
		this.nmRegra=nmRegra;
	}
	public String getNmRegra(){
		return this.nmRegra;
	}
	public void setStRegra(int stRegra){
		this.stRegra=stRegra;
	}
	public int getStRegra(){
		return this.stRegra;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setIdRegra(String idRegra){
		this.idRegra=idRegra;
	}
	public String getIdRegra(){
		return this.idRegra;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegra: " +  getCdRegra();
		valueToString += ", nmRegra: " +  getNmRegra();
		valueToString += ", stRegra: " +  getStRegra();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", idRegra: " +  getIdRegra();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowRegra(getCdRegra(),
			getNmRegra(),
			getStRegra(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getIdRegra(),
			getTxtDescricao(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone());
	}

}
