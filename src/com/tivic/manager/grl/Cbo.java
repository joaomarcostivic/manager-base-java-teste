package com.tivic.manager.grl;

public class Cbo {

	private int cdCbo;
	private String nmCbo;
	private String sgCbo;
	private String idCbo;
	private int nrNivel;
	private int cdCboSuperior;
	private String nrCbo;
	private String txtOcupacao;
	private String txtDescricaoSumaria;
	private String txtCondicaoExercicio;
	private String txtFormacao;
	private String txtExcecao;

	public Cbo() { }
	
	public Cbo(int cdCbo,
			String nmCbo,
			String sgCbo,
			String idCbo,
			int nrNivel,
			int cdCboSuperior,
			String nrCbo,
			String txtOcupacao,
			String txtDescricaoSumaria,
			String txtCondicaoExercicio,
			String txtFormacao,
			String txtExcecao){
		setCdCbo(cdCbo);
		setNmCbo(nmCbo);
		setSgCbo(sgCbo);
		setIdCbo(idCbo);
		setNrNivel(nrNivel);
		setCdCboSuperior(cdCboSuperior);
		setNrCbo(nrCbo);
		setTxtOcupacao(txtOcupacao);
		setTxtDescricaoSumaria(txtDescricaoSumaria);
		setTxtCondicaoExercicio(txtCondicaoExercicio);
		setTxtFormacao(txtFormacao);
		setTxtExcecao(txtExcecao);
	}
	public void setCdCbo(int cdCbo){
		this.cdCbo=cdCbo;
	}
	public int getCdCbo(){
		return this.cdCbo;
	}
	public void setNmCbo(String nmCbo){
		this.nmCbo=nmCbo;
	}
	public String getNmCbo(){
		return this.nmCbo;
	}
	public void setSgCbo(String sgCbo){
		this.sgCbo=sgCbo;
	}
	public String getSgCbo(){
		return this.sgCbo;
	}
	public void setIdCbo(String idCbo){
		this.idCbo=idCbo;
	}
	public String getIdCbo(){
		return this.idCbo;
	}
	public void setNrNivel(int nrNivel){
		this.nrNivel=nrNivel;
	}
	public int getNrNivel(){
		return this.nrNivel;
	}
	public void setCdCboSuperior(int cdCboSuperior){
		this.cdCboSuperior=cdCboSuperior;
	}
	public int getCdCboSuperior(){
		return this.cdCboSuperior;
	}
	public void setNrCbo(String nrCbo){
		this.nrCbo=nrCbo;
	}
	public String getNrCbo(){
		return this.nrCbo;
	}
	public void setTxtOcupacao(String txtOcupacao){
		this.txtOcupacao=txtOcupacao;
	}
	public String getTxtOcupacao(){
		return this.txtOcupacao;
	}
	public void setTxtDescricaoSumaria(String txtDescricaoSumaria){
		this.txtDescricaoSumaria=txtDescricaoSumaria;
	}
	public String getTxtDescricaoSumaria(){
		return this.txtDescricaoSumaria;
	}
	public void setTxtCondicaoExercicio(String txtCondicaoExercicio){
		this.txtCondicaoExercicio=txtCondicaoExercicio;
	}
	public String getTxtCondicaoExercicio(){
		return this.txtCondicaoExercicio;
	}
	public void setTxtFormacao(String txtFormacao){
		this.txtFormacao=txtFormacao;
	}
	public String getTxtFormacao(){
		return this.txtFormacao;
	}
	public void setTxtExcecao(String txtExcecao){
		this.txtExcecao=txtExcecao;
	}
	public String getTxtExcecao(){
		return this.txtExcecao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCbo: " +  getCdCbo();
		valueToString += ", nmCbo: " +  getNmCbo();
		valueToString += ", sgCbo: " +  getSgCbo();
		valueToString += ", idCbo: " +  getIdCbo();
		valueToString += ", nrNivel: " +  getNrNivel();
		valueToString += ", cdCboSuperior: " +  getCdCboSuperior();
		valueToString += ", nrCbo: " +  getNrCbo();
		valueToString += ", txtOcupacao: " +  getTxtOcupacao();
		valueToString += ", txtDescricaoSumaria: " +  getTxtDescricaoSumaria();
		valueToString += ", txtCondicaoExercicio: " +  getTxtCondicaoExercicio();
		valueToString += ", txtFormacao: " +  getTxtFormacao();
		valueToString += ", txtExcecao: " +  getTxtExcecao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Cbo(getCdCbo(),
			getNmCbo(),
			getSgCbo(),
			getIdCbo(),
			getNrNivel(),
			getCdCboSuperior(),
			getNrCbo(),
			getTxtOcupacao(),
			getTxtDescricaoSumaria(),
			getTxtCondicaoExercicio(),
			getTxtFormacao(),
			getTxtExcecao());
	}

}
