package com.tivic.manager.ctb;

import java.util.GregorianCalendar;

public class Lote {

	private int cdLote;
	private int cdUsuario;
	private String nmLote;
	private String nrLote;
	private GregorianCalendar dtAbertura;
	private GregorianCalendar dtEncerramento;
	private int stLote;
	private int cdEmpresa;
	private float vlLote;

	public Lote(int cdLote,
			int cdUsuario,
			String nmLote,
			String nrLote,
			GregorianCalendar dtAbertura,
			GregorianCalendar dtEncerramento,
			int stLote,
			int cdEmpresa,
			float vlLote){
		setCdLote(cdLote);
		setCdUsuario(cdUsuario);
		setNmLote(nmLote);
		setNrLote(nrLote);
		setDtAbertura(dtAbertura);
		setDtEncerramento(dtEncerramento);
		setStLote(stLote);
		setCdEmpresa(cdEmpresa);
		setVlLote(vlLote);
	}
	public void setCdLote(int cdLote){
		this.cdLote=cdLote;
	}
	public int getCdLote(){
		return this.cdLote;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public void setNmLote(String nmLote){
		this.nmLote=nmLote;
	}
	public String getNmLote(){
		return this.nmLote;
	}
	public void setNrLote(String nrLote){
		this.nrLote=nrLote;
	}
	public String getNrLote(){
		return this.nrLote;
	}
	public void setDtAbertura(GregorianCalendar dtAbertura){
		this.dtAbertura=dtAbertura;
	}
	public GregorianCalendar getDtAbertura(){
		return this.dtAbertura;
	}
	public void setDtEncerramento(GregorianCalendar dtEncerramento){
		this.dtEncerramento=dtEncerramento;
	}
	public GregorianCalendar getDtEncerramento(){
		return this.dtEncerramento;
	}
	public void setStLote(int stLote){
		this.stLote=stLote;
	}
	public int getStLote(){
		return this.stLote;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setVlLote(float vlLote){
		this.vlLote=vlLote;
	}
	public float getVlLote(){
		return this.vlLote;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdLote: " +  getCdLote();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		valueToString += ", nmLote: " +  getNmLote();
		valueToString += ", nrLote: " +  getNrLote();
		valueToString += ", dtAbertura: " +  sol.util.Util.formatDateTime(getDtAbertura(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEncerramento: " +  sol.util.Util.formatDateTime(getDtEncerramento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stLote: " +  getStLote();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", vlLote: " +  getVlLote();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Lote(getCdLote(),
			getCdUsuario(),
			getNmLote(),
			getNrLote(),
			getDtAbertura()==null ? null : (GregorianCalendar)getDtAbertura().clone(),
			getDtEncerramento()==null ? null : (GregorianCalendar)getDtEncerramento().clone(),
			getStLote(),
			getCdEmpresa(),
			getVlLote());
	}

}
