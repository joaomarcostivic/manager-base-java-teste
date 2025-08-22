package com.tivic.manager.adm;

import java.util.GregorianCalendar;

public class Turno {

	private int cdTurno;
	private String nmTurno;
	private String idTurno;
	private GregorianCalendar hrInicioTurno;
	private GregorianCalendar hrFinalTurno;
	private int nrOrdem;

	public Turno(int cdTurno,
			String nmTurno,
			String idTurno,
			GregorianCalendar hrInicioTurno,
			GregorianCalendar hrFinalTurno,
			int nrOrdem){
		setCdTurno(cdTurno);
		setNmTurno(nmTurno);
		setIdTurno(idTurno);
		setHrInicioTurno(hrInicioTurno);
		setHrFinalTurno(hrFinalTurno);
		setNrOrdem(nrOrdem);
	}
	public void setCdTurno(int cdTurno){
		this.cdTurno=cdTurno;
	}
	public int getCdTurno(){
		return this.cdTurno;
	}
	public void setNmTurno(String nmTurno){
		this.nmTurno=nmTurno;
	}
	public String getNmTurno(){
		return this.nmTurno;
	}
	public void setIdTurno(String idTurno){
		this.idTurno=idTurno;
	}
	public String getIdTurno(){
		return this.idTurno;
	}
	public void setHrInicioTurno(GregorianCalendar hrInicioTurno){
		this.hrInicioTurno=hrInicioTurno;
	}
	public GregorianCalendar getHrInicioTurno(){
		return this.hrInicioTurno;
	}
	public void setHrFinalTurno(GregorianCalendar hrFinalTurno){
		this.hrFinalTurno=hrFinalTurno;
	}
	public GregorianCalendar getHrFinalTurno(){
		return this.hrFinalTurno;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdTurno: " +  getCdTurno();
		valueToString += ", nmTurno: " +  getNmTurno();
		valueToString += ", idTurno: " +  getIdTurno();
		valueToString += ", hrInicioTurno: " +  sol.util.Util.formatDateTime(getHrInicioTurno(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrFinalTurno: " +  sol.util.Util.formatDateTime(getHrFinalTurno(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrOrdem: " +  getNrOrdem();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Turno(getCdTurno(),
			getNmTurno(),
			getIdTurno(),
			getHrInicioTurno()==null ? null : (GregorianCalendar)getHrInicioTurno().clone(),
			getHrFinalTurno()==null ? null : (GregorianCalendar)getHrFinalTurno().clone(),
			getNrOrdem());
	}

}
