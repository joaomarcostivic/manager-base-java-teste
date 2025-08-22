package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaContaBancaria {

	private int cdContaBancaria;
	private int cdPessoa;
	private int cdBanco;
	private String nrConta;
	private String nrDv;
	private String nrAgencia;
	private int tpOperacao;
	private String nrCpfCnpj;
	private String nmTitular;
	private int stConta;
	private GregorianCalendar dtAbertura;
	private int lgPrincipal;
	private int tpConta;
	private int lgContaConjunta;

	public PessoaContaBancaria() { }

	public PessoaContaBancaria(int cdContaBancaria,
			int cdPessoa,
			int cdBanco,
			String nrConta,
			String nrDv,
			String nrAgencia,
			int tpOperacao,
			String nrCpfCnpj,
			String nmTitular,
			int stConta,
			GregorianCalendar dtAbertura,
			int lgPrincipal,
			int tpConta,
			int lgContaConjunta) {
		setCdContaBancaria(cdContaBancaria);
		setCdPessoa(cdPessoa);
		setCdBanco(cdBanco);
		setNrConta(nrConta);
		setNrDv(nrDv);
		setNrAgencia(nrAgencia);
		setTpOperacao(tpOperacao);
		setNrCpfCnpj(nrCpfCnpj);
		setNmTitular(nmTitular);
		setStConta(stConta);
		setDtAbertura(dtAbertura);
		setLgPrincipal(lgPrincipal);
		setTpConta(tpConta);
		setLgContaConjunta(lgContaConjunta);
	}
	public void setCdContaBancaria(int cdContaBancaria){
		this.cdContaBancaria=cdContaBancaria;
	}
	public int getCdContaBancaria(){
		return this.cdContaBancaria;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdBanco(int cdBanco){
		this.cdBanco=cdBanco;
	}
	public int getCdBanco(){
		return this.cdBanco;
	}
	public void setNrConta(String nrConta){
		this.nrConta=nrConta;
	}
	public String getNrConta(){
		return this.nrConta;
	}
	public void setNrDv(String nrDv){
		this.nrDv=nrDv;
	}
	public String getNrDv(){
		return this.nrDv;
	}
	public void setNrAgencia(String nrAgencia){
		this.nrAgencia=nrAgencia;
	}
	public String getNrAgencia(){
		return this.nrAgencia;
	}
	public void setTpOperacao(int tpOperacao){
		this.tpOperacao=tpOperacao;
	}
	public int getTpOperacao(){
		return this.tpOperacao;
	}
	public void setNrCpfCnpj(String nrCpfCnpj){
		this.nrCpfCnpj=nrCpfCnpj;
	}
	public String getNrCpfCnpj(){
		return this.nrCpfCnpj;
	}
	public void setNmTitular(String nmTitular){
		this.nmTitular=nmTitular;
	}
	public String getNmTitular(){
		return this.nmTitular;
	}
	public void setStConta(int stConta){
		this.stConta=stConta;
	}
	public int getStConta(){
		return this.stConta;
	}
	public void setDtAbertura(GregorianCalendar dtAbertura){
		this.dtAbertura=dtAbertura;
	}
	public GregorianCalendar getDtAbertura(){
		return this.dtAbertura;
	}
	public void setLgPrincipal(int lgPrincipal){
		this.lgPrincipal=lgPrincipal;
	}
	public int getLgPrincipal(){
		return this.lgPrincipal;
	}
	public void setTpConta(int tpConta){
		this.tpConta=tpConta;
	}
	public int getTpConta(){
		return this.tpConta;
	}
	public void setLgContaConjunta(int lgContaConjunta){
		this.lgContaConjunta=lgContaConjunta;
	}
	public int getLgContaConjunta(){
		return this.lgContaConjunta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaBancaria: " +  getCdContaBancaria();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdBanco: " +  getCdBanco();
		valueToString += ", nrConta: " +  getNrConta();
		valueToString += ", nrDv: " +  getNrDv();
		valueToString += ", nrAgencia: " +  getNrAgencia();
		valueToString += ", tpOperacao: " +  getTpOperacao();
		valueToString += ", nrCpfCnpj: " +  getNrCpfCnpj();
		valueToString += ", nmTitular: " +  getNmTitular();
		valueToString += ", stConta: " +  getStConta();
		valueToString += ", dtAbertura: " +  sol.util.Util.formatDateTime(getDtAbertura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", lgPrincipal: " +  getLgPrincipal();
		valueToString += ", tpConta: " +  getTpConta();
		valueToString += ", lgContaConjunta: " +  getLgContaConjunta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaContaBancaria(getCdContaBancaria(),
			getCdPessoa(),
			getCdBanco(),
			getNrConta(),
			getNrDv(),
			getNrAgencia(),
			getTpOperacao(),
			getNrCpfCnpj(),
			getNmTitular(),
			getStConta(),
			getDtAbertura()==null ? null : (GregorianCalendar)getDtAbertura().clone(),
			getLgPrincipal(),
			getTpConta(),
			getLgContaConjunta());
	}

}