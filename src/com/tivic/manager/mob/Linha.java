package com.tivic.manager.mob;

public class Linha {

	private int cdLinha;
	private int cdConcessao;
	private String nrLinha;
	private String idLinha;
	private int tpLinha;
	private int qtCiclos;
	private int stLinha;

	public Linha(){ }

	public Linha(int cdLinha,
			int cdConcessao,
			String nrLinha,
			String idLinha,
			int tpLinha,
			int qtCiclos,
			int stLinha){
		setCdLinha(cdLinha);
		setCdConcessao(cdConcessao);
		setNrLinha(nrLinha);
		setIdLinha(idLinha);
		setTpLinha(tpLinha);
		setQtCiclos(qtCiclos);
		setStLinha(stLinha);
	}
	public void setCdLinha(int cdLinha){
		this.cdLinha=cdLinha;
	}
	public int getCdLinha(){
		return this.cdLinha;
	}
	public void setCdConcessao(int cdConcessao){
		this.cdConcessao=cdConcessao;
	}
	public int getCdConcessao(){
		return this.cdConcessao;
	}
	public void setNrLinha(String nrLinha){
		this.nrLinha=nrLinha;
	}
	public String getNrLinha(){
		return this.nrLinha;
	}
	public void setIdLinha(String idLinha){
		this.idLinha=idLinha;
	}
	public String getIdLinha(){
		return this.idLinha;
	}
	public void setTpLinha(int tpLinha){
		this.tpLinha=tpLinha;
	}
	public int getTpLinha(){
		return this.tpLinha;
	}
	public void setQtCiclos(int qtCiclos){
		this.qtCiclos=qtCiclos;
	}
	public int getQtCiclos(){
		return this.qtCiclos;
	}
	public void setStLinha(int stLinha){
		this.stLinha=stLinha;
	}
	public int getStLinha(){
		return this.stLinha;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLinha: " +  getCdLinha();
		valueToString += ", cdConcessao: " +  getCdConcessao();
		valueToString += ", nrLinha: " +  getNrLinha();
		valueToString += ", idLinha: " +  getIdLinha();
		valueToString += ", tpLinha: " +  getTpLinha();
		valueToString += ", qtCiclos: " +  getQtCiclos();
		valueToString += ", stLinha: " +  getStLinha();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Linha(getCdLinha(),
			getCdConcessao(),
			getNrLinha(),
			getIdLinha(),
			getTpLinha(),
			getQtCiclos(),
			getStLinha());
	}

}