package com.tivic.manager.grl;

public class PessoaArquivo {

	private int cdArquivo;
	private int cdPessoa;
	private String txtObservacao;
	private int cdNivelAcesso;
	private int cdSetor;

	public PessoaArquivo() { } 
	
	public PessoaArquivo(int cdArquivo,
			int cdPessoa,
			String txtObservacao,
			int cdNivelAcesso,
			int cdSetor){
		setCdArquivo(cdArquivo);
		setCdPessoa(cdPessoa);
		setTxtObservacao(txtObservacao);
		setCdNivelAcesso(cdNivelAcesso);
		setCdSetor(cdSetor);
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdNivelAcesso(int cdNivelAcesso){
		this.cdNivelAcesso=cdNivelAcesso;
	}
	public int getCdNivelAcesso(){
		return this.cdNivelAcesso;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdArquivo: " +  getCdArquivo();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdNivelAcesso: " +  getCdNivelAcesso();
		valueToString += ", cdSetor: " +  getCdSetor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaArquivo(getCdArquivo(),
			getCdPessoa(),
			getTxtObservacao(),
			getCdNivelAcesso(),
			getCdSetor());
	}

}
