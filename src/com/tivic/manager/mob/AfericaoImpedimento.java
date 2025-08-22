package com.tivic.manager.mob;

public class AfericaoImpedimento {

	private int cdAfericaoImpedimento;
	private int cdImpedimentoAfericao;
	private int cdAfericaoCatraca;

	public AfericaoImpedimento(){ }

	public AfericaoImpedimento(int cdAfericaoImpedimento,
			int cdImpedimentoAfericao,
			int cdAfericaoCatraca){
		setCdAfericaoImpedimento(cdAfericaoImpedimento);
		setCdImpedimentoAfericao(cdImpedimentoAfericao);
		setCdAfericaoCatraca(cdAfericaoCatraca);
	}
	public void setCdAfericaoImpedimento(int cdAfericaoImpedimento){
		this.cdAfericaoImpedimento=cdAfericaoImpedimento;
	}
	public int getCdAfericaoImpedimento(){
		return this.cdAfericaoImpedimento;
	}
	public void setCdImpedimentoAfericao(int cdImpedimentoAfericao){
		this.cdImpedimentoAfericao=cdImpedimentoAfericao;
	}
	public int getCdImpedimentoAfericao(){
		return this.cdImpedimentoAfericao;
	}
	public void setCdAfericaoCatraca(int cdAfericaoCatraca){
		this.cdAfericaoCatraca=cdAfericaoCatraca;
	}
	public int getCdAfericaoCatraca(){
		return this.cdAfericaoCatraca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAfericaoImpedimento: " +  getCdAfericaoImpedimento();
		valueToString += ", cdImpedimentoAfericao: " +  getCdImpedimentoAfericao();
		valueToString += ", cdAfericaoCatraca: " +  getCdAfericaoCatraca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AfericaoImpedimento(getCdAfericaoImpedimento(),
			getCdImpedimentoAfericao(),
			getCdAfericaoCatraca());
	}

}