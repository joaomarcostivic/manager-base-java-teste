package com.tivic.manager.adm;

public class CondicaoPagamentoCliente {
	private int cdCondicaoPagamentoCliente;
	private int cdCondicaoPagamento;
	private int cdEmpresa;
	private int cdPessoa;
	private int cdClassificacao;

	public CondicaoPagamentoCliente(int cdCondicaoPagamentoCliente,
			int cdCondicaoPagamento,
			int cdEmpresa,
			int cdPessoa,
			int cdClassificacao){
		setCdCondicaoPagamentoCliente(cdCondicaoPagamentoCliente);
		setCdCondicaoPagamento(cdCondicaoPagamento);
		setCdEmpresa(cdEmpresa);
		setCdPessoa(cdPessoa);
		setCdClassificacao(cdClassificacao);
	}
	public void setCdCondicaoPagamentoCliente(int cdCondicaoPagamentoCliente){
		this.cdCondicaoPagamentoCliente=cdCondicaoPagamentoCliente;
	}
	public int getCdCondicaoPagamentoCliente(){
		return this.cdCondicaoPagamentoCliente;
	}
	public void setCdCondicaoPagamento(int cdCondicaoPagamento){
		this.cdCondicaoPagamento=cdCondicaoPagamento;
	}
	public int getCdCondicaoPagamento(){
		return this.cdCondicaoPagamento;
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
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdCondicaoPagamentoCliente: " +  getCdCondicaoPagamentoCliente();
		valueToString += "cdCondicaoPagamento: " +  getCdCondicaoPagamento();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CondicaoPagamentoCliente(getCdCondicaoPagamentoCliente(),
			getCdCondicaoPagamento(),
			getCdEmpresa(),
			getCdPessoa(),
			getCdClassificacao());
	}
}
