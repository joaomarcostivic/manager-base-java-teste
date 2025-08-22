package com.tivic.manager.blb;

public class Penalidade {

	private int cdPenalidade;
	private String nmPenalidade;
	private int tpPenalidade;
	private int nrDiasAtraso;
	private float vlPenalidade;
	private String txtAdvertencia;
	private int tpIncidencia;
	private int nrDiasPenalidade;
	private int nrDiasMulta;

	public Penalidade(){ }

	public Penalidade(int cdPenalidade,
			String nmPenalidade,
			int tpPenalidade,
			int nrDiasAtraso,
			float vlPenalidade,
			String txtAdvertencia,
			int tpIncidencia,
			int nrDiasPenalidade,
			int nrDiasMulta){
		setCdPenalidade(cdPenalidade);
		setNmPenalidade(nmPenalidade);
		setTpPenalidade(tpPenalidade);
		setNrDiasAtraso(nrDiasAtraso);
		setVlPenalidade(vlPenalidade);
		setTxtAdvertencia(txtAdvertencia);
		setTpIncidencia(tpIncidencia);
		setNrDiasPenalidade(nrDiasPenalidade);
		setNrDiasMulta(nrDiasMulta);
	}
	public void setCdPenalidade(int cdPenalidade){
		this.cdPenalidade=cdPenalidade;
	}
	public int getCdPenalidade(){
		return this.cdPenalidade;
	}
	public void setNmPenalidade(String nmPenalidade){
		this.nmPenalidade=nmPenalidade;
	}
	public String getNmPenalidade(){
		return this.nmPenalidade;
	}
	public void setTpPenalidade(int tpPenalidade){
		this.tpPenalidade=tpPenalidade;
	}
	public int getTpPenalidade(){
		return this.tpPenalidade;
	}
	public void setNrDiasAtraso(int nrDiasAtraso){
		this.nrDiasAtraso=nrDiasAtraso;
	}
	public int getNrDiasAtraso(){
		return this.nrDiasAtraso;
	}
	public void setVlPenalidade(float vlPenalidade){
		this.vlPenalidade=vlPenalidade;
	}
	public float getVlPenalidade(){
		return this.vlPenalidade;
	}
	public void setTxtAdvertencia(String txtAdvertencia){
		this.txtAdvertencia=txtAdvertencia;
	}
	public String getTxtAdvertencia(){
		return this.txtAdvertencia;
	}
	public void setTpIncidencia(int tpIncidencia){
		this.tpIncidencia=tpIncidencia;
	}
	public int getTpIncidencia(){
		return this.tpIncidencia;
	}
	public void setNrDiasPenalidade(int nrDiasPenalidade){
		this.nrDiasPenalidade=nrDiasPenalidade;
	}
	public int getNrDiasPenalidade(){
		return this.nrDiasPenalidade;
	}
	public void setNrDiasMulta(int nrDiasMulta){
		this.nrDiasMulta=nrDiasMulta;
	}
	public int getNrDiasMulta(){
		return this.nrDiasMulta;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPenalidade: " +  getCdPenalidade();
		valueToString += ", nmPenalidade: " +  getNmPenalidade();
		valueToString += ", tpPenalidade: " +  getTpPenalidade();
		valueToString += ", nrDiasAtraso: " +  getNrDiasAtraso();
		valueToString += ", vlPenalidade: " +  getVlPenalidade();
		valueToString += ", txtAdvertencia: " +  getTxtAdvertencia();
		valueToString += ", tpIncidencia: " +  getTpIncidencia();
		valueToString += ", nrDiasPenalidade: " +  getNrDiasPenalidade();
		valueToString += ", nrDiasMulta: " +  getNrDiasMulta();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Penalidade(getCdPenalidade(),
			getNmPenalidade(),
			getTpPenalidade(),
			getNrDiasAtraso(),
			getVlPenalidade(),
			getTxtAdvertencia(),
			getTpIncidencia(),
			getNrDiasPenalidade(),
			getNrDiasMulta());
	}

}