package com.tivic.manager.prc;

import java.util.GregorianCalendar;

public class ProcessoSentenca {

	private int cdSentenca;
	private int cdProcesso;
	private int tpSentenca;
	private GregorianCalendar dtSentenca;
	private Double vlSentenca;
	private Double vlAcordo;
	private Double vlTotal;

	public ProcessoSentenca() { }

	public ProcessoSentenca(int cdSentenca,
			int cdProcesso,
			int tpSentenca,
			GregorianCalendar dtSentenca,
			Double vlSentenca,
			Double vlAcordo,
			Double vlTotal) {
		setCdSentenca(cdSentenca);
		setCdProcesso(cdProcesso);
		setTpSentenca(tpSentenca);
		setDtSentenca(dtSentenca);
		setVlSentenca(vlSentenca);
		setVlAcordo(vlAcordo);
		setVlTotal(vlTotal);
	}
	public void setCdSentenca(int cdSentenca){
		this.cdSentenca=cdSentenca;
	}
	public int getCdSentenca(){
		return this.cdSentenca;
	}
	public void setCdProcesso(int cdProcesso){
		this.cdProcesso=cdProcesso;
	}
	public int getCdProcesso(){
		return this.cdProcesso;
	}
	public void setTpSentenca(int tpSentenca){
		this.tpSentenca=tpSentenca;
	}
	public int getTpSentenca(){
		return this.tpSentenca;
	}
	public void setDtSentenca(GregorianCalendar dtSentenca){
		this.dtSentenca=dtSentenca;
	}
	public GregorianCalendar getDtSentenca(){
		return this.dtSentenca;
	}
	public void setVlSentenca(Double vlSentenca){
		this.vlSentenca=vlSentenca;
	}
	public Double getVlSentenca(){
		return this.vlSentenca;
	}
	public void setVlAcordo(Double vlAcordo){
		this.vlAcordo=vlAcordo;
	}
	public Double getVlAcordo(){
		return this.vlAcordo;
	}
	public void setVlTotal(Double vlTotal){
		this.vlTotal=vlTotal;
	}
	public Double getVlTotal(){
		return this.vlTotal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdSentenca: " +  getCdSentenca();
		valueToString += ", cdProcesso: " +  getCdProcesso();
		valueToString += ", tpSentenca: " +  getTpSentenca();
		valueToString += ", dtSentenca: " +  sol.util.Util.formatDateTime(getDtSentenca(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", vlSentenca: " +  getVlSentenca();
		valueToString += ", vlAcordo: " +  getVlAcordo();
		valueToString += ", vlTotal: " +  getVlTotal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new ProcessoSentenca(getCdSentenca(),
			getCdProcesso(),
			getTpSentenca(),
			getDtSentenca()==null ? null : (GregorianCalendar)getDtSentenca().clone(),
			getVlSentenca(),
			getVlAcordo(),
			getVlTotal());
	}

}