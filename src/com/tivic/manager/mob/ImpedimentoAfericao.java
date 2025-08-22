package com.tivic.manager.mob;

public class ImpedimentoAfericao {

	private int cdImpedimentoAfericao;
	private String nmImpedimento;
	private int tpReferente;

	public ImpedimentoAfericao(){ }

	public ImpedimentoAfericao(int cdImpedimentoAfericao,
			String nmImpedimento,
			int tpReferente){
		setCdImpedimentoAfericao(cdImpedimentoAfericao);
		setNmImpedimento(nmImpedimento);
		setTpReferente(tpReferente);
	}
	public void setCdImpedimentoAfericao(int cdImpedimentoAfericao){
		this.cdImpedimentoAfericao=cdImpedimentoAfericao;
	}
	public int getCdImpedimentoAfericao(){
		return this.cdImpedimentoAfericao;
	}
	public void setNmImpedimento(String nmImpedimento){
		this.nmImpedimento=nmImpedimento;
	}
	public String getNmImpedimento(){
		return this.nmImpedimento;
	}
	public void setTpReferente(int tpReferente){
		this.tpReferente=tpReferente;
	}
	public int getTpReferente(){
		return this.tpReferente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdImpedimentoAfericao: " +  getCdImpedimentoAfericao();
		valueToString += ", nmImpedimento: " +  getNmImpedimento();
		valueToString += ", tpReferente: " +  getTpReferente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ImpedimentoAfericao(getCdImpedimentoAfericao(),
			getNmImpedimento(),
			getTpReferente());
	}

}