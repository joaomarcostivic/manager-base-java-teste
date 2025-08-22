package com.tivic.manager.mob;

import java.util.GregorianCalendar;

public class MotoristaFolgaAgendamento {

	private int cdFolgaAgendamento;
	private int cdMotorista;
	private GregorianCalendar dtAgendamento;
	private int stFolga;
	private String txtObservacao;
	private int cdFolga;
	
	public MotoristaFolgaAgendamento() { }

	public MotoristaFolgaAgendamento(int cdFolgaAgendamento,
			int cdMotorista,
			GregorianCalendar dtAgendamento,
			int stFolga,
			String txtObservacao,
			int cdFolga) {
		setCdFolgaAgendamento(cdFolgaAgendamento);
		setCdMotorista(cdMotorista);
		setDtAgendamento(dtAgendamento);
		setStFolga(stFolga);
		setTxtObservacao(txtObservacao);
		setCdFolga(cdFolga);
	}
	public void setCdFolgaAgendamento(int cdFolgaAgendamento){
		this.cdFolgaAgendamento=cdFolgaAgendamento;
	}
	public int getCdFolgaAgendamento(){
		return this.cdFolgaAgendamento;
	}
	public void setCdMotorista(int cdMotorista){
		this.cdMotorista=cdMotorista;
	}
	public int getCdMotorista(){
		return this.cdMotorista;
	}
	public void setDtAgendamento(GregorianCalendar dtAgendamento){
		this.dtAgendamento=dtAgendamento;
	}
	public GregorianCalendar getDtAgendamento(){
		return this.dtAgendamento;
	}
	public void setStFolga(int stFolga){
		this.stFolga=stFolga;
	}
	public int getStFolga(){
		return this.stFolga;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdFolga(int cdFolga) {
		this.cdFolga = cdFolga;
	}
	public int getCdFolga() {
		return cdFolga;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdFolgaAgendamento: " +  getCdFolgaAgendamento();
		valueToString += ", cdMotorista: " +  getCdMotorista();
		valueToString += ", dtAgendamento: " +  sol.util.Util.formatDateTime(getDtAgendamento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stFolga: " +  getStFolga();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdFolga: " +  getCdFolga();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MotoristaFolgaAgendamento(getCdFolgaAgendamento(),
			getCdMotorista(),
			getDtAgendamento()==null ? null : (GregorianCalendar)getDtAgendamento().clone(),
			getStFolga(),
			getTxtObservacao(),
			getCdFolga());
	}

}