package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaReferencia {

	private int cdPessoaReferencia;
	private int cdPessoa;
	private String nmPessoaReferencia;
	private String nrTelefone;
	private GregorianCalendar dtInformacao;
	private int tpReferencia;

	public PessoaReferencia(int cdPessoaReferencia,
			int cdPessoa,
			String nmPessoaReferencia,
			String nrTelefone,
			GregorianCalendar dtInformacao,
			int tpReferencia){
		setCdPessoaReferencia(cdPessoaReferencia);
		setCdPessoa(cdPessoa);
		setNmPessoaReferencia(nmPessoaReferencia);
		setNrTelefone(nrTelefone);
		setDtInformacao(dtInformacao);
		setTpReferencia(tpReferencia);
	}
	public void setCdPessoaReferencia(int cdPessoaReferencia){
		this.cdPessoaReferencia=cdPessoaReferencia;
	}
	public int getCdPessoaReferencia(){
		return this.cdPessoaReferencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNmPessoaReferencia(String nmPessoaReferencia){
		this.nmPessoaReferencia=nmPessoaReferencia;
	}
	public String getNmPessoaReferencia(){
		return this.nmPessoaReferencia;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setDtInformacao(GregorianCalendar dtInformacao){
		this.dtInformacao=dtInformacao;
	}
	public GregorianCalendar getDtInformacao(){
		return this.dtInformacao;
	}
	public void setTpReferencia(int tpReferencia){
		this.tpReferencia=tpReferencia;
	}
	public int getTpReferencia(){
		return this.tpReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoaReferencia: " +  getCdPessoaReferencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nmPessoaReferencia: " +  getNmPessoaReferencia();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", dtInformacao: " +  sol.util.Util.formatDateTime(getDtInformacao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpReferencia: " +  getTpReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaReferencia(getCdPessoaReferencia(),
			getCdPessoa(),
			getNmPessoaReferencia(),
			getNrTelefone(),
			getDtInformacao()==null ? null : (GregorianCalendar)getDtInformacao().clone(),
			getTpReferencia());
	}

}
