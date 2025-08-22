package com.tivic.manager.adm;

public class ClienteTabelaPreco {

	private int cdClienteTabelaPreco;
	private int cdTabelaPreco;
	private int cdPessoa;
	private int cdEmpresa;
	private int cdClassificacao;
	private int cdProdutoServico;
	private int cdGrupo;

	public ClienteTabelaPreco(int cdClienteTabelaPreco,
			int cdTabelaPreco,
			int cdPessoa,
			int cdEmpresa,
			int cdClassificacao,
			int cdProdutoServico,
			int cdGrupo){
		setCdClienteTabelaPreco(cdClienteTabelaPreco);
		setCdTabelaPreco(cdTabelaPreco);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdClassificacao(cdClassificacao);
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
	}
	
	public ClienteTabelaPreco(int cdClienteTabelaPreco,
			int cdTabelaPreco,
			int cdClassificacao,
			int cdProdutoServico,
			int cdGrupo){
		setCdClienteTabelaPreco(cdClienteTabelaPreco);
		setCdTabelaPreco(cdTabelaPreco);
		setCdClassificacao(cdClassificacao);
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
	}
	
	public ClienteTabelaPreco(int cdClienteTabelaPreco,
			int cdTabelaPreco,
			int cdPessoa,
			int cdEmpresa,
			int cdProdutoServico,
			int cdGrupo){
		setCdClienteTabelaPreco(cdClienteTabelaPreco);
		setCdTabelaPreco(cdTabelaPreco);
		setCdPessoa(cdPessoa);
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdGrupo(cdGrupo);
	}
	
	
	public void setCdClienteTabelaPreco(int cdClienteTabelaPreco){
		this.cdClienteTabelaPreco=cdClienteTabelaPreco;
	}
	public int getCdClienteTabelaPreco(){
		return this.cdClienteTabelaPreco;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClienteTabelaPreco: " +  getCdClienteTabelaPreco();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdClassificacao: " +  getCdClassificacao();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClienteTabelaPreco(getCdClienteTabelaPreco(),
			getCdTabelaPreco(),
			getCdPessoa(),
			getCdEmpresa(),
			getCdClassificacao(),
			getCdProdutoServico(),
			getCdGrupo());
	}

}