package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class MailingPlanejamento {

	private int cdPlanejamento;
	private int cdMailing;
	private GregorianCalendar dtPlanejamento;
	private GregorianCalendar dtEnvio;
	private int stPlanejamento;
	private int cdUsuario;
	private int cdContaEnvio;
	private String nmAssunto;
	private String txtParametros;

	public MailingPlanejamento(int cdPlanejamento,
			int cdMailing,
			GregorianCalendar dtPlanejamento,
			GregorianCalendar dtEnvio,
			int stPlanejamento,
			int cdUsuario,
			int cdContaEnvio,
			String nmAssunto,
			String txtParametros){
		setCdPlanejamento(cdPlanejamento);
		setCdMailing(cdMailing);
		setDtPlanejamento(dtPlanejamento);
		setDtEnvio(dtEnvio);
		setStPlanejamento(stPlanejamento);
		setCdUsuario(cdUsuario);
		setCdContaEnvio(cdContaEnvio);
		setNmAssunto(nmAssunto);
		setTxtParametros(txtParametros);
	}
	public void setCdPlanejamento(int cdPlanejamento){
		this.cdPlanejamento=cdPlanejamento;
	}
	public int getCdPlanejamento(){
		return this.cdPlanejamento;
	}
	public void setCdMailing(int cdMailing){
		this.cdMailing=cdMailing;
	}
	public int getCdMailing(){
		return this.cdMailing;
	}
	public void setDtPlanejamento(GregorianCalendar dtPlanejamento){
		this.dtPlanejamento=dtPlanejamento;
	}
	public GregorianCalendar getDtPlanejamento(){
		return this.dtPlanejamento;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setStPlanejamento(int stPlanejamento){
		this.stPlanejamento=stPlanejamento;
	}
	public int getStPlanejamento(){
		return this.stPlanejamento;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setCdContaEnvio(int cdContaEnvio){
		this.cdContaEnvio=cdContaEnvio;
	}
	public int getCdContaEnvio(){
		return this.cdContaEnvio;
	}
	public void setNmAssunto(String nmAssunto){
		this.nmAssunto=nmAssunto;
	}
	public String getNmAssunto(){
		return this.nmAssunto;
	}
	public void setTxtParametros(String txtParametros){
		this.txtParametros=txtParametros;
	}
	public String getTxtParametros(){
		return this.txtParametros;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdPlanejamento: " +  getCdPlanejamento();
		valueToString += ", cdMailing: " +  getCdMailing();
		valueToString += ", dtPlanejamento: " +  sol.util.Util.formatDateTime(getDtPlanejamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stPlanejamento: " +  getStPlanejamento();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", cdContaEnvio: " +  getCdContaEnvio();
		valueToString += ", nmAssunto: " +  getNmAssunto();
		valueToString += ", txtParametros: " +  getTxtParametros();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MailingPlanejamento(getCdPlanejamento(),
			getCdMailing(),
			getDtPlanejamento()==null ? null : (GregorianCalendar)getDtPlanejamento().clone(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getStPlanejamento(),
			getCdUsuario(),
			getCdContaEnvio(),
			getNmAssunto(),
			getTxtParametros());
	}

}
