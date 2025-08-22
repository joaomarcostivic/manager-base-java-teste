package com.tivic.manager.mob;

public class AitMovimentoDocumento {

	private int cdAit;
	private int cdMovimento;
	private int cdDocumento;

	public AitMovimentoDocumento() { }

	public AitMovimentoDocumento(int cdAit,
			int cdMovimento,
			int cdDocumento) {
		setCdAit(cdAit);
		setCdMovimento(cdMovimento);
		setCdDocumento(cdDocumento);
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setCdMovimento(int cdMovimento){
		this.cdMovimento=cdMovimento;
	}
	public int getCdMovimento(){
		return this.cdMovimento;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", cdMovimento: " +  getCdMovimento();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitMovimentoDocumento(getCdAit(),
			getCdMovimento(),
			getCdDocumento());
	}

}