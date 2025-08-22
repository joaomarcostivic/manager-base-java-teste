package com.tivic.manager.agd;

import java.util.GregorianCalendar;

public class AgendamentoAssunto {

	private int cdAssunto;
	private int cdAgendamento;
	private int nrOrdem;
	private int cdResponsavel;
	private int stFinal;
	private String txtMemo;
	private String txtFechamento;
	private int tpPrioridade;
	private GregorianCalendar dtPrevisaoConclusao;

	public AgendamentoAssunto(int cdAssunto,
			int cdAgendamento,
			int nrOrdem,
			int cdResponsavel,
			int stFinal,
			String txtMemo,
			String txtFechamento,
			int tpPrioridade,
			GregorianCalendar dtPrevisaoConclusao){
		setCdAssunto(cdAssunto);
		setCdAgendamento(cdAgendamento);
		setNrOrdem(nrOrdem);
		setCdResponsavel(cdResponsavel);
		setStFinal(stFinal);
		setTxtMemo(txtMemo);
		setTxtFechamento(txtFechamento);
		setTpPrioridade(tpPrioridade);
		setDtPrevisaoConclusao(dtPrevisaoConclusao);
	}
	public void setCdAssunto(int cdAssunto){
		this.cdAssunto=cdAssunto;
	}
	public int getCdAssunto(){
		return this.cdAssunto;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setNrOrdem(int nrOrdem){
		this.nrOrdem=nrOrdem;
	}
	public int getNrOrdem(){
		return this.nrOrdem;
	}
	public void setCdResponsavel(int cdResponsavel){
		this.cdResponsavel=cdResponsavel;
	}
	public int getCdResponsavel(){
		return this.cdResponsavel;
	}
	public void setStFinal(int stFinal){
		this.stFinal=stFinal;
	}
	public int getStFinal(){
		return this.stFinal;
	}
	public void setTxtMemo(String txtMemo){
		this.txtMemo=txtMemo;
	}
	public String getTxtMemo(){
		return this.txtMemo;
	}
	public void setTxtFechamento(String txtFechamento){
		this.txtFechamento=txtFechamento;
	}
	public String getTxtFechamento(){
		return this.txtFechamento;
	}
	public void setTpPrioridade(int tpPrioridade){
		this.tpPrioridade=tpPrioridade;
	}
	public int getTpPrioridade(){
		return this.tpPrioridade;
	}
	public void setDtPrevisaoConclusao(GregorianCalendar dtPrevisaoConclusao){
		this.dtPrevisaoConclusao=dtPrevisaoConclusao;
	}
	public GregorianCalendar getDtPrevisaoConclusao(){
		return this.dtPrevisaoConclusao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAssunto: " +  getCdAssunto();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", nrOrdem: " +  getNrOrdem();
		valueToString += ", cdResponsavel: " +  getCdResponsavel();
		valueToString += ", stFinal: " +  getStFinal();
		valueToString += ", txtMemo: " +  getTxtMemo();
		valueToString += ", txtFechamento: " +  getTxtFechamento();
		valueToString += ", tpPrioridade: " +  getTpPrioridade();
		valueToString += ", dtPrevisaoConclusao: " +  sol.util.Util.formatDateTime(getDtPrevisaoConclusao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoAssunto(getCdAssunto(),
			getCdAgendamento(),
			getNrOrdem(),
			getCdResponsavel(),
			getStFinal(),
			getTxtMemo(),
			getTxtFechamento(),
			getTpPrioridade(),
			getDtPrevisaoConclusao()==null ? null : (GregorianCalendar)getDtPrevisaoConclusao().clone());
	}

}
