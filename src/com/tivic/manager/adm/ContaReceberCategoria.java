package com.tivic.manager.adm;

public class ContaReceberCategoria {

	private int cdContaReceber;
	private int cdCategoriaEconomica;
	private double vlContaCategoria;
	private int cdCentroCusto;
	private int cdContaReceberCategoria;

	public ContaReceberCategoria(){ }

	public ContaReceberCategoria(int cdContaReceber,
			int cdCategoriaEconomica,
			double vlContaCategoria,
			int cdCentroCusto,
			int cdContaReceberCategoria){
		setCdContaReceber(cdContaReceber);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setVlContaCategoria(vlContaCategoria);
		setCdCentroCusto(cdCentroCusto);
		setCdContaReceberCategoria(cdContaReceberCategoria);
	}
	
	public ContaReceberCategoria(int cdContaReceber,
			int cdCategoriaEconomica,
			double vlContaCategoria,
			int cdCentroCusto)	{
		setCdContaReceber(cdContaReceber);
		setCdCategoriaEconomica(cdCategoriaEconomica);
		setVlContaCategoria(vlContaCategoria);
		setCdCentroCusto(cdCentroCusto);
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
	public void setCdContaReceberCategoria(int cdContaReceberCategoria){
		this.cdContaReceberCategoria=cdContaReceberCategoria;
	}
	public int getCdContaReceberCategoria(){
		return this.cdContaReceberCategoria;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContaReceber: " +  getCdContaReceber();
		valueToString += ", cdCategoriaEconomica: " +  getCdCategoriaEconomica();
		valueToString += ", vlContaCategoria: " +  getVlContaCategoria();
		valueToString += ", cdCentroCusto: " +  getCdCentroCusto();
		valueToString += ", cdContaReceberCategoria: " +  getCdContaReceberCategoria();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ContaReceberCategoria(getCdContaReceber(),
			getCdCategoriaEconomica(),
			getVlContaCategoria(),
			getCdCentroCusto(),
			getCdContaReceberCategoria());
	}

}