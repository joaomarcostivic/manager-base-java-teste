package com.tivic.manager.ptc;

public class WorkflowAcao {

	private int cdAcao;
	private int cdRegra;
	private int tpAcao;
	private int lgSugestao;
	private int cdSetor;
	private int cdTipoPrazo;
	private int nrDias;
	private int cdFase;
	private int cdTipoOcorrencia;
	private int cdTipoPendencia;
	private int cdTipoDocumento;
	private int cdModelo;

	public WorkflowAcao(){ }

	public WorkflowAcao(int cdAcao,
			int cdRegra,
			int tpAcao,
			int lgSugestao,
			int cdSetor,
			int cdTipoPrazo,
			int nrDias,
			int cdFase,
			int cdTipoOcorrencia,
			int cdTipoPendencia,
			int cdTipoDocumento,
			int cdModelo){
		setCdAcao(cdAcao);
		setCdRegra(cdRegra);
		setTpAcao(tpAcao);
		setLgSugestao(lgSugestao);
		setCdSetor(cdSetor);
		setCdTipoPrazo(cdTipoPrazo);
		setNrDias(nrDias);
		setCdFase(cdFase);
		setCdTipoOcorrencia(cdTipoOcorrencia);
		setCdTipoPendencia(cdTipoPendencia);
		setCdTipoDocumento(cdTipoDocumento);
		setCdModelo(cdModelo);
	}
	public void setCdAcao(int cdAcao){
		this.cdAcao=cdAcao;
	}
	public int getCdAcao(){
		return this.cdAcao;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setTpAcao(int tpAcao){
		this.tpAcao=tpAcao;
	}
	public int getTpAcao(){
		return this.tpAcao;
	}
	public void setLgSugestao(int lgSugestao){
		this.lgSugestao=lgSugestao;
	}
	public int getLgSugestao(){
		return this.lgSugestao;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdTipoPrazo(int cdTipoPrazo){
		this.cdTipoPrazo=cdTipoPrazo;
	}
	public int getCdTipoPrazo(){
		return this.cdTipoPrazo;
	}
	public void setNrDias(int nrDias){
		this.nrDias=nrDias;
	}
	public int getNrDias(){
		return this.nrDias;
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setCdTipoOcorrencia(int cdTipoOcorrencia){
		this.cdTipoOcorrencia=cdTipoOcorrencia;
	}
	public int getCdTipoOcorrencia(){
		return this.cdTipoOcorrencia;
	}
	public void setCdTipoPendencia(int cdTipoPendencia){
		this.cdTipoPendencia=cdTipoPendencia;
	}
	public int getCdTipoPendencia(){
		return this.cdTipoPendencia;
	}
	public void setCdTipoDocumento(int cdTipoDocumento){
		this.cdTipoDocumento=cdTipoDocumento;
	}
	public int getCdTipoDocumento(){
		return this.cdTipoDocumento;
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAcao: " +  getCdAcao();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", tpAcao: " +  getTpAcao();
		valueToString += ", lgSugestao: " +  getLgSugestao();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdTipoPrazo: " +  getCdTipoPrazo();
		valueToString += ", nrDias: " +  getNrDias();
		valueToString += ", cdFase: " +  getCdFase();
		valueToString += ", cdTipoOcorrencia: " +  getCdTipoOcorrencia();
		valueToString += ", cdTipoPendencia: " +  getCdTipoPendencia();
		valueToString += ", cdTipoDocumento: " +  getCdTipoDocumento();
		valueToString += ", cdModelo: " +  getCdModelo();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new WorkflowAcao(getCdAcao(),
			getCdRegra(),
			getTpAcao(),
			getLgSugestao(),
			getCdSetor(),
			getCdTipoPrazo(),
			getNrDias(),
			getCdFase(),
			getCdTipoOcorrencia(),
			getCdTipoPendencia(),
			getCdTipoDocumento(),
			getCdModelo());
	}

}
