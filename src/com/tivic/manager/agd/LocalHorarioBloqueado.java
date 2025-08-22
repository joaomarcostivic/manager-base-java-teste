package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class LocalHorarioBloqueado {

	private int cdHorarioBloqueado;
	private GregorianCalendar dtInicio;
	private GregorianCalendar dtTermino;
	private GregorianCalendar hrInicio;
	private GregorianCalendar hrTermino;
	private int cdLocal;

	public LocalHorarioBloqueado(){ }

	public LocalHorarioBloqueado(int cdHorarioBloqueado,
			GregorianCalendar dtInicio,
			GregorianCalendar dtTermino,
			GregorianCalendar hrInicio,
			GregorianCalendar hrTermino,
			int cdLocal){
		setCdHorarioBloqueado(cdHorarioBloqueado);
		setDtInicio(dtInicio);
		setDtTermino(dtTermino);
		setHrInicio(hrInicio);
		setHrTermino(hrTermino);
		setCdLocal(cdLocal);
	}
	public void setCdHorarioBloqueado(int cdHorarioBloqueado){
		this.cdHorarioBloqueado=cdHorarioBloqueado;
	}
	public int getCdHorarioBloqueado(){
		return this.cdHorarioBloqueado;
	}
	public void setDtInicio(GregorianCalendar dtInicio){
		this.dtInicio=dtInicio;
	}
	public GregorianCalendar getDtInicio(){
		return this.dtInicio;
	}
	public void setDtTermino(GregorianCalendar dtTermino){
		this.dtTermino=dtTermino;
	}
	public GregorianCalendar getDtTermino(){
		return this.dtTermino;
	}
	public void setHrInicio(GregorianCalendar hrInicio){
		this.hrInicio=hrInicio;
	}
	public GregorianCalendar getHrInicio(){
		return this.hrInicio;
	}
	public void setHrTermino(GregorianCalendar hrTermino){
		this.hrTermino=hrTermino;
	}
	public GregorianCalendar getHrTermino(){
		return this.hrTermino;
	}
	public void setCdLocal(int cdLocal){
		this.cdLocal=cdLocal;
	}
	public int getCdLocal(){
		return this.cdLocal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdHorarioBloqueado: " +  getCdHorarioBloqueado();
		valueToString += ", dtInicio: " +  sol.util.Util.formatDateTime(getDtInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtTermino: " +  sol.util.Util.formatDateTime(getDtTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrInicio: " +  sol.util.Util.formatDateTime(getHrInicio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrTermino: " +  sol.util.Util.formatDateTime(getHrTermino(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdLocal: " +  getCdLocal();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new LocalHorarioBloqueado(getCdHorarioBloqueado(),
			getDtInicio()==null ? null : (GregorianCalendar)getDtInicio().clone(),
			getDtTermino()==null ? null : (GregorianCalendar)getDtTermino().clone(),
			getHrInicio()==null ? null : (GregorianCalendar)getHrInicio().clone(),
			getHrTermino()==null ? null : (GregorianCalendar)getHrTermino().clone(),
			getCdLocal());
	}

}