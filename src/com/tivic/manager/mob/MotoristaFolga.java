package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class MotoristaFolga {

	private int cdFolga;
	private int cdMotorista;
	private String txtMotivo;
	private GregorianCalendar dtRegistro;
	private int qtDias;

	public MotoristaFolga() { }

	public MotoristaFolga(int cdFolga,
			int cdMotorista,
			String txtMotivo,
			GregorianCalendar dtRegistro,
			int qtDias) {
		setCdFolga(cdFolga);
		setCdMotorista(cdMotorista);
		setTxtMotivo(txtMotivo);
		setDtRegistro(dtRegistro);
		setQtDias(qtDias);
	}
	public void setCdFolga(int cdFolga){
		this.cdFolga=cdFolga;
	}
	public int getCdFolga(){
		return this.cdFolga;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setTxtMotivo(String txtMotivo){
		this.txtMotivo=txtMotivo;
	}
	public String getTxtMotivo(){
		return this.txtMotivo;
	}
	public void setDtRegistro(GregorianCalendar dtRegistro){
		this.dtRegistro=dtRegistro;
	}
	public GregorianCalendar getDtRegistro(){
		return this.dtRegistro;
	}
	public void setQtDias(int qtDias){
		this.qtDias=qtDias;
	}
	public int getQtDias(){
		return this.qtDias;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFolga: " +  getCdFolga();
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", txtMotivo: " +  getTxtMotivo();
		valueToString += ", dtRegistro: " +  sol.util.Util.formatDateTime(getDtRegistro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", qtDias: " +  getQtDias();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotoristaFolga(getCdFolga(),
			getCdMotorista(),
			getTxtMotivo(),
			getDtRegistro()==null ? null : (GregorianCalendar)getDtRegistro().clone(),
			getQtDias());
	}

}