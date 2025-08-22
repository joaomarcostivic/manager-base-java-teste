package com.tivic.manager.acd;

public class ProgramaRepasseProgramacao {

	private int cdPrograma;
	private int cdFonteReceita;
	private int cdCentroCustoDespesa;
	private int cdCategoriaEconomicaDespesa;
	private Double prProgramacaoMin;
	private Double prProgramacaoMax;
	private int cdUnidadeExecutora;
	private int cdInstituicao;
	private int cdExercicio;

	public ProgramaRepasseProgramacao(){ }

	public ProgramaRepasseProgramacao(int cdPrograma,
			int cdFonteReceita,
			int cdCentroCustoDespesa,
			int cdCategoriaEconomicaDespesa,
			Double prProgramacaoMin,
			Double prProgramacaoMax,
			int cdUnidadeExecutora,
			int cdInstituicao,
			int cdExercicio){
		setCdPrograma(cdPrograma);
		setCdFonteReceita(cdFonteReceita);
		setCdCentroCustoDespesa(cdCentroCustoDespesa);
		setCdCategoriaEconomicaDespesa(cdCategoriaEconomicaDespesa);
		setPrProgramacaoMin(prProgramacaoMin);
		setPrProgramacaoMax(prProgramacaoMax);
		setCdUnidadeExecutora(cdUnidadeExecutora);
		setCdInstituicao(cdInstituicao);
		setCdExercicio(cdExercicio);
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
	public void setCdCentroCustoDespesa(int cdCentroCustoDespesa){
		this.cdCentroCustoDespesa=cdCentroCustoDespesa;
	}
	public int getCdCentroCustoDespesa(){
		return this.cdCentroCustoDespesa;
	}
	public void setCdCategoriaEconomicaDespesa(int cdCategoriaEconomicaDespesa){
		this.cdCategoriaEconomicaDespesa=cdCategoriaEconomicaDespesa;
	}
	public int getCdCategoriaEconomicaDespesa(){
		return this.cdCategoriaEconomicaDespesa;
	}
	public void setPrProgramacaoMin(Double prProgramacaoMin){
		this.prProgramacaoMin=prProgramacaoMin;
	}
	public Double getPrProgramacaoMin(){
		return this.prProgramacaoMin;
	}
	public void setPrProgramacaoMax(Double prProgramacaoMax){
		this.prProgramacaoMax=prProgramacaoMax;
	}
	public Double getPrProgramacaoMax(){
		return this.prProgramacaoMax;
	}
	public void setCdUnidadeExecutora(int cdUnidadeExecutora){
		this.cdUnidadeExecutora=cdUnidadeExecutora;
	}
	public int getCdUnidadeExecutora(){
		return this.cdUnidadeExecutora;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdExercicio(int cdExercicio){
		this.cdExercicio=cdExercicio;
	}
	public int getCdExercicio(){
		return this.cdExercicio;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPrograma: " +  getCdPrograma();
		valueToString += ", cdFonteReceita: " +  getCdFonteReceita();
		valueToString += ", cdCentroCustoDespesa: " +  getCdCentroCustoDespesa();
		valueToString += ", cdCategoriaEconomicaDespesa: " +  getCdCategoriaEconomicaDespesa();
		valueToString += ", prProgramacaoMin: " +  getPrProgramacaoMin();
		valueToString += ", prProgramacaoMax: " +  getPrProgramacaoMax();
		valueToString += ", cdUnidadeExecutora: " +  getCdUnidadeExecutora();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdExercicio: " +  getCdExercicio();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProgramaRepasseProgramacao(getCdPrograma(),
			getCdFonteReceita(),
			getCdCentroCustoDespesa(),
			getCdCategoriaEconomicaDespesa(),
			getPrProgramacaoMin(),
			getPrProgramacaoMax(),
			getCdUnidadeExecutora(),
			getCdInstituicao(),
			getCdExercicio());
	}

}