package com.tivic.manager.psq;

public class Questao {

	private int cdQuestao;
	private int cdQuestionario;
	private int cdTema;
	private String txtQuestao;
	private String dsQuestao;
	private int tpQuestao;
	private int lgExclusivo;
	private int nrOrdem;
	private float vlTotalSoma;
	private int lgAjuda;
	private String txtAjuda;
	private String dsDica;
	private int lgQuebraPagina;
	private int lgSeparador;
	private String idQuestao;
	private int lgObrigatoria;
	private int lgObservacao;
	private int tpApresentacao;

	public Questao(int cdQuestao,
			int cdQuestionario,
			int cdTema,
			String txtQuestao,
			String dsQuestao,
			int tpQuestao,
			int lgExclusivo,
			int nrOrdem,
			float vlTotalSoma,
			int lgAjuda,
			String txtAjuda,
			String dsDica,
			int lgQuebraPagina,
			int lgSeparador,
			String idQuestao,
			int lgObrigatoria,
			int lgObservacao,
			int tpApresentacao){
		setCdQuestao(cdQuestao);
		setCdQuestionario(cdQuestionario);
		setCdTema(cdTema);
		setTxtQuestao(txtQuestao);
		setDsQuestao(dsQuestao);
		setTpQuestao(tpQuestao);
		setLgExclusivo(lgExclusivo);
		setNrOrdem(nrOrdem);
		setVlTotalSoma(vlTotalSoma);
		setLgAjuda(lgAjuda);
		setTxtAjuda(txtAjuda);
		setDsDica(dsDica);
		setLgQuebraPagina(lgQuebraPagina);
		setLgSeparador(lgSeparador);
		setIdQuestao(idQuestao);
		setLgObrigatoria(lgObrigatoria);
		setLgObservacao(lgObservacao);
		setTpApresentacao(tpApresentacao);
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
	public void setCdTema(int cdTema){
		this.cdTema=cdTema;
	}
	public int getCdTema(){
		return this.cdTema;
	}
	public void setTxtQuestao(String txtQuestao){
		this.txtQuestao=txtQuestao;
	}
	public String getTxtQuestao(){
		return this.txtQuestao;
	}
	public void setDsQuestao(String dsQuestao){
		this.dsQuestao=dsQuestao;
	}
	public String getDsQuestao(){
		return this.dsQuestao;
	}
	public void setTpQuestao(int tpQuestao){
		this.tpQuestao=tpQuestao;
	}
	public int getTpQuestao(){
		return this.tpQuestao;
	}
	public void setLgExclusivo(int lgExclusivo){
		this.lgExclusivo=lgExclusivo;
	}
	public int getLgExclusivo(){
		return this.lgExclusivo;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setVlTotalSoma(float vlTotalSoma){
		this.vlTotalSoma=vlTotalSoma;
	}
	public float getVlTotalSoma(){
		return this.vlTotalSoma;
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
	public void setLgQuebraPagina(int lgQuebraPagina){
		this.lgQuebraPagina=lgQuebraPagina;
	}
	public int getLgQuebraPagina(){
		return this.lgQuebraPagina;
	}
	public void setLgSeparador(int lgSeparador){
		this.lgSeparador=lgSeparador;
	}
	public int getLgSeparador(){
		return this.lgSeparador;
	}
	public void setIdQuestao(String idQuestao){
		this.idQuestao=idQuestao;
	}
	public String getIdQuestao(){
		return this.idQuestao;
	}
	public void setLgObrigatoria(int lgObrigatoria){
		this.lgObrigatoria=lgObrigatoria;
	}
	public int getLgObrigatoria(){
		return this.lgObrigatoria;
	}
	public void setLgObservacao(int lgObservacao){
		this.lgObservacao=lgObservacao;
	}
	public int getLgObservacao(){
		return this.lgObservacao;
	}
	public void setTpApresentacao(int tpApresentacao){
		this.tpApresentacao=tpApresentacao;
	}
	public int getTpApresentacao(){
		return this.tpApresentacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdQuestao: " +  getCdQuestao();
		valueToString += ", cdQuestionario: " +  getCdQuestionario();
		valueToString += ", cdTema: " +  getCdTema();
		valueToString += ", txtQuestao: " +  getTxtQuestao();
		valueToString += ", dsQuestao: " +  getDsQuestao();
		valueToString += ", tpQuestao: " +  getTpQuestao();
		valueToString += ", lgExclusivo: " +  getLgExclusivo();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", vlTotalSoma: " +  getVlTotalSoma();
		valueToString += ", lgAjuda: " +  getLgAjuda();
		valueToString += ", txtAjuda: " +  getTxtAjuda();
		valueToString += ", dsDica: " +  getDsDica();
		valueToString += ", lgQuebraPagina: " +  getLgQuebraPagina();
		valueToString += ", lgSeparador: " +  getLgSeparador();
		valueToString += ", idQuestao: " +  getIdQuestao();
		valueToString += ", lgObrigatoria: " +  getLgObrigatoria();
		valueToString += ", lgObservacao: " +  getLgObservacao();
		valueToString += ", tpApresentacao: " +  getTpApresentacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Questao(getCdQuestao(),
			getCdQuestionario(),
			getCdTema(),
			getTxtQuestao(),
			getDsQuestao(),
			getTpQuestao(),
			getLgExclusivo(),
			getNrOrdem(),
			getVlTotalSoma(),
			getLgAjuda(),
			getTxtAjuda(),
			getDsDica(),
			getLgQuebraPagina(),
			getLgSeparador(),
			getIdQuestao(),
			getLgObrigatoria(),
			getLgObservacao(),
			getTpApresentacao());
	}

}
