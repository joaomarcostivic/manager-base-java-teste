package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class AgendaItemParticipante {

	private int cdAgendaItem;
	private int cdPessoa;
	private int stParticipacao;
	private int tpParticipacao;
	private GregorianCalendar dtCadastro;
	private String txtObservacao;

	public AgendaItemParticipante(){ }

	public AgendaItemParticipante(int cdAgendaItem,
			int cdPessoa,
			int stParticipacao,
			int tpParticipacao,
			GregorianCalendar dtCadastro,
			String txtObservacao){
		setCdAgendaItem(cdAgendaItem);
		setCdPessoa(cdPessoa);
		setStParticipacao(stParticipacao);
		setTpParticipacao(tpParticipacao);
		setDtCadastro(dtCadastro);
		setTxtObservacao(txtObservacao);
	}
	public void setCdAgendaItem(int cdAgendaItem){
		this.cdAgendaItem=cdAgendaItem;
	}
	public int getCdAgendaItem(){
		return this.cdAgendaItem;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setStParticipacao(int stParticipacao){
		this.stParticipacao=stParticipacao;
	}
	public int getStParticipacao(){
		return this.stParticipacao;
	}
	public void setTpParticipacao(int tpParticipacao){
		this.tpParticipacao=tpParticipacao;
	}
	public int getTpParticipacao(){
		return this.tpParticipacao;
	}
	public void setDtCadastro(GregorianCalendar dtCadastro){
		this.dtCadastro=dtCadastro;
	}
	public GregorianCalendar getDtCadastro(){
		return this.dtCadastro;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendaItem: " +  getCdAgendaItem();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", stParticipacao: " +  getStParticipacao();
		valueToString += ", tpParticipacao: " +  getTpParticipacao();
		valueToString += ", dtCadastro: " +  sol.util.Util.formatDateTime(getDtCadastro(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendaItemParticipante(getCdAgendaItem(),
			getCdPessoa(),
			getStParticipacao(),
			getTpParticipacao(),
			getDtCadastro()==null ? null : (GregorianCalendar)getDtCadastro().clone(),
			getTxtObservacao());
	}

}
