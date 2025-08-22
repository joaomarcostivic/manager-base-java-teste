package com.tivic.manager.crm;

public class MailingDestino {

	private int cdDestino;
	private int cdMailing;
	private int cdGrupo;
	private int cdPessoa;
	private int cdFonte;
	private int cdAgendamento;

	public MailingDestino(int cdDestino,
			int cdMailing,
			int cdGrupo,
			int cdPessoa,
			int cdFonte,
			int cdAgendamento){
		setCdDestino(cdDestino);
		setCdMailing(cdMailing);
		setCdGrupo(cdGrupo);
		setCdPessoa(cdPessoa);
		setCdFonte(cdFonte);
		setCdAgendamento(cdAgendamento);
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
	public void setCdGrupo(int cdGrupo){
		this.cdGrupo=cdGrupo;
	}
	public int getCdGrupo(){
		return this.cdGrupo;
	}
	public void setCdPessoa(int cdPessoa){
		this.cdPessoa=cdPessoa;
	}
	public int getCdPessoa(){
		return this.cdPessoa;
	}
	public void setCdFonte(int cdFonte){
		this.cdFonte=cdFonte;
	}
	public int getCdFonte(){
		return this.cdFonte;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDestino: " +  getCdDestino();
		valueToString += ", cdMailing: " +  getCdMailing();
		valueToString += ", cdGrupo: " +  getCdGrupo();
		valueToString += ", cdPessoa: " +  getCdPessoa();
		valueToString += ", cdFonte: " +  getCdFonte();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new MailingDestino(getCdDestino(),
			getCdMailing(),
			getCdGrupo(),
			getCdPessoa(),
			getCdFonte(),
			getCdAgendamento());
	}

}
