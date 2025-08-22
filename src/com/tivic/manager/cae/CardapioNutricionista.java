package com.tivic.manager.cae;

public class CardapioNutricionista {

	private int cdCardapio;
	private int cdNutricionista;

	public CardapioNutricionista(){ }

	public CardapioNutricionista(int cdCardapio,
			int cdNutricionista){
		setCdCardapio(cdCardapio);
		setCdNutricionista(cdNutricionista);
	}
	public void setCdCardapio(int cdCardapio){
		this.cdCardapio=cdCardapio;
	}
	public int getCdCardapio(){
		return this.cdCardapio;
	}
	public void setCdNutricionista(int cdNutricionista){
		this.cdNutricionista=cdNutricionista;
	}
	public int getCdNutricionista(){
		return this.cdNutricionista;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCardapio: " +  getCdCardapio();
		valueToString += ", cdNutricionista: " +  getCdNutricionista();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CardapioNutricionista(getCdCardapio(),
			getCdNutricionista());
	}

}
