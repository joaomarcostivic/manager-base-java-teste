package com.tivic.manager.psq;

public class QuestaoValor {

	private int cdQuestaoValor;
	private int cdQuestao;
	private int cdQuestionario;
	private int cdBibliotecaRecurso;
	private int tpDado;
	private byte[] blbValor;
	private String txtApresentacao;
	private int nrOrdem;
	private int lgAjuda;
	private String txtAjuda;
	private String dsDica;
	private float vlPeso;

	public QuestaoValor(int cdQuestaoValor,
			int cdQuestao,
			int cdQuestionario,
			int cdBibliotecaRecurso,
			int tpDado,
			byte[] blbValor,
			String txtApresentacao,
			int nrOrdem,
			int lgAjuda,
			String txtAjuda,
			String dsDica,
			float vlPeso){
		setCdQuestaoValor(cdQuestaoValor);
		setCdQuestao(cdQuestao);
		setCdQuestionario(cdQuestionario);
		setCdBibliotecaRecurso(cdBibliotecaRecurso);
		setTpDado(tpDado);
		setBlbValor(blbValor);
		setTxtApresentacao(txtApresentacao);
		setNrOrdem(nrOrdem);
		setLgAjuda(lgAjuda);
		setTxtAjuda(txtAjuda);
		setDsDica(dsDica);
		setVlPeso(vlPeso);
	}
	public void setCdQuestaoValor(int cdQuestaoValor){
		this.cdQuestaoValor=cdQuestaoValor;
	}
	public int getCdQuestaoValor(){
		return this.cdQuestaoValor;
	}
	public void setCdQuestao(int cdQuestao){
		this.cdQuestao=cdQuestao;
	}
	public int getCdQuestao(){
		return this.cdQuestao;
	}
	public void setCdQuestionario(int cdQuestionario){
		this.cdQuestionario=cdQuestionario;
	}
	public int getCdQuestionario(){
		return this.cdQuestionario;
	}
	public void setCdBibliotecaRecurso(int cdBibliotecaRecurso){
		this.cdBibliotecaRecurso=cdBibliotecaRecurso;
	}
	public int getCdBibliotecaRecurso(){
		return this.cdBibliotecaRecurso;
	}
	public void setTpDado(int tpDado){
		this.tpDado=tpDado;
	}
	public int getTpDado(){
		return this.tpDado;
	}
	public void setBlbValor(byte[] blbValor){
		this.blbValor=blbValor;
	}
	public byte[] getBlbValor(){
		return this.blbValor;
	}
	public void setTxtApresentacao(String txtApresentacao){
		this.txtApresentacao=txtApresentacao;
	}
	public String getTxtApresentacao(){
		return this.txtApresentacao;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setLgAjuda(int lgAjuda){
		this.lgAjuda=lgAjuda;
	}
	public int getLgAjuda(){
		return this.lgAjuda;
	}
	public void setTxtAjuda(String txtAjuda){
		this.txtAjuda=txtAjuda;
	}
	public String getTxtAjuda(){
		return this.txtAjuda;
	}
	public void setDsDica(String dsDica){
		this.dsDica=dsDica;
	}
	public String getDsDica(){
		return this.dsDica;
	}
	public void setVlPeso(float vlPeso){
		this.vlPeso=vlPeso;
	}
	public float getVlPeso(){
		return this.vlPeso;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuestaoValor: " +  getCdQuestaoValor();
		valueToString += ", cdQuestao: " +  getCdQuestao();
		valueToString += ", cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdBibliotecaRecurso: " +  getCdBibliotecaRecurso();
		valueToString += ", tpDado: " +  getTpDado();
		valueToString += ", blbValor: " +  getBlbValor();
		valueToString += ", txtApresentacao: " +  getTxtApresentacao();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", lgAjuda: " +  getLgAjuda();
		valueToString += ", txtAjuda: " +  getTxtAjuda();
		valueToString += ", dsDica: " +  getDsDica();
		valueToString += ", vlPeso: " +  getVlPeso();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new QuestaoValor(getCdQuestaoValor(),
			getCdQuestao(),
			getCdQuestionario(),
			getCdBibliotecaRecurso(),
			getTpDado(),
			getBlbValor(),
			getTxtApresentacao(),
			getNrOrdem(),
			getLgAjuda(),
			getTxtAjuda(),
			getDsDica(),
			getVlPeso());
	}

}
