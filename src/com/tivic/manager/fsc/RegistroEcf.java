package com.tivic.manager.fsc;

import java.util.GregorianCalendar;

public class RegistroEcf {

	private int cdRegistroEcf;
	private String tpRegistroEcf;
	private GregorianCalendar dtRegistroEcf;
	private float vlRegistroEcf;
	private String txtRegistroEcf;
	private int cdReferenciaEcf;
	private int lgSped;
	private String status;

	public RegistroEcf(int cdRegistroEcf,
			String tpRegistroEcf,
			GregorianCalendar dtRegistroEcf,
			float vlRegistroEcf,
			String txtRegistroEcf,
			int cdReferenciaEcf,
			int lgSped,
			String status){
		setCdRegistroEcf(cdRegistroEcf);
		setTpRegistroEcf(tpRegistroEcf);
		setDtRegistroEcf(dtRegistroEcf);
		setVlRegistroEcf(vlRegistroEcf);
		setTxtRegistroEcf(txtRegistroEcf);
		setCdReferenciaEcf(cdReferenciaEcf);
		setLgSped(lgSped);
		setStatus(status);
	}
	public void setCdRegistroEcf(int cdRegistroEcf){
		this.cdRegistroEcf=cdRegistroEcf;
	}
	public int getCdRegistroEcf(){
		return this.cdRegistroEcf;
	}
	public void setTpRegistroEcf(String tpRegistroEcf){
		this.tpRegistroEcf=tpRegistroEcf;
	}
	public String getTpRegistroEcf(){
		return this.tpRegistroEcf;
	}
	public void setDtRegistroEcf(GregorianCalendar dtRegistroEcf){
		this.dtRegistroEcf=dtRegistroEcf;
	}
	public GregorianCalendar getDtRegistroEcf(){
		return this.dtRegistroEcf;
	}
	public void setVlRegistroEcf(float vlRegistroEcf){
		this.vlRegistroEcf=vlRegistroEcf;
	}
	public float getVlRegistroEcf(){
		return this.vlRegistroEcf;
	}
	public void setTxtRegistroEcf(String txtRegistroEcf){
		this.txtRegistroEcf=txtRegistroEcf;
	}
	public String getTxtRegistroEcf(){
		return this.txtRegistroEcf;
	}
	public void setCdReferenciaEcf(int cdReferenciaEcf){
		this.cdReferenciaEcf=cdReferenciaEcf;
	}
	public int getCdReferenciaEcf(){
		return this.cdReferenciaEcf;
	}
	public void setLgSped(int lgSped){
		this.lgSped=lgSped;
	}
	public int getLgSped(){
		return this.lgSped;
	}
	public void setStatus(String status){
		this.status=status;
	}
	public String getStatus(){
		return this.status;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdRegistroEcf: " +  getCdRegistroEcf();
		valueToString += ", tpRegistroEcf: " +  getTpRegistroEcf();
		valueToString += ", dtRegistroEcf: " +  sol.util.Util.formatDateTime(getDtRegistroEcf(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlRegistroEcf: " +  getVlRegistroEcf();
		valueToString += ", txtRegistroEcf: " +  getTxtRegistroEcf();
		valueToString += ", cdReferenciaEcf: " +  getCdReferenciaEcf();
		valueToString += ", lgSped: " +  getLgSped();
		valueToString += ", status: " + getStatus();
		return "{" + valueToString + "}";
	}
	
	public Object clone() {
		return new RegistroEcf(getCdRegistroEcf(),
			getTpRegistroEcf(),
			getDtRegistroEcf()==null ? null : (GregorianCalendar)getDtRegistroEcf().clone(),
			getVlRegistroEcf(),
			getTxtRegistroEcf(),
			getCdReferenciaEcf(),
			getLgSped(),
			getStatus());
	}

}