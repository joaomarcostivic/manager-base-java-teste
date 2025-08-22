package com.tivic.manager.acd;

public class OfertaAvaliacaoQuestaoPlanoTopico {

	private int cdOfertaAvaliacaoQuestao;
	private int cdOfertaAvaliacao;
	private int cdOferta;
	private int cdPlano;
	private int cdSecao;
	private int cdTopico;

	public OfertaAvaliacaoQuestaoPlanoTopico() { }

	public OfertaAvaliacaoQuestaoPlanoTopico(int cdOfertaAvaliacaoQuestao,
			int cdOfertaAvaliacao,
			int cdOferta,
			int cdPlano,
			int cdSecao,
			int cdTopico) {
		setCdOfertaAvaliacaoQuestao(cdOfertaAvaliacaoQuestao);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setCdPlano(cdPlano);
		setCdSecao(cdSecao);
		setCdTopico(cdTopico);
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
	public void setCdPlano(int cdPlano){
		this.cdPlano=cdPlano;
	}
	public int getCdPlano(){
		return this.cdPlano;
	}
	public void setCdSecao(int cdSecao){
		this.cdSecao=cdSecao;
	}
	public int getCdSecao(){
		return this.cdSecao;
	}
	public void setCdTopico(int cdTopico){
		this.cdTopico=cdTopico;
	}
	public int getCdTopico(){
		return this.cdTopico;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdOfertaAvaliacaoQuestao: " +  getCdOfertaAvaliacaoQuestao();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", cdPlano: " +  getCdPlano();
		valueToString += ", cdSecao: " +  getCdSecao();
		valueToString += ", cdTopico: " +  getCdTopico();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new OfertaAvaliacaoQuestaoPlanoTopico(getCdOfertaAvaliacaoQuestao(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getCdPlano(),
			getCdSecao(),
			getCdTopico());
	}

}