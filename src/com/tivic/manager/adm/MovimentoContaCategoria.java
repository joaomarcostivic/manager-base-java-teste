package com.tivic.manager.adm;

public class MovimentoContaCategoria {

	private int cdConta;
	private int cdMovimentoConta;
	private int cdCategoriaEconomica;
	private double vlMovimentoCategoria;
	private int cdMovimentoContaCategoria;
	private int cdContaPagar;
	private int cdContaReceber;
	private int tpMovimento;
	private int cdCentroCusto;

	public MovimentoContaCategoria(int cdConta,
			int cdMovimentoConta,
			int cdCategoriaEconomica,
			double vlMovimentoCategoria,
			int cdMovimentoContaCategoria,
			int cdContaPagar,
			int cdContaReceber,
			int tpMovimento,
			int cdCentroCusto){
		setCdConta(cdConta);
		setCdMovimentoConta(cdMovimentoConta);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setVlMovimentoCategoria(vlMovimentoCategoria);
		setCdMovimentoContaCategoria(cdMovimentoContaCategoria);
		setCdContaPagar(cdContaPagar);
		setCdContaReceber(cdContaReceber);
		setTpMovimento(tpMovimento);
		setCdCentroCusto(cdCentroCusto);
	}
	
	public MovimentoContaCategoria(){}
	
	public void setCdConta(int cdConta){
		this.cdConta=cdConta;
	}
	public int getCdConta(){
		return this.cdConta;
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
	public void setVlMovimentoCategoria(Double vlMovimentoCategoria){
		this.vlMovimentoCategoria=vlMovimentoCategoria;
	}
	public Double getVlMovimentoCategoria(){
		return this.vlMovimentoCategoria;
	}
	public void setCdMovimentoContaCategoria(int cdMovimentoContaCategoria){
		this.cdMovimentoContaCategoria=cdMovimentoContaCategoria;
	}
	public int getCdMovimentoContaCategoria(){
		return this.cdMovimentoContaCategoria;
	}
	public void setCdContaPagar(int cdContaPagar){
		this.cdContaPagar=cdContaPagar;
	}
	public int getCdContaPagar(){
		return this.cdContaPagar;
	}
	public void setCdContaReceber(int cdContaReceber){
		this.cdContaReceber=cdContaReceber;
	}
	public int getCdContaReceber(){
		return this.cdContaReceber;
	}
	public void setTpMovimento(int tpMovimento){
		this.tpMovimento=tpMovimento;
	}
	public int getTpMovimento(){
		return this.tpMovimento;
	}
	public int getCdCentroCusto() {
		return cdCentroCusto;
	}
	public void setCdCentroCusto(int cdCentroCusto) {
		this.cdCentroCusto = cdCentroCusto;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConta: " +  getCdConta();
		valueToString += ", cdMovimentoConta: " +  getCdMovimentoConta();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", vlMovimentoCategoria: " +  getVlMovimentoCategoria();
		valueToString += ", cdMovimentoContaCategoria: " +  getCdMovimentoContaCategoria();
		valueToString += ", cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdContaReceber: " +  getCdContaReceber();
		valueToString += ", tpMovimento: " +  getTpMovimento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MovimentoContaCategoria(getCdConta(),
			getCdMovimentoConta(),
			getCdCategoriaEconomica(),
			getVlMovimentoCategoria(),
			getCdMovimentoContaCategoria(),
			getCdContaPagar(),
			getCdContaReceber(),
			getTpMovimento(),
			getCdCentroCusto());
	}

}
