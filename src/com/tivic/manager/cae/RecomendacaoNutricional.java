package com.tivic.manager.cae;

public class RecomendacaoNutricional {

	private int cdRecomendacaoNutricional;
	private int idadeInicio;
	private int idadeFim;
	private Double vlKcalMin;
	private Double vlKcalMax;
	private Double vlChoMin;
	private Double vlChoMax;
	private Double vlPtnMin;
	private Double vlPtnMax;
	private Double vlLipMin;
	private Double vlLipMax;
	private Double vlFibrasMin;
	private Double vlFibrasMax;
	private Double vlVitAMin;
	private Double vlVitAMax;
	private Double vlVitCMin;
	private Double vlVitCMax;
	private Double vlCaMin;
	private Double vlCaMax;
	private Double vlFeMin;
	private Double vlFeMax;
	private Double vlMgMin;
	private Double vlMgMax;
	private Double vlZnMin;
	private Double vlZnMax;
	private String nmRecomendacaoNutricional;
	
	public RecomendacaoNutricional(){ }

	public RecomendacaoNutricional(int cdRecomendacaoNutricional,
			int idadeInicio,
			int idadeFim,
			Double vlKcalMin,
			Double vlKcalMax,
			Double vlChoMin,
			Double vlChoMax,
			Double vlPtnMin,
			Double vlPtnMax,
			Double vlLipMin,
			Double vlLipMax,
			Double vlFibrasMin,
			Double vlFibrasMax,
			Double vlVitAMin,
			Double vlVitAMax,
			Double vlVitCMin,
			Double vlVitCMax,
			Double vlCaMin,
			Double vlCaMax,
			Double vlFeMin,
			Double vlFeMax,
			Double vlMgMin,
			Double vlMgMax,
			Double vlZnMin,
			Double vlZnMax,
			String nmRecomendacaoNutricional){
		setCdRecomendacaoNutricional(cdRecomendacaoNutricional);
		setIdadeInicio(idadeInicio);
		setIdadeFim(idadeFim);
		setVlKcalMin(vlKcalMin);
		setVlKcalMax(vlKcalMax);
		setVlChoMin(vlChoMin);
		setVlChoMax(vlChoMax);
		setVlPtnMin(vlPtnMin);
		setVlPtnMax(vlPtnMax);
		setVlLipMin(vlLipMin);
		setVlLipMax(vlLipMax);
		setVlFibrasMin(vlFibrasMin);
		setVlFibrasMax(vlFibrasMax);
		setVlVitAMin(vlVitAMin);
		setVlVitAMax(vlVitAMax);
		setVlVitCMin(vlVitCMin);
		setVlVitCMax(vlVitCMax);
		setVlCaMin(vlCaMin);
		setVlCaMax(vlCaMax);
		setVlFeMin(vlFeMin);
		setVlFeMax(vlFeMax);
		setVlMgMin(vlMgMin);
		setVlMgMax(vlMgMax);
		setVlZnMin(vlZnMin);
		setVlZnMax(vlZnMax);
		setNmRecomendacaoNutricional(nmRecomendacaoNutricional);
	}
	public void setCdRecomendacaoNutricional(int cdRecomendacaoNutricional){
		this.cdRecomendacaoNutricional=cdRecomendacaoNutricional;
	}
	public int getCdRecomendacaoNutricional(){
		return this.cdRecomendacaoNutricional;
	}
	public void setIdadeInicio(int idadeInicio){
		this.idadeInicio=idadeInicio;
	}
	public int getIdadeInicio(){
		return this.idadeInicio;
	}
	public void setIdadeFim(int idadeFim){
		this.idadeFim=idadeFim;
	}
	public int getIdadeFim(){
		return this.idadeFim;
	}
	public void setVlKcalMin(Double vlKcalMin){
		this.vlKcalMin=vlKcalMin;
	}
	public Double getVlKcalMin(){
		return this.vlKcalMin;
	}
	public void setVlKcalMax(Double vlKcalMax){
		this.vlKcalMax=vlKcalMax;
	}
	public Double getVlKcalMax(){
		return this.vlKcalMax;
	}
	public void setVlChoMin(Double vlChoMin){
		this.vlChoMin=vlChoMin;
	}
	public Double getVlChoMin(){
		return this.vlChoMin;
	}
	public void setVlChoMax(Double vlChoMax){
		this.vlChoMax=vlChoMax;
	}
	public Double getVlChoMax(){
		return this.vlChoMax;
	}
	public void setVlPtnMin(Double vlPtnMin){
		this.vlPtnMin=vlPtnMin;
	}
	public Double getVlPtnMin(){
		return this.vlPtnMin;
	}
	public void setVlPtnMax(Double vlPtnMax){
		this.vlPtnMax=vlPtnMax;
	}
	public Double getVlPtnMax(){
		return this.vlPtnMax;
	}
	public void setVlLipMin(Double vlLipMin){
		this.vlLipMin=vlLipMin;
	}
	public Double getVlLipMin(){
		return this.vlLipMin;
	}
	public void setVlLipMax(Double vlLipMax){
		this.vlLipMax=vlLipMax;
	}
	public Double getVlLipMax(){
		return this.vlLipMax;
	}
	public void setVlFibrasMin(Double vlFibrasMin){
		this.vlFibrasMin=vlFibrasMin;
	}
	public Double getVlFibrasMin(){
		return this.vlFibrasMin;
	}
	public void setVlFibrasMax(Double vlFibrasMax){
		this.vlFibrasMax=vlFibrasMax;
	}
	public Double getVlFibrasMax(){
		return this.vlFibrasMax;
	}
	public void setVlVitAMin(Double vlVitAMin){
		this.vlVitAMin=vlVitAMin;
	}
	public Double getVlVitAMin(){
		return this.vlVitAMin;
	}
	public void setVlVitAMax(Double vlVitAMax){
		this.vlVitAMax=vlVitAMax;
	}
	public Double getVlVitAMax(){
		return this.vlVitAMax;
	}
	public void setVlVitCMin(Double vlVitCMin){
		this.vlVitCMin=vlVitCMin;
	}
	public Double getVlVitCMin(){
		return this.vlVitCMin;
	}
	public void setVlVitCMax(Double vlVitCMax){
		this.vlVitCMax=vlVitCMax;
	}
	public Double getVlVitCMax(){
		return this.vlVitCMax;
	}
	public void setVlCaMin(Double vlCaMin){
		this.vlCaMin=vlCaMin;
	}
	public Double getVlCaMin(){
		return this.vlCaMin;
	}
	public void setVlCaMax(Double vlCaMax){
		this.vlCaMax=vlCaMax;
	}
	public Double getVlCaMax(){
		return this.vlCaMax;
	}
	public void setVlFeMin(Double vlFeMin){
		this.vlFeMin=vlFeMin;
	}
	public Double getVlFeMin(){
		return this.vlFeMin;
	}
	public void setVlFeMax(Double vlFeMax){
		this.vlFeMax=vlFeMax;
	}
	public Double getVlFeMax(){
		return this.vlFeMax;
	}
	public void setVlMgMin(Double vlMgMin){
		this.vlMgMin=vlMgMin;
	}
	public Double getVlMgMin(){
		return this.vlMgMin;
	}
	public void setVlMgMax(Double vlMgMax){
		this.vlMgMax=vlMgMax;
	}
	public Double getVlMgMax(){
		return this.vlMgMax;
	}
	public void setVlZnMin(Double vlZnMin){
		this.vlZnMin=vlZnMin;
	}
	public Double getVlZnMin(){
		return this.vlZnMin;
	}
	public void setVlZnMax(Double vlZnMax){
		this.vlZnMax=vlZnMax;
	}
	public Double getVlZnMax(){
		return this.vlZnMax;
	}
	public void setNmRecomendacaoNutricional(String nmRecomendacaoNutricional){
		this.nmRecomendacaoNutricional=nmRecomendacaoNutricional;
	}
	public String getNmRecomendacaoNutricional(){
		return this.nmRecomendacaoNutricional;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRecomendacaoNutricional: " +  getCdRecomendacaoNutricional();
		valueToString += ", idadeInicio: " +  getIdadeInicio();
		valueToString += ", idadeFim: " +  getIdadeFim();
		valueToString += ", vlKcalMin: " +  getVlKcalMin();
		valueToString += ", vlKcalMax: " +  getVlKcalMax();
		valueToString += ", vlChoMin: " +  getVlChoMin();
		valueToString += ", vlChoMax: " +  getVlChoMax();
		valueToString += ", vlPtnMin: " +  getVlPtnMin();
		valueToString += ", vlPtnMax: " +  getVlPtnMax();
		valueToString += ", vlLipMin: " +  getVlLipMin();
		valueToString += ", vlLipMax: " +  getVlLipMax();
		valueToString += ", vlFibrasMin: " +  getVlFibrasMin();
		valueToString += ", vlFibrasMax: " +  getVlFibrasMax();
		valueToString += ", vlVitAMin: " +  getVlVitAMin();
		valueToString += ", vlVitAMax: " +  getVlVitAMax();
		valueToString += ", vlVitCMin: " +  getVlVitCMin();
		valueToString += ", vlVitCMax: " +  getVlVitCMax();
		valueToString += ", vlCaMin: " +  getVlCaMin();
		valueToString += ", vlCaMax: " +  getVlCaMax();
		valueToString += ", vlFeMin: " +  getVlFeMin();
		valueToString += ", vlFeMax: " +  getVlFeMax();
		valueToString += ", vlMgMin: " +  getVlMgMin();
		valueToString += ", vlMgMax: " +  getVlMgMax();
		valueToString += ", vlZnMin: " +  getVlZnMin();
		valueToString += ", vlZnMax: " +  getVlZnMax();
		valueToString += ", nmRecomendacaoNutricional: " +  getNmRecomendacaoNutricional();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new RecomendacaoNutricional(getCdRecomendacaoNutricional(),
			getIdadeInicio(),
			getIdadeFim(),
			getVlKcalMin(),
			getVlKcalMax(),
			getVlChoMin(),
			getVlChoMax(),
			getVlPtnMin(),
			getVlPtnMax(),
			getVlLipMin(),
			getVlLipMax(),
			getVlFibrasMin(),
			getVlFibrasMax(),
			getVlVitAMin(),
			getVlVitAMax(),
			getVlVitCMin(),
			getVlVitCMax(),
			getVlCaMin(),
			getVlCaMax(),
			getVlFeMin(),
			getVlFeMax(),
			getVlMgMin(),
			getVlMgMax(),
			getVlZnMin(),
			getVlZnMax(),
			getNmRecomendacaoNutricional());
	}

}