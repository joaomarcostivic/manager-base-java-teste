package com.tivic.manager.mob.lotes.model.publicacao;

public class LotePublicacaoAit {

	private int cdLotePublicacao;
	private int cdAit;
	private String txtErro;

	public LotePublicacaoAit() { }

	public LotePublicacaoAit(int cdLotePublicacao,
			int cdAit,
			String txtErro) {
		setCdLotePublicacao(cdLotePublicacao);
		setCdAit(cdAit);
		setTxtErro(txtErro);
	}
	
	public void setCdLotePublicacao(int cdLotePublicacao){
		this.cdLotePublicacao=cdLotePublicacao;
	}
	
	public int getCdLotePublicacao(){
		return this.cdLotePublicacao;
	}
	
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	
	public int getCdAit(){
		return this.cdAit;
	}
	
	public void setTxtErro(String txtErro){
		this.txtErro=txtErro;
	}
	
	public String getTxtErro(){
		return this.txtErro;
	}
}