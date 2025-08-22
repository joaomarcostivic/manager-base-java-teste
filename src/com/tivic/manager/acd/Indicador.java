package com.tivic.manager.acd;

public class Indicador {

	private int cdIndicador;
	private String nmIndicador;
	private String txtIndicador;
	private String idIndicador;
	private int tpIndicador;

	public Indicador() { }

	public Indicador(int cdIndicador,
			String nmIndicador,
			String txtIndicador,
			String idIndicador,
			int tpIndicador) {
		setCdIndicador(cdIndicador);
		setNmIndicador(nmIndicador);
		setTxtIndicador(txtIndicador);
		setIdIndicador(idIndicador);
		setTpIndicador(tpIndicador);
	}
	public void setCdIndicador(int cdIndicador){
		this.cdIndicador=cdIndicador;
	}
	public int getCdIndicador(){
		return this.cdIndicador;
	}
	public void setNmIndicador(String nmIndicador){
		this.nmIndicador=nmIndicador;
	}
	public String getNmIndicador(){
		return this.nmIndicador;
	}
	public void setTxtIndicador(String txtIndicador){
		this.txtIndicador=txtIndicador;
	}
	public String getTxtIndicador(){
		return this.txtIndicador;
	}
	public void setIdIndicador(String idIndicador){
		this.idIndicador=idIndicador;
	}
	public String getIdIndicador(){
		return this.idIndicador;
	}
	public void setTpIndicador(int tpIndicador){
		this.tpIndicador=tpIndicador;
	}
	public int getTpIndicador(){
		return this.tpIndicador;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdIndicador: " +  getCdIndicador();
		valueToString += ", nmIndicador: " +  getNmIndicador();
		valueToString += ", txtIndicador: " +  getTxtIndicador();
		valueToString += ", idIndicador: " +  getIdIndicador();
		valueToString += ", tpIndicador: " +  getTpIndicador();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Indicador(getCdIndicador(),
			getNmIndicador(),
			getTxtIndicador(),
			getIdIndicador(),
			getTpIndicador());
	}

}