package com.tivic.manager.fta;

public class ModeloPneu {

	private int cdModelo;
	private String nmModelo;
	private int nrAro;
	private String nrLargura;
	private String nrAltura;
	private int qtVidaUtil;

	public ModeloPneu(int cdModelo,
			String nmModelo,
			int nrAro,
			String nrLargura,
			String nrAltura,
			int qtVidaUtil){
		setCdModelo(cdModelo);
		setNmModelo(nmModelo);
		setNrAro(nrAro);
		setNrLargura(nrLargura);
		setNrAltura(nrAltura);
		setQtVidaUtil(qtVidaUtil);
	}
	public void setCdModelo(int cdModelo){
		this.cdModelo=cdModelo;
	}
	public int getCdModelo(){
		return this.cdModelo;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setNrAro(int nrAro){
		this.nrAro=nrAro;
	}
	public int getNrAro(){
		return this.nrAro;
	}
	public void setNrLargura(String nrLargura){
		this.nrLargura=nrLargura;
	}
	public String getNrLargura(){
		return this.nrLargura;
	}
	public void setNrAltura(String nrAltura){
		this.nrAltura=nrAltura;
	}
	public String getNrAltura(){
		return this.nrAltura;
	}
	public void setQtVidaUtil(int qtVidaUtil){
		this.qtVidaUtil=qtVidaUtil;
	}
	public int getQtVidaUtil(){
		return this.qtVidaUtil;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdModelo: " +  getCdModelo();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", nrAro: " +  getNrAro();
		valueToString += ", nrLargura: " +  getNrLargura();
		valueToString += ", nrAltura: " +  getNrAltura();
		valueToString += ", qtVidaUtil: " +  getQtVidaUtil();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ModeloPneu(getCdModelo(),
			getNmModelo(),
			getNrAro(),
			getNrLargura(),
			getNrAltura(),
			getQtVidaUtil());
	}

}