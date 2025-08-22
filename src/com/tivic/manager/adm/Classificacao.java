package com.tivic.manager.adm;

public class Classificacao {

	private int cdClassificacao;
	private String nmClassificacao;
	private String idClassificacao;
	private String txtDescricao;
	private int lgAtivo;
	private int lgPadrao;
	private int cdCobranca;
	
	public Classificacao(){}
	
	public Classificacao(int cdClassificacao,
			String nmClassificacao,
			String idClassificacao,
			String txtDescricao,
			int lgAtivo,
			int lgPadrao,
			int cdCobranca){
		setCdClassificacao(cdClassificacao);
		setNmClassificacao(nmClassificacao);
		setIdClassificacao(idClassificacao);
		setTxtDescricao(txtDescricao);
		setLgAtivo(lgAtivo);
		setLgPadrao(lgPadrao);
		setCdCobranca(cdCobranca);
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setNmClassificacao(String nmClassificacao){
		this.nmClassificacao=nmClassificacao;
	}
	public String getNmClassificacao(){
		return this.nmClassificacao;
	}
	public void setIdClassificacao(String idClassificacao){
		this.idClassificacao=idClassificacao;
	}
	public String getIdClassificacao(){
		return this.idClassificacao;
	}
	public void setTxtDescricao(String txtDescricao){
		this.txtDescricao=txtDescricao;
	}
	public String getTxtDescricao(){
		return this.txtDescricao;
	}
	public void setLgAtivo(int lgAtivo){
		this.lgAtivo=lgAtivo;
	}
	public int getLgAtivo(){
		return this.lgAtivo;
	}
	public void setLgPadrao(int lgPadrao){
		this.lgPadrao=lgPadrao;
	}
	public int getLgPadrao(){
		return this.lgPadrao;
	}
	public void setCdCobranca(int cdCobranca){
		this.cdCobranca=cdCobranca;
	}
	public int getCdCobranca(){
		return this.cdCobranca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClassificacao: " +  getCdClassificacao();
		valueToString += ", nmClassificacao: " +  getNmClassificacao();
		valueToString += ", idClassificacao: " +  getIdClassificacao();
		valueToString += ", txtDescricao: " +  getTxtDescricao();
		valueToString += ", lgAtivo: " +  getLgAtivo();
		valueToString += ", lgPadrao: " +  getLgPadrao();
		valueToString += ", cdCobranca: " +  getCdCobranca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Classificacao(getCdClassificacao(),
			getNmClassificacao(),
			getIdClassificacao(),
			getTxtDescricao(),
			getLgAtivo(),
			getLgPadrao(),
			getCdCobranca());
	}

}