package com.tivic.manager.str;

public class Infracao {

	private int cdInfracao;
	private String dsInfracao;
	private int nrPontuacao;
	private int nrCodDetran;
	private float nrValorUfir;
	private String nmNatureza;
	private String nrArtigo;
	private String nrParagrafo;
	private String nrInciso;
	private String nrAlinea;
	private int tpCompetencia;
	private float vlInfracao;
	private int lgPrioritaria;

	public Infracao(){ }

	public Infracao(int cdInfracao,
			String dsInfracao,
			int nrPontuacao,
			int nrCodDetran,
			float nrValorUfir,
			String nmNatureza,
			String nrArtigo,
			String nrParagrafo,
			String nrInciso,
			String nrAlinea,
			int tpCompetencia,
			float vlInfracao,
			int lgPrioritaria
			){
		setCdInfracao(cdInfracao);
		setDsInfracao(dsInfracao);
		setNrPontuacao(nrPontuacao);
		setNrCodDetran(nrCodDetran);
		setNrValorUfir(nrValorUfir);
		setNmNatureza(nmNatureza);
		setNrArtigo(nrArtigo);
		setNrParagrafo(nrParagrafo);
		setNrInciso(nrInciso);
		setNrAlinea(nrAlinea);
		setTpCompetencia(tpCompetencia);
		setVlInfracao(vlInfracao);
		setLgPrioritaria(lgPrioritaria);
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setDsInfracao(String dsInfracao){
		this.dsInfracao=dsInfracao;
	}
	public String getDsInfracao(){
		return this.dsInfracao;
	}
	public void setNrPontuacao(int nrPontuacao){
		this.nrPontuacao=nrPontuacao;
	}
	public int getNrPontuacao(){
		return this.nrPontuacao;
	}
	public void setNrCodDetran(int nrCodDetran){
		this.nrCodDetran=nrCodDetran;
	}
	public int getNrCodDetran(){
		return this.nrCodDetran;
	}
	public void setNrValorUfir(float nrValorUfir){
		this.nrValorUfir=nrValorUfir;
	}
	public float getNrValorUfir(){
		return this.nrValorUfir;
	}
	public void setNmNatureza(String nmNatureza){
		this.nmNatureza=nmNatureza;
	}
	public String getNmNatureza(){
		return this.nmNatureza;
	}
	public void setNrArtigo(String nrArtigo){
		this.nrArtigo=nrArtigo;
	}
	public String getNrArtigo(){
		return this.nrArtigo;
	}
	public void setNrParagrafo(String nrParagrafo){
		this.nrParagrafo=nrParagrafo;
	}
	public String getNrParagrafo(){
		return this.nrParagrafo;
	}
	public void setNrInciso(String nrInciso){
		this.nrInciso=nrInciso;
	}
	public String getNrInciso(){
		return this.nrInciso;
	}
	public void setNrAlinea(String nrAlinea){
		this.nrAlinea=nrAlinea;
	}
	public String getNrAlinea(){
		return this.nrAlinea;
	}
	public void setTpCompetencia(int tpCompetencia){
		this.tpCompetencia=tpCompetencia;
	}
	public int getTpCompetencia(){
		return this.tpCompetencia;
	}
	
	public float getVlInfracao() {
		return vlInfracao;
	}

	public void setVlInfracao(float vlInfracao) {
		this.vlInfracao = vlInfracao;
	}
	
	public int getLgPrioritaria() {
		return lgPrioritaria;
	}
	
	public void setLgPrioritaria(int lgPrioritaria) {
		this.lgPrioritaria = lgPrioritaria;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdInfracao: " +  getCdInfracao();
		valueToString += ", dsInfracao: " +  getDsInfracao();
		valueToString += ", nrPontuacao: " +  getNrPontuacao();
		valueToString += ", nrCodDetran: " +  getNrCodDetran();
		valueToString += ", nrValorUfir: " +  getNrValorUfir();
		valueToString += ", nmNatureza: " +  getNmNatureza();
		valueToString += ", nrArtigo: " +  getNrArtigo();
		valueToString += ", nrParagrafo: " +  getNrParagrafo();
		valueToString += ", nrInciso: " +  getNrInciso();
		valueToString += ", nrAlinea: " +  getNrAlinea();
		valueToString += ", tpCompetencia: " +  getTpCompetencia();
		valueToString += ", lgPrioritaria: " + getLgPrioritaria();
		valueToString += ", vlInfracao: " + getVlInfracao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Infracao(getCdInfracao(),
			getDsInfracao(),
			getNrPontuacao(),
			getNrCodDetran(),
			getNrValorUfir(),
			getNmNatureza(),
			getNrArtigo(),
			getNrParagrafo(),
			getNrInciso(),
			getNrAlinea(),
			getTpCompetencia(),
			getVlInfracao(),
			getLgPrioritaria());
	}

}