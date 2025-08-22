package com.tivic.manager.ptc;

public class Fase {

	private int cdFase;
	private String nmFase;
	private String idFase;
	private int cdSetor;
	private int cdEmpresa;
	private String nrFaseExterna;
	private int nrOrdem;

	public Fase() { }
			
	public Fase(int cdFase,
			String nmFase,
			String idFase,
			int cdSetor,
			int cdEmpresa,
			String nrFaseExterna,
			int nrOrdem){
		setCdFase(cdFase);
		setNmFase(nmFase);
		setIdFase(idFase);
		setCdSetor(cdSetor);
		setCdEmpresa(cdEmpresa);
		setNrFaseExterna(nrFaseExterna);
		setNrOrdem(nrOrdem);
	}
	public void setCdFase(int cdFase){
		this.cdFase=cdFase;
	}
	public int getCdFase(){
		return this.cdFase;
	}
	public void setNmFase(String nmFase){
		this.nmFase=nmFase;
	}
	public String getNmFase(){
		return this.nmFase;
	}
	public void setIdFase(String idFase){
		this.idFase=idFase;
	}
	public String getIdFase(){
		return this.idFase;
	}
	public void setCdSetor(int cdSetor){
		this.cdSetor=cdSetor;
	}
	public int getCdSetor(){
		return this.cdSetor;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setNrFaseExterna(String nrFaseExterna){
		this.nrFaseExterna=nrFaseExterna;
	}
	public String getNrFaseExterna(){
		return this.nrFaseExterna;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFase: " +  getCdFase();
		valueToString += ", nmFase: " +  getNmFase();
		valueToString += ", idFase: " +  getIdFase();
		valueToString += ", cdSetor: " +  getCdSetor();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", nrFaseExterna: " + getNrFaseExterna();
		valueToString += ", nrOrdem: " + getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Fase(getCdFase(),
			getNmFase(),
			getIdFase(),
			getCdSetor(),
			getCdEmpresa(),
			getNrFaseExterna(),
			getNrOrdem());
	}

}