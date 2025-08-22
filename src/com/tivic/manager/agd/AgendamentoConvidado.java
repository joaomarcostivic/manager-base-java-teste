package com.tivic.manager.agd;

public class AgendamentoConvidado {

	private int cdConvidado;
	private int cdAgendamento;
	private String nmConvidado;
	private String nmEmail;
	private int lgPresenca;

	public AgendamentoConvidado(int cdConvidado,
			int cdAgendamento,
			String nmConvidado,
			String nmEmail,
			int lgPresenca){
		setCdConvidado(cdConvidado);
		setCdAgendamento(cdAgendamento);
		setNmConvidado(nmConvidado);
		setNmEmail(nmEmail);
		setLgPresenca(lgPresenca);
	}
	public void setCdConvidado(int cdConvidado){
		this.cdConvidado=cdConvidado;
	}
	public int getCdConvidado(){
		return this.cdConvidado;
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setNmConvidado(String nmConvidado){
		this.nmConvidado=nmConvidado;
	}
	public String getNmConvidado(){
		return this.nmConvidado;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setLgPresenca(int lgPresenca){
		this.lgPresenca=lgPresenca;
	}
	public int getLgPresenca(){
		return this.lgPresenca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdConvidado: " +  getCdConvidado();
		valueToString += ", cdAgendamento: " +  getCdAgendamento();
		valueToString += ", nmConvidado: " +  getNmConvidado();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", lgPresenca: " +  getLgPresenca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoConvidado(getCdConvidado(),
			getCdAgendamento(),
			getNmConvidado(),
			getNmEmail(),
			getLgPresenca());
	}

}
