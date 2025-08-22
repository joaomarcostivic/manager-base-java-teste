package com.tivic.manager.adm;

public class TabelaPrecoRegra {

	private int cdTabelaPreco;
	private int cdRegra;
	private int cdTabelaPrecoBase;
	private int cdProdutoServico;
	private int cdFornecedor;
	private int cdGrupo;
	private float prDesconto;
	private float prMargemLucro;
	private float prMargemMinima;
	private float prMargemMaxima;
	private int lgIncluirImpostos;
	private int lgPrecoMinimo;
	private int tpAproximacao;
	private int nrPrioridade;
	private int tpValorBase;

	public TabelaPrecoRegra(int cdTabelaPreco,
			int cdRegra,
			int cdTabelaPrecoBase,
			int cdProdutoServico,
			int cdFornecedor,
			int cdGrupo,
			float prDesconto,
			float prMargemLucro,
			float prMargemMinima,
			float prMargemMaxima,
			int lgIncluirImpostos,
			int lgPrecoMinimo,
			int tpAproximacao,
			int nrPrioridade,
			int tpValorBase){
		setCdTabelaPreco(cdTabelaPreco);
		setCdRegra(cdRegra);
		setCdTabelaPrecoBase(cdTabelaPrecoBase);
		setCdProdutoServico(cdProdutoServico);
		setCdFornecedor(cdFornecedor);
		setCdGrupo(cdGrupo);
		setPrDesconto(prDesconto);
		setPrMargemLucro(prMargemLucro);
		setPrMargemMinima(prMargemMinima);
		setPrMargemMaxima(prMargemMaxima);
		setLgIncluirImpostos(lgIncluirImpostos);
		setLgPrecoMinimo(lgPrecoMinimo);
		setTpAproximacao(tpAproximacao);
		setNrPrioridade(nrPrioridade);
		setTpValorBase(tpValorBase);
	}
	public void setCdTabelaPreco(int cdTabelaPreco){
		this.cdTabelaPreco=cdTabelaPreco;
	}
	public int getCdTabelaPreco(){
		return this.cdTabelaPreco;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setCdTabelaPrecoBase(int cdTabelaPrecoBase){
		this.cdTabelaPrecoBase=cdTabelaPrecoBase;
	}
	public int getCdTabelaPrecoBase(){
		return this.cdTabelaPrecoBase;
	}
	public void setCdProdutoServico(int cdProdutoServico){
		this.cdProdutoServico=cdProdutoServico;
	}
	public int getCdProdutoServico(){
		return this.cdProdutoServico;
	}
	public void setCdFornecedor(int cdFornecedor){
		this.cdFornecedor=cdFornecedor;
	}
	public int getCdFornecedor(){
		return this.cdFornecedor;
	}
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setPrDesconto(float prDesconto){
		this.prDesconto=prDesconto;
	}
	public float getPrDesconto(){
		return this.prDesconto;
	}
	public void setPrMargemLucro(float prMargemLucro){
		this.prMargemLucro=prMargemLucro;
	}
	public float getPrMargemLucro(){
		return this.prMargemLucro;
	}
	public void setPrMargemMinima(float prMargemMinima){
		this.prMargemMinima=prMargemMinima;
	}
	public float getPrMargemMinima(){
		return this.prMargemMinima;
	}
	public void setPrMargemMaxima(float prMargemMaxima){
		this.prMargemMaxima=prMargemMaxima;
	}
	public float getPrMargemMaxima(){
		return this.prMargemMaxima;
	}
	public void setLgIncluirImpostos(int lgIncluirImpostos){
		this.lgIncluirImpostos=lgIncluirImpostos;
	}
	public int getLgIncluirImpostos(){
		return this.lgIncluirImpostos;
	}
	public void setLgPrecoMinimo(int lgPrecoMinimo){
		this.lgPrecoMinimo=lgPrecoMinimo;
	}
	public int getLgPrecoMinimo(){
		return this.lgPrecoMinimo;
	}
	public void setTpAproximacao(int tpAproximacao){
		this.tpAproximacao=tpAproximacao;
	}
	public int getTpAproximacao(){
		return this.tpAproximacao;
	}
	public void setNrPrioridade(int nrPrioridade){
		this.nrPrioridade=nrPrioridade;
	}
	public int getNrPrioridade(){
		return this.nrPrioridade;
	}
	public void setTpValorBase(int tpValorBase){
		this.tpValorBase=tpValorBase;
	}
	public int getTpValorBase(){
		return this.tpValorBase;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTabelaPreco: " +  getCdTabelaPreco();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", cdTabelaPrecoBase: " +  getCdTabelaPrecoBase();
		valueToString += ", cdProdutoServico: " +  getCdProdutoServico();
		valueToString += ", cdFornecedor: " +  getCdFornecedor();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", prDesconto: " +  getPrDesconto();
		valueToString += ", prMargemLucro: " +  getPrMargemLucro();
		valueToString += ", prMargemMinima: " +  getPrMargemMinima();
		valueToString += ", prMargemMaxima: " +  getPrMargemMaxima();
		valueToString += ", lgIncluirImpostos: " +  getLgIncluirImpostos();
		valueToString += ", lgPrecoMinimo: " +  getLgPrecoMinimo();
		valueToString += ", tpAproximacao: " +  getTpAproximacao();
		valueToString += ", nrPrioridade: " +  getNrPrioridade();
		valueToString += ", tpValorBase: " +  getTpValorBase();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new TabelaPrecoRegra(getCdTabelaPreco(),
			getCdRegra(),
			getCdTabelaPrecoBase(),
			getCdProdutoServico(),
			getCdFornecedor(),
			getCdGrupo(),
			getPrDesconto(),
			getPrMargemLucro(),
			getPrMargemMinima(),
			getPrMargemMaxima(),
			getLgIncluirImpostos(),
			getLgPrecoMinimo(),
			getTpAproximacao(),
			getNrPrioridade(),
			getTpValorBase());
	}

}
