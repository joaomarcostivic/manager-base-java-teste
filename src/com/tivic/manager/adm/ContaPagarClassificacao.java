package com.tivic.manager.adm;

public class ContaPagarClassificacao {

	private int cdContaPagarClassificacao;
	private int cdContaPagar;
	private int cdCategoriaEconomica;
	private int cdCentroCusto;
	private int cdLancamento;
	private int cdContaDebito;
	private int cdContaCredito;
	private float vlContaClassificacao;

	public ContaPagarClassificacao(int cdContaPagarClassificacao,
			int cdContaPagar,
			int cdCategoriaEconomica,
			int cdCentroCusto,
			int cdLancamento,
			int cdContaDebito,
			int cdContaCredito,
			float vlContaClassificacao){
		setCdContaPagarClassificacao(cdContaPagarClassificacao);
		setCdContaPagar(cdContaPagar);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCentroCusto(cdCentroCusto);
		setCdLancamento(cdLancamento);
		setCdContaDebito(cdContaDebito);
		setCdContaCredito(cdContaCredito);
		setVlContaClassificacao(vlContaClassificacao);
	}
	public void setCdContaPagarClassificacao(int cdContaPagarClassificacao){
		this.cdContaPagarClassificacao=cdContaPagarClassificacao;
	}
	public int getCdContaPagarClassificacao(){
		return this.cdContaPagarClassificacao;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
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
	public void setVlContaClassificacao(float vlContaClassificacao){
		this.vlContaClassificacao=vlContaClassificacao;
	}
	public float getVlContaClassificacao(){
		return this.vlContaClassificacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPagarClassificacao: " +  getCdContaPagarClassificacao();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdLancamento: " +  getCdLancamento();
		valueToString += ", cdContaDebito: " +  getCdContaDebito();
		valueToString += ", cdContaCredito: " +  getCdContaCredito();
		valueToString += ", vlContaClassificacao: " +  getVlContaClassificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPagarClassificacao(getCdContaPagarClassificacao(),
			getCdContaPagar(),
			getCdCategoriaEconomica(),
			getCdCentroCusto(),
			getCdLancamento(),
			getCdContaDebito(),
			getCdContaCredito(),
			getVlContaClassificacao());
	}

}
