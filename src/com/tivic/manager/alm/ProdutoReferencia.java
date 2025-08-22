package com.tivic.manager.alm;

import java.util.GregorianCalendar;

public class ProdutoReferencia {

	private int cdReferencia;
	private int cdProdutoServico;
	private int cdEmpresa;
	private String nmReferencia;
	private String idReferencia;
	private GregorianCalendar dtValidade;
	private GregorianCalendar dtChegada;
	private int tpReferencia;
	private int stReferencia;
	private int cdReferenciaSuperior;
	private int nrNivel;
	private String idReduzido;
	private int cdLocalArmazenamento;

	public ProdutoReferencia(int cdReferencia,
			int cdProdutoServico,
			int cdEmpresa,
			String nmReferencia,
			String idReferencia,
			GregorianCalendar dtValidade,
			GregorianCalendar dtChegada,
			int tpReferencia,
			int stReferencia,
			int cdReferenciaSuperior,
			int nrNivel,
			String idReduzido,
			int cdLocalArmazenamento){
		setCdReferencia(cdReferencia);
		setCdProdutoServico(cdProdutoServico);
		setCdEmpresa(cdEmpresa);
		setNmReferencia(nmReferencia);
		setIdReferencia(idReferencia);
		setDtValidade(dtValidade);
		setDtChegada(dtChegada);
		setTpReferencia(tpReferencia);
		setStReferencia(stReferencia);
		setCdReferenciaSuperior(cdReferenciaSuperior);
		setNrNivel(nrNivel);
		setIdReduzido(idReduzido);
		setCdLocalArmazenamento(cdLocalArmazenamento);
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNmReferencia(String nmReferencia){
		this.nmReferencia=nmReferencia;
	}
	public String getNmReferencia(){
		return this.nmReferencia;
	}
	public void setIdReferencia(String idReferencia){
		this.idReferencia=idReferencia;
	}
	public String getIdReferencia(){
		return this.idReferencia;
	}
	public void setDtValidade(GregorianCalendar dtValidade){
		this.dtValidade=dtValidade;
	}
	public GregorianCalendar getDtValidade(){
		return this.dtValidade;
	}
	public void setDtChegada(GregorianCalendar dtChegada){
		this.dtChegada=dtChegada;
	}
	public GregorianCalendar getDtChegada(){
		return this.dtChegada;
	}
	public void setTpReferencia(int tpReferencia){
		this.tpReferencia=tpReferencia;
	}
	public int getTpReferencia(){
		return this.tpReferencia;
	}
	public void setStReferencia(int stReferencia){
		this.stReferencia=stReferencia;
	}
	public int getStReferencia(){
		return this.stReferencia;
	}
	public void setCdReferenciaSuperior(int cdReferenciaSuperior){
		this.cdReferenciaSuperior=cdReferenciaSuperior;
	}
	public int getCdReferenciaSuperior(){
		return this.cdReferenciaSuperior;
	}
	public void setNrNivel(int nrNivel){
		this.nrNivel=nrNivel;
	}
	public int getNrNivel(){
		return this.nrNivel;
	}
	public void setIdReduzido(String idReduzido){
		this.idReduzido=idReduzido;
	}
	public String getIdReduzido(){
		return this.idReduzido;
	}
	public void setCdLocalArmazenamento(int cdLocalArmazenamento){
		this.cdLocalArmazenamento=cdLocalArmazenamento;
	}
	public int getCdLocalArmazenamento(){
		return this.cdLocalArmazenamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nmReferencia: " +  getNmReferencia();
		valueToString += ", idReferencia: " +  getIdReferencia();
		valueToString += ", dtValidade: " +  sol.util.Util.formatDateTime(getDtValidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtChegada: " +  sol.util.Util.formatDateTime(getDtChegada(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpReferencia: " +  getTpReferencia();
		valueToString += ", stReferencia: " +  getStReferencia();
		valueToString += ", cdReferenciaSuperior: " +  getCdReferenciaSuperior();
		valueToString += ", nrNivel: " +  getNrNivel();
		valueToString += ", idReduzido: " +  getIdReduzido();
		valueToString += ", cdLocalArmazenamento: " +  getCdLocalArmazenamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoReferencia(getCdReferencia(),
			getCdProdutoServico(),
			getCdEmpresa(),
			getNmReferencia(),
			getIdReferencia(),
			getDtValidade()==null ? null : (GregorianCalendar)getDtValidade().clone(),
			getDtChegada()==null ? null : (GregorianCalendar)getDtChegada().clone(),
			getTpReferencia(),
			getStReferencia(),
			getCdReferenciaSuperior(),
			getNrNivel(),
			getIdReduzido(),
			getCdLocalArmazenamento());
	}

}
