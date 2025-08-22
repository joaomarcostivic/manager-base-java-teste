package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class GeoTracker {

	private int cdTracker;
	private String idOrgao;
	private String idEquipamento;
	private String nrMatriculaAgente;
	private double vlLatitude;
	private double vlLongitude;
	private GregorianCalendar dtHistorico;
	private int cdOrgao;
	private int cdAgente;
	
	public GeoTracker(){ }

	public GeoTracker(int cdTracker,
			String idOrgao,
			String idEquipamento,
			String nrMatriculaAgente,
			double vlLatitude,
			double vlLongitude,
			GregorianCalendar dtHistorico){
		setCdTracker(cdTracker);
		setIdOrgao(idOrgao);
		setIdEquipamento(idEquipamento);
		setNrMatriculaAgente(nrMatriculaAgente);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setDtHistorico(dtHistorico);
	}
	
	public GeoTracker(int cdTracker,
			String idOrgao,
			String idEquipamento,
			String nrMatriculaAgente,
			double vlLatitude,
			double vlLongitude,
			GregorianCalendar dtHistorico,
			int cdOrgao,
			int cdAgente){
		setCdTracker(cdTracker);
		setIdOrgao(idOrgao);
		setIdEquipamento(idEquipamento);
		setNrMatriculaAgente(nrMatriculaAgente);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setDtHistorico(dtHistorico);
		setCdOrgao(cdOrgao);
		setCdAgente(cdAgente);
	}
	public void setCdTracker(int cdTracker){
		this.cdTracker=cdTracker;
	}
	public int getCdTracker(){
		return this.cdTracker;
	}
	public void setIdOrgao(String idOrgao){
		this.idOrgao=idOrgao;
	}
	public String getIdOrgao(){
		return this.idOrgao;
	}
	public void setIdEquipamento(String idEquipamento){
		this.idEquipamento=idEquipamento;
	}
	public String getIdEquipamento(){
		return this.idEquipamento;
	}
	public void setNrMatriculaAgente(String nrMatriculaAgente){
		this.nrMatriculaAgente=nrMatriculaAgente;
	}
	public String getNrMatriculaAgente(){
		return this.nrMatriculaAgente;
	}
	public void setVlLatitude(double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setDtHistorico(GregorianCalendar dtHistorico){
		this.dtHistorico=dtHistorico;
	}
	public GregorianCalendar getDtHistorico(){
		return this.dtHistorico;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTracker: " +  getCdTracker();
		valueToString += ", idOrgao: " +  getIdOrgao();
		valueToString += ", idEquipamento: " +  getIdEquipamento();
		valueToString += ", nrMatriculaAgente: " +  getNrMatriculaAgente();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", dtHistorico: " +  sol.util.Util.formatDateTime(getDtHistorico(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", cdAgente: " +  getCdAgente();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new GeoTracker(getCdTracker(),
			getIdOrgao(),
			getIdEquipamento(),
			getNrMatriculaAgente(),
			getVlLatitude(),
			getVlLongitude(),
			getDtHistorico()==null ? null : (GregorianCalendar)getDtHistorico().clone(),
			getCdOrgao(),
			getCdAgente());
	}

}