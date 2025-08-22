package com.tivic.manager.adm;

public class ContaPagarCategoria {

	private int cdContaPagar;
	private int cdCategoriaEconomica;
	private double vlContaCategoria;
	private int cdCentroCusto;
	private int cdContaPagarCategoria;

	public ContaPagarCategoria(){ }
	
	public ContaPagarCategoria(int cdContaPagar,
			int cdCategoriaEconomica,
			double vlContaCategoria, 
			int cdCentroCusto){
		setCdContaPagar(cdContaPagar);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setVlContaCategoria(vlContaCategoria);
		setCdCentroCusto(cdCentroCusto);
		setCdContaPagarCategoria(0);
	}
	
	public ContaPagarCategoria(int cdContaPagar,
			int cdCategoriaEconomica,
			double vlContaCategoria,
			int cdCentroCusto,
			int cdContaPagarCategoria){
		setCdContaPagar(cdContaPagar);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setVlContaCategoria(vlContaCategoria);
		setCdCentroCusto(cdCentroCusto);
		setCdContaPagarCategoria(cdContaPagarCategoria);
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
	public void setVlContaCategoria(Double vlContaCategoria){
		this.vlContaCategoria=vlContaCategoria;
	}
	public Double getVlContaCategoria(){
		return this.vlContaCategoria;
	}
	public void setCdCentroCusto(int cdCentroCusto){
		this.cdCentroCusto=cdCentroCusto;
	}
	public int getCdCentroCusto(){
		return this.cdCentroCusto;
	}
	public void setCdContaPagarCategoria(int cdContaPagarCategoria){
		this.cdContaPagarCategoria=cdContaPagarCategoria;
	}
	public int getCdContaPagarCategoria(){
		return this.cdContaPagarCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaPagar: " +  getCdContaPagar();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", vlContaCategoria: " +  getVlContaCategoria();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdContaPagarCategoria: " +  getCdContaPagarCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaPagarCategoria(getCdContaPagar(),
			getCdCategoriaEconomica(),
			getVlContaCategoria(),
			getCdCentroCusto(),
			getCdContaPagarCategoria());
	}

}
