package com.tivic.manager.acd;

public class FormacaoEducarte {

	private int cdFormacao;
	private String cdModalidade;
	private int cdInstituicao;
	private int cdCirculo;
	private int cdInstrutor;
	private int tpTurno;
	private String nrDiaSemana;
	private int lgPlanejamento;
	private int lgOficina;
	private String txtObservacao;

	public FormacaoEducarte() { }

	public FormacaoEducarte(int cdFormacao,
			String cdModalidade,
			int cdInstituicao,
			int cdCirculo,
			int cdInstrutor,
			int tpTurno,
			String nrDiaSemana,
			int lgPlanejamento,
			int lgOficina,
			String txtObservacao) {
		setCdFormacao(cdFormacao);
		setCdModalidade(cdModalidade);
		setCdInstituicao(cdInstituicao);
		setCdCirculo(cdCirculo);
		setCdInstrutor(cdInstrutor);
		setTpTurno(tpTurno);
		setNrDiaSemana(nrDiaSemana);
		setLgPlanejamento(lgPlanejamento);
		setLgOficina(lgOficina);
		setTxtObservacao(txtObservacao);
	}
	public void setCdFormacao(int cdFormacao){
		this.cdFormacao=cdFormacao;
	}
	public int getCdFormacao(){
		return this.cdFormacao;
	}
	public void setCdModalidade(String cdModalidade){
		this.cdModalidade=cdModalidade;
	}
	public String getCdModalidade(){
		return this.cdModalidade;
	}
	public void setCdInstituicao(int cdInstituicao){
		this.cdInstituicao=cdInstituicao;
	}
	public int getCdInstituicao(){
		return this.cdInstituicao;
	}
	public void setCdCirculo(int cdCirculo){
		this.cdCirculo=cdCirculo;
	}
	public int getCdCirculo(){
		return this.cdCirculo;
	}
	public void setCdInstrutor(int cdInstrutor){
		this.cdInstrutor=cdInstrutor;
	}
	public int getCdInstrutor(){
		return this.cdInstrutor;
	}
	public void setTpTurno(int tpTurno){
		this.tpTurno=tpTurno;
	}
	public int getTpTurno(){
		return this.tpTurno;
	}
	public void setNrDiaSemana(String nrDiaSemana){
		this.nrDiaSemana=nrDiaSemana;
	}
	public String getNrDiaSemana(){
		return this.nrDiaSemana;
	}
	public void setLgPlanejamento(int lgPlanejamento){
		this.lgPlanejamento=lgPlanejamento;
	}
	public int getLgPlanejamento(){
		return this.lgPlanejamento;
	}
	public void setLgOficina(int lgOficina){
		this.lgOficina=lgOficina;
	}
	public int getLgOficina(){
		return this.lgOficina;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormacao: " +  getCdFormacao();
		valueToString += ", cdModalidade: " +  getCdModalidade();
		valueToString += ", cdInstituicao: " +  getCdInstituicao();
		valueToString += ", cdCirculo: " +  getCdCirculo();
		valueToString += ", cdInstrutor: " +  getCdInstrutor();
		valueToString += ", tpTurno: " +  getTpTurno();
		valueToString += ", nrDiaSemana: " +  getNrDiaSemana();
		valueToString += ", lgPlanejamento: " +  getLgPlanejamento();
		valueToString += ", lgOficina: " +  getLgOficina();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new FormacaoEducarte(getCdFormacao(),
			getCdModalidade(),
			getCdInstituicao(),
			getCdCirculo(),
			getCdInstrutor(),
			getTpTurno(),
			getNrDiaSemana(),
			getLgPlanejamento(),
			getLgOficina(),
			getTxtObservacao());
	}

}