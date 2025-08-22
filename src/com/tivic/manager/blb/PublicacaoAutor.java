package com.tivic.manager.blb;

public class PublicacaoAutor {

	private int cdAutor;
	private int cdPublicacao;

	public PublicacaoAutor(){ }

	public PublicacaoAutor(int cdAutor,
			int cdPublicacao){
		setCdAutor(cdAutor);
		setCdPublicacao(cdPublicacao);
	}
	public void setCdAutor(int cdAutor){
		this.cdAutor=cdAutor;
	}
	public int getCdAutor(){
		return this.cdAutor;
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAutor: " +  getCdAutor();
		valueToString += ", cdPublicacao: " +  getCdPublicacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new PublicacaoAutor(getCdAutor(),
			getCdPublicacao());
	}

}