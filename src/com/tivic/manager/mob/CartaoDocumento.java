package com.tivic.manager.mob;

public class CartaoDocumento {

	private int cdCartaoDocumento;
	private int cdDocumento;
	private int cdCartao;

	public CartaoDocumento(){ }

	public CartaoDocumento(int cdCartaoDocumento,
			int cdDocumento,
			int cdCartao){
		setCdCartaoDocumento(cdCartaoDocumento);
		setCdDocumento(cdDocumento);
		setCdCartao(cdCartao);
	}
	public void setCdCartaoDocumento(int cdCartaoDocumento){
		this.cdCartaoDocumento=cdCartaoDocumento;
	}
	public int getCdCartaoDocumento(){
		return this.cdCartaoDocumento;
	}
	public void setCdDocumento(int cdDocumento){
		this.cdDocumento=cdDocumento;
	}
	public int getCdDocumento(){
		return this.cdDocumento;
	}
	public void setCdCartao(int cdCartao){
		this.cdCartao=cdCartao;
	}
	public int getCdCartao(){
		return this.cdCartao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCartaoDocumento: " +  getCdCartaoDocumento();
		valueToString += ", cdDocumento: " +  getCdDocumento();
		valueToString += ", cdCartao: " +  getCdCartao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new CartaoDocumento(getCdCartaoDocumento(),
			getCdDocumento(),
			getCdCartao());
	}

}