package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class RegiaoAtuacao {

	private int cdRegiao;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdVinculo;
	private int cdContaCarteira;
	private int cdConta;
	private String nmRegiaoAtuacao;
	private GregorianCalendar dtInicioAtuacao;
	private GregorianCalendar dtFinalAtuacao;
	private int lgAtivo;
	private String idRegiaoAtuacao;

	public RegiaoAtuacao(int cdRegiao,
			int cdEmpresa,
			int cdPessoa,
			int cdVinculo,
			int cdContaCarteira,
			int cdConta,
			String nmRegiaoAtuacao,
			GregorianCalendar dtInicioAtuacao,
			GregorianCalendar dtFinalAtuacao,
			int lgAtivo,
			String idRegiaoAtuacao){
		setCdRegiao(cdRegiao);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdVinculo(cdVinculo);
		setCdContaCarteira(cdContaCarteira);
		setCdConta(cdConta);
		setNmRegiaoAtuacao(nmRegiaoAtuacao);
		setDtInicioAtuacao(dtInicioAtuacao);
		setDtFinalAtuacao(dtFinalAtuacao);
		setLgAtivo(lgAtivo);
		setIdRegiaoAtuacao(idRegiaoAtuacao);
	}
	public void setCdRegiao(int cdRegiao){
		this.cdRegiao=cdRegiao;
	}
	public int getCdRegiao(){
		return this.cdRegiao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdVinculo(int cdVinculo){
		this.cdVinculo=cdVinculo;
	}
	public int getCdVinculo(){
		return this.cdVinculo;
	}
	public void setCdContaCarteira(int cdContaCarteira){
		this.cdContaCarteira=cdContaCarteira;
	}
	public int getCdContaCarteira(){
		return this.cdContaCarteira;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setNmRegiaoAtuacao(String nmRegiaoAtuacao){
		this.nmRegiaoAtuacao=nmRegiaoAtuacao;
	}
	public String getNmRegiaoAtuacao(){
		return this.nmRegiaoAtuacao;
	}
	public void setDtInicioAtuacao(GregorianCalendar dtInicioAtuacao){
		this.dtInicioAtuacao=dtInicioAtuacao;
	}
	public GregorianCalendar getDtInicioAtuacao(){
		return this.dtInicioAtuacao;
	}
	public void setDtFinalAtuacao(GregorianCalendar dtFinalAtuacao){
		this.dtFinalAtuacao=dtFinalAtuacao;
	}
	public GregorianCalendar getDtFinalAtuacao(){
		return this.dtFinalAtuacao;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setIdRegiaoAtuacao(String idRegiaoAtuacao){
		this.idRegiaoAtuacao=idRegiaoAtuacao;
	}
	public String getIdRegiaoAtuacao(){
		return this.idRegiaoAtuacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegiao: " +  getCdRegiao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdVinculo: " +  getCdVinculo();
		valueToString += ", cdContaCarteira: " +  getCdContaCarteira();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", nmRegiaoAtuacao: " +  getNmRegiaoAtuacao();
		valueToString += ", dtInicioAtuacao: " +  sol.util.Util.formatDateTime(getDtInicioAtuacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinalAtuacao: " +  sol.util.Util.formatDateTime(getDtFinalAtuacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", idRegiaoAtuacao: " +  getIdRegiaoAtuacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RegiaoAtuacao(getCdRegiao(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdVinculo(),
			getCdContaCarteira(),
			getCdConta(),
			getNmRegiaoAtuacao(),
			getDtInicioAtuacao()==null ? null : (GregorianCalendar)getDtInicioAtuacao().clone(),
			getDtFinalAtuacao()==null ? null : (GregorianCalendar)getDtFinalAtuacao().clone(),
			getLgAtivo(),
			getIdRegiaoAtuacao());
	}

}
