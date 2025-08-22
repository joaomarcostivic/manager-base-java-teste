package com.tivic.manager.adm;

public class MovimentoClassificacao {

	private int cdMovimentoClassificacao;
	private int cdContaFinanceira;
	private int cdMovimentoConta;
	private int cdCategoriaEconomica;
	private int cdCentroCusto;
	private int cdLancamento;
	private int cdContaDebito;
	private int cdContaCredito;
	private float vlMovimentoClassificacao;
	private int cdMovimentoContaCategoria;

	public MovimentoClassificacao(int cdMovimentoClassificacao,
			int cdContaFinanceira,
			int cdMovimentoConta,
			int cdCategoriaEconomica,
			int cdCentroCusto,
			int cdLancamento,
			int cdContaDebito,
			int cdContaCredito,
			float vlMovimentoClassificacao,
			int cdMovimentoContaCategoria){
		setCdMovimentoClassificacao(cdMovimentoClassificacao);
		setCdContaFinanceira(cdContaFinanceira);
		setCdMovimentoConta(cdMovimentoConta);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCentroCusto(cdCentroCusto);
		setCdLancamento(cdLancamento);
		setCdContaDebito(cdContaDebito);
		setCdContaCredito(cdContaCredito);
		setVlMovimentoClassificacao(vlMovimentoClassificacao);
		setCdMovimentoContaCategoria(cdMovimentoContaCategoria);
	}
	public void setCdMovimentoClassificacao(int cdMovimentoClassificacao){
		this.cdMovimentoClassificacao=cdMovimentoClassificacao;
	}
	public int getCdMovimentoClassificacao(){
		return this.cdMovimentoClassificacao;
	}
	public void setCdContaFinanceira(int cdContaFinanceira){
		this.cdContaFinanceira=cdContaFinanceira;
	}
	public int getCdContaFinanceira(){
		return this.cdContaFinanceira;
	}
	public void setCdMovimentoConta(int cdMovimentoConta){
		this.cdMovimentoConta=cdMovimentoConta;
	}
	public int getCdMovimentoConta(){
		return this.cdMovimentoConta;
	}
	public void setCdCategoriaEconomica(int cdCategoriaEconomica){
		this.cdCategoriaEconomica=cdCategoriaEconomica;
	}
	public int getCdCategoriaEconomica(){
		return this.cdCategoriaEconomica;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setCdLancamento(int cdLancamento){
		this.cdLancamento=cdLancamento;
	}
	public int getCdLancamento(){
		return this.cdLancamento;
	}
	public void setCdContaDebito(int cdContaDebito){
		this.cdContaDebito=cdContaDebito;
	}
	public int getCdContaDebito(){
		return this.cdContaDebito;
	}
	public void setCdContaCredito(int cdContaCredito){
		this.cdContaCredito=cdContaCredito;
	}
	public int getCdContaCredito(){
		return this.cdContaCredito;
	}
	public void setVlMovimentoClassificacao(float vlMovimentoClassificacao){
		this.vlMovimentoClassificacao=vlMovimentoClassificacao;
	}
	public float getVlMovimentoClassificacao(){
		return this.vlMovimentoClassificacao;
	}
	public void setCdMovimentoContaCategoria(int cdMovimentoContaCategoria){
		this.cdMovimentoContaCategoria=cdMovimentoContaCategoria;
	}
	public int getCdMovimentoContaCategoria(){
		return this.cdMovimentoContaCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMovimentoClassificacao: " +  getCdMovimentoClassificacao();
		valueToString += ", cdContaFinanceira: " +  getCdContaFinanceira();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdLancamento: " +  getCdLancamento();
		valueToString += ", cdContaDebito: " +  getCdContaDebito();
		valueToString += ", cdContaCredito: " +  getCdContaCredito();
		valueToString += ", vlMovimentoClassificacao: " +  getVlMovimentoClassificacao();
		valueToString += ", cdMovimentoContaCategoria: " +  getCdMovimentoContaCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoClassificacao(getCdMovimentoClassificacao(),
			getCdContaFinanceira(),
			getCdMovimentoConta(),
			getCdCategoriaEconomica(),
			getCdCentroCusto(),
			getCdLancamento(),
			getCdContaDebito(),
			getCdContaCredito(),
			getVlMovimentoClassificacao(),
			getCdMovimentoContaCategoria());
	}

}
