package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class AitTranporteMovimento {

	private int cdAitTranporteMovimento;
	private int cdAitTranporte;
	private GregorianCalendar dtMovimento;
	private int tpStatus;

	public AitTranporteMovimento(){ }

	public AitTranporteMovimento(int cdAitTranporteMovimento,
			int cdAitTranporte,
			GregorianCalendar dtMovimento,
			int tpStatus){
		setCdAitTranporteMovimento(cdAitTranporteMovimento);
		setCdAitTranporte(cdAitTranporte);
		setDtMovimento(dtMovimento);
		setTpStatus(tpStatus);
	}
	public void setCdAitTranporteMovimento(int cdAitTranporteMovimento){
		this.cdAitTranporteMovimento=cdAitTranporteMovimento;
	}
	public int getCdAitTranporteMovimento(){
		return this.cdAitTranporteMovimento;
	}
	public void setCdAitTranporte(int cdAitTranporte){
		this.cdAitTranporte=cdAitTranporte;
	}
	public int getCdAitTranporte(){
		return this.cdAitTranporte;
	}
	public void setDtMovimento(GregorianCalendar dtMovimento){
		this.dtMovimento=dtMovimento;
	}
	public GregorianCalendar getDtMovimento(){
		return this.dtMovimento;
	}
	public void setTpStatus(int tpStatus){
		this.tpStatus=tpStatus;
	}
	public int getTpStatus(){
		return this.tpStatus;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAitTranporteMovimento: " +  getCdAitTranporteMovimento();
		valueToString += ", cdAitTranporte: " +  getCdAitTranporte();
		valueToString += ", dtMovimento: " +  sol.util.Util.formatDateTime(getDtMovimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpStatus: " +  getTpStatus();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitTranporteMovimento(getCdAitTranporteMovimento(),
			getCdAitTranporte(),
			getDtMovimento()==null ? null : (GregorianCalendar)getDtMovimento().clone(),
			getTpStatus());
	}

}