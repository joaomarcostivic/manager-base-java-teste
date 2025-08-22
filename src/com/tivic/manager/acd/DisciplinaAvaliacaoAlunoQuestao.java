package com.tivic.manager.acd;

public class DisciplinaAvaliacaoAlunoQuestao {

	private int cdMatriculaDisciplina;
	private int cdOfertaAvaliacaoQuestao;
	private int cdOfertaAvaliacao;
	private int cdOferta;
	private int cdAlternativa;
	private String txtResposta;

	public DisciplinaAvaliacaoAlunoQuestao() { }

	public DisciplinaAvaliacaoAlunoQuestao(int cdMatriculaDisciplina,
			int cdOfertaAvaliacaoQuestao,
			int cdOfertaAvaliacao,
			int cdOferta,
			int cdAlternativa,
			String txtResposta) {
		setCdMatriculaDisciplina(cdMatriculaDisciplina);
		setCdOfertaAvaliacaoQuestao(cdOfertaAvaliacaoQuestao);
		setCdOfertaAvaliacao(cdOfertaAvaliacao);
		setCdOferta(cdOferta);
		setCdAlternativa(cdAlternativa);
		setTxtResposta(txtResposta);
	}
	public void setCdMatriculaDisciplina(int cdMatriculaDisciplina){
		this.cdMatriculaDisciplina=cdMatriculaDisciplina;
	}
	public int getCdMatriculaDisciplina(){
		return this.cdMatriculaDisciplina;
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
	public void setCdAlternativa(int cdAlternativa){
		this.cdAlternativa=cdAlternativa;
	}
	public int getCdAlternativa(){
		return this.cdAlternativa;
	}
	public void setTxtResposta(String txtResposta){
		this.txtResposta=txtResposta;
	}
	public String getTxtResposta(){
		return this.txtResposta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdMatriculaDisciplina: " +  getCdMatriculaDisciplina();
		valueToString += ", cdOfertaAvaliacaoQuestao: " +  getCdOfertaAvaliacaoQuestao();
		valueToString += ", cdOfertaAvaliacao: " +  getCdOfertaAvaliacao();
		valueToString += ", cdOferta: " +  getCdOferta();
		valueToString += ", cdAlternativa: " +  getCdAlternativa();
		valueToString += ", txtResposta: " +  getTxtResposta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new DisciplinaAvaliacaoAlunoQuestao(getCdMatriculaDisciplina(),
			getCdOfertaAvaliacaoQuestao(),
			getCdOfertaAvaliacao(),
			getCdOferta(),
			getCdAlternativa(),
			getTxtResposta());
	}

}