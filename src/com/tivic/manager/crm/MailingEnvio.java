package com.tivic.manager.crm;

import java.util.GregorianCalendar;

public class MailingEnvio {

	private int cdDestino;
	private int cdMailing;
	private int cdPlanejamento;
	private GregorianCalendar dtEnvio;
	private int stEnvio;
	private String txtConteudo;
	private int cdUsuario;

	public MailingEnvio(int cdDestino,
			int cdMailing,
			int cdPlanejamento,
			GregorianCalendar dtEnvio,
			int stEnvio,
			String txtConteudo,
			int cdUsuario){
		setCdDestino(cdDestino);
		setCdMailing(cdMailing);
		setCdPlanejamento(cdPlanejamento);
		setDtEnvio(dtEnvio);
		setStEnvio(stEnvio);
		setTxtConteudo(txtConteudo);
		setCdUsuario(cdUsuario);
	}
	public void setCdDestino(int cdDestino){
		this.cdDestino=cdDestino;
	}
	public int getCdDestino(){
		return this.cdDestino;
	}
	public void setCdMailing(int cdMailing){
		this.cdMailing=cdMailing;
	}
	public int getCdMailing(){
		return this.cdMailing;
	}
	public void setCdPlanejamento(int cdPlanejamento){
		this.cdPlanejamento=cdPlanejamento;
	}
	public int getCdPlanejamento(){
		return this.cdPlanejamento;
	}
	public void setDtEnvio(GregorianCalendar dtEnvio){
		this.dtEnvio=dtEnvio;
	}
	public GregorianCalendar getDtEnvio(){
		return this.dtEnvio;
	}
	public void setStEnvio(int stEnvio){
		this.stEnvio=stEnvio;
	}
	public int getStEnvio(){
		return this.stEnvio;
	}
	public void setTxtConteudo(String txtConteudo){
		this.txtConteudo=txtConteudo;
	}
	public String getTxtConteudo(){
		return this.txtConteudo;
	}
	public void setCdUsuario(int cdUsuario){
		this.cdUsuario=cdUsuario;
	}
	public int getCdUsuario(){
		return this.cdUsuario;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDestino: " +  getCdDestino();
		valueToString += ", cdMailing: " +  getCdMailing();
		valueToString += ", cdPlanejamento: " +  getCdPlanejamento();
		valueToString += ", dtEnvio: " +  sol.util.Util.formatDateTime(getDtEnvio(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stEnvio: " +  getStEnvio();
		valueToString += ", txtConteudo: " +  getTxtConteudo();
		valueToString += ", cdUsuario: " +  getCdUsuario();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MailingEnvio(getCdDestino(),
			getCdMailing(),
			getCdPlanejamento(),
			getDtEnvio()==null ? null : (GregorianCalendar)getDtEnvio().clone(),
			getStEnvio(),
			getTxtConteudo(),
			getCdUsuario());
	}

}
