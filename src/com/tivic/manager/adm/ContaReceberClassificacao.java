package com.tivic.manager.adm;

public class ContaReceberClassificacao {

	private int cdContaReceberClassificacao;
	private int cdContaReceber;
	private int cdCategoriaEconomica;
	private int cdCentroCusto;
	private int cdLancamento;
	private int cdContaDebito;
	private int cdContaCredito;
	private float vlContaClassificacao;

	public ContaReceberClassificacao(int cdContaReceberClassificacao,
			int cdContaReceber,
			int cdCategoriaEconomica,
			int cdCentroCusto,
			int cdLancamento,
			int cdContaDebito,
			int cdContaCredito,
			float vlContaClassificacao){
		setCdContaReceberClassificacao(cdContaReceberClassificacao);
		setCdContaReceber(cdContaReceber);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setCdCentroCusto(cdCentroCusto);
		setCdLancamento(cdLancamento);
		setCdContaDebito(cdContaDebito);
		setCdContaCredito(cdContaCredito);
		setVlContaClassificacao(vlContaClassificacao);
	}
	public void setCdContaReceberClassificacao(int cdContaReceberClassificacao){
		this.cdContaReceberClassificacao=cdContaReceberClassificacao;
	}
	public int getCdContaReceberClassificacao(){
		return this.cdContaReceberClassificacao;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
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
		valueToString += "cdContaReceberClassificacao: " +  getCdContaReceberClassificacao();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdLancamento: " +  getCdLancamento();
		valueToString += ", cdContaDebito: " +  getCdContaDebito();
		valueToString += ", cdContaCredito: " +  getCdContaCredito();
		valueToString += ", vlContaClassificacao: " +  getVlContaClassificacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceberClassificacao(getCdContaReceberClassificacao(),
			getCdContaReceber(),
			getCdCategoriaEconomica(),
			getCdCentroCusto(),
			getCdLancamento(),
			getCdContaDebito(),
			getCdContaCredito(),
			getVlContaClassificacao());
	}

}
