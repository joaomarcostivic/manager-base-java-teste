package com.tivic.manager.acd;

public class OfertaAvaliacaoQuestao {

	private int cdOfertaAvaliacaoQuestao;
	private int cdOfertaAvaliacao;
	private int cdOferta;
	private int nrOrdem;
	private String txtQuestao;
	private int tpQuestao;
	private Double vlPeso;
	private int cdOfertaAvaliacaoQuestaoSuperior;
	
	public OfertaAvaliacaoQuestao() { }

	public OfertaAvaliacaoQuestao(int cdOfertaAvaliacaoQuestao,
			int cdOfertaAvaliacao,
			int cdOferta,
			int nrOrdem,
			String txtQuestao,
			int tpQuestao,
			Double vlPeso,
			int cdOfertaAvaliacaoQuestaoSuperior) {
		setCdOfertaAvaliacaoQuestao(cdOfertaAvaliacaoQuestao);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setNrOrdem(nrOrdem);
		setTxtQuestao(txtQuestao);
		setTpQuestao(tpQuestao);
		setVlPeso(vlPeso);
		setCdOfertaAvaliacaoQuestaoSuperior(cdOfertaAvaliacaoQuestaoSuperior);
	}
	public void setCdOfertaAvaliacaoQuestao(int cdOfertaAvaliacaoQuestao){
		this.cdOfertaAvaliacaoQuestao=cdOfertaAvaliacaoQuestao;
	}
	public int getCdOfertaAvaliacaoQuestao(){
		return this.cdOfertaAvaliacaoQuestao;
	}
	public void setCdOfertaAvaliacao(int cdOfertaAvaliacao){
		this.cdOfertaAvaliacao=cdOfertaAvaliacao;
	}
	public int getCdOfertaAvaliacao(){
		return this.cdOfertaAvaliacao;
	}
	public void setCdOferta(int cdOferta){
		this.cdOferta=cdOferta;
	}
	public int getCdOferta(){
		return this.cdOferta;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTxtQuestao(String txtQuestao){
		this.txtQuestao=txtQuestao;
	}
	public String getTxtQuestao(){
		return this.txtQuestao;
	}
	public void setTpQuestao(int tpQuestao){
		this.tpQuestao=tpQuestao;
	}
	public int getTpQuestao(){
		return this.tpQuestao;
	}
	public void setVlPeso(Double vlPeso){
		this.vlPeso=vlPeso;
	}
	public Double getVlPeso(){
		return this.vlPeso;
	}
	public void setCdOfertaAvaliacaoQuestaoSuperior(int cdOfertaAvaliacaoQuestaoSuperior) {
		this.cdOfertaAvaliacaoQuestaoSuperior = cdOfertaAvaliacaoQuestaoSuperior;
	}
	public int getCdOfertaAvaliacaoQuestaoSuperior() {
		return cdOfertaAvaliacaoQuestaoSuperior;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdOfertaAvaliacaoQuestao: " +  getCdOfertaAvaliacaoQuestao();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", txtQuestao: " +  getTxtQuestao();
		valueToString += ", tpQuestao: " +  getTpQuestao();
		valueToString += ", vlPeso: " +  getVlPeso();
		valueToString += ", cdOfertaAvaliacaoQuestaoSuperior: " +  getCdOfertaAvaliacaoQuestaoSuperior();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OfertaAvaliacaoQuestao(getCdOfertaAvaliacaoQuestao(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getNrOrdem(),
			getTxtQuestao(),
			getTpQuestao(),
			getVlPeso(),
			getCdOfertaAvaliacaoQuestaoSuperior());
	}

}