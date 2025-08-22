package com.tivic.manager.adm;

public class ClienteProduto {

	private int cdEmpresa;
	private int cdProdutoServico;
	private int cdPessoa;
	private float qtLimite;
	private float vlLimite;
	private int cdTabelaPreco;
	private int tpAplicacaoTabela;

	public ClienteProduto(int cdEmpresa,
			int cdProdutoServico,
			int cdPessoa,
			float qtLimite,
			float vlLimite,
			int cdTabelaPreco,
			int tpAplicacaoTabela){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setCdPessoa(cdPessoa);
		setQtLimite(qtLimite);
		setVlLimite(vlLimite);
		setCdTabelaPreco(cdTabelaPreco);
		setTpAplicacaoTabela(tpAplicacaoTabela);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setQtLimite(float qtLimite){
		this.qtLimite=qtLimite;
	}
	public float getQtLimite(){
		return this.qtLimite;
	}
	public void setVlLimite(float vlLimite){
		this.vlLimite=vlLimite;
	}
	public float getVlLimite(){
		return this.vlLimite;
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setTpAplicacaoTabela(int tpAplicacaoTabela){
		this.tpAplicacaoTabela=tpAplicacaoTabela;
	}
	public int getTpAplicacaoTabela(){
		return this.tpAplicacaoTabela;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", qtLimite: " +  getQtLimite();
		valueToString += ", vlLimite: " +  getVlLimite();
		valueToString += ", cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", tpAplicacaoTabela: " +  getTpAplicacaoTabela();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClienteProduto(getCdEmpresa(),
			getCdProdutoServico(),
			getCdPessoa(),
			getQtLimite(),
			getVlLimite(),
			getCdTabelaPreco(),
			getTpAplicacaoTabela());
	}

}
