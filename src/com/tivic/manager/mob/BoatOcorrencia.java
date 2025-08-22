package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class BoatOcorrencia {

	private int cdBoatOcorrencia;
	private int cdBoat;
	private int cdAgente;
	private GregorianCalendar dtOcorrencia;
	private String dsOcorrencia;
	private int cdOcorrencia;

	public BoatOcorrencia() { }

	public BoatOcorrencia(int cdBoatOcorrencia,
			int cdBoat,
			int cdAgente,
			GregorianCalendar dtOcorrencia,
			String dsOcorrencia,
			int cdOcorrencia) {
		setCdBoatOcorrencia(cdBoatOcorrencia);
		setCdBoat(cdBoat);
		setCdAgente(cdAgente);
		setDtOcorrencia(dtOcorrencia);
		setDsOcorrencia(dsOcorrencia);
		setCdOcorrencia(cdOcorrencia);
	}
	public void setCdBoatOcorrencia(int cdBoatOcorrencia){
		this.cdBoatOcorrencia=cdBoatOcorrencia;
	}
	public int getCdBoatOcorrencia(){
		return this.cdBoatOcorrencia;
	}
	public void setCdBoat(int cdBoat){
		this.cdBoat=cdBoat;
	}
	public int getCdBoat(){
		return this.cdBoat;
	}
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setDtOcorrencia(GregorianCalendar dtOcorrencia){
		this.dtOcorrencia=dtOcorrencia;
	}
	public GregorianCalendar getDtOcorrencia(){
		return this.dtOcorrencia;
	}
	public void setDsOcorrencia(String dsOcorrencia){
		this.dsOcorrencia=dsOcorrencia;
	}
	public String getDsOcorrencia(){
		return this.dsOcorrencia;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdBoatOcorrencia: " +  getCdBoatOcorrencia();
		valueToString += ", cdBoat: " +  getCdBoat();
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", dtOcorrencia: " +  sol.util.Util.formatDateTime(getDtOcorrencia(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dsOcorrencia: " +  getDsOcorrencia();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new BoatOcorrencia(getCdBoatOcorrencia(),
			getCdBoat(),
			getCdAgente(),
			getDtOcorrencia()==null ? null : (GregorianCalendar)getDtOcorrencia().clone(),
			getDsOcorrencia(),
			getCdOcorrencia());
	}

}