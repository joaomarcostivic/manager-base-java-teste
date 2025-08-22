package com.tivic.manager.acd;

public class ProgramaRepasse {

	private int cdPrograma;
	private int cdFonteReceita;
	private int cdCategoriaEconomicaReceita;
	private int cdCentroCustoReceita;

	public ProgramaRepasse(){ }

	public ProgramaRepasse(int cdPrograma,
			int cdFonteReceita,
			int cdCategoriaEconomicaReceita,
			int cdCentroCustoReceita){
		setCdPrograma(cdPrograma);
		setCdFonteReceita(cdFonteReceita);
		setCdCategoriaEconomicaReceita(cdCategoriaEconomicaReceita);
		setCdCentroCustoReceita(cdCentroCustoReceita);
	}
	public void setCdPrograma(int cdPrograma){
		this.cdPrograma=cdPrograma;
	}
	public int getCdPrograma(){
		return this.cdPrograma;
	}
	public void setCdFonteReceita(int cdFonteReceita){
		this.cdFonteReceita=cdFonteReceita;
	}
	public int getCdFonteReceita(){
		return this.cdFonteReceita;
	}
	public void setCdCategoriaEconomicaReceita(int cdCategoriaEconomicaReceita){
		this.cdCategoriaEconomicaReceita=cdCategoriaEconomicaReceita;
	}
	public int getCdCategoriaEconomicaReceita(){
		return this.cdCategoriaEconomicaReceita;
	}
	public void setCdCentroCustoReceita(int cdCentroCustoReceita){
		this.cdCentroCustoReceita=cdCentroCustoReceita;
	}
	public int getCdCentroCustoReceita(){
		return this.cdCentroCustoReceita;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPrograma: " +  getCdPrograma();
		valueToString += ", cdFonteReceita: " +  getCdFonteReceita();
		valueToString += ", cdCategoriaEconomicaReceita: " +  getCdCategoriaEconomicaReceita();
		valueToString += ", cdCentroCustoReceita: " +  getCdCentroCustoReceita();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProgramaRepasse(getCdPrograma(),
			getCdFonteReceita(),
			getCdCategoriaEconomicaReceita(),
			getCdCentroCustoReceita());
	}

}