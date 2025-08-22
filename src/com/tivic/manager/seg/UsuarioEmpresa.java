package com.tivic.manager.seg;

import java.util.GregorianCalendar;

public class UsuarioEmpresa {

	private int cdEmpresa;
	private int cdUsuario;
	private int nrHorario;
	private GregorianCalendar hrInicial;
	private GregorianCalendar hrFinal;

	public UsuarioEmpresa() { }

	public UsuarioEmpresa(int cdEmpresa,
			int cdUsuario,
			int nrHorario,
			GregorianCalendar hrInicial,
			GregorianCalendar hrFinal) {
		setCdEmpresa(cdEmpresa);
		setCdUsuario(cdUsuario);
		setNrHorario(nrHorario);
		setHrInicial(hrInicial);
		setHrFinal(hrFinal);
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setNrHorario(int nrHorario){
		this.nrHorario=nrHorario;
	}
	public int getNrHorario(){
		return this.nrHorario;
	}
	public void setHrInicial(GregorianCalendar hrInicial){
		this.hrInicial=hrInicial;
	}
	public GregorianCalendar getHrInicial(){
		return this.hrInicial;
	}
	public void setHrFinal(GregorianCalendar hrFinal){
		this.hrFinal=hrFinal;
	}
	public GregorianCalendar getHrFinal(){
		return this.hrFinal;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nrHorario: " +  getNrHorario();
		valueToString += ", hrInicial: " +  sol.util.Util.formatDateTime(getHrInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", hrFinal: " +  sol.util.Util.formatDateTime(getHrFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new UsuarioEmpresa(getCdEmpresa(),
			getCdUsuario(),
			getNrHorario(),
			getHrInicial()==null ? null : (GregorianCalendar)getHrInicial().clone(),
			getHrFinal()==null ? null : (GregorianCalendar)getHrFinal().clone());
	}

}