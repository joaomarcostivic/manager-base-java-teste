package com.tivic.manager.grl;

public class ProdutoServicoParametro {

	private int cdEmpresa;
	private int cdProdutoServico;
	private int lgVerificarEstoqueNaVenda;
	private int lgBloqueiaVenda;
	private int lgPermiteDesconto;
	private int lgFazEntrega;
	private int lgNaoControlaEstoque;
	private int lgImprimeNaTabelaPreco;
	private int lgProdutoUsoConsumo;
	
	public ProdutoServicoParametro(){}
	
	public ProdutoServicoParametro(int cdEmpresa,
			int cdProdutoServico,
			int lgVerificarEstoqueNaVenda,
			int lgBloqueiaVenda,
			int lgPermiteDesconto,
			int lgFazEntrega,
			int lgNaoControlaEstoque,
			int lgImprimeNaTabelaPreco,
			int lgProdutoUsoConsumo){
		setCdEmpresa(cdEmpresa);
		setCdProdutoServico(cdProdutoServico);
		setLgVerificarEstoqueNaVenda(lgVerificarEstoqueNaVenda);
		setLgBloqueiaVenda(lgBloqueiaVenda);
		setLgPermiteDesconto(lgPermiteDesconto);
		setLgFazEntrega(lgFazEntrega);
		setLgNaoControlaEstoque(lgNaoControlaEstoque);
		setLgImprimeNaTabelaPreco(lgImprimeNaTabelaPreco);
		setLgProdutoUsoConsumo(lgProdutoUsoConsumo);
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
	public void setLgVerificarEstoqueNaVenda(int lgVerificarEstoqueNaVenda){
		this.lgVerificarEstoqueNaVenda=lgVerificarEstoqueNaVenda;
	}
	public int getLgVerificarEstoqueNaVenda(){
		return this.lgVerificarEstoqueNaVenda;
	}
	public void setLgBloqueiaVenda(int lgBloqueiaVenda){
		this.lgBloqueiaVenda=lgBloqueiaVenda;
	}
	public int getLgBloqueiaVenda(){
		return this.lgBloqueiaVenda;
	}
	public void setLgPermiteDesconto(int lgPermiteDesconto){
		this.lgPermiteDesconto=lgPermiteDesconto;
	}
	public int getLgPermiteDesconto(){
		return this.lgPermiteDesconto;
	}
	public void setLgFazEntrega(int lgFazEntrega){
		this.lgFazEntrega=lgFazEntrega;
	}
	public int getLgFazEntrega(){
		return this.lgFazEntrega;
	}
	public void setLgNaoControlaEstoque(int lgNaoControlaEstoque){
		this.lgNaoControlaEstoque=lgNaoControlaEstoque;
	}
	public int getLgNaoControlaEstoque(){
		return this.lgNaoControlaEstoque;
	}
	public void setLgImprimeNaTabelaPreco(int lgImprimeNaTabelaPreco){
		this.lgImprimeNaTabelaPreco=lgImprimeNaTabelaPreco;
	}
	public int getLgImprimeNaTabelaPreco(){
		return this.lgImprimeNaTabelaPreco;
	}
	public void setLgProdutoUsoConsumo(int lgProdutoUsoConsumo){
		this.lgProdutoUsoConsumo=lgProdutoUsoConsumo;
	}
	public int getLgProdutoUsoConsumo(){
		return this.lgProdutoUsoConsumo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", lgVerificarEstoqueNaVenda: " +  getLgVerificarEstoqueNaVenda();
		valueToString += ", lgBloqueiaVenda: " +  getLgBloqueiaVenda();
		valueToString += ", lgPermiteDesconto: " +  getLgPermiteDesconto();
		valueToString += ", lgFazEntrega: " +  getLgFazEntrega();
		valueToString += ", lgNaoControlaEstoque: " +  getLgNaoControlaEstoque();
		valueToString += ", lgImprimeNaTabelaPreco: " +  getLgImprimeNaTabelaPreco();
		valueToString += ", lgProdutoUsoConsumo: " +  getLgProdutoUsoConsumo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProdutoServicoParametro(getCdEmpresa(),
			getCdProdutoServico(),
			getLgVerificarEstoqueNaVenda(),
			getLgBloqueiaVenda(),
			getLgPermiteDesconto(),
			getLgFazEntrega(),
			getLgNaoControlaEstoque(),
			getLgImprimeNaTabelaPreco(),
			getLgProdutoUsoConsumo());
	}

}