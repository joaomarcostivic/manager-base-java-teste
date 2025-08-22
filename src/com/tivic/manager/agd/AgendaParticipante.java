package com.tivic.manager.agd;

public class AgendaParticipante {

	private int cdAgenda;
	private int cdParticipante;
	private int stParticipante;
	private int tpParticipante;

	public AgendaParticipante(int cdAgenda,
			int cdParticipante,
			int stParticipante,
			int tpParticipante){
		setCdAgenda(cdAgenda);
		setCdParticipante(cdParticipante);
		setStParticipante(stParticipante);
		setTpParticipante(tpParticipante);
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
	public void setStParticipante(int stParticipante){
		this.stParticipante=stParticipante;
	}
	public int getStParticipante(){
		return this.stParticipante;
	}
	public void setTpParticipante(int tpParticipante){
		this.tpParticipante=tpParticipante;
	}
	public int getTpParticipante(){
		return this.tpParticipante;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdAgenda: " +  getCdAgenda();
		valueToString += ", cdParticipante: " +  getCdParticipante();
		valueToString += ", stParticipante: " +  getStParticipante();
		valueToString += ", tpParticipante: " +  getTpParticipante();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AgendaParticipante(getCdAgenda(),
			getCdParticipante(),
			getStParticipante(),
			getTpParticipante());
	}

}
