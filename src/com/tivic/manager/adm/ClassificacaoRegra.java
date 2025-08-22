package com.tivic.manager.adm;

public class ClassificacaoRegra {

	private int cdClassificacao;
	private int cdRegra;
	private int nrOrdem;
	private int tpOperador;

	public ClassificacaoRegra(int cdClassificacao,
			int cdRegra,
			int nrOrdem,
			int tpOperador){
		setCdClassificacao(cdClassificacao);
		setCdRegra(cdRegra);
		setNrOrdem(nrOrdem);
		setTpOperador(tpOperador);
	}
	public void setCdClassificacao(int cdClassificacao){
		this.cdClassificacao=cdClassificacao;
	}
	public int getCdClassificacao(){
		return this.cdClassificacao;
	}
	public void setCdRegra(int cdRegra){
		this.cdRegra=cdRegra;
	}
	public int getCdRegra(){
		return this.cdRegra;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setTpOperador(int tpOperador){
		this.tpOperador=tpOperador;
	}
	public int getTpOperador(){
		return this.tpOperador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdClassificacao: " +  getCdClassificacao();
		valueToString += ", cdRegra: " +  getCdRegra();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", tpOperador: " +  getTpOperador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ClassificacaoRegra(getCdClassificacao(),
			getCdRegra(),
			getNrOrdem(),
			getTpOperador());
	}

}
