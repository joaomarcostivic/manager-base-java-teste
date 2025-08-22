package com.tivic.manager.sinc;

import java.util.GregorianCalendar;

public class Tabela {

	private int cdTabela;
	private String nmTabela;
	private GregorianCalendar dtInicio;
	private int stSincronizacao;
	private String nmCampoUnico;
	private String nmCampoChave;
	private String nmCampoNaoNulo;
	private int tpSincronizacao;

	public Tabela(){ }

	public Tabela(int cdTabela,
			String nmTabela,
			GregorianCalendar dtInicio,
			int stSincronizacao,
			String nmCampoUnico,
			String nmCampoChave,
			String nmCampoNaoNulo,
			int tpSincronizacao){
		setCdTabela(cdTabela);
		setNmTabela(nmTabela);
		setDtInicio(dtInicio);
		setStSincronizacao(stSincronizacao);
		setNmCampoUnico(nmCampoUnico);
		setNmCampoChave(nmCampoChave);
		setNmCampoNaoNulo(nmCampoNaoNulo);
		setTpSincronizacao(tpSincronizacao);
	}
	public void setCdTabela(int cdTabela){
		this.cdTabela=cdTabela;
	}
	public int getCdTabela(){
		return this.cdTabela;
	}
	public void setNmTabela(String nmTabela){
		this.nmTabela=nmTabela;
	}
	public String getNmTabela(){
		return this.nmTabela;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setStSincronizacao(int stSincronizacao){
		this.stSincronizacao=stSincronizacao;
	}
	public int getStSincronizacao(){
		return this.stSincronizacao;
	}
	public void setNmCampoUnico(String nmCampoUnico){
		this.nmCampoUnico=nmCampoUnico;
	}
	public String getNmCampoUnico(){
		return this.nmCampoUnico;
	}
	public void setNmCampoChave(String nmCampoChave){
		this.nmCampoChave=nmCampoChave;
	}
	public String getNmCampoChave(){
		return this.nmCampoChave;
	}
	public void setNmCampoNaoNulo(String nmCampoNaoNulo){
		this.nmCampoNaoNulo=nmCampoNaoNulo;
	}
	public String getNmCampoNaoNulo(){
		return this.nmCampoNaoNulo;
	}
	public void setTpSincronizacao(int tpSincronizacao){
		this.tpSincronizacao=tpSincronizacao;
	}
	public int getTpSincronizacao(){
		return this.tpSincronizacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabela: " +  getCdTabela();
		valueToString += ", nmTabela: " +  getNmTabela();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stSincronizacao: " +  getStSincronizacao();
		valueToString += ", nmCampoUnico: " +  getNmCampoUnico();
		valueToString += ", nmCampoChave: " +  getNmCampoChave();
		valueToString += ", nmCampoNaoNulo: " +  getNmCampoNaoNulo();
		valueToString += ", tpSincronizacao: " +  getTpSincronizacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Tabela(getCdTabela(),
			getNmTabela(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getStSincronizacao(),
			getNmCampoUnico(),
			getNmCampoChave(),
			getNmCampoNaoNulo(),
			getTpSincronizacao());
	}

}