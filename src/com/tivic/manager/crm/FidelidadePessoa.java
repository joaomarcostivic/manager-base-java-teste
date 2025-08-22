package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class FidelidadePessoa {

	private int cdPessoa;
	private int cdFidelidade;
	private GregorianCalendar dtCadastro;
	private int stCadastro;
	private String txtObservacao;
	private String nrMatricula;

	public FidelidadePessoa(int cdPessoa,
			int cdFidelidade,
			GregorianCalendar dtCadastro,
			int stCadastro,
			String txtObservacao,
			String nrMatricula){
		setCdPessoa(cdPessoa);
		setCdFidelidade(cdFidelidade);
		setDtCadastro(dtCadastro);
		setStCadastro(stCadastro);
		setTxtObservacao(txtObservacao);
		setNrMatricula(nrMatricula);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdFidelidade(int cdFidelidade){
		this.cdFidelidade=cdFidelidade;
	}
	public int getCdFidelidade(){
		return this.cdFidelidade;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setStCadastro(int stCadastro){
		this.stCadastro=stCadastro;
	}
	public int getStCadastro(){
		return this.stCadastro;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNrMatricula(String nrMatricula){
		this.nrMatricula=nrMatricula;
	}
	public String getNrMatricula(){
		return this.nrMatricula;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdFidelidade: " +  getCdFidelidade();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stCadastro: " +  getStCadastro();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nrMatricula: " +  getNrMatricula();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FidelidadePessoa(getCdPessoa(),
			getCdFidelidade(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getStCadastro(),
			getTxtObservacao(),
			getNrMatricula());
	}

}
