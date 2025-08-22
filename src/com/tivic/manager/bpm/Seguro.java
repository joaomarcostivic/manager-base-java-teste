package com.tivic.manager.bpm;

public class Seguro {

	private int cdContrato;
	private int cdCorretora;
	private String nrApolice;
	private String nrProposta;
	private float vlFranquia;
	private String nrClasseBonus;
	private float vlCoberturaCasco;
	private float vlCoberturaDm;
	private float vlCoberturaDp;
	private String txtObservacao;
	private int cdReferencia;

	public Seguro(int cdContrato,
			int cdCorretora,
			String nrApolice,
			String nrProposta,
			float vlFranquia,
			String nrClasseBonus,
			float vlCoberturaCasco,
			float vlCoberturaDm,
			float vlCoberturaDp,
			String txtObservacao,
			int cdReferencia){
		setCdContrato(cdContrato);
		setCdCorretora(cdCorretora);
		setNrApolice(nrApolice);
		setNrProposta(nrProposta);
		setVlFranquia(vlFranquia);
		setNrClasseBonus(nrClasseBonus);
		setVlCoberturaCasco(vlCoberturaCasco);
		setVlCoberturaDm(vlCoberturaDm);
		setVlCoberturaDp(vlCoberturaDp);
		setTxtObservacao(txtObservacao);
		setCdReferencia(cdReferencia);
	}
	public void setCdContrato(int cdContrato){
		this.cdContrato=cdContrato;
	}
	public int getCdContrato(){
		return this.cdContrato;
	}
	public void setCdCorretora(int cdCorretora){
		this.cdCorretora=cdCorretora;
	}
	public int getCdCorretora(){
		return this.cdCorretora;
	}
	public void setNrApolice(String nrApolice){
		this.nrApolice=nrApolice;
	}
	public String getNrApolice(){
		return this.nrApolice;
	}
	public void setNrProposta(String nrProposta){
		this.nrProposta=nrProposta;
	}
	public String getNrProposta(){
		return this.nrProposta;
	}
	public void setVlFranquia(float vlFranquia){
		this.vlFranquia=vlFranquia;
	}
	public float getVlFranquia(){
		return this.vlFranquia;
	}
	public void setNrClasseBonus(String nrClasseBonus){
		this.nrClasseBonus=nrClasseBonus;
	}
	public String getNrClasseBonus(){
		return this.nrClasseBonus;
	}
	public void setVlCoberturaCasco(float vlCoberturaCasco){
		this.vlCoberturaCasco=vlCoberturaCasco;
	}
	public float getVlCoberturaCasco(){
		return this.vlCoberturaCasco;
	}
	public void setVlCoberturaDm(float vlCoberturaDm){
		this.vlCoberturaDm=vlCoberturaDm;
	}
	public float getVlCoberturaDm(){
		return this.vlCoberturaDm;
	}
	public void setVlCoberturaDp(float vlCoberturaDp){
		this.vlCoberturaDp=vlCoberturaDp;
	}
	public float getVlCoberturaDp(){
		return this.vlCoberturaDp;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdReferencia(int cdReferencia){
		this.cdReferencia=cdReferencia;
	}
	public int getCdReferencia(){
		return this.cdReferencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdContrato: " +  getCdContrato();
		valueToString += ", cdCorretora: " +  getCdCorretora();
		valueToString += ", nrApolice: " +  getNrApolice();
		valueToString += ", nrProposta: " +  getNrProposta();
		valueToString += ", vlFranquia: " +  getVlFranquia();
		valueToString += ", nrClasseBonus: " +  getNrClasseBonus();
		valueToString += ", vlCoberturaCasco: " +  getVlCoberturaCasco();
		valueToString += ", vlCoberturaDm: " +  getVlCoberturaDm();
		valueToString += ", vlCoberturaDp: " +  getVlCoberturaDp();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdReferencia: " +  getCdReferencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Seguro(cdContrato,
			cdCorretora,
			nrApolice,
			nrProposta,
			vlFranquia,
			nrClasseBonus,
			vlCoberturaCasco,
			vlCoberturaDm,
			vlCoberturaDp,
			txtObservacao,
			cdReferencia);
	}

}