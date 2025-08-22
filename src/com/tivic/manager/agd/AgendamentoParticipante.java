package com.tivic.manager.agd;

public class AgendamentoParticipante {

	private int cdAgendamento;
	private int cdAgenda;
	private int cdParticipante;
	private int lgPresenca;

	public AgendamentoParticipante() {}
	
	public AgendamentoParticipante(int cdAgendamento,
			int cdAgenda,
			int cdParticipante,
			int lgPresenca){
		setCdAgendamento(cdAgendamento);
		setCdAgenda(cdAgenda);
		setCdParticipante(cdParticipante);
		setLgPresenca(lgPresenca);
	}
	public void setCdAgendamento(int cdAgendamento){
		this.cdAgendamento=cdAgendamento;
	}
	public int getCdAgendamento(){
		return this.cdAgendamento;
	}
	public void setCdAgenda(int cdAgenda){
		this.cdAgenda=cdAgenda;
	}
	public int getCdAgenda(){
		return this.cdAgenda;
	}
	public void setCdParticipante(int cdParticipante){
		this.cdParticipante=cdParticipante;
	}
	public int getCdParticipante(){
		return this.cdParticipante;
	}
	public void setLgPresenca(int lgPresenca){
		this.lgPresenca=lgPresenca;
	}
	public int getLgPresenca(){
		return this.lgPresenca;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgendamento: " +  getCdAgendamento();
		valueToString += ", cdAgenda: " +  getCdAgenda();
		valueToString += ", cdParticipante: " +  getCdParticipante();
		valueToString += ", lgPresenca: " +  getLgPresenca();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendamentoParticipante(getCdAgendamento(),
			getCdAgenda(),
			getCdParticipante(),
			getLgPresenca());
	}

}
