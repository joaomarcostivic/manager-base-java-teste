package com.tivic.manager.geo;

import java.util.ArrayList;

public class Camada {

	private int cdCamada;
	private String nmCamada;
	private String txtCamada;
	private int cdTipo;
	private int tpOrigem;
	private byte[] imgCamada;
	private int vlCor;
	private int cdMapa;

	private ArrayList<Referencia> referencias;
	
	public Camada(){ }

	public Camada(int cdCamada,
			String nmCamada,
			String txtCamada,
			int cdTipo,
			int tpOrigem,
			byte[] imgCamada,
			int vlCor,
			int cdMapa){
		setCdCamada(cdCamada);
		setNmCamada(nmCamada);
		setTxtCamada(txtCamada);
		setCdTipo(cdTipo);
		setTpOrigem(tpOrigem);
		setImgCamada(imgCamada);
		setVlCor(vlCor);
		setCdMapa(cdMapa);
	}
	public void setCdCamada(int cdCamada){
		this.cdCamada=cdCamada;
	}
	public int getCdCamada(){
		return this.cdCamada;
	}
	public void setNmCamada(String nmCamada){
		this.nmCamada=nmCamada;
	}
	public String getNmCamada(){
		return this.nmCamada;
	}
	public void setTxtCamada(String txtCamada){
		this.txtCamada=txtCamada;
	}
	public String getTxtCamada(){
		return this.txtCamada;
	}
	public void setCdTipo(int cdTipo){
		this.cdTipo=cdTipo;
	}
	public int getCdTipo(){
		return this.cdTipo;
	}
	public void setTpOrigem(int tpOrigem){
		this.tpOrigem=tpOrigem;
	}
	public int getTpOrigem(){
		return this.tpOrigem;
	}
	public void setImgCamada(byte[] imgCamada){
		this.imgCamada=imgCamada;
	}
	public byte[] getImgCamada(){
		return this.imgCamada;
	}
	public void setVlCor(int vlCor){
		this.vlCor=vlCor;
	}
	public int getVlCor(){
		return this.vlCor;
	}
	public void setCdMapa(int cdMapa){
		this.cdMapa=cdMapa;
	}
	public int getCdMapa(){
		return this.cdMapa;
	}
	public void setReferencias(ArrayList<Referencia> referencias) {
		this.referencias = referencias;
	}
	public ArrayList<Referencia> getReferencias() {
		return referencias;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdCamada: " +  getCdCamada();
		valueToString += ", nmCamada: " +  getNmCamada();
		valueToString += ", txtCamada: " +  getTxtCamada();
		valueToString += ", cdTipo: " +  getCdTipo();
		valueToString += ", tpOrigem: " +  getTpOrigem();
		valueToString += ", imgCamada: " +  getImgCamada();
		valueToString += ", vlCor: " +  getVlCor();
		valueToString += ", cdMapa: " +  getCdMapa();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Camada(getCdCamada(),
			getNmCamada(),
			getTxtCamada(),
			getCdTipo(),
			getTpOrigem(),
			getImgCamada(),
			getVlCor(),
			getCdMapa());
	}

}
