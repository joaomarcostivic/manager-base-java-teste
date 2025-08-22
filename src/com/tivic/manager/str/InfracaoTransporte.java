package com.tivic.manager.str;

public class InfracaoTransporte {

	private int cdInfracao;
	private String dsInfracao;
	private int nrPontuacao;
	private float nrValorUfir;
	private String nmNatureza;
	private String nrArtigo;
	private String nrParagrafo;
	private String nrInciso;
	private String nrAlinea;
	private int tpConcessao;
	private String nrInfracao;

	public InfracaoTransporte(){ }

	public InfracaoTransporte(int cdInfracao,
			String dsInfracao,
			int nrPontuacao,
			float nrValorUfir,
			String nmNatureza,
			String nrArtigo,
			String nrParagrafo,
			String nrInciso,
			String nrAlinea,
			int tpConcessao,
			String nrInfracao){
		setCdInfracao(cdInfracao);
		setDsInfracao(dsInfracao);
		setNrPontuacao(nrPontuacao);
		setNrValorUfir(nrValorUfir);
		setNmNatureza(nmNatureza);
		setNrArtigo(nrArtigo);
		setNrParagrafo(nrParagrafo);
		setNrInciso(nrInciso);
		setNrAlinea(nrAlinea);
		setTpConcessao(tpConcessao);
		setNrInfracao(nrInfracao);
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
	public void setTpConcessao(int tpConcessao){
		this.tpConcessao=tpConcessao;
	}
	public int getTpConcessao(){
		return this.tpConcessao;
	}
	public void setNrInfracao(String nrInfracao){
		this.nrInfracao=nrInfracao;
	}
	public String getNrInfracao(){
		return this.nrInfracao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdInfracao: " +  getCdInfracao();
		valueToString += ", dsInfracao: " +  getDsInfracao();
		valueToString += ", nrPontuacao: " +  getNrPontuacao();
		valueToString += ", nrValorUfir: " +  getNrValorUfir();
		valueToString += ", nmNatureza: " +  getNmNatureza();
		valueToString += ", nrArtigo: " +  getNrArtigo();
		valueToString += ", nrParagrafo: " +  getNrParagrafo();
		valueToString += ", nrInciso: " +  getNrInciso();
		valueToString += ", nrAlinea: " +  getNrAlinea();
		valueToString += ", tpConcessao: " +  getTpConcessao();
		valueToString += ", nrInfracao: " +  getNrInfracao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new InfracaoTransporte(getCdInfracao(),
			getDsInfracao(),
			getNrPontuacao(),
			getNrValorUfir(),
			getNmNatureza(),
			getNrArtigo(),
			getNrParagrafo(),
			getNrInciso(),
			getNrAlinea(),
			getTpConcessao(),
			getNrInfracao());
	}

}
