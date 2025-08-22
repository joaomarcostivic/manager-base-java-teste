package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class Formulario {

	private int cdFormulario;
	private String nmFormulario;
	private String idFormulario;
	private GregorianCalendar dtVersao;
	private int stFormulario;
	private String dsFormulario;
	private String nmLinkFormulario;
	private GregorianCalendar dtInicioFormulario;
	private GregorianCalendar dtFimFormulario;

	public Formulario(){ }

	public Formulario(int cdFormulario,
			String nmFormulario,
			String idFormulario,
			GregorianCalendar dtVersao,
			int stFormulario,
			String dsFormulario,
			String nmLinkFormulario,
			GregorianCalendar dtInicioFormulario,
			GregorianCalendar dtFimFormulario){
		setCdFormulario(cdFormulario);
		setNmFormulario(nmFormulario);
		setIdFormulario(idFormulario);
		setDtVersao(dtVersao);
		setStFormulario(stFormulario);
		setDsFormulario(dsFormulario);
		setNmLinkFormulario(nmLinkFormulario);
		setDtInicioFormulario(dtInicioFormulario);
		setDtFimFormulario(dtFimFormulario);
	}
	public void setCdFormulario(int cdFormulario){
		this.cdFormulario=cdFormulario;
	}
	public int getCdFormulario(){
		return this.cdFormulario;
	}
	public void setNmFormulario(String nmFormulario){
		this.nmFormulario=nmFormulario;
	}
	public String getNmFormulario(){
		return this.nmFormulario;
	}
	public void setIdFormulario(String idFormulario){
		this.idFormulario=idFormulario;
	}
	public String getIdFormulario(){
		return this.idFormulario;
	}
	public void setDtVersao(GregorianCalendar dtVersao){
		this.dtVersao=dtVersao;
	}
	public GregorianCalendar getDtVersao(){
		return this.dtVersao;
	}
	public void setStFormulario(int stFormulario){
		this.stFormulario=stFormulario;
	}
	public int getStFormulario(){
		return this.stFormulario;
	}
	public void setDsFormulario(String dsFormulario){
		this.dsFormulario=dsFormulario;
	}
	public String getDsFormulario(){
		return this.dsFormulario;
	}
	public void setNmLinkFormulario(String nmLinkFormulario){
		this.nmLinkFormulario=nmLinkFormulario;
	}
	public String getNmLinkFormulario(){
		return this.nmLinkFormulario;
	}
	public void setDtInicioFormulario(GregorianCalendar dtInicioFormulario){
		this.dtInicioFormulario=dtInicioFormulario;
	}
	public GregorianCalendar getDtInicioFormulario(){
		return this.dtInicioFormulario;
	}
	public void setDtFimFormulario(GregorianCalendar dtFimFormulario){
		this.dtFimFormulario=dtFimFormulario;
	}
	public GregorianCalendar getDtFimFormulario(){
		return this.dtFimFormulario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFormulario: " +  getCdFormulario();
		valueToString += ", nmFormulario: " +  getNmFormulario();
		valueToString += ", idFormulario: " +  getIdFormulario();
		valueToString += ", dtVersao: " +  sol.util.Util.formatDateTime(getDtVersao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stFormulario: " +  getStFormulario();
		valueToString += ", dsFormulario: " +  getDsFormulario();
		valueToString += ", nmLinkFormulario: " +  getNmLinkFormulario();
		valueToString += ", dtInicioFormulario: " +  sol.util.Util.formatDateTime(getDtInicioFormulario(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFimFormulario: " +  sol.util.Util.formatDateTime(getDtFimFormulario(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Formulario(getCdFormulario(),
			getNmFormulario(),
			getIdFormulario(),
			getDtVersao()==null ? null : (GregorianCalendar)getDtVersao().clone(),
			getStFormulario(),
			getDsFormulario(),
			getNmLinkFormulario(),
			getDtInicioFormulario()==null ? null : (GregorianCalendar)getDtInicioFormulario().clone(),
			getDtFimFormulario()==null ? null : (GregorianCalendar)getDtFimFormulario().clone());
	}

}