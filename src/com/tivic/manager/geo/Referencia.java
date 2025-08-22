package com.tivic.manager.geo;

import java.util.ArrayList;

public class Referencia {

	private int cdReferencia;
	private String nmReferencia;
	private int tpReferencia;
	private int tpRepresentacao;
	private String txtObservacao;
	private byte[] imgReferencia;
	private int vlCor;

	private ArrayList<Ponto> pontos;
	
	public Referencia(){ }

	public Referencia(int cdReferencia,
			String nmReferencia,
			int tpReferencia,
			int tpRepresentacao,
			String txtObservacao,
			byte[] imgReferencia,
			int vlCor){
		setCdReferencia(cdReferencia);
		setNmReferencia(nmReferencia);
		setTpReferencia(tpReferencia);
		setTpRepresentacao(tpRepresentacao);
		setTxtObservacao(txtObservacao);
		setImgReferencia(imgReferencia);
		setVlCor(vlCor);
	}
	
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public void setNmReferencia(String nmReferencia){
		this.nmReferencia=nmReferencia;
	}
	public String getNmReferencia(){
		return this.nmReferencia;
	}
	public void setTpReferencia(int tpReferencia){
		this.tpReferencia=tpReferencia;
	}
	public int getTpReferencia(){
		return this.tpReferencia;
	}
	public void setTpRepresentacao(int tpRepresentacao){
		this.tpRepresentacao=tpRepresentacao;
	}
	public int getTpRepresentacao(){
		return this.tpRepresentacao;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setImgReferencia(byte[] imgReferencia){
		this.imgReferencia=imgReferencia;
	}
	public byte[] getImgReferencia(){
		return this.imgReferencia;
	}
	public void setVlCor(int vlCor){
		this.vlCor=vlCor;
	}
	public int getVlCor(){
		return this.vlCor;
	}
	public void setPontos(ArrayList<Ponto> pontos) {
		this.pontos = pontos;
	}
	public ArrayList<Ponto> getPontos() {
		return pontos;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdReferencia: " +  getCdReferencia();
		valueToString += ", nmReferencia: " +  getNmReferencia();
		valueToString += ", tpReferencia: " +  getTpReferencia();
		valueToString += ", tpRepresentacao: " +  getTpRepresentacao();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", imgReferencia: " +  getImgReferencia();
		valueToString += ", vlCor: " +  getVlCor();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Referencia(getCdReferencia(),
			getNmReferencia(),
			getTpReferencia(),
			getTpRepresentacao(),
			getTxtObservacao(),
			getImgReferencia(),
			getVlCor());
	}

}
