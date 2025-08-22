package com.tivic.manager.blb;

import java.util.GregorianCalendar;

public class UsuarioPenalidade {

	private int nrPenalidade;
	private int cdPenalidade;
	private int cdPublicacao;
	private int cdExemplar;
	private int nrOcorrencia;
	private int cdPessoa;
	private GregorianCalendar dtPenalidade;
	private int tpPenalidade;
	private int nrDias;
	private float vlPenalidade;

	public UsuarioPenalidade(){ }

	public UsuarioPenalidade(int nrPenalidade,
			int cdPenalidade,
			int cdPublicacao,
			int cdExemplar,
			int nrOcorrencia,
			int cdPessoa,
			GregorianCalendar dtPenalidade,
			int tpPenalidade,
			int nrDias,
			float vlPenalidade){
		setNrPenalidade(nrPenalidade);
		setCdPenalidade(cdPenalidade);
		setCdPublicacao(cdPublicacao);
		setCdExemplar(cdExemplar);
		setNrOcorrencia(nrOcorrencia);
		setCdPessoa(cdPessoa);
		setDtPenalidade(dtPenalidade);
		setTpPenalidade(tpPenalidade);
		setNrDias(nrDias);
		setVlPenalidade(vlPenalidade);
	}
	public void setNrPenalidade(int nrPenalidade){
		this.nrPenalidade=nrPenalidade;
	}
	public int getNrPenalidade(){
		return this.nrPenalidade;
	}
	public void setCdPenalidade(int cdPenalidade){
		this.cdPenalidade=cdPenalidade;
	}
	public int getCdPenalidade(){
		return this.cdPenalidade;
	}
	public void setCdPublicacao(int cdPublicacao){
		this.cdPublicacao=cdPublicacao;
	}
	public int getCdPublicacao(){
		return this.cdPublicacao;
	}
	public void setCdExemplar(int cdExemplar){
		this.cdExemplar=cdExemplar;
	}
	public int getCdExemplar(){
		return this.cdExemplar;
	}
	public void setNrOcorrencia(int nrOcorrencia){
		this.nrOcorrencia=nrOcorrencia;
	}
	public int getNrOcorrencia(){
		return this.nrOcorrencia;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setDtPenalidade(GregorianCalendar dtPenalidade){
		this.dtPenalidade=dtPenalidade;
	}
	public GregorianCalendar getDtPenalidade(){
		return this.dtPenalidade;
	}
	public void setTpPenalidade(int tpPenalidade){
		this.tpPenalidade=tpPenalidade;
	}
	public int getTpPenalidade(){
		return this.tpPenalidade;
	}
	public void setNrDias(int nrDias){
		this.nrDias=nrDias;
	}
	public int getNrDias(){
		return this.nrDias;
	}
	public void setVlPenalidade(float vlPenalidade){
		this.vlPenalidade=vlPenalidade;
	}
	public float getVlPenalidade(){
		return this.vlPenalidade;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "nrPenalidade: " +  getNrPenalidade();
		valueToString += ", cdPenalidade: " +  getCdPenalidade();
		valueToString += ", cdPublicacao: " +  getCdPublicacao();
		valueToString += ", cdExemplar: " +  getCdExemplar();
		valueToString += ", nrOcorrencia: " +  getNrOcorrencia();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", dtPenalidade: " +  sol.util.Util.formatDateTime(getDtPenalidade(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpPenalidade: " +  getTpPenalidade();
		valueToString += ", nrDias: " +  getNrDias();
		valueToString += ", vlPenalidade: " +  getVlPenalidade();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioPenalidade(getNrPenalidade(),
			getCdPenalidade(),
			getCdPublicacao(),
			getCdExemplar(),
			getNrOcorrencia(),
			getCdPessoa(),
			getDtPenalidade()==null ? null : (GregorianCalendar)getDtPenalidade().clone(),
			getTpPenalidade(),
			getNrDias(),
			getVlPenalidade());
	}

}