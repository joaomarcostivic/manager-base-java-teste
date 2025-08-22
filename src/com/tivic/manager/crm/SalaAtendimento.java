package com.tivic.manager.crm;

public class SalaAtendimento {

	String idAtendimento;
	String logAtendimento;
	String nmAtendido;
	String jidAtendido;
	
	public SalaAtendimento(String idAtendimento,
			String logAtendimento,
			String nmAtendido,
			String jidAtendido){
		setIdAtendimento(idAtendimento);
		setLogAtendimento(logAtendimento);
		setNmAtendido(nmAtendido);
		setJidAtendido(jidAtendido);
	}
	
	public void setIdAtendimento(String idAtendimento){
		this.idAtendimento=idAtendimento;
	}
	public String getIdAtendimento(){
		return this.idAtendimento;
	}
	
	public void setLogAtendimento(String logAtendimento){
		this.logAtendimento=logAtendimento;
	}
	public String getLogAtendimento(){
		return this.logAtendimento;
	}	
	
	public void setNmAtendido(String nmAtendido){
		this.nmAtendido=nmAtendido;
	}
	public String getNmAtendido(){
		return this.nmAtendido;
	}
	
	public void setJidAtendido(String jidAtendido){
		this.jidAtendido=jidAtendido;
	}
	public String getJidAtendido(){
		return this.jidAtendido;
	}
}
