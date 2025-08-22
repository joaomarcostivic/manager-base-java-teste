package com.tivic.manager.grl;

import java.util.GregorianCalendar;

public class EquipamentoPessoa {

	private int cdEquipamentoPessoa;
	private int cdEquipamento;
	private int cdPessoa;
	private GregorianCalendar dtInicial;
	private GregorianCalendar dtFinal;
	private int stEmprestimo;
	private String txtObservacao;
	private int cdAgendamento;

	public EquipamentoPessoa() { }

	public EquipamentoPessoa(int cdEquipamentoPessoa,
			int cdEquipamento,
			int cdPessoa,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int stEmprestimo,
			String txtObservacao,
			int cdAgendamento) {
		setCdEquipamentoPessoa(cdEquipamentoPessoa);
		setCdEquipamento(cdEquipamento);
		setCdPessoa(cdPessoa);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setStEmprestimo(stEmprestimo);
		setTxtObservacao(txtObservacao);
		setCdAgendamento(cdAgendamento);
	}
	public void setCdEquipamentoPessoa(int cdEquipamentoPessoa){
		this.cdEquipamentoPessoa=cdEquipamentoPessoa;
	}
	public int getCdEquipamentoPessoa(){
		return this.cdEquipamentoPessoa;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setStEmprestimo(int stEmprestimo){
		this.stEmprestimo=stEmprestimo;
	}
	public int getStEmprestimo(){
		return this.stEmprestimo;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdEquipamentoPessoa: " +  getCdEquipamentoPessoa();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stEmprestimo: " +  getStEmprestimo();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new EquipamentoPessoa(getCdEquipamentoPessoa(),
			getCdEquipamento(),
			getCdPessoa(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getStEmprestimo(),
			getTxtObservacao(),
			getCdAgendamento());
	}

}