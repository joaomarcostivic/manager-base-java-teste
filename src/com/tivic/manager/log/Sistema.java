package com.tivic.manager.log;

import java.util.GregorianCalendar;

public class Sistema {

	private int cdLog;
	private GregorianCalendar dtLog;
	private String txtLog;
	private int tpLog;
	private int cdUsuario;
	private String nmHttpMetodo;
	private String nmCaminho;
	private int nrHttpStatus;
	private String dsIpCliente;
	private String dsUserAgent;

	public Sistema(){ }

	public Sistema(int cdLog,
			GregorianCalendar dtLog,
			String txtLog,
			int tpLog,
			int cdUsuario,
			String nmHttpMetodo,
			String nmCaminho,
			int nrHttpStatus,
			String dsIpCliente,
			String dsUserAgent){
		setCdLog(cdLog);
		setDtLog(dtLog);
		setTxtLog(txtLog);
		setTpLog(tpLog);
		setCdUsuario(cdUsuario);
		setNmHttpMetodo(nmHttpMetodo);
		setNmCaminho(nmCaminho);
		setNrHttpStatus(nrHttpStatus);
		setDsIpCliente(dsIpCliente);
		setDsUserAgent(dsUserAgent);
	}
	
	public Sistema(int cdLog,
			GregorianCalendar dtLog,
			String txtLog,
			int tpLog,
			int cdUsuario){
		setCdLog(cdLog);
		setDtLog(dtLog);
		setTxtLog(txtLog);
		setTpLog(tpLog);
		setCdUsuario(cdUsuario);;
	}
	
	public void setCdLog(int cdLog){
		this.cdLog=cdLog;
	}
	public int getCdLog(){
		return this.cdLog;
	}
	public void setDtLog(GregorianCalendar dtLog){
		this.dtLog=dtLog;
	}
	public GregorianCalendar getDtLog(){
		return this.dtLog;
	}
	public void setTxtLog(String txtLog){
		this.txtLog=txtLog;
	}
	public String getTxtLog(){
		return this.txtLog;
	}
	public void setTpLog(int tpLog){
		this.tpLog=tpLog;
	}
	public int getTpLog(){
		return this.tpLog;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}

	public String getNmHttpMetodo() {
		return nmHttpMetodo;
	}

	public void setNmHttpMetodo(String nmHttpMetodo) {
		this.nmHttpMetodo = nmHttpMetodo;
	}

	public String getNmCaminho() {
		return nmCaminho;
	}

	public void setNmCaminho(String nmCaminho) {
		this.nmCaminho = nmCaminho;
	}
	
	public int getNrHttpStatus() {
		return nrHttpStatus;
	}

	public void setNrHttpStatus(int nrHttpStatus) {
		this.nrHttpStatus = nrHttpStatus;
	}

	public String getDsIpCliente() {
		return dsIpCliente;
	}

	public void setDsIpCliente(String dsIpCliente) {
		this.dsIpCliente = dsIpCliente;
	}

	public String getDsUserAgent() {
		return dsUserAgent;
	}

	public void setDsUserAgent(String dsUserAgent) {
		this.dsUserAgent = dsUserAgent;
	}

	public String toString() {
		String valueToString = "";
		valueToString += "cdLog: " +  getCdLog();
		valueToString += ", dtLog: " +  sol.util.Util.formatDateTime(getDtLog(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtLog: " +  getTxtLog();
		valueToString += ", tpLog: " +  getTpLog();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Sistema(getCdLog(),
			getDtLog()==null ? null : (GregorianCalendar)getDtLog().clone(),
			getTxtLog(),
			getTpLog(),
			getCdUsuario());
	}

}
