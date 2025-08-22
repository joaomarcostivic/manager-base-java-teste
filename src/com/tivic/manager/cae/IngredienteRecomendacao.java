package com.tivic.manager.cae;

public class IngredienteRecomendacao {

	private int cdIngrediente;
	private int cdRecomendacaoNutricional;
	private Double vlPerCapta;

	public IngredienteRecomendacao() { }

	public IngredienteRecomendacao(int cdIngrediente,
			int cdRecomendacaoNutricional,
			Double vlPerCapta) {
		setCdIngrediente(cdIngrediente);
		setCdRecomendacaoNutricional(cdRecomendacaoNutricional);
		setVlPerCapta(vlPerCapta);
	}
	public void setCdIngrediente(int cdIngrediente){
		this.cdIngrediente=cdIngrediente;
	}
	public int getCdIngrediente(){
		return this.cdIngrediente;
	}
	public void setCdRecomendacaoNutricional(int cdRecomendacaoNutricional){
		this.cdRecomendacaoNutricional=cdRecomendacaoNutricional;
	}
	public int getCdRecomendacaoNutricional(){
		return this.cdRecomendacaoNutricional;
	}
	public void setVlPerCapta(Double vlPerCapta){
		this.vlPerCapta=vlPerCapta;
	}
	public Double getVlPerCapta(){
		return this.vlPerCapta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIngrediente: " +  getCdIngrediente();
		valueToString += ", cdRecomendacaoNutricional: " +  getCdRecomendacaoNutricional();
		valueToString += ", vlPerCapta: " +  getVlPerCapta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new IngredienteRecomendacao(getCdIngrediente(),
			getCdRecomendacaoNutricional(),
			getVlPerCapta());
	}

}