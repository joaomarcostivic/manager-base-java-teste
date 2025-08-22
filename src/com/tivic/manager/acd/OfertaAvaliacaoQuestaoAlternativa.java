package com.tivic.manager.acd;

public class OfertaAvaliacaoQuestaoAlternativa {

	private int cdAlternativa;
	private int cdOfertaAvaliacaoQuestao;
	private int cdOfertaAvaliacao;
	private int cdOferta;
	private int nrOrdem;
	private String txtAlternativa;
	private int lgCorreta;
	
	public OfertaAvaliacaoQuestaoAlternativa() { }

	public OfertaAvaliacaoQuestaoAlternativa(int cdAlternativa,
			int cdOfertaAvaliacaoQuestao,
			int cdOfertaAvaliacao,
			int cdOferta,
			int nrOrdem,
			String txtAlternativa,
			int lgCorreta) {
		setCdAlternativa(cdAlternativa);
		setCdOfertaAvaliacaoQuestao(cdOfertaAvaliacaoQuestao);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setNrOrdem(nrOrdem);
		setTxtAlternativa(txtAlternativa);
		setLgCorreta(lgCorreta);
	}
	public void setCdAlternativa(int cdAlternativa){
		this.cdAlternativa=cdAlternativa;
	}
	public int getCdAlternativa(){
		return this.cdAlternativa;
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
	public void setTxtAlternativa(String txtAlternativa){
		this.txtAlternativa=txtAlternativa;
	}
	public String getTxtAlternativa(){
		return this.txtAlternativa;
	}
	public void setLgCorreta(int lgCorreta) {
		this.lgCorreta = lgCorreta;
	}
	public int getLgCorreta() {
		return lgCorreta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAlternativa: " +  getCdAlternativa();
		valueToString += ", cdOfertaAvaliacaoQuestao: " +  getCdOfertaAvaliacaoQuestao();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", txtAlternativa: " +  getTxtAlternativa();
		valueToString += ", lgCorreta: " +  getLgCorreta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OfertaAvaliacaoQuestaoAlternativa(getCdAlternativa(),
			getCdOfertaAvaliacaoQuestao(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getNrOrdem(),
			getTxtAlternativa(),
			getLgCorreta());
	}

}