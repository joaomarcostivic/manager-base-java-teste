package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class PessoaTipoDocumentacao {

	private int cdPessoa;
	private int cdTipoDocumentacao;
	private String nrDocumento;
	private String folha;
	private String livro;
	private GregorianCalendar dtEmissao;
	private int cdOrgaoEmissor;
	private int tpModelo;
	private int cdCartorio;

	public PessoaTipoDocumentacao(){ }

	public PessoaTipoDocumentacao(int cdPessoa,
			int cdTipoDocumentacao,
			String nrDocumento,
			String folha,
			String livro,
			GregorianCalendar dtEmissao,
			int cdOrgaoEmissor,
			int tpModelo,
			int cdCartorio){
		setCdPessoa(cdPessoa);
		setCdTipoDocumentacao(cdTipoDocumentacao);
		setNrDocumento(nrDocumento);
		setFolha(folha);
		setLivro(livro);
		setDtEmissao(dtEmissao);
		setCdOrgaoEmissor(cdOrgaoEmissor);
		setTpModelo(tpModelo);
		setCdCartorio(cdCartorio);
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdTipoDocumentacao(int cdTipoDocumentacao){
		this.cdTipoDocumentacao=cdTipoDocumentacao;
	}
	public int getCdTipoDocumentacao(){
		return this.cdTipoDocumentacao;
	}
	public void setNrDocumento(String nrDocumento){
		this.nrDocumento=nrDocumento;
	}
	public String getNrDocumento(){
		return this.nrDocumento;
	}
	public void setFolha(String folha){
		this.folha=folha;
	}
	public String getFolha(){
		return this.folha;
	}
	public void setLivro(String livro){
		this.livro=livro;
	}
	public String getLivro(){
		return this.livro;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}
	public void setCdOrgaoEmissor(int cdOrgaoEmissor){
		this.cdOrgaoEmissor=cdOrgaoEmissor;
	}
	public int getCdOrgaoEmissor(){
		return this.cdOrgaoEmissor;
	}
	public void setTpModelo(int tpModelo){
		this.tpModelo=tpModelo;
	}
	public int getTpModelo(){
		return this.tpModelo;
	}
	public void setCdCartorio(int cdCartorio){
		this.cdCartorio=cdCartorio;
	}
	public int getCdCartorio(){
		return this.cdCartorio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPessoa: " +  getCdPessoa();
		valueToString += ", cdTipoDocumentacao: " +  getCdTipoDocumentacao();
		valueToString += ", nrDocumento: " +  getNrDocumento();
		valueToString += ", folha: " +  getFolha();
		valueToString += ", livro: " +  getLivro();
		valueToString += ", dtEmissao: " +  sol.util.Util.formatDateTime(getDtEmissao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdOrgaoEmissor: " +  getCdOrgaoEmissor();
		valueToString += ", tpModelo: " +  getTpModelo();
		valueToString += ", cdCartorio: " +  getCdCartorio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PessoaTipoDocumentacao(getCdPessoa(),
			getCdTipoDocumentacao(),
			getNrDocumento(),
			getFolha(),
			getLivro(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone(),
			getCdOrgaoEmissor(),
			getTpModelo(),
			getCdCartorio());
	}

}