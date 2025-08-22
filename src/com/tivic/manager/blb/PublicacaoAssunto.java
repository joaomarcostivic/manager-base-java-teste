package com.tivic.manager.blb;

public class PublicacaoAssunto {

	private int cdAssunto;
	private int cdPublicacao;

	public PublicacaoAssunto(){ }

	public PublicacaoAssunto(int cdAssunto,
			int cdPublicacao){
		setCdAssunto(cdAssunto);
		setCdPublicacao(cdPublicacao);
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssunto: " +  getCdAssunto();
		valueToString += ", cdPublicacao: " +  getCdPublicacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PublicacaoAssunto(getCdAssunto(),
			getCdPublicacao());
	}

}
