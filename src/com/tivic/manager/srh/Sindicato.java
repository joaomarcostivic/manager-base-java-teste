package com.tivic.manager.srh;

public class Sindicato {

	private int cdSindicato;
	private String nmSindicato;
	private int nrMesRecolhimento;
	private float vlAplicacao;
	private int cdFormulario;
	private int tpCobranca;
	private int cdEventoFinanceiro;
	private String idSindicato;

	public Sindicato(){}
	public Sindicato(int cdSindicato,
			String nmSindicato,
			int nrMesRecolhimento,
			float vlAplicacao,
			int cdFormulario,
			int tpCobranca,
			int cdEventoFinanceiro,
			String idSindicato){
		setCdSindicato(cdSindicato);
		setNmSindicato(nmSindicato);
		setNrMesRecolhimento(nrMesRecolhimento);
		setVlAplicacao(vlAplicacao);
		setCdFormulario(cdFormulario);
		setTpCobranca(tpCobranca);
		setCdEventoFinanceiro(cdEventoFinanceiro);
		setIdSindicato(idSindicato);
	}
	public void setCdSindicato(int cdSindicato){
		this.cdSindicato=cdSindicato;
	}
	public int getCdSindicato(){
		return this.cdSindicato;
	}
	public void setNmSindicato(String nmSindicato){
		this.nmSindicato=nmSindicato;
	}
	public String getNmSindicato(){
		return this.nmSindicato;
	}
	public void setNrMesRecolhimento(int nrMesRecolhimento){
		this.nrMesRecolhimento=nrMesRecolhimento;
	}
	public int getNrMesRecolhimento(){
		return this.nrMesRecolhimento;
	}
	public void setVlAplicacao(float vlAplicacao){
		this.vlAplicacao=vlAplicacao;
	}
	public float getVlAplicacao(){
		return this.vlAplicacao;
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setTpCobranca(int tpCobranca){
		this.tpCobranca=tpCobranca;
	}
	public int getTpCobranca(){
		return this.tpCobranca;
	}
	public void setCdEventoFinanceiro(int cdEventoFinanceiro){
		this.cdEventoFinanceiro=cdEventoFinanceiro;
	}
	public int getCdEventoFinanceiro(){
		return this.cdEventoFinanceiro;
	}
	public void setIdSindicato(String idSindicato){
		this.idSindicato=idSindicato;
	}
	public String getIdSindicato(){
		return this.idSindicato;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSindicato: " +  getCdSindicato();
		valueToString += ", nmSindicato: " +  getNmSindicato();
		valueToString += ", nrMesRecolhimento: " +  getNrMesRecolhimento();
		valueToString += ", vlAplicacao: " +  getVlAplicacao();
		valueToString += ", cdFormulario: " +  getCdFormulario();
		valueToString += ", tpCobranca: " +  getTpCobranca();
		valueToString += ", cdEventoFinanceiro: " +  getCdEventoFinanceiro();
		valueToString += ", idSindicato: " +  getIdSindicato();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Sindicato(getCdSindicato(),
			getNmSindicato(),
			getNrMesRecolhimento(),
			getVlAplicacao(),
			getCdFormulario(),
			getTpCobranca(),
			getCdEventoFinanceiro(),
			getIdSindicato());
	}

}