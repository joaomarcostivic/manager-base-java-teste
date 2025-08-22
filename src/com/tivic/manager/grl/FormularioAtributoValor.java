package com.tivic.manager.grl;

public class FormularioAtributoValor {

	private int cdFormularioAtributo;
	private int cdFormularioAtributoValor;
	private int cdProdutoServico;
	private int cdOpcao;
	private int cdEmpresa;
	private int cdDocumento;
	private String txtAtributoValor;
	private int cdPessoa;
	private int cdPessoaValor;
	private int cdCliente;
	private int cdArquivo;
	private int cdArquivoDocumento;

	public FormularioAtributoValor() { }

	public FormularioAtributoValor(int cdFormularioAtributo,
			int cdFormularioAtributoValor,
			int cdProdutoServico,
			int cdOpcao,
			int cdEmpresa,
			int cdDocumento,
			String txtAtributoValor,
			int cdPessoa,
			int cdPessoaValor,
			int cdCliente,
			int cdArquivo,
			int cdArquivoDocumento) {
		setCdFormularioAtributo(cdFormularioAtributo);
		setCdFormularioAtributoValor(cdFormularioAtributoValor);
		setCdProdutoServico(cdProdutoServico);
		setCdOpcao(cdOpcao);
		setCdEmpresa(cdEmpresa);
		setCdDocumento(cdDocumento);
		setTxtAtributoValor(txtAtributoValor);
		setCdPessoa(cdPessoa);
		setCdPessoaValor(cdPessoaValor);
		setCdCliente(cdCliente);
		setCdArquivo(cdArquivo);
		setCdArquivoDocumento(cdArquivoDocumento);
	}
	public void setCdFormularioAtributo(int cdFormularioAtributo){
		this.cdFormularioAtributo=cdFormularioAtributo;
	}
	public int getCdFormularioAtributo(){
		return this.cdFormularioAtributo;
	}
	public void setCdFormularioAtributoValor(int cdFormularioAtributoValor){
		this.cdFormularioAtributoValor=cdFormularioAtributoValor;
	}
	public int getCdFormularioAtributoValor(){
		return this.cdFormularioAtributoValor;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdOpcao(int cdOpcao){
		this.cdOpcao=cdOpcao;
	}
	public int getCdOpcao(){
		return this.cdOpcao;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setTxtAtributoValor(String txtAtributoValor){
		this.txtAtributoValor=txtAtributoValor;
	}
	public String getTxtAtributoValor(){
		return this.txtAtributoValor;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdPessoaValor(int cdPessoaValor){
		this.cdPessoaValor=cdPessoaValor;
	}
	public int getCdPessoaValor(){
		return this.cdPessoaValor;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdArquivo(int cdArquivo){
		this.cdArquivo=cdArquivo;
	}
	public int getCdArquivo(){
		return this.cdArquivo;
	}
	public void setCdArquivoDocumento(int cdArquivoDocumento){
		this.cdArquivoDocumento=cdArquivoDocumento;
	}
	public int getCdArquivoDocumento(){
		return this.cdArquivoDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormularioAtributo: " +  getCdFormularioAtributo();
		valueToString += ", cdFormularioAtributoValor: " +  getCdFormularioAtributoValor();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdOpcao: " +  getCdOpcao();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", txtAtributoValor: " +  getTxtAtributoValor();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdPessoaValor: " +  getCdPessoaValor();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdArquivo: " +  getCdArquivo();
		valueToString += ", cdArquivoDocumento: " +  getCdArquivoDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormularioAtributoValor(getCdFormularioAtributo(),
			getCdFormularioAtributoValor(),
			getCdProdutoServico(),
			getCdOpcao(),
			getCdEmpresa(),
			getCdDocumento(),
			getTxtAtributoValor(),
			getCdPessoa(),
			getCdPessoaValor(),
			getCdCliente(),
			getCdArquivo(),
			getCdArquivoDocumento());
	}

}