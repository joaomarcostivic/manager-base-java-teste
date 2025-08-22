package com.tivic.manager.ctb;

public class Parametro {

	private int cdParametro;
	private int cdEmpresa;
	private int cdConta;
	private int cdContaFinanceira;
	private int cdContrato;
	private int cdConvenio;
	private int cdCliente;
	private int cdFornecedor;
	private int cdEventoFinanceiro;
	private int cdSetor;
	private int cdLancamentoAuto;

	public Parametro(int cdParametro,
			int cdEmpresa,
			int cdConta,
			int cdContaFinanceira,
			int cdContrato,
			int cdConvenio,
			int cdCliente,
			int cdFornecedor,
			int cdEventoFinanceiro,
			int cdSetor,
			int cdLancamentoAuto){
		setCdParametro(cdParametro);
		setCdEmpresa(cdEmpresa);
		setCdConta(cdConta);
		setCdContaFinanceira(cdContaFinanceira);
		setCdContrato(cdContrato);
		setCdConvenio(cdConvenio);
		setCdCliente(cdCliente);
		setCdFornecedor(cdFornecedor);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setCdSetor(cdSetor);
		setCdLancamentoAuto(cdLancamentoAuto);
	}
	public void setCdParametro(int cdParametro){
		this.cdParametro=cdParametro;
	}
	public int getCdParametro(){
		return this.cdParametro;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
	}
	public void setCdContaFinanceira(int cdContaFinanceira){
		this.cdContaFinanceira=cdContaFinanceira;
	}
	public int getCdContaFinanceira(){
		return this.cdContaFinanceira;
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdConvenio(int cdConvenio){
		this.cdConvenio=cdConvenio;
	}
	public int getCdConvenio(){
		return this.cdConvenio;
	}
	public void setCdCliente(int cdCliente){
		this.cdCliente=cdCliente;
	}
	public int getCdCliente(){
		return this.cdCliente;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdLancamentoAuto(int cdLancamentoAuto){
		this.cdLancamentoAuto=cdLancamentoAuto;
	}
	public int getCdLancamentoAuto(){
		return this.cdLancamentoAuto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdParametro: " +  getCdParametro();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdConta: " +  getCdConta();
		valueToString += ", cdContaFinanceira: " +  getCdContaFinanceira();
		valueToString += ", cdContrato: " +  getCdContrato();
		valueToString += ", cdConvenio: " +  getCdConvenio();
		valueToString += ", cdCliente: " +  getCdCliente();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdLancamentoAuto: " +  getCdLancamentoAuto();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Parametro(getCdParametro(),
			getCdEmpresa(),
			getCdConta(),
			getCdContaFinanceira(),
			getCdContrato(),
			getCdConvenio(),
			getCdCliente(),
			getCdFornecedor(),
			getCdEventoFinanceiro(),
			getCdSetor(),
			getCdLancamentoAuto());
	}

}
