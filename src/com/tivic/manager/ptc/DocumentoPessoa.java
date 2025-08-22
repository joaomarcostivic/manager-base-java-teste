package com.tivic.manager.ptc;

public class DocumentoPessoa {

	private int cdDocumento;
	private int cdPessoa;
	String nmQualificacao;

	public DocumentoPessoa() { }
			
	public DocumentoPessoa(int cdDocumento,
			int cdPessoa, String nmQualificacao){
		setCdDocumento(cdDocumento);
		setCdPessoa(cdPessoa);
		setNmQualificacao(nmQualificacao);
		
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setNmQualificacao(String nmQualificacao){
		this.nmQualificacao=nmQualificacao;
	}
	public String getNmQualificacao(){
		return this.nmQualificacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDocumento: " +  getCdDocumento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", nmQualificacao: " +  getNmQualificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DocumentoPessoa(getCdDocumento(), getCdPessoa(), getNmQualificacao());
	}

}
